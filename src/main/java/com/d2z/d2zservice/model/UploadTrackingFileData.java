package com.d2z.d2zservice.model;

import java.sql.Timestamp;

public class UploadTrackingFileData {

	private String referenceNumber;
	private String articleID;
	private String trackEventDetails;
	private Timestamp trackEventDateOccured;
	private String fileName;
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
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
	public Timestamp getTrackEventDateOccured() {
		return trackEventDateOccured;
	}
	public void setTrackEventDateOccured(Timestamp trackEventDateOccured) {
		this.trackEventDateOccured = trackEventDateOccured;
	}

	

}
