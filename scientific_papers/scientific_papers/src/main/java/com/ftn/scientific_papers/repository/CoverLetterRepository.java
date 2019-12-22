package com.ftn.scientific_papers.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.modules.XMLResource;

import com.ftn.scientific_papers.exceptions.ResourceNotFoundException;
import com.ftn.scientific_papers.util.DBManager;

@Repository
public class CoverLetterRepository {
	
	@Autowired
	private DBManager dbManager;
	
	static String spCollectionId = "/db/sample/cover_letters";
	static String spSchemaPath = "src/main/resources/xsd/cover_letter.xsd"; 
	
	public XMLResource findOne(String id) throws Exception {
		
		 XMLResource result = dbManager.findOne(spCollectionId, id);
		 if(result == null) {
			 throw new ResourceNotFoundException("cover letter with id " + id + " was not found");
		 }
		 return result;
	}
	
	public void save(String scientificPaperXml) throws Exception {
		// generate id
		String id = "letter0";
		try {
			ResourceSet rs = dbManager.executeXQuery(spCollectionId, "count(/.)", "");
			id = "letter" + rs.getIterator().nextResource().getContent().toString();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		System.out.println("\nID: " + id);
		
		//save 
		dbManager.save(spCollectionId, id, scientificPaperXml);
		
	}
}
