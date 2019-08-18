package com.d2z.d2zservice.model;

import java.util.List;

import com.d2z.d2zservice.model.SenderDataResponse;
import com.d2z.d2zservice.model.ErrorDetails;

public class CreateConsignmentResponse {
	
	private String status;
	private ErrorDetails errors;
	private List<SenderDataResponse> responseData;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public ErrorDetails getErrors() {
		return errors;
	}
	public void setErrors(ErrorDetails errors) {
		this.errors = errors;
	}
	public List<SenderDataResponse> getResponseData() {
		return responseData;
	}
	public void setResponseData(List<SenderDataResponse> responseData) {
		this.responseData = responseData;
	}
	

}
