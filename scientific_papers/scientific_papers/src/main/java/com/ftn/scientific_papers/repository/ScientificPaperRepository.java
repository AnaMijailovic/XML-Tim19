package com.ftn.scientific_papers.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.modules.XMLResource;

import com.ftn.scientific_papers.exceptions.ResourceNotFoundException;
import com.ftn.scientific_papers.util.DBManager;

@Repository
public class ScientificPaperRepository {
	
	@Autowired
	private DBManager dbManager;
	
	@Value("${scientific-paper-collection-id}")
	private String scientificPaperCollectionId;
	
	@Value("${scientific-paper-schema-path}")
	private String scientificPaperSchemaPath; 
	
	public XMLResource findOne(String id) throws Exception {
		
		 XMLResource result = dbManager.findOne(scientificPaperCollectionId, id);
		 if(result == null) {
			 throw new ResourceNotFoundException("Scientific paper with id " + id + " was not found");
		 }
		 return result;
	}
	
	public String getAll() {
		String xQueryPath = ".\\src\\main\\resources\\xQuery\\textSearch.txt";
		String result = "";
		try {
			ResourceSet rs = dbManager.executeXQuery(scientificPaperCollectionId, "", xQueryPath);
			result = dbManager.resourceSetToString(rs);
			// System.out.println("Result: " + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public void save(String scientificPaperXml) throws Exception {
		// generate id
		String id = "paper0";				
		try {
			ResourceSet rs = dbManager.executeXQuery(scientificPaperCollectionId, "count(/.)", "");
			id = "paper" + rs.getIterator().nextResource().getContent().toString();
		}catch(Exception e) {
			
		}
		System.out.println("\nID: " + id);	
		//save 
		dbManager.save(scientificPaperCollectionId, id, scientificPaperXml);
		
	}
}
