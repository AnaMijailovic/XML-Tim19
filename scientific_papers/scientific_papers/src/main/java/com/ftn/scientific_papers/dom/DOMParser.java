package com.ftn.scientific_papers.dom;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

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

}
