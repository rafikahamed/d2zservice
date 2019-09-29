package com.d2z.d2zservice.entity;

import java.io.Serializable;

import javax.persistence.*;

/**
 * The persistent class for the trackandtrace database table.
 * 
 */
@Entity
@Table(name="TrackAndtrace")
//@IdClass(TrackEventId.class)
@NamedQuery(name="Trackandtrace.findAll", query="SELECT t FROM Trackandtrace t")
@NamedStoredProcedureQueries({
@NamedStoredProcedureQuery(name = "update-tracking", 
procedureName = "UpdateTracking"),
@NamedStoredProcedureQuery(name = "delete-duplicate", 
procedureName = "deleteduplicate")
})

public class Trackandtrace implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
	//@SequenceGenerator(name = "SEQ_GEN", sequenceName = "eTowerSeqNum", allocationSize = 20) 
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RowId")
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

	//@Id
	@Column(name="TrackEventDateOccured")
	private String trackEventDateOccured;

	//@Id
	@Column(name="TrackEventDetails")
	private String trackEventDetails;
	
	//@Id
	//@GeneratedValue
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
	//@SequenceGenerator(name = "SEQ_GEN", sequenceName = "TrackIDSeqNum", allocationSize = 20) 
	//@Column(name="TrackID")
	//private long trackID;

	@Column(name="TrackSequence")
	private int trackSequence;

	@Column(name="User_Id")
	private String user_Id;

	@Column(name="ConnoteNo")
	private String connoteNo;
	
	//@Id
	@Column(name="ArticleID")
	private String articleID;

	public String getArticleID() {
		return articleID;
	}

	public void setArticleID(String articleID) {
		this.articleID = articleID;
	}

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Reference_number",referencedColumnName = "Reference_number", insertable=false, updatable=false)
    private SenderdataMaster senderData;
	
	
	@Column(name="Location")
	private String location;
	
	
	public Trackandtrace() {
	}

	
	
	public int getRowId() {
		return rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getAirwayBill() {
		return this.airwayBill;
	}

	public void setAirwayBill(String airwayBill) {
		this.airwayBill = airwayBill;
	}

	public String getBarcodelabelNumber() {
		return this.barcodelabelNumber;
	}

	public void setBarcodelabelNumber(String barcodelabelNumber) {
		this.barcodelabelNumber = barcodelabelNumber;
	}

	public String getConnoteNo() {
		return this.connoteNo;
	}

	public void setConnoteNo(String connoteNo) {
		this.connoteNo = connoteNo;
	}

	public String getCourierEvents() {
		return this.courierEvents;
	}

	public void setCourierEvents(String courierEvents) {
		this.courierEvents = courierEvents;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getReference_number() {
		return this.reference_number;
	}

	public void setReference_number(String reference_number) {
		this.reference_number = reference_number;
	}

	public String getSignature() {
		return this.signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getSignerName() {
		return this.signerName;
	}

	public void setSignerName(String signerName) {
		this.signerName = signerName;
	}

	public String getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getTrackEventCode() {
		return this.trackEventCode;
	}

	public void setTrackEventCode(String trackEventCode) {
		this.trackEventCode = trackEventCode;
	}

	public String getTrackEventDateOccured() {
		return this.trackEventDateOccured;
	}

	public void setTrackEventDateOccured(String trackEventDateOccured) {
		this.trackEventDateOccured = trackEventDateOccured;
	}

	public String getTrackEventDetails() {
		return this.trackEventDetails;
	}

	public void setTrackEventDetails(String trackEventDetails) {
		this.trackEventDetails = trackEventDetails;
	}

	/*public long getTrackID() {
		return this.trackID;
	}

	public void setTrackID(long trackID) {
		this.trackID = trackID;
	}*/

	public int getTrackSequence() {
		return this.trackSequence;
	}

	public void setTrackSequence(int trackSequence) {
		this.trackSequence = trackSequence;
	}

	public String getUser_Id() {
		return this.user_Id;
	}

	public void setUser_Id(String user_Id) {
		this.user_Id = user_Id;
	}

}