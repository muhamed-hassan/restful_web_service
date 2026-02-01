package com.poc.web.error_handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.poc.web.error_handler.exceptions.DataNotFoundException;

@ControllerAdvice
public class RestErrorHandler {
	
	@ExceptionHandler
    public ResponseEntity<ErrorResponse> handleDataNotFoundException(DataNotFoundException exception) {
		
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setError(exception.getMessage());
		
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.NOT_FOUND);		
	}
	
	@ExceptionHandler
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
		
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setError(exception.getMessage());
		
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);		
	}

}
