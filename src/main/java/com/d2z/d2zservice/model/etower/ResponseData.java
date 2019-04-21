package com.d2z.d2zservice.model.etower;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ResponseData {
	private String status;
	private String orderId;
	private String referenceNo;
	private String trackingNo;
	private List<EtowerErrorResponse> errors;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getReferenceNo() {
		return referenceNo;
	}
	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}
	public String getTrackingNo() {
		return trackingNo;
	}
	public void setTrackingNo(String trackingNo) {
		this.trackingNo = trackingNo;
	}
	public List<EtowerErrorResponse> getErrors() {
		return errors;
	}
	public void setErrors(List<EtowerErrorResponse> errors) {
		this.errors = errors;
	}
	
	
}
