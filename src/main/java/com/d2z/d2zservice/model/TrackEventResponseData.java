package com.d2z.d2zservice.model;

import java.util.List;

public class TrackEventResponseData {

	private String orderId;
	private String status;
	private List<PFLErrorResponse> errors;
	private List<PFLTrackingDetails> events;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<PFLErrorResponse> getErrors() {
		return errors;
	}
	public void setErrors(List<PFLErrorResponse> errors) {
		this.errors = errors;
	}
	public List<PFLTrackingDetails> getEvents() {
		return events;
	}
	public void setEvents(List<PFLTrackingDetails> events) {
		this.events = events;
	}

	
}
