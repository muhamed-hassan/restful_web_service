package com.poc.web.error_handler;

import java.util.HashMap;

public class ErrorResponseWithForm {
	
	private HashMap<String, String> error;

	public HashMap<String, String> getError() {
		return error;
	}

	public void setError(HashMap<String, String> error) {
		this.error = error;
	}
	
}
