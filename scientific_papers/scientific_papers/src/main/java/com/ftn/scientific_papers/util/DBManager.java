package com.ftn.scientific_papers.util;

import static com.ftn.scientific_papers.util.XUpdateTemplate.TARGET_NAMESPACE;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import javax.xml.transform.OutputKeys;

import org.exist.xmldb.EXistResource;
import org.springframework.stereotype.Component;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.CompiledExpression;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XPathQueryService;
import org.xmldb.api.modules.XQueryService;
import org.xmldb.api.modules.XUpdateQueryService;

import com.ftn.scientific_papers.util.AuthenticationUtilities.ConnectionProperties;

@Component
public class DBManager {

	private static ConnectionProperties conn;

	public void save(String collectionId, String documentId, String xml) throws Exception {

		conn = AuthenticationUtilities.loadProperties();

		System.out.println("\t- collection ID: " + collectionId);
		System.out.println("\t- document ID: " + documentId);

		// initialize database driver
		System.out.println("[INFO] Loading driver class: " + conn.driver);
		Class<?> cl = Class.forName(conn.driver);

		// encapsulation of the database driver functionality
		Database database = (Database) cl.newInstance();
		database.setProperty("create-database", "true");

		// entry point for the API which enables you to get the Collection reference
		DatabaseManager.registerDatabase(database);

		// a collection of Resources stored within an XML database
		Collection col = null;
		XMLResource res = null;

		try {

			System.out.println("[INFO] Retrieving the collection: " + collectionId);
			col = getOrCreateCollection(collectionId);

			/*
			 * create new XMLResource with a given id an id is assigned to the new resource
			 * if left empty (null)
			 */
			System.out.println("[INFO] Inserting the document: " + documentId);
			res = (XMLResource) col.createResource(documentId, XMLResource.RESOURCE_TYPE);

			res.setContent(xml);
			System.out.println("[INFO] Storing the document: " + res.getId());

			col.storeResource(res);
			System.out.println("[INFO] Done.");

		} finally {

			// don't forget to cleanup
			if (res != null) {
				try {
					((EXistResource) res).freeResources();
				} catch (XMLDBException xe) {
					xe.printStackTrace();
				}
			}

			if (col != null) {
				try {
					col.close();
				} catch (XMLDBException xe) {
					xe.printStackTrace();
				}
			}
		}
	}

	public XMLResource findOne(String collectionId, String documentId) throws Exception {

		conn = AuthenticationUtilities.loadProperties();

		System.out.println("\t- collection ID: " + collectionId);
		System.out.println("\t- document ID: " + documentId + "\n");

		// initialize database driver
		System.out.println("[INFO] Loading driver class: " + conn.driver);
		Class<?> cl = Class.forName(conn.driver);

		Database database = (Database) cl.newInstance();
		database.setProperty("create-database", "true");

		DatabaseManager.registerDatabase(database);

		Collection col = null;
		XMLResource res = null;

		try {
			// get the collection
			System.out.println("[INFO] Retrieving the collection: " + collectionId);
			col = DatabaseManager.getCollection(conn.uri + collectionId);
			col.setProperty(OutputKeys.INDENT, "yes");

			System.out.println("[INFO] Retrieving the document: " + documentId);
			res = (XMLResource) col.getResource(documentId);

			if (res == null) {
				System.out.println("[WARNING] Document '" + documentId + "' can not be found!");
			} else {

				System.out.println("[INFO] Showing the document as XML resource: ");
				System.out.println(res.getContent());

			}
		} finally {
			// don't forget to clean up!

			if (res != null) {
				try {
					((EXistResource) res).freeResources();
				} catch (XMLDBException xe) {
					xe.printStackTrace();
				}
			}

			if (col != null) {
				try {
					col.close();
				} catch (XMLDBException xe) {
					xe.printStackTrace();
				}
			}

		}

		return res;
	}

	public ResourceSet executeXQuery(String collectionId, String xqueryExpression, HashMap<String, String> parameterMap,
			String xqueryFilePath) throws Exception {

		conn = AuthenticationUtilities.loadProperties();

		ResourceSet result;

		// initialize database driver
		Class<?> cl = Class.forName(conn.driver);

		Database database = (Database) cl.newInstance();
		database.setProperty("create-database", "true");

		DatabaseManager.registerDatabase(database);

		Collection col = null;

		try {

			// get the collection
			col = DatabaseManager.getCollection(conn.uri + collectionId);

			// get an instance of xquery service
			XQueryService xqueryService = (XQueryService) col.getService("XQueryService", "1.0");
			xqueryService.setProperty("indent", "yes");

			// make the service aware of namespaces
			xqueryService.setNamespace("b", TARGET_NAMESPACE);

			// read xquery if expression is not provided
			if (xqueryExpression.isEmpty()) {
				System.out.println("[INFO] Invoking XQuery service for: " + xqueryFilePath);
				xqueryExpression = readFile(xqueryFilePath, StandardCharsets.UTF_8);

			}
			
			for(String param: parameterMap.keySet())
            {	
				System.out.println(param + " " + parameterMap.get(param));
                xqueryService.declareVariable(param, parameterMap.get(param));
            }
			
			// compile and execute the expression
			CompiledExpression compiledXquery = xqueryService.compile(xqueryExpression);
			result = xqueryService.execute(compiledXquery);

		} finally {

			// don't forget to cleanup
			if (col != null) {
				try {
					col.close();
				} catch (XMLDBException xe) {
					xe.printStackTrace();
				}
			}
		}

		return result;
	}

	public ResourceSet executeXPath(String collectionId, String xPathExpression) throws Exception {
		conn = AuthenticationUtilities.loadProperties();

		ResourceSet result;

		System.out.println("\t- collection ID: " + collectionId);

		// initialize database driver
		System.out.println("[INFO] Loading driver class: " + conn.driver);
		Class<?> cl = Class.forName(conn.driver);

		Database database = (Database) cl.newInstance();
		database.setProperty("create-database", "true");

		DatabaseManager.registerDatabase(database);

		Collection collection = null;

		try {

			// get the collection
			System.out.println("[INFO] Retrieving the collection: " + collectionId);
			collection = DatabaseManager.getCollection(conn.uri + collectionId);

			if (collection == null) {
				collection = getOrCreateCollection(collectionId);
			}

			// get an instance of xpath query service
			XPathQueryService xpathService = (XPathQueryService) collection.getService("XPathQueryService", "1.0");
			xpathService.setProperty("indent", "yes");

			// make the service aware of namespaces, using the default one
			xpathService.setNamespace("", TARGET_NAMESPACE);

			// execute xpath expression
			System.out.println("[INFO] Invoking XPath query service for: " + xPathExpression);
			result = xpathService.query(xPathExpression);

		} finally {

			// don't forget to cleanup
			if (collection != null) {
				try {
					collection.close();
				} catch (XMLDBException xe) {
					xe.printStackTrace();
				}
			}
		}

		return result;
	}
	
	public void executeXUpdate(String collectionId, String xUpdateExpression, String documentId) throws XMLDBException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        
    	// initialize database driver
    	System.out.println("[INFO] Loading driver class: " + conn.driver);
        Class<?> cl = Class.forName(conn.driver);
        
        Database database = (Database) cl.newInstance();
        database.setProperty("create-database", "true");
        
        DatabaseManager.registerDatabase(database);
        
        Collection col = null;
        
        try { 
        	
        	// get the collection
        	System.out.println("[INFO] Retrieving the collection: " + collectionId);
            col = DatabaseManager.getCollection(conn.uri + collectionId, conn.user, conn.password);
            col.setProperty("indent", "yes");
        	
            // get an instance of xupdate query service
            System.out.println("[INFO] Fetching XUpdate service for the collection.");
            XUpdateQueryService xupdateService = (XUpdateQueryService) col.getService("XUpdateQueryService", "1.0");
            xupdateService.setProperty("indent", "yes");
            xupdateService.updateResource(documentId, xUpdateExpression);
            
        } finally {        	
            // don't forget to cleanup
            if(col != null) {
                try { 
                	col.close();
                } catch (XMLDBException xe) {
                	xe.printStackTrace();
                }
            }
        }
	}
	
	
	public String resourceSetToString(ResourceSet resourceSet) throws XMLDBException {
		// handle the results

		String result = "";

		ResourceIterator i = resourceSet.getIterator();
		Resource res = null;

		while (i.hasMoreResources()) {

			try {
				res = i.nextResource();
				result += res.getContent();
				System.out.println(res.getContent());

			} finally {

				// don't forget to cleanup resources
				try {
					((EXistResource) res).freeResources();
				} catch (XMLDBException xe) {
					xe.printStackTrace();
				}
			}
		}

		return result;
	}

	/**
	 * Convenience method for reading file contents into a string.
	 */
	public static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	private Collection getOrCreateCollection(String collectionUri) throws XMLDBException {
		return getOrCreateCollection(collectionUri, 0);
	}

	private Collection getOrCreateCollection(String collectionUri, int pathSegmentOffset) throws XMLDBException {

		Collection col = DatabaseManager.getCollection(conn.uri + collectionUri, conn.user, conn.password);

		// create the collection if it does not exist
		if (col == null) {

			if (collectionUri.startsWith("/")) {
				collectionUri = collectionUri.substring(1);
			}

			String pathSegments[] = collectionUri.split("/");

			if (pathSegments.length > 0) {
				StringBuilder path = new StringBuilder();

				for (int i = 0; i <= pathSegmentOffset; i++) {
					path.append("/" + pathSegments[i]);
				}

				Collection startCol = DatabaseManager.getCollection(conn.uri + path, conn.user, conn.password);

				if (startCol == null) {

					// child collection does not exist

					String parentPath = path.substring(0, path.lastIndexOf("/"));
					Collection parentCol = DatabaseManager.getCollection(conn.uri + parentPath, conn.user,
							conn.password);

					CollectionManagementService mgt = (CollectionManagementService) parentCol
							.getService("CollectionManagementService", "1.0");

					System.out.println("[INFO] Creating the collection: " + pathSegments[pathSegmentOffset]);
					col = mgt.createCollection(pathSegments[pathSegmentOffset]);

					col.close();
					parentCol.close();

				} else {
					startCol.close();
				}
			}
			return getOrCreateCollection(collectionUri, ++pathSegmentOffset);
		} else {
			return col;
		}
	}

}
