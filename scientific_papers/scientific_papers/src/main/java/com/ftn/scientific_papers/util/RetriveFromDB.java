package com.ftn.scientific_papers.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;
import org.xmldb.api.modules.XQueryService;

import com.ftn.scientific_papers.util.AuthenticationUtilities;
import com.ftn.scientific_papers.util.AuthenticationUtilities.ConnectionProperties;



public class RetriveFromDB {

    /**
     * conn XML DB connection properties args[0] Should be the collection ID to
     * access
     */
    public static ResourceSet executeXPathExpression(String collectionId, String xpathExp, String TARGET_NAMESPACE)
            throws Exception {

        ResourceSet result = null;

        ConnectionProperties conn = AuthenticationUtilities.loadProperties();
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
            col = DBUtil.getOrCreateCollection(conn, collectionId);

            if (col == null) {
                return null;
            }
            // get an instance of xpath query service
            XPathQueryService xpathService = (XPathQueryService) col.getService("XPathQueryService", "1.0");
            xpathService.setProperty("indent", "yes");

            // make the service aware of namespaces, using the default one
            xpathService.setNamespace("", TARGET_NAMESPACE);
            // System.out.println("\n[INPUT] Enter an XPath expression (e.g.
            // doc(\"1.xml\")//book[@category=\"WEB\" and price>35]/title): ");
            // execute xpath expression
            System.out.println("[INFO] Invoking XPath query service for: " + xpathExp);
            result = xpathService.query(xpathExp);
            // handle the results
            System.out.println("[INFO] Handling the results... ");
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

    public static ResourceSet executeXQuery(
            String collectionId, String xQueryExpPath,
            HashMap<String, String> parameterMap,  String TARGET_NAMESPACE) throws Exception {

        ConnectionProperties conn = AuthenticationUtilities.loadProperties();
        Class<?> cl = Class.forName(conn.driver);
        Database database = (Database) cl.newInstance();
        database.setProperty("create-database", "true");
        DatabaseManager.registerDatabase(database);
        Collection col = null;
        String xqueryExpression = null; 
        ResourceSet result = null;
        try { 
        	// get the collection
        	System.out.println("[INFO] Retrieving the collection: " + collectionId);
            col = DBUtil.getOrCreateCollection(conn, collectionId);
            // get an instance of xquery service
            XQueryService xqueryService = (XQueryService) col.getService("XQueryService", "1.0");
            xqueryService.setProperty("indent", "yes");
            // make the service aware of namespaces
            xqueryService.setNamespace("", TARGET_NAMESPACE);

            xqueryExpression = readFile(xQueryExpPath, StandardCharsets.UTF_8);
            
            // compile and execute the expression

            // za svaki query su drugaciji parametri koje saljemo.
            for(String param: parameterMap.keySet())
            {
                xqueryService.declareVariable(param, parameterMap.get(param));
            }
            
            result = xqueryService.query(xqueryExpression);
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
        return result;
    }

    public static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
    }
    


   
    
    
}
