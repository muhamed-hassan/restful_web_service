package com.poc.web.error_handler.exceptions;

public class DataNotFoundException extends RuntimeException {

	public DataNotFoundException() {
		super("Data not found");
	}	

}
