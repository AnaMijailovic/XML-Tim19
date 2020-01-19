package com.ftn.scientific_papers.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class RegisterUserDTO {
	
	@NotBlank
    private String name;
	
	@NotBlank
    private String surname;
	
	@NotBlank
	private String username;
	
	@Email
    private String email;
	
	@NotBlank
    private String password;
	
	@NotBlank
    private String passwordConfirm;
	
	@NotNull
	private Boolean isEditor;
	
	public RegisterUserDTO() {}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	public Boolean getIsEditor() {
		return isEditor;
	}

	public void setIsEditor(Boolean isEditor) {
		this.isEditor = isEditor;
	}
}
