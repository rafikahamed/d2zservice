package com.d2z.d2zservice.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PFLResponseData {
	private String status;
	private String Id;
	private String reference;
	private String tracking;
	private List<PFLErrorResponse> errors;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getTracking() {
		return tracking;
	}
	public void setTracking(String tracking) {
		this.tracking = tracking;
	}
	public List<PFLErrorResponse> getErrors() {
		return errors;
	}
	public void setErrors(List<PFLErrorResponse> errors) {
		this.errors = errors;
	}

	
}
