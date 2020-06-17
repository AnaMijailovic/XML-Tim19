package com.ftn.scientific_papers.controller;

import com.ftn.scientific_papers.dto.PublishingProcessDTO;
import com.ftn.scientific_papers.mapper.PublishingProcessMapper;
import com.ftn.scientific_papers.model.publishing_process.PublishingProcess;
import com.ftn.scientific_papers.model.scientific_paper.Author;
import com.ftn.scientific_papers.model.scientific_paper.ScientificPaper;
import com.ftn.scientific_papers.model.user.TUser;
import com.ftn.scientific_papers.security.TokenUtils;
import com.ftn.scientific_papers.service.CustomUserDetailsService;
import com.ftn.scientific_papers.service.ScientificPaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ftn.scientific_papers.service.PublishingProcessService;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.PathParam;
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

	@Autowired
	private PublishingProcessMapper mapper;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TokenUtils tokenUtils;

	@GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> findOne(@RequestParam(("paperId")) String paperId) throws Exception {
		String resource = publishingProcessService.findOneByPaperId(paperId);
		return new ResponseEntity<>(resource, HttpStatus.OK);
	}

	@GetMapping(value="/ongoing")
	@PreAuthorize("hasRole('ROLE_EDITOR')")
	public ResponseEntity<List<PublishingProcessDTO>> getPublicationsInOngoingProcess()
	{
		List<PublishingProcessDTO> result = new ArrayList<>();
		List<PublishingProcess> processes = publishingProcessService.getAll();

		for (PublishingProcess process: processes) {
			if (!isInOngoingProcess(process))
				continue;

			try {
				ScientificPaper scientificPaper = scientificPaperService.findOneUnmarshalled(process.getPaperVersion().get(process.getLatestVersion().intValue()-1).getScientificPaperId());
				PublishingProcessDTO publishingProcessDTO = mapper.toDTO(scientificPaper, process);
				result.add(publishingProcessDTO);
			} catch (Exception e) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PutMapping(value="/assignEditor/{processId}")
	@PreAuthorize("hasRole('ROLE_EDITOR')")
	public ResponseEntity<PublishingProcessDTO> assignPaperToEditor(@PathVariable("processId") String processId) {
		try {
			String username = tokenUtils.getUsernameFromRequest(request);
			TUser user = userService.findByUsername(username);

			publishingProcessService.assignEditor(processId, user.getUserId());
			PublishingProcess process = publishingProcessService.findOneUnmarshalled(processId);

			ScientificPaper scientificPaper = scientificPaperService.findOneUnmarshalled(process.getPaperVersion().get(process.getLatestVersion().intValue()-1).getScientificPaperId());
			PublishingProcessDTO publishingProcessDTO = mapper.toDTO(scientificPaper, process);

			return new ResponseEntity(publishingProcessDTO, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}

	private boolean isInOngoingProcess(PublishingProcess process) {
		String status = process.getStatus();

		if (status.equals("ACCEPTED") || status.equals("REJECTED") || status.equals("WITHDRAWN"))
			return false;
		return true;
	}
}
