package com.d2z.d2zservice.model;

import java.util.List;

public class PFLSubmitOrderResponse {
	
	private String result;
	private String method;
	private String requested ;
	private String error;
	private String code;
	private List<String> error_ids;
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getRequested() {
		return requested;
	}
	public void setRequested(String requested) {
		this.requested = requested;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public List<String> getError_ids() {
		return error_ids;
	}
	public void setError_ids(List<String> error_ids) {
		this.error_ids = error_ids;
	}
	
}
