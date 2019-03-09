package com.d2z.d2zservice.model.etower;

import java.util.List;

public class CreateShippingResponse {

	private String status;
	private List<ResponseData> data;
	private List<EtowerErrorResponse> errors;
	
	public List<EtowerErrorResponse> getErrors() {
		return errors;
	}
	public void setErrors(List<EtowerErrorResponse> errors) {
		this.errors = errors;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<ResponseData> getData() {
		return data;
	}
	public void setData(List<ResponseData> data) {
		this.data = data;
	}
	
	
}
