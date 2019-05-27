package com.d2z.d2zservice.model;

public class PFLErrorResponse {

	private String code;
	private String error;
	private String additional_info;
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getAdditional_info() {
		return additional_info;
	}
	public void setAdditional_info(String additional_info) {
		this.additional_info = additional_info;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	
	
}
