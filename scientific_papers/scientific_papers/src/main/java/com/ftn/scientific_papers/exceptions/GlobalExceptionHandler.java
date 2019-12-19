package com.ftn.scientific_papers.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.xml.sax.SAXParseException;


	

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomExceptionResponse> resourceNotFoundExceptionHandler(Exception ex, WebRequest request) {
        CustomExceptionResponse customExceptionResponse = new CustomExceptionResponse();
        customExceptionResponse.setTimestamp(LocalDateTime.now());
        customExceptionResponse.setException(ex.getMessage());
        customExceptionResponse.setStatus(HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(customExceptionResponse, HttpStatus.NOT_FOUND);

    }
    
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<CustomExceptionResponse> badRequestExceptionHandler(Exception ex, WebRequest request) {
        CustomExceptionResponse customExceptionResponse = new CustomExceptionResponse();
        customExceptionResponse.setTimestamp(LocalDateTime.now());
        customExceptionResponse.setException(ex.getMessage());
        customExceptionResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(customExceptionResponse, HttpStatus.BAD_REQUEST);

    }
    
    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<CustomExceptionResponse> authorizationExceptionHandler(Exception ex, WebRequest request) {
        CustomExceptionResponse customExceptionResponse = new CustomExceptionResponse();
        customExceptionResponse.setTimestamp(LocalDateTime.now());
        customExceptionResponse.setException(ex.getMessage());
        customExceptionResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(customExceptionResponse, HttpStatus.UNAUTHORIZED);

    }
    
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<CustomExceptionResponse> forbiddenExceptionHandler(Exception ex, WebRequest request) {
        CustomExceptionResponse customExceptionResponse = new CustomExceptionResponse();
        customExceptionResponse.setTimestamp(LocalDateTime.now());
        customExceptionResponse.setException(ex.getMessage());
        customExceptionResponse.setStatus(HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(customExceptionResponse, HttpStatus.FORBIDDEN);

    }
    
    @ExceptionHandler(SAXParseException.class)
    public ResponseEntity<CustomExceptionResponse> saxParseExceptionExceptionHandler(Exception ex, WebRequest request) {
        CustomExceptionResponse customExceptionResponse = new CustomExceptionResponse();
        customExceptionResponse.setTimestamp(LocalDateTime.now());
        customExceptionResponse.setException("Xml document is invalid");
        customExceptionResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(customExceptionResponse, HttpStatus.BAD_REQUEST);

    }
    
    
    
}
