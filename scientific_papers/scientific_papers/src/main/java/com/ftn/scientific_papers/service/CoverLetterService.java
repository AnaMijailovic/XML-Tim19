package com.ftn.scientific_papers.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xmldb.api.modules.XMLResource;

import com.ftn.scientific_papers.dom.DOMParser;
import com.ftn.scientific_papers.repository.CoverLetterRepository;

@Service
public class CoverLetterService {
	
	static String coverLetterSchemaPath = "src/main/resources/xsd/cover_letter.xsd";
	
	@Autowired
	private CoverLetterRepository coverLetterRepository;

	public XMLResource findOne(String id) throws Exception {
		
		return coverLetterRepository.findOne(id);
	}

	public void save(String scientificPaperXml) throws Exception {
		
	   // SAXParseExcetion is thrown when xml is not valid
       Document document =  DOMParser.buildDocument(scientificPaperXml, coverLetterSchemaPath);
   
       	// TODO Generate ids for chapters, paragraphs etc. 
        // TODO Check chapter levels (max is 5)
       
       coverLetterRepository.save(scientificPaperXml);

	}
}
