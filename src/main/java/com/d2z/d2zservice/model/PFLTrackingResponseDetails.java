package com.d2z.d2zservice.model;

import java.util.List;

public class PFLTrackingResponseDetails {
	
	private String date;
	private String status;
	private String location;
	private String status_code;
	private String barcodeLabel;
	private List<PFLTrackEvent> trackEvent;
	
	
	public List<PFLTrackEvent> getTrackEvent() {
		return trackEvent;
	}
	public void setTrackEvent(List<PFLTrackEvent> trackEvent) {
		this.trackEvent = trackEvent;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getStatus_code() {
		return status_code;
	}
	public void setStatus_code(String status_code) {
		this.status_code = status_code;
	}
	public String getBarcodeLabel() {
		return barcodeLabel;
	}
	public void setBarcodeLabel(String barcodeLabel) {
		this.barcodeLabel = barcodeLabel;
	}
	
}
