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
public class PublishingProcessRepository {

	@Autowired
	private DBManager dbManager;

	@Value("${publishing-process-collection-id}")
	private String publishingProcessCollectionId;

	public XMLResource findOne(String id) throws Exception {

		XMLResource result = dbManager.findOne(publishingProcessCollectionId, id);
		if (result == null) {
			throw new ResourceNotFoundException("Publishing process with id " + id + " was not found");
		}
		return result;
	}

	public String getNextId() {

		String id = "process0";
		try {
			ResourceSet rs = dbManager.executeXQuery(publishingProcessCollectionId, "count(/.)", new HashMap<>(), "");
			id = "process" + rs.getIterator().nextResource().getContent().toString();
		} catch (Exception e) {

		}
		System.out.println("\nID: " + id);

		return id;
	}

	public void save(String publishingProcessXml, String id) throws Exception {
		// save
		dbManager.save(publishingProcessCollectionId, id, publishingProcessXml);

	}
	
	public void update(String publishingProcessXml, String id) throws Exception {

		dbManager.save(publishingProcessCollectionId, id, publishingProcessXml);

	}
}
