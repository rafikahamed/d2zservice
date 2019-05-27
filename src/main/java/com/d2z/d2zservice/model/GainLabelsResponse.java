package com.d2z.d2zservice.model;

import java.util.List;

public class GainLabelsResponse {

	private String status;
	private List<LabelData> data;
	private List<PFLErrorResponse> errors;

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<LabelData> getData() {
		return data;
	}
	public void setData(List<LabelData> data) {
		this.data = data;
	}
	public List<PFLErrorResponse> getErrors() {
		return errors;
	}
	public void setErrors(List<PFLErrorResponse> errors) {
		this.errors = errors;
	}
	
	
	
}
