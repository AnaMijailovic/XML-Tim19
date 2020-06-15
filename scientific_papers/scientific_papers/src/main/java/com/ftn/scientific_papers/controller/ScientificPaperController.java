package com.ftn.scientific_papers.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xmldb.api.modules.XMLResource;

import com.ftn.scientific_papers.dto.SearchData;
import com.ftn.scientific_papers.security.TokenUtils;
import com.ftn.scientific_papers.service.PublishingProcessService;
import com.ftn.scientific_papers.service.ScientificPaperService;

@RestController
@RequestMapping(value = "/api/scientificPapers")
@CrossOrigin()
public class ScientificPaperController {

	@Autowired
	private ScientificPaperService spService;

	@Autowired
	private PublishingProcessService publishingProcessService;
	
	@Autowired
	private HttpServletRequest request;
	
    @Autowired
    private TokenUtils tokenUtils;

	@GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> findAll(@RequestParam(defaultValue = "") String searchText,
			@RequestParam(defaultValue = "") String title, @RequestParam(defaultValue = "") String author,
			@RequestParam(defaultValue = "") String affiliation, @RequestParam(defaultValue = "") String keyword,
			@RequestParam(defaultValue = "") String loggedAuthor,
			@RequestParam(required = false) Long fromDate, // timestamp
			@RequestParam(required = false) Long toDate // timestamp
	) throws IOException {
		if(!loggedAuthor.equals(""))
			loggedAuthor = tokenUtils.getUsernameFromRequest(request);
		
		System.out.println("Title: " + title + " Author: " + author + " Affiliation: " + affiliation + " Keyword: "
				+ keyword + " From Date: " + fromDate + " To Date: " + toDate + " Logged author: " + loggedAuthor );
		String resource = "";
		if (title.equals("") && author.equals("") && affiliation.equals("") && keyword.equals("") && fromDate == null
				&& toDate == null) {
			resource = spService.getAll(searchText, loggedAuthor);
		} else {
			SearchData searchData = new SearchData(title, author, affiliation, keyword, fromDate, toDate);
			resource = spService.metadataSearch(searchData, loggedAuthor);
		}

		return new ResponseEntity<>(resource, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> findOne(@PathVariable("id") String id) throws Exception {
		XMLResource resource = spService.findOne(id);

		return new ResponseEntity<>(resource.getContent().toString(), HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ROLE_AUTHOR')")
	@PostMapping(consumes = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> addPaper(@RequestBody String scientificPaperXml) throws Exception {
		
		// Get author username from token
		String username = tokenUtils.getUsernameFromRequest(request);
		String paperId = spService.save(scientificPaperXml, "1"); // 1 is version of the paper	
		String processId = publishingProcessService.createProcess(paperId, username);
		return new ResponseEntity<>(processId, HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasRole('ROLE_AUTHOR')")
	@PostMapping(value="/revision", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> addPaperRevision(@RequestParam(("processId")) String processId,
			@RequestBody String scientificPaperXml) throws Exception {
		
		// Get author username from token
	    String username = tokenUtils.getUsernameFromRequest(request);
		spService.addPaperRevision(processId, scientificPaperXml, username);
		return new ResponseEntity<>(processId, HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasRole('ROLE_AUTHOR')")
	@DeleteMapping(value="/{id}", produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> withdrawScientificPaper(@PathVariable("id")String paperId) throws Exception{
		
		// Get author username from token
	    String username = tokenUtils.getUsernameFromRequest(request);
		spService.withdrawScientificPaper(paperId, username);
		return new ResponseEntity<>("Deleted", HttpStatus.ACCEPTED);
	}
}
