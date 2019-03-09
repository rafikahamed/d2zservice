package com.d2z.d2zservice.model.etower;

import java.util.List;

public class TrackingEventResponse {

	private String status;
	private List<TrackEventResponseData> data;
	private List<EtowerErrorResponse> errors;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<TrackEventResponseData> getData() {
		return data;
	}
	public void setData(List<TrackEventResponseData> data) {
		this.data = data;
	}
	public List<EtowerErrorResponse> getErrors() {
		return errors;
	}
	public void setErrors(List<EtowerErrorResponse> errors) {
		this.errors = errors;
	}
	
	
}
