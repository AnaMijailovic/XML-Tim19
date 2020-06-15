package com.ftn.scientific_papers.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.exist.xmldb.EXistResource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

public class FileUtil {
	
	public static String readFile(String path) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded,StandardCharsets.UTF_8);
	}
	
	public static String resourceSetToString(ResourceSet resultSet) throws XMLDBException {
		String retVal = "";
		if (resultSet == null || (resultSet.getSize() == 0 )) {
			return retVal;
		}
		ResourceIterator i = resultSet.getIterator();
		XMLResource res = null;
		while (i.hasMoreResources()) {
			try {
				res = (XMLResource) i.nextResource();
				retVal += res.getContent().toString();
			} finally {
				// don't forget to cleanup resources
				try {
					if(res != null) {
						((EXistResource) res).freeResources();
					}
				} catch (XMLDBException xe) {
					xe.printStackTrace();
				}
			}
		}
		return retVal;
	}

}	
