package com.d2z.d2zservice.model.etower;

import java.util.List;

public class LabelData {

	private String orderId;
	private String referenceNo;
	private String trackingNo;
	private String articleId;
	private String barCode;
	private String barCode2D;

	public String getBarCode2D() {
		return barCode2D;
	}
	public void setBarCode2D(String barCode2D) {
		this.barCode2D = barCode2D;
	}
	private String status;
	private List<EtowerErrorResponse> errors;
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
	public String getArticleId() {
		return articleId;
	}
	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<EtowerErrorResponse> getErrors() {
		return errors;
	}
	public void setErrors(List<EtowerErrorResponse> errors) {
		this.errors = errors;
	}
	
}
