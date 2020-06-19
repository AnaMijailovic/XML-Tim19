package com.ftn.scientific_papers.repository;

import java.util.HashMap;

import com.ftn.scientific_papers.dom.DOMParser;
import com.ftn.scientific_papers.exceptions.CustomExceptionResponse;
import com.ftn.scientific_papers.exceptions.CustomUnexpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.modules.XMLResource;

import com.ftn.scientific_papers.exceptions.ResourceNotFoundException;
import com.ftn.scientific_papers.util.DBManager;

@Repository
public class EvaluationFormRepository {
	public static final String EVALUATION_FORM_XSL_FO_PATH = "src/main/resources/xsl-fo/evaluation_form_fo.xsl";
	public static final String EVALUATION_FORM_XSL_PATH = "src/main/resources/xslt/evaluation_form.xsl";
	
	@Autowired
	private DBManager dbManager;
	
	@Value("${evaluation-form-collection-id}")
	private String evaluationFormCollectionId;

	public XMLResource findOne(String id) throws Exception {
		
		 XMLResource result = dbManager.findOne(evaluationFormCollectionId, id);
		 if(result == null) {
			 throw new ResourceNotFoundException("Evaluation form with id " + id + " was not found");
		 }
		 return result;
	}
	
	public void save(String evaluationFormXML, String evaluationFormId) {
		try {
			dbManager.save(evaluationFormCollectionId, evaluationFormId, evaluationFormXML);
		} catch (Exception e) {
			throw new CustomUnexpectedException("Exception while saving evaluation form");
		}
	}

	public String getNextId() {
		// generate id
		String id = "evaluationForm0";
		try {
			ResourceSet rs = dbManager.executeXQuery(evaluationFormCollectionId, "count(/.)", new HashMap<>(), "");
			id = "evaluationForm" + rs.getIterator().nextResource().getContent().toString();
		} catch (Exception e) {

		}
		return id;
	}
}
