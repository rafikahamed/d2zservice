package com.d2z.d2zservice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="TrackEvents")
@NamedQuery(name="TrackEvents.findAll", query="SELECT t FROM TrackEvents t")
public class TrackEvents {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="rowId")
	private int rowId;
			
	@Column(name="AirwayBill")
	private String airwayBill;
	
	@Column(name="BarcodelabelNumber")
	private String barcodelabelNumber;

	@Column(name="CourierEvents")
	private String courierEvents;

	@Column(name="FileName")
	private String fileName;

	@Column(name="IsDeleted")
	private String isDeleted;

	@Column(name="Reference_number")
	private String reference_number;

	@Column(name="Signature")
	private String signature;

	@Column(name="SignerName")
	private String signerName;

	@Column(name="Timestamp")
	private String timestamp;

	@Column(name="TrackEventCode")
	private String trackEventCode;

	@Column(name="TrackEventDateOccured")
	private String trackEventDateOccured;

	@Column(name="TrackEventDetails")
	private String trackEventDetails;
	
	@Column(name="TrackSequence")
	private int trackSequence;

	@Column(name="User_Id")
	private String user_Id;

	@Column(name="ConnoteNo")
	private String connoteNo;
	
	@Column(name="ArticleID")
	private String articleID;
	
	@Column(name="Location")
	private String location;

	public int getRowId() {
		return rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public String getAirwayBill() {
		return airwayBill;
	}

	public void setAirwayBill(String airwayBill) {
		this.airwayBill = airwayBill;
	}

	public String getBarcodelabelNumber() {
		return barcodelabelNumber;
	}

	public void setBarcodelabelNumber(String barcodelabelNumber) {
		this.barcodelabelNumber = barcodelabelNumber;
	}

	public String getCourierEvents() {
		return courierEvents;
	}

	public void setCourierEvents(String courierEvents) {
		this.courierEvents = courierEvents;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getReference_number() {
		return reference_number;
	}

	public void setReference_number(String reference_number) {
		this.reference_number = reference_number;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getSignerName() {
		return signerName;
	}

	public void setSignerName(String signerName) {
		this.signerName = signerName;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getTrackEventCode() {
		return trackEventCode;
	}

	public void setTrackEventCode(String trackEventCode) {
		this.trackEventCode = trackEventCode;
	}

	public String getTrackEventDateOccured() {
		return trackEventDateOccured;
	}

	public void setTrackEventDateOccured(String trackEventDateOccured) {
		this.trackEventDateOccured = trackEventDateOccured;
	}

	public String getTrackEventDetails() {
		return trackEventDetails;
	}

	public void setTrackEventDetails(String trackEventDetails) {
		this.trackEventDetails = trackEventDetails;
	}

	public int getTrackSequence() {
		return trackSequence;
	}

	public void setTrackSequence(int trackSequence) {
		this.trackSequence = trackSequence;
	}

	public String getUser_Id() {
		return user_Id;
	}

	public void setUser_Id(String user_Id) {
		this.user_Id = user_Id;
	}

	public String getConnoteNo() {
		return connoteNo;
	}

	public void setConnoteNo(String connoteNo) {
		this.connoteNo = connoteNo;
	}

	public String getArticleID() {
		return articleID;
	}

	public void setArticleID(String articleID) {
		this.articleID = articleID;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	
	
}
