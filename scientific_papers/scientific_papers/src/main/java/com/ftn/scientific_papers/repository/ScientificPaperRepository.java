package com.ftn.scientific_papers.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.modules.XMLResource;

import com.ftn.scientific_papers.exceptions.ResourceNotFoundException;
import com.ftn.scientific_papers.util.DBManager;

@Repository
public class ScientificPaperRepository {
	
	@Autowired
	private DBManager dbManager;
	
	static String spCollectionId = "/db/sample/scientific_papers";
	static String spSchemaPath = "src/main/resources/xsd/scientific_paper.xsd"; 
	
	public XMLResource findOne(String id) throws Exception {
		
		 XMLResource result = dbManager.findOne(spCollectionId, id);
		 if(result == null) {
			 throw new ResourceNotFoundException("Scientific paper with id " + id + " was not found");
		 }
		 return result;
	}
	
	public void save(String scientificPaperXml) throws Exception {
		
		// generate id
		ResourceSet rs = dbManager.executeXQuery(spCollectionId, "count(/.)", "");
		String id = "paper" + rs.getIterator().nextResource().getContent().toString();
		System.out.println("\nID: " + id);
		
		//save 
		dbManager.save(spCollectionId, id, scientificPaperXml);
		
	}
}
