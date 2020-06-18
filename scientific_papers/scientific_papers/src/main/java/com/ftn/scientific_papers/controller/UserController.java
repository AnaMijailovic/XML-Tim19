package com.ftn.scientific_papers.controller;

import javax.validation.Valid;

import com.ftn.scientific_papers.model.user.TRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
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
import com.ftn.scientific_papers.service.EmailService;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(value = "/api/users")
public class UserController {
	
	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private TokenUtils tokenUtils;
	@Autowired
	private EmailService emailService;
	
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

		Roles roles = new Roles();
		if (dto.getIsEditor()) {
			roles.getRole().add(new TRole("ROLE_REVIEWER"));
			roles.getRole().add(new TRole("ROLE_EDITOR"));
		}
		roles.getRole().add(new TRole("ROLE_AUTHOR"));
		user.setRoles(roles);
		
		userDetailsService.registerUser(user);
		try {
			emailService.registrationEmail(user.getEmail());
		} catch (MailException | InterruptedException e) {
			System.out.println("There was an error while sending an e-mail");
			e.printStackTrace();
		}
		
		return new ResponseEntity<>("Successfully saved", HttpStatus.CREATED);
	}
	
	@PostMapping(value = "/login")
	public ResponseEntity<String> login(@Valid @RequestBody LoginDTO dto) {
		
		try {
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
					dto.getUsername(), dto.getPassword());
			
			authenticationManager.authenticate(token);
			UserDetails userDetails = userDetailsService.loadUserByUsername(dto.getUsername());
			
			String generatedToken = tokenUtils.generateToken(userDetails);
			
			return new ResponseEntity<String>(generatedToken, HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<String>("Invalid login", HttpStatus.BAD_REQUEST);
		}
	}

}
