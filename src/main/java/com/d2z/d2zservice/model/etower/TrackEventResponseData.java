package com.d2z.d2zservice.model.etower;

import java.util.List;

public class TrackEventResponseData {

	private String orderId;
	private String status;
	private List<EtowerErrorResponse> errors;
	private List<ETowerTrackingDetails> events;
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
	public List<EtowerErrorResponse> getErrors() {
		return errors;
	}
	public void setErrors(List<EtowerErrorResponse> errors) {
		this.errors = errors;
	}
	public List<ETowerTrackingDetails> getEvents() {
		return events;
	}
	public void setEvents(List<ETowerTrackingDetails> events) {
		this.events = events;
	}

	
}
