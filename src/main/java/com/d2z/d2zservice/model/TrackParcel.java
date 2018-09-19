package com.d2z.d2zservice.model;

import java.util.List;

public class TrackParcel {

	private String referenceNumber;
	
	private String barcodelabelNumber;
	private List<TrackEventDetails> trackEventDetails;
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
	public List<TrackEventDetails> getTrackEventDetails() {
		return trackEventDetails;
	}
	public void setTrackEventDetails(List<TrackEventDetails> trackEventDetails) {
		this.trackEventDetails = trackEventDetails;
	}
	
	
}
