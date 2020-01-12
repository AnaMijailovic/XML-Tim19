package com.ftn.scientific_papers.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.xmldb.api.modules.XMLResource;

import com.ftn.scientific_papers.model.user.User;
import com.ftn.scientific_papers.util.DBManager;

@Repository
public class UserRepository {
	
	@Autowired
	private DBManager dbManager;
	
	static String spCollectionId = "/db/sample/users";
	static String spSchemaPath = "src/main/resources/xsd/user.xsd"; 

	public XMLResource findOne(String id) {
		
		 return null;
	}

	public User findByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	public void save(User user) {
		// TODO Auto-generated method stub
		
	}
	
	
}
