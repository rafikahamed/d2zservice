package com.d2z.d2zservice.model;

import java.util.List;


public class PFLCreateShippingResponse {

	/*private String status;
	private List<PFLResponseData> data;
	private List<PFLErrorResponse> errors;
	
	public List<PFLErrorResponse> getErrors() {
		return errors;
	}
	public void setErrors(List<PFLErrorResponse> errors) {
		this.errors = errors;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<PFLResponseData> getData() {
		return data;
	}
	public void setData(List<PFLResponseData> data) {
		this.data = data;
	}*/
	private String error;
	private int code;
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	
	
}
