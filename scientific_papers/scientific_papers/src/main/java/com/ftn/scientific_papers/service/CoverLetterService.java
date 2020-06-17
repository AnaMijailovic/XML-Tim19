package com.ftn.scientific_papers.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xmldb.api.modules.XMLResource;

import com.ftn.scientific_papers.dom.DOMParser;
import com.ftn.scientific_papers.repository.CoverLetterRepository;
import com.ftn.scientific_papers.util.FileUtil;

@Service
public class CoverLetterService {
	
	static String coverLetterSchemaPath = "src/main/resources/xsd/cover_letter.xsd";
	
	@Value("${cover-letter-template-path}")
	private String clTemplatePath;
	
	@Autowired
	private CoverLetterRepository coverLetterRepository;

	public XMLResource findOne(String id) throws Exception {
		
		return coverLetterRepository.findOne(id);
	}
	
	public String getTemplate() throws IOException {
		
		return FileUtil.readFile(clTemplatePath);
	}

	public String save(String scientificPaperXml) throws Exception {
		
	   // SAXParseExcetion is thrown when xml is not valid
       Document document =  DOMParser.buildDocument(scientificPaperXml, coverLetterSchemaPath);
       
       return coverLetterRepository.save(scientificPaperXml);

	}
}
