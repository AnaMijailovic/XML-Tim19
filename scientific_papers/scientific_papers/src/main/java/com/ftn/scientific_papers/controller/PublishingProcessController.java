package com.ftn.scientific_papers.controller;

import com.ftn.scientific_papers.dto.PublishingProcessDTO;
import com.ftn.scientific_papers.model.publishing_process.PublishingProcess;
import com.ftn.scientific_papers.model.scientific_paper.Author;
import com.ftn.scientific_papers.model.scientific_paper.ScientificPaper;
import com.ftn.scientific_papers.model.user.TUser;
import com.ftn.scientific_papers.service.CustomUserDetailsService;
import com.ftn.scientific_papers.service.ScientificPaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.scientific_papers.service.PublishingProcessService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/publishingProcess")
@CrossOrigin()
public class PublishingProcessController {

	@Autowired
	private PublishingProcessService publishingProcessService;

	@Autowired
	private ScientificPaperService scientificPaperService;

	@Autowired
	private CustomUserDetailsService userService;

	@GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> findOne(@RequestParam(("paperId")) String paperId) throws Exception {
		String resource = publishingProcessService.findOneByPaperId(paperId);
		return new ResponseEntity<>(resource, HttpStatus.OK);
	}

	@GetMapping(value="ongoing-process")
	@PreAuthorize("hasRole('ROLE_EDITOR')")
	public ResponseEntity<List<PublishingProcessDTO>> getPublicationsInOngoingProcess()
	{
		List<PublishingProcessDTO> result = new ArrayList<>();
		List<PublishingProcess> processes = publishingProcessService.getAll();

		for (PublishingProcess process: processes) {
			if (!isInOngoingProcess(process))
				continue;

			try {
				ScientificPaper scientificPaper = scientificPaperService.findOneUnmarshalled(process.getPaperVersion().get(process.getLatestVersion().intValue()).getScientificPaperId());

				PublishingProcessDTO publishingProcessDTO = new PublishingProcessDTO();
				publishingProcessDTO.setProcessId(process.getId());
				publishingProcessDTO.setPaperTitle(formatTitle(scientificPaper.getHead().getTitle()));
				publishingProcessDTO.setAuthor(formatAuthors(scientificPaper.getHead().getAuthor()));
				publishingProcessDTO.setStatus(process.getStatus());
				publishingProcessDTO.setReviewers(formatReviewers(process.getPaperVersion().get(process.getLatestVersion().intValue()).getVersionReviews()));
				publishingProcessDTO.setVersion(process.getLatestVersion().toString());

				result.add(publishingProcessDTO);
			} catch (Exception e) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	private List<String> formatReviewers(PublishingProcess.PaperVersion.VersionReviews versionReviews) {
		List<String> reviweres = new ArrayList<>();

		for (int i = 0; i < versionReviews.getVersionReview().size(); i++) {
			TUser author = userService.findById(versionReviews.getVersionReview().get(i).getReviewerId());
			reviweres.add(author.getName() + " "  + author.getSurname());
		}

		return reviweres;
	}


	private String formatAuthors(List<Author> authors) {
		String title = "";
		for (int i = 0; i < authors.size(); i++) {
			title += authors.get(i).getFirstName() + " " + authors.get(i).getLastName() + "; ";
		}

		return title;
	}

	private String formatTitle(List<ScientificPaper.Head.Title> titles) {
		String title = "";
		for (int i = 0; i < titles.size(); i++) {
			title += titles.get(i).getValue() + "; ";
		}

		return title;
	}



	private boolean isInOngoingProcess(PublishingProcess process) {
		String status = process.getStatus();

		if (status.equals("ACCEPTED") || status.equals("REJECTED") || status.equals("WITHDRAWN"))
			return false;
		return true;
	}


}
