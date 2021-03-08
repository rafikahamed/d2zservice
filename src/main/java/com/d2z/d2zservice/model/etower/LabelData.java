package com.d2z.d2zservice.model.etower;

import java.util.List;

public class LabelData {

	private String orderId;
	private String referenceNo;
	private String trackingNo;
	private String articleId;
	private String barCode;
	private String barCode2D;
	private String provider;
	private String hub;
	private String matrix;
	private String status;
	private String carrier;
	private String header;
	private String footer;
	
	
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getFooter() {
		return footer;
	}
	public void setFooter(String footer) {
		this.footer = footer;
	}
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
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getBarCode2D() {
		return barCode2D;
	}
	public void setBarCode2D(String barCode2D) {
		this.barCode2D = barCode2D;
	}
	public String getHub() {
		return hub;
	}
	public void setHub(String hub) {
		this.hub = hub;
	}
	public String getMatrix() {
		return matrix;
	}
	public void setMatrix(String matrix) {
		this.matrix = matrix;
	}
	public String getCarrier() {
		return carrier;
	}
	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}
	
}
