package com.ftn.scientific_papers.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xmldb.api.modules.XMLResource;

import com.ftn.scientific_papers.dto.SearchData;
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
	
	@GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
	private ResponseEntity<String> findAll(@RequestParam(defaultValue = "") String searchText,
			@RequestParam(defaultValue = "") String title,
			@RequestParam(defaultValue = "") String author,
			@RequestParam(defaultValue = "") String affiliation,
			@RequestParam(defaultValue = "") String keyword,
			@RequestParam(required = false) Long fromDate, // timestamp
			@RequestParam(required = false) Long toDate // timestamp
			) throws IOException{
		System.out.println("Title: " + title +
						   " Author: " + author +
						   " Affiliation: " + affiliation +
						   " Keyword: " + keyword + 
						   " From Date: " + fromDate + 
						   " To Date: " + toDate);
		String resource = "";
		if(title.equals("") && author.equals("") && affiliation.equals("") && keyword.equals("") &&
				fromDate == null && toDate == null) {
			resource = spService.getAll(searchText);
		}else {
			SearchData searchData = new SearchData(title, author, affiliation, keyword, fromDate, toDate);
			resource = spService.metadataSearch(searchData);
		}
		
		return new ResponseEntity<>(resource, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_XML_VALUE)
	private ResponseEntity<String> findOne(@PathVariable("id") String id) throws Exception {
		XMLResource resource = spService.findOne(id);

		return new ResponseEntity<>(resource.getContent().toString(), HttpStatus.OK);
	}

	@PostMapping(consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> add(@RequestBody String scientificPaperXml) throws Exception {
		
		String paperId = spService.save(scientificPaperXml);
		// TODO Get author id
		String processId = publishingProcessService.createProcess(paperId, "");
		return new ResponseEntity<>(processId, HttpStatus.CREATED);
	}

}
