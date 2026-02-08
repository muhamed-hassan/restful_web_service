package com.poc.web.error_handler;

import java.util.HashMap;

public class WebValidationException extends RuntimeException {
	
	private HashMap<String, String> errorInformation;
	
	public HashMap<String, String> getErrorInformation() {
		return errorInformation;
	}

	public void setErrorInformation(HashMap<String, String> errorInformation) {
		this.errorInformation = errorInformation;
	}
	
}
