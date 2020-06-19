package com.ftn.scientific_papers.service;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import com.ftn.scientific_papers.dom.DOMParser;
import com.ftn.scientific_papers.exceptions.CustomExceptionResponse;
import com.ftn.scientific_papers.exceptions.CustomUnexpectedException;
import com.ftn.scientific_papers.model.evaluation_form.EvaluationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.modules.XMLResource;

import com.ftn.scientific_papers.repository.CoverLetterRepository;
import com.ftn.scientific_papers.repository.EvaluationFormRepository;
import com.ftn.scientific_papers.util.XSLFOTransformer;

@Service
public class EvaluationFormService {
	
	@Autowired
	private EvaluationFormRepository evaluationFormRepository;

	@Value("${evaluation-form-schema-path}")
	private String evaluationFormSchemaPath;
	
	@Autowired
	private XSLFOTransformer xslfoTransformer;

	public XMLResource findOne(String id) throws Exception {
		
		return evaluationFormRepository.findOne(id);
	}


	public EvaluationForm findOneUnmarshalled(String reviewId) {
		return evaluationFormRepository.findOneUnmarshalled(reviewId);
	}

	public String save(String evaluationFormXML, String paperId, String reviewerId) {
		try {
			Document document = DOMParser.buildDocument(evaluationFormXML, evaluationFormSchemaPath);
			String evaluationFormId = evaluationFormRepository.getNextId();

			NodeList revaluationFormNodeList = document.getElementsByTagName("evaluation_form");
			Element evaluationFormElement = (Element) revaluationFormNodeList.item(0);
			//NamedNodeMap attributes = evaluationFormElement.getAttributes();
			//attributes.getNamedItem("id").setNodeValue(evaluationFormId);

			evaluationFormElement.setAttribute("id", evaluationFormId);
			evaluationFormElement.setAttribute("scientific_paper_id", paperId);
			evaluationFormElement.setAttribute("reviewer_id", reviewerId);

			String xmlFile = DOMParser.getStringFromDocument(document);
			System.out.println("New: \n" + xmlFile);

			evaluationFormRepository.save(xmlFile, evaluationFormId);
			return evaluationFormId;
		} catch (Exception e) {
			throw new CustomUnexpectedException("Exception while saving review");
		}
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
