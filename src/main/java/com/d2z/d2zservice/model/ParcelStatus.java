package com.d2z.d2zservice.model;

import java.sql.Timestamp;

public class ParcelStatus {
	private String referenceNumber;
	private String barcodelabelNumber;
	private String trackEventDetails;
	private Timestamp trackEventDateOccured;
	public String getReferenceNumber() {
		return referenceNumber;
	}
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
	public String getBarcodelabelNumber() {
		return barcodelabelNumber;
	}
	public void setBarcodelabelNumber(String barcodelabelNumber) {
		this.barcodelabelNumber = barcodelabelNumber;
	}
	public String getTrackEventDetails() {
		return trackEventDetails;
	}
	public void setTrackEventDetails(String trackEventDetails) {
		this.trackEventDetails = trackEventDetails;
	}
	public Timestamp getTrackEventDateOccured() {
		return trackEventDateOccured;
	}
	public void setTrackEventDateOccured(Timestamp trackEventDateOccured) {
		this.trackEventDateOccured = trackEventDateOccured;
	}
	
	
}