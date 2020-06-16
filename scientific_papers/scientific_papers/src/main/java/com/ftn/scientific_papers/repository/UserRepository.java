package com.ftn.scientific_papers.repository;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.exist.xmldb.EXistResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;

import com.ftn.scientific_papers.exceptions.DatabaseException;
import com.ftn.scientific_papers.model.user.TUser;
import com.ftn.scientific_papers.util.DBManager;

@Repository
public class UserRepository {
	
	@Autowired
	private DBManager dbManager;
	
	@Value("${user-collection-id}")
	private String userCollectionId;

	public TUser findByUsername(String username) {
		try {
			String xPathExpression = String.format("//user[username='%s']", username);
			ResourceSet result = dbManager.executeXPath(userCollectionId, xPathExpression);
			
			if (result == null) {
				return null;
			}
			
			ResourceIterator i = result.getIterator();
            Resource res = null;
            TUser user = null;
            
            while(i.hasMoreResources()) {
                
            	try {
                    res = i.nextResource();
                    user = unmarshallUser(res.getContent().toString());
                } finally {
                	// don't forget to cleanup resources
                    try { 
                    	((EXistResource)res).freeResources(); 
                    } catch (XMLDBException e) {
                    	e.printStackTrace();
                    }
                }
            }
            
            return user;
			
		} catch (Exception e) {
			throw new DatabaseException("Exception while finding user by username.");
		}
	}

	public TUser findById(String userId) {
		try {
			String xPathExpression = String.format("//user[user_id='%s']", userId);
			ResourceSet result = dbManager.executeXPath(userCollectionId, xPathExpression);

			if (result == null) {
				return null;
			}

			ResourceIterator i = result.getIterator();
			Resource res = null;
			TUser user = null;

			while(i.hasMoreResources()) {

				try {
					res = i.nextResource();
					user = unmarshallUser(res.getContent().toString());
				} finally {
					// don't forget to cleanup resources
					try {
						((EXistResource)res).freeResources();
					} catch (XMLDBException e) {
						e.printStackTrace();
					}
				}
			}

			return user;

		} catch (Exception e) {
			throw new DatabaseException("Exception while finding user by id.");
		}
	}

	public void save(TUser user) {
		try {
			ResourceSet rs = dbManager.executeXQuery(userCollectionId, "count(/.)", new HashMap<>(), "");
			String id = "user" + rs.getIterator().nextResource().getContent().toString();
			
			user.setUserId(id);
			String userXML = marshallUser(user);

			dbManager.save(userCollectionId, id,  userXML);
		
		} catch (JAXBException e) {
			throw new DatabaseException("An error occured while marshalling new user.");
		} catch (Exception e) {
			throw new DatabaseException("An error occured while user registration.");
		}
	}
	
	private String marshallUser(TUser user) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(TUser.class);
	 	Marshaller marshaller = context.createMarshaller();
	 	
	 	marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	 	
	 	ByteArrayOutputStream stream = new ByteArrayOutputStream();
        marshaller.marshal(user, stream);
        
        return new String(stream.toByteArray());
	}	
	
	private TUser unmarshallUser(String userXML) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(TUser.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		
		return (TUser) unmarshaller.unmarshal(new StringReader(userXML));
	}


}
