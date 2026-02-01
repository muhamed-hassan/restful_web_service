package com.poc.domain.exceptions;

public class DataNotFoundException extends RuntimeException {

	public DataNotFoundException() {
		super("Data not found");
	}	

}
