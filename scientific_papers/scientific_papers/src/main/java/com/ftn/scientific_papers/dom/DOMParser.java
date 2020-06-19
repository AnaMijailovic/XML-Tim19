package com.ftn.scientific_papers.dom;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.jena.sparql.algebra.Transformer;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DOMParser {

	private static DocumentBuilderFactory factory;

	private static Document document;

	/*
	 * Factory initialization static-block
	 */
	static {
		factory = DocumentBuilderFactory.newInstance();

		factory.setNamespaceAware(true);
		factory.setIgnoringComments(true);
		factory.setIgnoringElementContentWhitespace(true);

	}

	public static Document buildDocument(String xml, String schemaPath)
			throws  IOException, ParserConfigurationException, SAXException {

		String xmlConst = XMLConstants.W3C_XML_SCHEMA_NS_URI;
		SchemaFactory xsdFactory = SchemaFactory.newInstance(xmlConst);
		Schema schema = xsdFactory.newSchema(new File(schemaPath));

		factory.setSchema(schema);

		DocumentBuilder builder = factory.newDocumentBuilder();
		builder.setErrorHandler(new DomErrorHandler());
		
		document = builder.parse(new InputSource(new StringReader(xml)));
	
		return document;
	}
	
	public static Document buildDocumentWithoutSchema(String xmlString) throws Exception
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document document;
		factory.setValidating(false);
		
		factory.setNamespaceAware(true);
		factory.setIgnoringComments(true);
		factory.setIgnoringElementContentWhitespace(true);
			
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		builder.setErrorHandler(new DomErrorHandler());
		
		document = builder.parse(new InputSource(new StringReader(xmlString)));
			
		return document;
	}
	
	
	//method to convert Document to String
	public static String getStringFromDocument(Document doc)
	{
	    try
	    {
	       DOMSource domSource = new DOMSource(doc);
	       StringWriter writer = new StringWriter();
	       StreamResult result = new StreamResult(writer);
	       TransformerFactory tf = TransformerFactory.newInstance();
	       javax.xml.transform.Transformer transformer = tf.newTransformer();
	       transformer.transform(domSource, result);
	       return writer.toString();
	    }
	    catch(TransformerException ex)
	    {
	       ex.printStackTrace();
	       return null;
	    }
	} 

}
