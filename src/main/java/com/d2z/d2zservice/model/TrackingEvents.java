package com.d2z.d2zservice.model;

import java.sql.Timestamp;

public class TrackingEvents {
	
	private String eventDetails;
	private String trackEventDateOccured;
	
	public String getEventDetails() {
		return eventDetails;
	}
	public void setEventDetails(String eventDetails) {
		this.eventDetails = eventDetails;
	}
	public String getTrackEventDateOccured() {
		return trackEventDateOccured;
	}
	public void setTrackEventDateOccured(String trackEventDateOccured) {
		this.trackEventDateOccured = trackEventDateOccured;
	}
	
	
}
