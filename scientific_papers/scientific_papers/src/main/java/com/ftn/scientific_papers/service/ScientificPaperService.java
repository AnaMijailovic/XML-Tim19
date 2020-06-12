package com.ftn.scientific_papers.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xmldb.api.modules.XMLResource;

import com.ftn.scientific_papers.dom.DOMParser;
import com.ftn.scientific_papers.dto.SearchData;
import com.ftn.scientific_papers.fuseki.FusekiManager;
import com.ftn.scientific_papers.fuseki.FusekiReader;
import com.ftn.scientific_papers.fuseki.MetadataExtractor;
import com.ftn.scientific_papers.repository.ScientificPaperRepository;

@Service
public class ScientificPaperService {
	
	static String spSchemaPath = "src/main/resources/xsd/scientific_paper.xsd";
	private static final String QUERY_FILE_PATH = "src/main/resources/sparql/metadataSearch.rq";
	
	@Autowired
	private ScientificPaperRepository spRepository;
	
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
		
		String fromDate = searchData.getFromDate() == null ? "" :  sdf.format(searchData.getFromDate());
		String toDate = searchData.getToDate() == null ? "" :  sdf.format(searchData.getToDate());
		values.put("fromDate", fromDate);		
		values.put("toDate", toDate);
		
		// execute sparql query
		Set<String> paperURLs = FusekiReader.executeQuery(QUERY_FILE_PATH, values);
		Set<String> paperIds = getIdsFromUrls(paperURLs);
		
		return getByIds(paperIds);
		
	}
	
	// get paper ids from full urls
	// url example: https://github.com/AnaMijailovic/XML-Tim19/scientific_papers/paper0
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

	public void save(String scientificPaperXml) throws Exception {
		
	   // SAXParseExcetion is thrown when xml is not valid
       Document document =  DOMParser.buildDocument(scientificPaperXml, spSchemaPath);
       
       	// TODO Generate ids for chapters, paragraphs etc. 
        // TODO Check chapter levels (max is 5)
       
       // Save to get id
	   String id = spRepository.save(scientificPaperXml);
	   
	   // change id in xml document	-> needed for metadata search   
	   // get scientific_paper element from dom and set id attribute value
	   NodeList paperNodeList = document.getElementsByTagName("scientific_paper");
	   Element paperElement = (Element) paperNodeList.item(0);
	   paperElement.getAttributes().getNamedItem("id").setNodeValue(id);
	   
	   // get head element from dom and set rdfa:about attribute value
	   NodeList headNodeList = paperElement.getElementsByTagName("head");
	   Element headElement = (Element) headNodeList.item(0);

	   String aboutAttributeValue = "https://github.com/AnaMijailovic/XML-Tim19/scientific_papers/" + id;
	   headElement.getAttributes().getNamedItem("rdfa:about").setNodeValue(aboutAttributeValue);
	   
	   // get author elements and change rdfa:href attribute value
	   NodeList authorsNodeList = paperElement.getElementsByTagName("author");

	   for(int i = 0; i < authorsNodeList.getLength(); i++) {
		   Element authorElement = (Element) authorsNodeList.item(i);		   
		   authorElement.getAttributes().getNamedItem("rdfa:href").setNodeValue(aboutAttributeValue);
	   }
	  
	   // Convert Document to string and update
	   String newXml = DOMParser.getStringFromDocument(document);
	   System.out.println("New: \n" +  newXml);
	   spRepository.update(newXml, id);
	   
	   // Save metadata 
       String rdfFilePath = "src/main/resources/rdf/newMetadata.rdf";
       metadataExtractor.extractMetadata(newXml, rdfFilePath);
       fusekiManager.saveMetadata(rdfFilePath, "/scientificPapers");

	}
}
