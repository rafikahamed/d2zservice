package com.d2z.d2zservice.model;

public class PFLResponseData {
	
	private String status;
	private String Id;
	private String reference;
	private String tracking;
	private String hub;
	private String matrix;
	private String error;
	private String code;
//	private List<PFLErrorResponse> errors;
	
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
//	public List<PFLErrorResponse> getErrors() {
//		return errors;
//	}
//	public void setErrors(List<PFLErrorResponse> errors) {
//		this.errors = errors;
//	}
	public String getHub() {
		return hub;
	}
	public void setHub(String hub) {
		this.hub = hub;
	}
	public String getMatrix() {
		return matrix;
	}
	public void setMatrix(String matrix) {
		this.matrix = matrix;
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
	
}
