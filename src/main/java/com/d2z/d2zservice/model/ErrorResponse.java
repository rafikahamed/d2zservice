package com.d2z.d2zservice.model;

import java.util.List;

import org.springframework.http.HttpStatus;

public class ErrorResponse {

	private HttpStatus status;
	private String errorMessage;
	private List<String> errorDetails;
	
	
	public ErrorResponse(HttpStatus status, String errorMessage, List<String> errorDetails) {
		this.status = status;
		this.errorMessage = errorMessage;
		this.errorDetails = errorDetails;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public List<String> getErrorDetails() {
		return errorDetails;
	}

	public void setErrorDetails(List<String> errorDetails) {
		this.errorDetails = errorDetails;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	

}
