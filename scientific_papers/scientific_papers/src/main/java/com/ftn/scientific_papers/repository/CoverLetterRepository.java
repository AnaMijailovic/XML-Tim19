package com.ftn.scientific_papers.repository;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.modules.XMLResource;

import com.ftn.scientific_papers.exceptions.ResourceNotFoundException;
import com.ftn.scientific_papers.util.DBManager;

@Repository
public class CoverLetterRepository {
	
	@Autowired
	private DBManager dbManager;
	
	@Value("${cover-letter-collection-id}")
	private String coverLetterCollectionId;
	
	@Value("${cover-letter-schema-path}")
	private String coverLetterSchemaPath;
	
	public XMLResource findOne(String id) throws Exception {
		
		 XMLResource result = dbManager.findOne(coverLetterCollectionId, id);
		 if(result == null) {
			 throw new ResourceNotFoundException("cover letter with id " + id + " was not found");
		 }
		 return result;
	}
	
	public String save(String scientificPaperXml) throws Exception {
		// generate id
		String id = "letter0";
		try {
			ResourceSet rs = dbManager.executeXQuery(coverLetterCollectionId, "count(/.)", new HashMap<>(), "");
			id = "letter" + rs.getIterator().nextResource().getContent().toString();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		System.out.println("\nID: " + id);
		
		//save 
		dbManager.save(coverLetterCollectionId, id, scientificPaperXml);
		return id;
		
	}
}
