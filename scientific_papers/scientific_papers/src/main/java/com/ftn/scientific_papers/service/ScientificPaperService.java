package com.ftn.scientific_papers.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xmldb.api.modules.XMLResource;

import com.ftn.scientific_papers.dom.DOMParser;
import com.ftn.scientific_papers.fuseki.FusekiManager;
import com.ftn.scientific_papers.fuseki.MetadataExtractor;
import com.ftn.scientific_papers.repository.ScientificPaperRepository;

@Service
public class ScientificPaperService {
	
	static String spSchemaPath = "src/main/resources/xsd/scientific_paper.xsd";
	
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

	public void save(String scientificPaperXml) throws Exception {
		
	   // SAXParseExcetion is thrown when xml is not valid
       Document document =  DOMParser.buildDocument(scientificPaperXml, spSchemaPath);
       
       // TODO change newMetadata.rdf path
       String rdfFilePath = "src/main/resources/rdf/newMetadata.rdf";
       metadataExtractor.extractMetadata(scientificPaperXml, rdfFilePath);
       fusekiManager.saveMetadata(rdfFilePath, "/scientificPapers2");
       
       	// TODO Generate ids for chapters, paragraphs etc. 
        // TODO Check chapter levels (max is 5)
       
	   spRepository.save(scientificPaperXml);

	}
}
