package com.ftn.scientific_papers.repository;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.modules.XMLResource;

import com.ftn.scientific_papers.exceptions.ResourceNotFoundException;
import com.ftn.scientific_papers.util.DBManager;
import com.ftn.scientific_papers.util.FileUtil;
import com.ftn.scientific_papers.util.XUpdateTemplate;

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
	
	public String findOneByPaperId(String paperId) throws Exception {

		String xQueryPath = "./src/main/resources/xQuery/getProcessByPaperId.txt";
		
		HashMap<String, String> params = new HashMap<>();
		params.put("id", paperId);
		ResourceSet result = dbManager.executeXQuery(publishingProcessCollectionId, "", params, xQueryPath);
		
		if(result.getSize() == 0)
			throw new ResourceNotFoundException("Proces for paper with id: " + paperId +  " was not found.");
		
		String processId = result.getIterator().nextResource().getContent().toString();
		System.out.println("ProcessId from xQuery: " + processId);
		return processId;
	}

	public String getProcessStatus(String processId) throws Exception {

		String query = FileUtil.readFile("./src/main/resources/xQuery/getProcessStatus.txt");
		HashMap<String, String> params = new HashMap<>();
		params.put("id", processId);
		System.out.println("Process id: " + processId);
		ResourceSet rs = dbManager.executeXQuery(publishingProcessCollectionId, query, params, "");
		System.out.println("status size " + rs.getSize());
		String status = rs.getIterator()
				.nextResource()
				.getContent()
				.toString();
		System.out.println("Status from xQuery: " + status);
		return status;
	}

	public String getProcessLatestVersion(String processId) throws Exception {

		String xQueryPath = "./src/main/resources/xQuery/getProcessLatestVersion.txt";
		
		HashMap<String, String> params = new HashMap<>();
		params.put("id", processId);
		ResourceSet rs = dbManager.executeXQuery(publishingProcessCollectionId, "", params, xQueryPath);
		System.out.println("version size " + rs.getSize());
		String latestVersion = rs.getIterator().nextResource().getContent().toString();
		System.out.println("Latest version from xQuery: " + latestVersion);
		return latestVersion;
	}
	
	public String getAuthorFromProcess(String processId) throws Exception {

		String xQueryPath = "./src/main/resources/xQuery/getAuthorFromProcess.txt";
		
		HashMap<String, String> params = new HashMap<>();
		params.put("id", processId);
		ResourceSet rs = dbManager.executeXQuery(publishingProcessCollectionId, "", params, xQueryPath);
		String author = rs.getIterator().nextResource().getContent().toString();
		System.out.println("Author: " + author);
		return author;
	}

	public void updateLatestVersion(String processId, String newVersion) throws Exception {

		String updatePath = "/publishing-process/@latestVersion";
		String xUpdateExpression = String.format(XUpdateTemplate.UPDATE, updatePath, newVersion);

		dbManager.executeXUpdate(publishingProcessCollectionId, xUpdateExpression, processId);
		System.out.println("Proces after updating latestVersion: \n" + findOne(processId).getContent().toString());
	}

	public void updateStatus(String processId, String newStatus) throws Exception {

		String updatePath = "/publishing-process/@status";
		String xUpdateExpression = String.format(XUpdateTemplate.UPDATE, updatePath, newStatus);

		dbManager.executeXUpdate(publishingProcessCollectionId, xUpdateExpression, processId);
		System.out.println("Proces after updating status: \n" + findOne(processId).getContent().toString());
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
