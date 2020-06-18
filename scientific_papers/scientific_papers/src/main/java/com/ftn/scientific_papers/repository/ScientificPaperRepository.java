package com.ftn.scientific_papers.repository;

import java.io.StringReader;
import java.util.HashMap;

import com.ftn.scientific_papers.exceptions.DatabaseException;
import com.ftn.scientific_papers.model.scientific_paper.ScientificPaper;
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
import com.ftn.scientific_papers.fuseki.FusekiManager;
import com.ftn.scientific_papers.fuseki.MetadataExtractor;
import com.ftn.scientific_papers.util.DBManager;
import com.ftn.scientific_papers.util.FileUtil;
import com.ftn.scientific_papers.util.XUpdateTemplate;


import org.json.JSONObject;
import org.json.XML;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;


@Repository
public class ScientificPaperRepository {

	private static final String XQUERY_PATH = ".\\src\\main\\resources\\xQuery";
	public static final String SCIENTIFIC_PAPER_XSL_FO_PATH = "src/main/resources/xsl-fo/scientific_paper_fo.xsl";
	public static final String SCIENTIFIC_PAPER_XSL_PATH = "src/main/resources/xslt/scientific_paper.xsl";
	@Autowired
	private DBManager dbManager;
	
	@Autowired
	private MetadataExtractor metadataExtractor;

	@Autowired
	private FusekiManager fusekiManager;

	@Value("${scientific-paper-collection-id}")
	private String scientificPaperCollectionId;

	@Value("${scientific-paper-schema-path}")
	private String scientificPaperSchemaPath;

	public XMLResource findOne(String id) throws Exception {

		XMLResource result = dbManager.findOne(scientificPaperCollectionId, id);
		if (result == null) {
			throw new ResourceNotFoundException("Scientific paper with id " + id + " was not found");
		}
		return result;
	}

	public ScientificPaper findOneUnmarshalled(String id) {
		try {
			String xPathExpression = String.format("/scientific_paper[@id='%s']", id);
			ResourceSet result = dbManager.executeXPath(scientificPaperCollectionId, xPathExpression);

			if (result == null) {
				return null;
			}

			ResourceIterator i = result.getIterator();
			Resource res = null;
			ScientificPaper scientificPaper = null;

			while(i.hasMoreResources()) {

				try {
					res = i.nextResource();
					scientificPaper = unmarshallScientificPaper(res.getContent().toString());
				} finally {
					// don't forget to cleanup resources
					try {
						((EXistResource)res).freeResources();
					} catch (XMLDBException e) {
						e.printStackTrace();
					}
				}
			}

			return scientificPaper;

		} catch (Exception e) {
			throw new DatabaseException("Exception while finding user by id.");
		}
	}

	public String getAll(String searchText, String loggedAuthor) {
		String xQueryPath = XQUERY_PATH + "\\textSearch.txt";
		String result = "";

		HashMap<String, String> params = new HashMap<>();
		params.put("searchText", searchText);
		params.put("loggedAuthor", loggedAuthor);

		try {
			ResourceSet rs = dbManager.executeXQuery(scientificPaperCollectionId, "", params, xQueryPath);
			result = dbManager.resourceSetToString(rs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("\nResult: \n" + result);
		return result;
	}

	public String getById(String id, String loggedAuthor) {
		String xQueryPath = XQUERY_PATH + "\\findById.txt";
		String result = "";

		HashMap<String, String> params = new HashMap<>();
		params.put("id", id);
		params.put("loggedAuthor", loggedAuthor);

		try {
			ResourceSet rs = dbManager.executeXQuery(scientificPaperCollectionId, "", params, xQueryPath);
			result = dbManager.resourceSetToString(rs);
			System.out.println("Result: " + result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public String getMetadataXml(String id) throws Exception {
		XMLResource paper = findOne(id);
		if (paper == null) {
			throw new ResourceNotFoundException("Scientific paper with id: " + id + " was not found.");
		}
		
		// Get metadata
		String rdfFilePath = "src/main/resources/rdf/metadata.rdf";
		metadataExtractor.extractMetadata(paper.getContent().toString(), rdfFilePath);

		return FileUtil.readFile(rdfFilePath);
	}

	public String getMetadataJson(String id) throws Exception {
	
		String metadataXml = getMetadataXml(id);
		JSONObject xmlJSONObj = XML.toJSONObject(metadataXml);

		return xmlJSONObj.toString(4);
	}

	public void updateStatus(String paperId, String newStatus) throws Exception {

		String updatePath = "/scientific_paper/@status";
		String xUpdateExpression = String.format(XUpdateTemplate.UPDATE, updatePath, newStatus);

		dbManager.executeXUpdate(scientificPaperCollectionId, xUpdateExpression, paperId);
		System.out.println("Paper after updating status: \n" + findOne(paperId).getContent().toString());
	}

	public boolean update(String scientificPaperXml, String id) throws Exception {

		dbManager.save(scientificPaperCollectionId, id, scientificPaperXml);

		return true;
	}

	public String getNextId() {
		// generate id
		String id = "paper0";
		try {
			ResourceSet rs = dbManager.executeXQuery(scientificPaperCollectionId, "count(/.)", new HashMap<>(), "");
			id = "paper" + rs.getIterator().nextResource().getContent().toString();
		} catch (Exception e) {

		}
		return id;
	}

	public void save(String scientificPaperXml, String id) throws Exception {

		dbManager.save(scientificPaperCollectionId, id, scientificPaperXml);

	}

	private ScientificPaper unmarshallScientificPaper(String scientificPaperXML) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(ScientificPaper.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();

		return (ScientificPaper) unmarshaller.unmarshal(new StringReader(scientificPaperXML));
	}
}
