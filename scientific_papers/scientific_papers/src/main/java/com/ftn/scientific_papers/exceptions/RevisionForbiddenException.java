package com.ftn.scientific_papers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Revision is forbidden in this phase of publishing process")
public class RevisionForbiddenException extends Exception{
	
	private static final long serialVersionUID = -4472204945301133912L;

	public RevisionForbiddenException(String message){
		super(message);
	}

}
