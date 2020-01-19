package com.ftn.scientific_papers.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.modules.XMLResource;

import com.ftn.scientific_papers.exceptions.ResourceNotFoundException;
import com.ftn.scientific_papers.util.DBManager;

@Repository
public class EvaluationFormRepository {
	
	@Autowired
	private DBManager dbManager;
	
	@Value("${evaluation-form-collection-id}")
	private String evaluationFormCollectionId;
	
	@Value("${evaluation-form-schema-path}")
	private String evaluationFormSchemaPath;
	
	public XMLResource findOne(String id) throws Exception {
		
		 XMLResource result = dbManager.findOne(evaluationFormCollectionId, id);
		 if(result == null) {
			 throw new ResourceNotFoundException("Evaluation form with id " + id + " was not found");
		 }
		 return result;
	}
	
	public void save(String evaluationFormXML) throws Exception {
		// generate id
		String id = "paper0";				
		try {
			ResourceSet rs = dbManager.executeXQuery(evaluationFormCollectionId, "count(/.)", "");
			id = "paper" + rs.getIterator().nextResource().getContent().toString();
		}catch(Exception e) {
			
		}
		System.out.println("\nID: " + id);	
		//save 
		dbManager.save(evaluationFormCollectionId, id, evaluationFormXML);
		
	}

}
