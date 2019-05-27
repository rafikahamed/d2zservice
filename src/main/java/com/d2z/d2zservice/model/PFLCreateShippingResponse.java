package com.d2z.d2zservice.model;

import java.util.List;

public class PFLCreateShippingResponse {

	private String status;
	private List<PFLResponseData> result;
	private String error;
	private int code;
	private String method;
	private String requested;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<PFLResponseData> getResult() {
		return result;
	}
	public void setResult(List<PFLResponseData> result) {
		this.result = result;
	}
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
	
	
}
