package com.ftn.scientific_papers.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.scientific_papers.dto.LoginDTO;
import com.ftn.scientific_papers.dto.RegisterUserDTO;
import com.ftn.scientific_papers.exceptions.PasswordsDoNotMatchException;
import com.ftn.scientific_papers.model.user.TUser;
import com.ftn.scientific_papers.model.user.TUser.Roles;
import com.ftn.scientific_papers.security.TokenUtils;
import com.ftn.scientific_papers.service.CustomUserDetailsService;

@RestController
@RequestMapping(value = "/users")
public class UserController {
	
	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private TokenUtils tokenUtils;
	
	@PostMapping(value = "/register")
	public ResponseEntity<String> register(@Valid @RequestBody RegisterUserDTO dto) {
		
		if (!dto.getPassword().equals(dto.getPasswordConfirm())) {
			throw new PasswordsDoNotMatchException();
		}
		
		TUser user = new TUser();
		user.setUsername(dto.getUsername());
		user.setPassword(dto.getPassword());
		user.setName(dto.getName());
		user.setSurname(dto.getSurname());
		user.setEmail(dto.getEmail());
		user.setIsEditor(dto.getIsEditor());
		user.setRoles(new Roles());
		
		userDetailsService.registerUser(user);
		
		return new ResponseEntity<>("Successfully saved", HttpStatus.CREATED);
	}
	
	@PostMapping(value = "/login")
	public ResponseEntity<String> login(@Valid @RequestBody LoginDTO dto) {
		
		try {
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
					dto.getUsername(), dto.getPassword());
			
			//authenticationManager.authenticate(token);
			UserDetails userDetails = userDetailsService.loadUserByUsername(dto.getUsername());
			
			String generatedToken = tokenUtils.generateToken(userDetails);
			
			return new ResponseEntity<String>(generatedToken, HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<String>("Invalid login", HttpStatus.BAD_REQUEST);
		}
	}

}
