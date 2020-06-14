package com.ftn.scientific_papers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Process Status Exception")
public class ProcessStatusException extends Exception{

	private static final long serialVersionUID = 7134772324781662803L;
	
	public ProcessStatusException(String message){
		super(message);
	}
	
}
