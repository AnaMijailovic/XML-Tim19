package com.ftn.scientific_papers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Max Chapter Levels Excedeed - maximum is 5 levels")
public class MaxChapterLevelsExcedeedException extends Exception {

	private static final long serialVersionUID = -586214780247591888L;
	
	public MaxChapterLevelsExcedeedException(String message){
		super(message);
	}


}
