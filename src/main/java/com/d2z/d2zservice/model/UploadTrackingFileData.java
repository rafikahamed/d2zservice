package com.d2z.d2zservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;

public class UploadTrackingFileData {

	private String referenceNumber;
	private String connoteNo;
	private String trackEventDetails;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private String trackEventDateOccured;
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

	public String getConnoteNo() {
		return connoteNo;
	}
	public void setConnoteNo(String connoteNo) {
		this.connoteNo = connoteNo;
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
