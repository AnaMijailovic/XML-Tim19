package com.ftn.scientific_papers.repository;

import java.util.HashMap;

import static com.ftn.scientific_papers.util.XUpdateTemplate.TARGET_NAMESPACE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.modules.XMLResource;

import com.ftn.scientific_papers.exceptions.ResourceNotFoundException;
import com.ftn.scientific_papers.util.DBManager;
import com.ftn.scientific_papers.util.FileUtil;
import com.ftn.scientific_papers.util.RetriveFromDB;

@Repository
public class ScientificPaperRepository {

	private static final String XQUERY_PATH = ".\\src\\main\\resources\\xQuery";
	public static final String XSL_PATH = ".\\src\\main\\resources\\xsl";
	public static final String XSLFO_PATH = ".\\src\\main\\resources\\xsl_fo";

	
	@Autowired
	private DBManager dbManager;

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

	public String getAll(String searchText) {
		String xQueryPath = XQUERY_PATH + "\\textSearch.txt";
		String result = "";

		HashMap<String, String> params = new HashMap<>();
		params.put("searchText", searchText);

		try {
			ResourceSet rs = dbManager.executeXQuery(scientificPaperCollectionId, "", params, xQueryPath);
			result = dbManager.resourceSetToString(rs);
			// System.out.println("Result: " + result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public String getById(String id) {
		String xQueryPath = XQUERY_PATH + "\\findById.txt";
		String result = "";

		HashMap<String, String> params = new HashMap<>();
		params.put("id", id);

		try {
			ResourceSet rs = dbManager.executeXQuery(scientificPaperCollectionId, "", params, xQueryPath);
			result = dbManager.resourceSetToString(rs);
			System.out.println("Result: " + result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public boolean update(String scientificPaperXml, String id) throws Exception {

		dbManager.save(scientificPaperCollectionId, id, scientificPaperXml);

		return true;
	}

	public String getNextId() {
		// generate id
		// TODO Change this?
		String id = "paper0";
		try {
			ResourceSet rs = dbManager.executeXQuery(scientificPaperCollectionId, "count(/.)", new HashMap<>(), "");
			id = "paper" + rs.getIterator().nextResource().getContent().toString();
		} catch (Exception e) {

		}
		System.out.println("\nID: " + id);
		return id;
	}

	public void save(String scientificPaperXml, String id) throws Exception {

		dbManager.save(scientificPaperCollectionId, id, scientificPaperXml);

	}

	public String findOneScientificPaper(String id) throws Exception {
		
		String xQueryPath = XQUERY_PATH + "\\findById.txt";

		
		HashMap<String, String> params = new HashMap<>();
		params.put("ID", id);

		ResourceSet resultSet = RetriveFromDB.executeXQuery(
			scientificPaperCollectionId, xQueryPath, params, TARGET_NAMESPACE);
		
		return FileUtil.resourceSetToString(resultSet);
	}
}
