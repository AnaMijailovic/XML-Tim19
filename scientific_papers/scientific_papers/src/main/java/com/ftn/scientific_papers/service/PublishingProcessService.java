package com.ftn.scientific_papers.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xmldb.api.modules.XMLResource;

import com.ftn.scientific_papers.dom.DOMParser;
import com.ftn.scientific_papers.repository.PublishingProcessRepository;
import com.ftn.scientific_papers.util.DBManager;
import com.ftn.scientific_papers.util.FileUtil;
import com.ftn.scientific_papers.util.XUpdateTemplate;

@Service
public class PublishingProcessService {
	
	@Value("${publishing-process-template-path}")
	private String templatePath;
	
	@Value("${publishing-process-schema-path}")
	private String schemaPath;
	
	@Value("${publishing-process-collection-id}")
	private String collectionId;

	@Autowired
	private PublishingProcessRepository publishingProcessRepository;
	
	@Autowired
	private DBManager dbManager;
	
	public XMLResource findOne(String id) throws Exception {
		return publishingProcessRepository.findOne(id);
	}
	
	public String createProcess(String paperId, String authorId) throws Exception {

		// Read xml template
		String template = FileUtil.readFile(templatePath);
		System.out.println("Template: " + template);

		Document document = DOMParser.buildDocument(template, schemaPath);

		// Set publishing process id
		String id = publishingProcessRepository.getNextId();

		NodeList nodeList = document.getElementsByTagName("publishing-process");
		Element paperElement = (Element) nodeList.item(0);
		paperElement.getAttributes().getNamedItem("id").setNodeValue(id);

		// Set scientific paper id
		NodeList paperIdNode = document.getElementsByTagName("scientific-paper-id");
		Element paperIdElement = (Element) paperIdNode.item(0);
		paperIdElement.setTextContent(paperId);

		// Set author id
		NodeList authorIdNode = document.getElementsByTagName("author-id");
		Element authorIdElement = (Element) authorIdNode.item(0);
		authorIdElement.setTextContent(authorId);

		// Convert Document to string and save to database
		String documentString = DOMParser.getStringFromDocument(document);
		System.out.println("Process to save: \n" + documentString);
		publishingProcessRepository.save(documentString, id);

		return id;
	}
	
	public void addCoverLetter(String processId, String coverLetterId) throws Exception {
		
		String updatePath = "/publishing-process/paper-version[last()]/cover-letter-id";
		String xUpdateExpression =  String.format(XUpdateTemplate.UPDATE, updatePath, coverLetterId);
		
		dbManager.executeXUpdate(collectionId, xUpdateExpression, processId);
		System.out.println("Proces after adding cover letter id: \n" + findOne(processId).getContent().toString());
		
	}

}
