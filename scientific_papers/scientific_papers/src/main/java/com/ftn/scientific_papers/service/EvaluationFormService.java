package com.ftn.scientific_papers.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xmldb.api.modules.XMLResource;

import com.ftn.scientific_papers.repository.EvaluationFormRepository;

@Service
public class EvaluationFormService {
	
	@Autowired
	private EvaluationFormRepository evaluationFormRepository;

public XMLResource findOne(String id) throws Exception {
		
		return evaluationFormRepository.findOne(id);
	}

	public void save(String evaluationFormXML) throws Exception {
	   // SAXParseExcetion is thrown when xml is not valid
       //Document document =  DOMParser.buildDocument(evaluationFormXML, evaluationFormRepository.EVALUATION_FORM_SCHEMA);
       
	   evaluationFormRepository.save(evaluationFormXML);

	}
	
}
