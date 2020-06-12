package com.ftn.scientific_papers.fuseki;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.text.StringSubstitutor;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.RDFNode;

import com.ftn.scientific_papers.fuseki.FusekiAuthenticationUtilities.FusekiConnectionProperties;
import com.ftn.scientific_papers.util.DBManager;

public class FusekiReader {
	
	private FusekiReader() {}
	
	public static Set<String> executeQuery(String filePath, Map<String, String> params) throws IOException {
		
		// load properties
		FusekiConnectionProperties conn = FusekiAuthenticationUtilities.loadProperties();
		
		// read sparql query from a file
		String sparqlQueryTemplate = DBManager.readFile(filePath, StandardCharsets.UTF_8);
		System.out.println("Query: " + sparqlQueryTemplate);
		String sparqlQuery = StringSubstitutor.replace(sparqlQueryTemplate, params, "{{", "}}");
		System.out.println("Query: " + sparqlQuery);
		
		// Create a QueryExecution that will access a SPARQL service over HTTP
		QueryExecution query = QueryExecutionFactory.sparqlService(conn.queryEndpoint, sparqlQuery);
		
		// Query the SPARQL endpoint, iterate over the result set...
		ResultSet results = query.execSelect();
	
		String varName;
		RDFNode varValue;
		
		Set<String> papers = new HashSet<>();
		while(results.hasNext()) {
		    
			// A single answer from a SELECT query
			QuerySolution querySolution = results.next() ;
			Iterator<String> variableBindings = querySolution.varNames();
			
			// Retrieve variable bindings
		    while (variableBindings.hasNext()) {
		   
		    	varName = variableBindings.next();
		    	varValue = querySolution.get(varName);
		    	
		    	if (varName.equals("paper"))
		    		papers.add(varValue.toString());
		    	System.out.println("bla bla: " + varName + ": " + varValue);
		    }
		    System.out.println();
		}
		
	    ResultSetFormatter.outputAsXML(System.out, results);
		
		query.close() ;
		
		System.out.println("[INFO] SPARQL Query End.");
		
		// Set of paper urls
		// example:  https://github.com/AnaMijailovic/XML-Tim19/scientific_papers/paper0
		return papers;
	}

}
