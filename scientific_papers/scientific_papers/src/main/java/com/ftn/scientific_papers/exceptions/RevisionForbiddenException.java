package com.ftn.scientific_papers.exceptions;

public class RevisionForbiddenException extends Exception{
	
	private static final long serialVersionUID = -4472204945301133912L;

	public RevisionForbiddenException(String message){
		super(message);
	}

}
