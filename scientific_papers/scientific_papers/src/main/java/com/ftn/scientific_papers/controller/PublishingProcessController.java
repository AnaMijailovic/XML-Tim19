package com.ftn.scientific_papers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.scientific_papers.service.PublishingProcessService;

@RestController
@RequestMapping(value = "/api/publishingProcess")
@CrossOrigin()
public class PublishingProcessController {

	@Autowired
	private PublishingProcessService publishingProcessService;

	@GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> findOne(@RequestParam(("paperId")) String paperId) throws Exception {
		String resource = publishingProcessService.findOneByPaperId(paperId);
		return new ResponseEntity<>(resource, HttpStatus.OK);
	}

}
