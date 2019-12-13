package com.ftn.scientific_papers.exceptions;

public class AuthorizationException extends Exception {
	private static final long serialVersionUID = 1L;

	public AuthorizationException(String message) {
        super(message);
    }
}
