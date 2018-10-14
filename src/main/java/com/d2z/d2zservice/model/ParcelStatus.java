package com.d2z.d2zservice.model;

public class ParcelStatus {
	private String referenceNumber;
	private String articleID;
	private String trackEventDetails;
	private String trackEventDateOccured;
	public String getReferenceNumber() {
		return referenceNumber;
	}
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
	
	public String getArticleID() {
		return articleID;
	}
	public void setArticleID(String articleID) {
		this.articleID = articleID;
	}
	public String getTrackEventDetails() {
		return trackEventDetails;
	}
	public void setTrackEventDetails(String trackEventDetails) {
		this.trackEventDetails = trackEventDetails;
	}
	public String getTrackEventDateOccured() {
		return trackEventDateOccured;
	}
	public void setTrackEventDateOccured(String trackEventDateOccured) {
		this.trackEventDateOccured = trackEventDateOccured;
	}
	
	
	
}