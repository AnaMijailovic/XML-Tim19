package com.ftn.scientific_papers.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.modules.XMLResource;

import com.ftn.scientific_papers.exceptions.ResourceNotFoundException;
import com.ftn.scientific_papers.util.DBManager;

public class EvaluationFormRepository {
	
	@Autowired
	private DBManager manager;
	
	private final String EF_COLLECTION_ID = "/db/sample/scientific_papers";
	public final String EVALUATION_FORM_SCHEMA = "src/main/resources/xsd/evaluation_form.xsd";
	
	public XMLResource findOne(String id) throws Exception {
		
		 XMLResource result = manager.findOne(EF_COLLECTION_ID, id);
		 if(result == null) {
			 throw new ResourceNotFoundException("Evaluation form with id " + id + " was not found");
		 }
		 return result;
	}
	
	public void save(String evaluationFormXML) throws Exception {
		// generate id
		String id = "paper0";				
		try {
			ResourceSet rs = manager.executeXQuery(EF_COLLECTION_ID, "count(/.)", "");
			id = "paper" + rs.getIterator().nextResource().getContent().toString();
		}catch(Exception e) {
			
		}
		System.out.println("\nID: " + id);	
		//save 
		manager.save(EF_COLLECTION_ID, id, EVALUATION_FORM_SCHEMA);
		
	}

}
