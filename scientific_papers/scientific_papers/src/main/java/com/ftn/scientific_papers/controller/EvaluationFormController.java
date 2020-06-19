package com.ftn.scientific_papers.controller;

import com.ftn.scientific_papers.mapper.PublishingProcessMapper;
import com.ftn.scientific_papers.model.evaluation_form.EvaluationForm;
import com.ftn.scientific_papers.model.publishing_process.PublishingProcess;
import com.ftn.scientific_papers.model.publishing_process.VersionReview;
import com.ftn.scientific_papers.model.scientific_paper.ScientificPaper;
import com.ftn.scientific_papers.model.user.TUser;
import com.ftn.scientific_papers.security.TokenUtils;
import com.ftn.scientific_papers.service.CustomUserDetailsService;
import com.ftn.scientific_papers.service.PublishingProcessService;
import com.ftn.scientific_papers.service.ScientificPaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.xmldb.api.modules.XMLResource;

import com.ftn.scientific_papers.service.EvaluationFormService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/evaluationForms")
@CrossOrigin()
public class EvaluationFormController {
	
	@Autowired
	private EvaluationFormService evaluationFormService;

	@Autowired
	private PublishingProcessService publishingProcessService;

	@Autowired
	private ScientificPaperService scientificPaperService;

	@Autowired
	private CustomUserDetailsService userService;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TokenUtils tokenUtils;
	
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_XML_VALUE)
	private ResponseEntity<String> findOne(@PathVariable("id") String id) throws Exception {
		XMLResource resource = evaluationFormService.findOne(id);

		return new ResponseEntity<>(resource.getContent().toString(), HttpStatus.OK);
	}

	@PostMapping(value="/{processId}", consumes = MediaType.APPLICATION_XML_VALUE)
	@PreAuthorize("hasRole('ROLE_REVIEWER')")
	public ResponseEntity<String> submitEvaluationForm(@PathVariable("processId") String processId, @RequestBody String evaluationFormXML) throws Exception {
		String username = tokenUtils.getUsernameFromRequest(request);
		TUser user = userService.findByUsername(username);

		PublishingProcess process = publishingProcessService.findOneUnmarshalled(processId);

		if (process == null) {
			return  new ResponseEntity("Invalid process id", HttpStatus.NOT_FOUND);
		}

		ScientificPaper scientificPaper = scientificPaperService.findOneUnmarshalled(process.getPaperVersion().get(process.getLatestVersion().intValue()-1).getScientificPaperId());
		String reviewId = evaluationFormService.save(evaluationFormXML, scientificPaper.getId(), user.getUserId());

		publishingProcessService.submitReview(process, user.getUserId(), reviewId);
		return new ResponseEntity(HttpStatus.CREATED);
	}

	@GetMapping(value="/finished/{processId},{paperId}")
	@PreAuthorize("hasRole('ROLE_AUTHOR')")
	public ResponseEntity<String> getFinishedEvaluationForms(@PathVariable("processId") String processId, @PathVariable("paperId") String paperId) {
		PublishingProcess process = publishingProcessService.findOneUnmarshalled(processId);
		if (process == null) {
			return new ResponseEntity("Invalid process id", HttpStatus.NOT_FOUND);
		}

		List<PublishingProcess.PaperVersion> paperVersions = process.getPaperVersion();
		PublishingProcess.PaperVersion paperVersion = getPaperVersion(paperVersions, paperId);

		if (paperVersion == null) {
			return new ResponseEntity("Invalid paperId or userId", HttpStatus.BAD_REQUEST);
		}

		List<EvaluationForm> evaluationForms = getFinishedEvaluationFormsFromPaperVersion(paperVersion);

		if (evaluationForms == null) {
			return new ResponseEntity("Reviews not finished", HttpStatus.BAD_REQUEST);
		}

		// TODO: return merged evaluation forms without reviewer info

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping(value = "/xml/{id}", produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> findOneXml(@PathVariable("id") String id) throws Exception {
		XMLResource resource = evaluationFormService.findOneXml(id);
		return new ResponseEntity<>(resource.getContent().toString(), HttpStatus.OK);
	}
    
	
	@GetMapping(value = "/html/{id}", produces = MediaType.TEXT_HTML_VALUE)
	public ResponseEntity<String> findOneHtml(@PathVariable("id") String id)throws Exception { 
		byte[] resource = evaluationFormService.findOneHtml(id);
		return new ResponseEntity<>(new String(resource), HttpStatus.OK); 
	}

    @GetMapping(value = "/pdf/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> findOnePdf(@PathVariable("id") String id) throws Exception {

		byte[] contents = evaluationFormService.findOnePdf(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add("Content-Disposition", "inline; filename=" + id + ".pdf");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;
    }

    @GetMapping(value="/template", produces = MediaType.APPLICATION_XML_VALUE)
	@PreAuthorize("hasRole('ROLE_REVIEWER')")
	public ResponseEntity<String> getEvaluationFormTemplate() {
		return new ResponseEntity<>(evaluationFormService.getTemplate(), HttpStatus.OK);
	}


	private List<EvaluationForm> getFinishedEvaluationFormsFromPaperVersion(PublishingProcess.PaperVersion paperVersion) {
		List<EvaluationForm> evaluationForms = new ArrayList<>();
		for (VersionReview review : paperVersion.getVersionReviews().getVersionReview()) {
			if (!review.getStatus().equals("FINISHED")) {
				return null;
			} else {
				EvaluationForm evaluationForm =  evaluationFormService.findOneUnmarshalled(review.getReviewId());
				evaluationForms.add(evaluationForm);
			}
		}

		return evaluationForms;
	}

	private PublishingProcess.PaperVersion getPaperVersion(List<PublishingProcess.PaperVersion> paperVersions, String paperId) {
		for (PublishingProcess.PaperVersion paperVersion : paperVersions) {
			if (paperVersion.getScientificPaperId().equals(paperId)) {
				return paperVersion;
			}
		}
		return null;
	}
}
