package com.ftn.scientific_papers.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
import com.ftn.scientific_papers.service.CoverLetterService;
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
	private CoverLetterService coverLetterService;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TokenUtils tokenUtils;

	@GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> findAll(@RequestParam(defaultValue = "") String searchText,
			@RequestParam(defaultValue = "") String title, @RequestParam(defaultValue = "") String author,
			@RequestParam(defaultValue = "") String affiliation, @RequestParam(defaultValue = "") String keyword,
			@RequestParam(defaultValue = "") String loggedAuthor, @RequestParam(required = false) Long acceptedFromDate, // timestamp
			@RequestParam(required = false) Long acceptedToDate, // timestamp
			@RequestParam(required = false) Long recievedFromDate, // timestamp
			@RequestParam(required = false) Long recievedToDate // timestamp
	) throws IOException {
		if (!loggedAuthor.equals(""))
			loggedAuthor = tokenUtils.getUsernameFromRequest(request);

		System.out.println("Title: " + title + " Author: " + author + " Affiliation: " + affiliation + " Keyword: "
				+ keyword + "Accepted From Date: " + acceptedFromDate + "Accepted To Date: " + acceptedToDate
				+ "Recieved from: " + recievedFromDate + "Recieved to: " + recievedToDate + " Logged author: "
				+ loggedAuthor);
		String resource = "";
		if (title.equals("") && author.equals("") && affiliation.equals("") && keyword.equals("")
				&& acceptedFromDate == null && acceptedToDate == null && recievedFromDate == null
				&& recievedToDate == null) {
			resource = spService.getAll(searchText, loggedAuthor);
		} else {
			SearchData searchData = new SearchData(title, author, affiliation, keyword, acceptedFromDate,
					acceptedToDate, recievedFromDate, recievedToDate);
			resource = spService.metadataSearch(searchData, loggedAuthor);
		}

		return new ResponseEntity<>(resource, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> findOne(@PathVariable("id") String id) throws Exception {
		XMLResource resource = spService.findOne(id);

		return new ResponseEntity<>(resource.getContent().toString(), HttpStatus.OK);
	}

	@GetMapping(value = "/template", produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> getScientificPaperTemplate() throws Exception {

		return new ResponseEntity<>(spService.getTemplate(), HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}/quotes", produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> getQuotedBy(@PathVariable("id") String paperId) throws Exception {
		return new ResponseEntity<>(spService.getQuotedBy(paperId), HttpStatus.OK);
	}

	@GetMapping(value = "/{id}/coverLetter/xml", produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> getCoverLetterXml(@PathVariable("id") String paperId) throws Exception {
		String coverLetterId = publishingProcessService.getLetterByPaperId(paperId);
		String letterXml = coverLetterService.findOneXml(coverLetterId).getContent().toString();
		return new ResponseEntity<>(letterXml, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}/coverLetter/html", produces = MediaType.TEXT_HTML_VALUE)
	public ResponseEntity<String> getCoverLetterHtml(@PathVariable("id") String paperId) throws Exception {
		String coverLetterId = publishingProcessService.getLetterByPaperId(paperId);
		byte[] resource = coverLetterService.findOneHtml(coverLetterId);
		return new ResponseEntity<>(new String(resource), HttpStatus.OK);
	}

	@GetMapping(value = "/{id}/coverLetter/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> getCoverLetterPdf(@PathVariable("id") String paperId) throws Exception {
		String coverLetterId = publishingProcessService.getLetterByPaperId(paperId);
		byte[] contents = coverLetterService.findOnePdf(coverLetterId);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.add("Content-Disposition", "inline; filename=" + coverLetterId + ".pdf");
		return new ResponseEntity<>(contents, headers, HttpStatus.OK);

	}

	@GetMapping(value = "/metadata/xml/{id}", produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> getMetadataXml(@PathVariable("id") String id) throws Exception {
		String metadata = spService.getMetadataXml(id);
		return new ResponseEntity<>(metadata, HttpStatus.OK);
	}

	@GetMapping(value = "/metadata/json/{id}", produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> getMetadataJson(@PathVariable("id") String id) throws Exception {
		String metadata = spService.getMetadataJson(id);
		return new ResponseEntity<>(metadata, HttpStatus.OK);
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
	@PostMapping(value = "/revision", consumes = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> addPaperRevision(@RequestParam(("processId")) String processId,
			@RequestBody String scientificPaperXml) throws Exception {

		// Get author username from token
		String username = tokenUtils.getUsernameFromRequest(request);
		spService.addPaperRevision(processId, scientificPaperXml, username);
		return new ResponseEntity<>(processId, HttpStatus.CREATED);
	}

	@PreAuthorize("hasRole('ROLE_AUTHOR')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<String> withdrawScientificPaper(@PathVariable("id") String paperId) throws Exception {

		// Get author username from token
		String username = tokenUtils.getUsernameFromRequest(request);
		spService.withdrawScientificPaper(paperId, username);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping(value = "/xml/{id}", produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> findOneXml(@PathVariable("id") String id) throws Exception {
		XMLResource resource = spService.findOneXml(id);
		return new ResponseEntity<>(resource.getContent().toString(), HttpStatus.OK);
	}

	@GetMapping(value = "/html/{id}", produces = MediaType.TEXT_HTML_VALUE)
	public ResponseEntity<String> findOneHtml(@PathVariable("id") String id) throws Exception {
		byte[] resource = spService.findOneHtml(id);
		return new ResponseEntity<>(new String(resource), HttpStatus.OK);
	}

	@GetMapping(value = "/pdf/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> findOnePdf(@PathVariable("id") String id) throws Exception {

		byte[] contents = spService.findOnePdf(id);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.add("Content-Disposition", "inline; filename=" + id + ".pdf");
		ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
		return response;
	}
	
	@GetMapping(value = "/anonymous/html/{id}", produces = MediaType.TEXT_HTML_VALUE)
	public ResponseEntity<String> anonymousFindOneHtml(@PathVariable("id") String id) throws Exception {
		byte[] resource = spService.anonymousFindOneHtml(id);
		return new ResponseEntity<>(new String(resource), HttpStatus.OK);
	}

	@GetMapping(value = "/anonymous/pdf/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> anonymousFindOnePdf(@PathVariable("id") String id) throws Exception {

		byte[] contents = spService.anonymousFindOnePdf(id);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.add("Content-Disposition", "inline; filename=" + id + ".pdf");
		ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
		return response;
	}
}
