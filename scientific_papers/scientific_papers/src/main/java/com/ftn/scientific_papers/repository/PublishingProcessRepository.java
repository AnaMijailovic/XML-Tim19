package com.ftn.scientific_papers.repository;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ftn.scientific_papers.exceptions.CustomUnexpectedException;
import com.ftn.scientific_papers.exceptions.DatabaseException;
import com.ftn.scientific_papers.model.publishing_process.PublishingProcess;
import com.ftn.scientific_papers.model.user.TUser;
import org.exist.xmldb.EXistResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import com.ftn.scientific_papers.exceptions.ResourceNotFoundException;
import com.ftn.scientific_papers.util.DBManager;
import com.ftn.scientific_papers.util.FileUtil;
import com.ftn.scientific_papers.util.XUpdateTemplate;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

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

	public PublishingProcess findOneUnmarshalled(String id) {
		try {
			String xPath = String.format("/publishing-process[@id='%s']", id);
			ResourceSet result = dbManager.executeXPath(publishingProcessCollectionId, xPath);

			if (result == null) {
				return null;
			}

			ResourceIterator i = result.getIterator();
			Resource res = null;

			while (i.hasMoreResources()) {
				try {
					res = i.nextResource();
					PublishingProcess publishingProcess = unmarshallPublishingProcess(res.getContent().toString());
					return  publishingProcess;
				} finally {
					// don't forget to cleanup resources
					try {
						((EXistResource)res).freeResources();
					} catch (XMLDBException e) {
						e.printStackTrace();
					}
				}
			}

			return  null;

		} catch(Exception e) {
			throw new DatabaseException("Exception while getting publishing processes." + id);
		}
	}

	public List<PublishingProcess> getAll() {
		try {
			String xPath = "/publishing-process";
			ResourceSet result = dbManager.executeXPath(publishingProcessCollectionId, xPath);

			if (result == null) {
				return new ArrayList<PublishingProcess>();
			}

			ResourceIterator i = result.getIterator();
			Resource res = null;
			List<PublishingProcess> publishingProcesses = new ArrayList<>();

			while (i.hasMoreResources()) {
				try {
					res = i.nextResource();
					PublishingProcess publishingProcess = unmarshallPublishingProcess(res.getContent().toString());
					publishingProcesses.add(publishingProcess);
				} finally {
					// don't forget to cleanup resources
					try {
						((EXistResource)res).freeResources();
					} catch (XMLDBException e) {
						e.printStackTrace();
					}
				}
			}

			return  publishingProcesses;

		} catch(Exception e) {
			throw new DatabaseException("Exception while getting all publishing processes.");
		}
	}

	public String findOneByPaperId(String paperId) throws Exception {

		String xQueryPath = "./src/main/resources/xQuery/getProcessByPaperId.txt";
		
		HashMap<String, String> params = new HashMap<>();
		params.put("id", paperId);
		ResourceSet result = dbManager.executeXQuery(publishingProcessCollectionId, "", params, xQueryPath);
		
		if(result.getSize() == 0)
			throw new ResourceNotFoundException("Process for paper with id: " + paperId +  " was not found.");
		
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
		System.out.println("Proccessss id: " + processId);
		HashMap<String, String> params = new HashMap<>();
		params.put("id", processId);
		ResourceSet rs = dbManager.executeXQuery(publishingProcessCollectionId, "", params, xQueryPath);
		String author = rs.getIterator().nextResource().getContent().toString();
		System.out.println("Author: " + author);
		return author;
	}
	
	public String getLetterByPaperId(String paperId) throws Exception{
		String xQueryPath = "./src/main/resources/xQuery/getLetterByPaperId.txt";

		HashMap<String, String> params = new HashMap<>();
		params.put("id", paperId);
		ResourceSet rs = dbManager.executeXQuery(publishingProcessCollectionId, "", params, xQueryPath);
		String letterId = rs.getIterator().nextResource().getContent().toString();
		System.out.println("Letter id: " + letterId);
		return letterId;
	}

	public void updateLatestVersion(String processId, String newVersion) throws Exception {

		String updatePath = "/publishing-process/@latestVersion";
		String xUpdateExpression = String.format(XUpdateTemplate.UPDATE, updatePath, newVersion);

		dbManager.executeXUpdate(publishingProcessCollectionId, xUpdateExpression, processId);
		System.out.println("Process after updating latestVersion: \n" + findOne(processId).getContent().toString());
	}

	public void updateStatus(String processId, String newStatus) throws Exception {

		String updatePath = "/publishing-process/@status";
		String xUpdateExpression = String.format(XUpdateTemplate.UPDATE, updatePath, newStatus);

		dbManager.executeXUpdate(publishingProcessCollectionId, xUpdateExpression, processId);
		System.out.println("Process after updating status: \n" + findOne(processId).getContent().toString());
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

	public void update(PublishingProcess publishingProcess) {
		try {
			String userXML = marshallPublishingProcess(publishingProcess);

			dbManager.save(publishingProcessCollectionId, publishingProcess.getId(),  userXML);

		} catch (JAXBException e) {
			throw new DatabaseException("An error occured while marshalling publishing process.");
		} catch (Exception e) {
			throw new DatabaseException("An error occured while updating publishing process.");
		}
	}

	public void assignEditor(String processId, String userId) {
		try {
			XMLResource result = dbManager.findOne(publishingProcessCollectionId, processId);
			if (result == null) {
				throw new ResourceNotFoundException("Publishing process with id " + processId + " was not found");
			}

			String updatePath = "/publishing-process/editor-id";
			String xUpdateExpression = String.format(XUpdateTemplate.UPDATE, updatePath, userId);
			dbManager.executeXUpdate(publishingProcessCollectionId, xUpdateExpression, processId);
			System.out.println("Process after updating status: \n" + result.getContent().toString());

		} catch (Exception e) {
			throw new CustomUnexpectedException("Exception occurred while assigning editor " + userId + " to process " + processId);
		}
	}

	private PublishingProcess unmarshallPublishingProcess(String publishingProcessXML) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(PublishingProcess.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();

		return (PublishingProcess) unmarshaller.unmarshal(new StringReader(publishingProcessXML));
	}

	private String marshallPublishingProcess(PublishingProcess publishingProcess) throws Exception {
		JAXBContext context = JAXBContext.newInstance(PublishingProcess.class);
		Marshaller marshaller = context.createMarshaller();

		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		marshaller.marshal(publishingProcess, stream);

		return new String(stream.toByteArray());
	}
}
