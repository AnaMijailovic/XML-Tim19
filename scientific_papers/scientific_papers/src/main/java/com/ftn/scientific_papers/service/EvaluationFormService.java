package com.ftn.scientific_papers.service;

import java.io.ByteArrayOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xmldb.api.modules.XMLResource;

import com.ftn.scientific_papers.repository.CoverLetterRepository;
import com.ftn.scientific_papers.repository.EvaluationFormRepository;
import com.ftn.scientific_papers.util.XSLFOTransformer;

@Service
public class EvaluationFormService {
	
	@Autowired
	private EvaluationFormRepository evaluationFormRepository;
	
	@Autowired
	private XSLFOTransformer xslfoTransformer;

	public XMLResource findOne(String id) throws Exception {
		
		return evaluationFormRepository.findOne(id);
	}

	public void save(String evaluationFormXML) throws Exception {
	   // SAXParseExcetion is thrown when xml is not valid
       //Document document =  DOMParser.buildDocument(evaluationFormXML, evaluationFormRepository.EVALUATION_FORM_SCHEMA);
       
	   evaluationFormRepository.save(evaluationFormXML);

	}
	
	public XMLResource findOneXml(String id) throws Exception {
		XMLResource evaluationFormXml = evaluationFormRepository.findOne(id);
		return evaluationFormXml;
	}
  
    public byte[] findOnePdf(String id) throws Exception {
		String xmlString = evaluationFormRepository.findOne(id).getContent().toString();
		String xslString = EvaluationFormRepository.EVALUATION_FORM_XSL_FO_PATH;
		ByteArrayOutputStream evaluationFormPdf = xslfoTransformer.generatePDF(xmlString, xslString); 
		return evaluationFormPdf.toByteArray();
    }
    
    public byte[] findOneHtml(String id) throws Exception {
    	String xmlString = evaluationFormRepository.findOne(id).getContent().toString();
    	String xslString = EvaluationFormRepository.EVALUATION_FORM_XSL_PATH;
    	ByteArrayOutputStream evaluationFormHtml = xslfoTransformer.generateHTML(xmlString, xslString); 
		return evaluationFormHtml.toByteArray();
	}
	
}
