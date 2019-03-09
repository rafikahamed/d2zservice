package com.d2z.d2zservice.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="eTowerResponse")
@NamedQuery(name="ETowerResponse.findAll", query="SELECT e FROM ETowerResponse e")
public class ETowerResponse implements Serializable {

	private static final long serialVersionUID = 1L; 
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="RowID")
    private int rowId;
	
	@Column(name="APIName")
	private String APIName;
	
	@Column(name="ReferenceNumber")
	private String referenceNumber;
	
	@Column(name="OrderId")
	private String orderId;
	
	@Column(name="TrackingNo")
	private String trackingNo;
	
	@Column(name="ErrorCode")
	private String errorCode;
	
	@Column(name="ErrorMessage")
	private String errorMessage;
	
	@Column(name="Timestamp")
	private Timestamp timestamp;
	
	@Column(name="Status")
	private String status;
	
	

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getRowId() {
		return rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public String getAPIName() {
		return APIName;
	}

	public void setAPIName(String aPIName) {
		APIName = aPIName;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTrackingNo() {
		return trackingNo;
	}

	public void setTrackingNo(String trackingNo) {
		this.trackingNo = trackingNo;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	
	
}
