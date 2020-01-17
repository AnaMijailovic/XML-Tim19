package com.ftn.scientific_papers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xmldb.api.modules.XMLResource;

import com.ftn.scientific_papers.service.CoverLetterService;
import com.ftn.scientific_papers.service.ScientificPaperService;

@RestController
@RequestMapping(value = "/coverLetters")
public class CoverLetterController {

	@Autowired
	private CoverLetterService coverLetterService;

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_XML_VALUE)
	private ResponseEntity<String> findOne(@PathVariable("id") String id) throws Exception {
		XMLResource resource = coverLetterService.findOne(id);

		return new ResponseEntity<>(resource.getContent().toString(), HttpStatus.OK);
	}

	@PostMapping(consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> add(@RequestBody String scientificPaperXml) throws Exception {
		
		coverLetterService.save(scientificPaperXml);
		return new ResponseEntity<>("Successfully saved cover letter", HttpStatus.CREATED);
	}

}
