package com.ftn.scientific_papers.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xmldb.api.modules.XMLResource;

import com.ftn.scientific_papers.dom.DOMParser;
import com.ftn.scientific_papers.dto.SearchData;
import com.ftn.scientific_papers.exceptions.MaxChapterLevelsExcedeedException;
import com.ftn.scientific_papers.exceptions.RevisionForbiddenException;
import com.ftn.scientific_papers.fuseki.FusekiManager;
import com.ftn.scientific_papers.fuseki.FusekiReader;
import com.ftn.scientific_papers.fuseki.MetadataExtractor;
import com.ftn.scientific_papers.repository.PublishingProcessRepository;
import com.ftn.scientific_papers.repository.ScientificPaperRepository;

@Service
public class ScientificPaperService {

	static String spSchemaPath = "src/main/resources/xsd/scientific_paper.xsd";
	private static final String QUERY_FILE_PATH = "src/main/resources/sparql/metadataSearch.rq";
	
	@Value("${max-chapter-levels}")
	private int maxChapterLevels;
	
	@Autowired
	private ScientificPaperRepository spRepository;
	
	@Autowired
	private PublishingProcessRepository publishingProcessRepository;

 	@Autowired
 	private PublishingProcessService publishingProcessService;

	@Autowired
	private MetadataExtractor metadataExtractor;

	@Autowired
	private FusekiManager fusekiManager;

	public XMLResource findOne(String id) throws Exception {

		return spRepository.findOne(id);
	}

	public String getAll(String searchText) {
		return spRepository.getAll(searchText);
	}

	public String getByIds(Set<String> ids) {

		StringBuilder sb = new StringBuilder();
		sb.append("<search>");

		ids.forEach(id -> sb.append(spRepository.getById(id)));

		sb.append("</search>");

		System.out.println("Full result:  \n" + sb.toString());
		return sb.toString();
	}

	// sparql query returns a set of urls of matching papers
	// xQuerys are then executed to find papers with that ids
	public String metadataSearch(SearchData searchData) throws IOException {

		HashMap<String, String> values = new HashMap<>();

		values.put("keyword", searchData.getKeyword());
		values.put("title", searchData.getTitle());
		values.put("author", searchData.getAuthor());
		values.put("affiliation", searchData.getAffiliation());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String fromDate = searchData.getFromDate() == null ? "" : sdf.format(searchData.getFromDate());
		String toDate = searchData.getToDate() == null ? "" : sdf.format(searchData.getToDate());
		values.put("fromDate", fromDate);
		values.put("toDate", toDate);

		// execute sparql query
		Set<String> paperURLs = FusekiReader.executeQuery(QUERY_FILE_PATH, values);
		Set<String> paperIds = getIdsFromUrls(paperURLs);

		return getByIds(paperIds);

	}

	// get paper ids from full urls
	// url example:
	// https://github.com/AnaMijailovic/XML-Tim19/scientific_papers/paper0
	// id: paper0
	public Set<String> getIdsFromUrls(Set<String> paperURLs) {
		Set<String> paperIds = new HashSet<>();

		for (String url : paperURLs) {
			String[] idArray = url.split("/");
			String id = idArray[idArray.length - 1];
			paperIds.add(id);
			System.out.println("Paper Id: " + id);
		}

		return paperIds;
	}

	public void generateIds(Document document, String paperId) throws MaxChapterLevelsExcedeedException {

		// Set abstract id
		Element abstractEl = (Element) document.getElementsByTagName("abstract").item(0);
		abstractEl.setAttribute("id", paperId + "/abstract");

		// Set chapters ids

		NodeList chapters = document.getElementsByTagName("chapter");
		
		for (int i = 0; i < chapters.getLength(); i++) {
			
			Element chapter = (Element) chapters.item(i);
			
			// only if  parent is body element! -> 1. level chapter
			if (chapter.getParentNode().getNodeName().equals("body")) {
				String id = paperId + "/chapter" + i;
				chapter.setAttribute("id", id);
				
				// Set paragraphs ids
				NodeList paragraphs = chapter.getElementsByTagName("paragraph");
				for (int j = 0; j < paragraphs.getLength(); j++) {
					Element paragraph = (Element) paragraphs.item(j);
					paragraph.setAttribute("id", id + "/paragraph" + j);
				}
				
				setSubchapterIds(chapter, id, 1);
					
			}
						
		}

		// Set images ids
		NodeList images = document.getElementsByTagName("image");
		for (int i = 0; i < images.getLength(); i++) {
			Element image = (Element) images.item(i);
			image.setAttribute("id", paperId + "/image" + i);
		}

		// Set tables ids
		NodeList tables = document.getElementsByTagName("table");
		for (int i = 0; i < tables.getLength(); i++) {
			Element table = (Element) tables.item(i);
			table.setAttribute("id", paperId + "/table" + i);
		}

	}
	
	// recursively set subchapter ids
	// check max chapter level
	public void setSubchapterIds(Element chapter, String parentId, int levelCount) throws MaxChapterLevelsExcedeedException {
		
		if(levelCount > maxChapterLevels) 
			throw new MaxChapterLevelsExcedeedException("");
		
		NodeList subchapters = chapter.getElementsByTagName("chapter");

		for (int i = 0; i < subchapters.getLength(); i++) {
			
			Element subchapter = (Element) subchapters.item(i);
			
			// only if parent is chapter with the right id -> only next level chapters
			if (((Element) subchapter.getParentNode()).getAttribute("id").equals(parentId)) {
				String id = parentId + "/subchapter" + i;
				subchapter.setAttribute("id", id);
				
				// Set paragraph ids
				NodeList paragraphs = subchapter.getElementsByTagName("paragraph");
				for (int j = 0; j < paragraphs.getLength(); j++) {
					Element paragraph = (Element) paragraphs.item(j);
					paragraph.setAttribute("id", id + "/paragraph" + j);
				}
				
				setSubchapterIds(subchapter, id, levelCount+1);
					
			}
						
		}
		
	}

	public String save(String scientificPaperXml, String paperVersion) throws Exception {

		// SAXParseExcetion is thrown when xml is not valid
		Document document = DOMParser.buildDocument(scientificPaperXml, spSchemaPath);

		// Get id
		String id = spRepository.getNextId();
		
		// set ids and check chapter levels
		generateIds(document, id);
		
		// change id in xml document -> needed for metadata search
		// get scientific_paper element from dom and set id attribute value
		NodeList paperNodeList = document.getElementsByTagName("scientific_paper");
		Element paperElement = (Element) paperNodeList.item(0);
		paperElement.getAttributes().getNamedItem("id").setNodeValue(id);
		
		// set paper version
		paperElement.setAttribute("version", paperVersion.toString());

		// get head element from dom and set rdfa:about attribute value
		NodeList headNodeList = paperElement.getElementsByTagName("head");
		Element headElement = (Element) headNodeList.item(0);

		String aboutAttributeValue = "https://github.com/AnaMijailovic/XML-Tim19/scientific_papers/" + id;
		headElement.getAttributes().getNamedItem("rdfa:about").setNodeValue(aboutAttributeValue);

		// get author elements and change rdfa:href attribute value
		NodeList authorsNodeList = paperElement.getElementsByTagName("author");

		for (int i = 0; i < authorsNodeList.getLength(); i++) {
			Element authorElement = (Element) authorsNodeList.item(i);
			authorElement.getAttributes().getNamedItem("rdfa:href").setNodeValue(aboutAttributeValue);
		}

		// Convert Document to string and save
		String newXml = DOMParser.getStringFromDocument(document);
		System.out.println("New: \n" + newXml);
		spRepository.save(newXml, id);

		// Save metadata
		String rdfFilePath = "src/main/resources/rdf/newMetadata.rdf";
		metadataExtractor.extractMetadata(newXml, rdfFilePath);
		fusekiManager.saveMetadata(rdfFilePath, "/scientificPapers");

		return id;

	}
	
	public void addPaperRevision(String processId, String scientificPaperXml) throws Exception {
		
		// TODO check author

		// check process status
		String processStatus = publishingProcessRepository.getProcessStatus(processId);
		if(!processStatus.equals("NEW_REVISION")) {
			throw new RevisionForbiddenException("");
		 } 
		
		// get paper latest version from process
	    Integer latestVersion = Integer.valueOf(publishingProcessRepository.getProcessLatestVersion(processId));
		String newVersion = Integer.toString(latestVersion + 1);
		// String newVersion = "33";
		
		String paperVersionId = save(scientificPaperXml, newVersion);
				
		publishingProcessService.addNewPaperVersion(processId, paperVersionId);
		publishingProcessRepository.updateLatestVersion(processId, newVersion);
		publishingProcessRepository.updateStatus(processId, "NEW_SUBMISSION");
		
	}

}
