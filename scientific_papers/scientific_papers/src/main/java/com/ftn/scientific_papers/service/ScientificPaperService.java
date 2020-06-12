package com.ftn.scientific_papers.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
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
	
	public void metadataSearch(SearchData searchData) throws IOException {

		HashMap<String, String> values = new HashMap<>();
		
		values.put("keyword", searchData.getKeyword());
		values.put("title", searchData.getTitle());
		values.put("author", searchData.getAuthor());
		values.put("affiliation", searchData.getAffiliation());
		// TODO Da li mi se poklapaju formati datuma u xml i ovde?
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		String fromDate = searchData.getFromDate() == null ? "" :  sdf.format(searchData.getFromDate());
		String toDate = searchData.getToDate() == null ? "" :  sdf.format(searchData.getToDate());
		values.put("fromDate", fromDate);		
		values.put("toDate", toDate);

		FusekiReader.executeQuery(QUERY_FILE_PATH, values);
	}
	
	public void save(String scientificPaperXml) throws Exception {
		
	   // SAXParseExcetion is thrown when xml is not valid
       Document document =  DOMParser.buildDocument(scientificPaperXml, spSchemaPath);
       
       // TODO change newMetadata.rdf path
       String rdfFilePath = "src/main/resources/rdf/newMetadata.rdf";
       metadataExtractor.extractMetadata(scientificPaperXml, rdfFilePath);
       // zasto je ovde bilo /scientificPapers2 ?
       fusekiManager.saveMetadata(rdfFilePath, "/scientificPapers");
       
       	// TODO Generate ids for chapters, paragraphs etc. 
        // TODO Check chapter levels (max is 5)
       
	   spRepository.save(scientificPaperXml);

	}
}
