package com.d2z.d2zservice.model;

public class ReturnsAction {
	
	private String articleId;
	private String barcodelabelNumber;
	private String brokerName;
	private String referenceNumber;
	private String action;
	private String resendRefNumber;
	
	public String getArticleId() {
		return articleId;
	}
	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
	public String getBarcodelabelNumber() {
		return barcodelabelNumber;
	}
	public void setBarcodelabelNumber(String barcodelabelNumber) {
		this.barcodelabelNumber = barcodelabelNumber;
	}
	public String getBrokerName() {
		return brokerName;
	}
	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
	}
	public String getReferenceNumber() {
		return referenceNumber;
	}
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getResendRefNumber() {
		return resendRefNumber;
	}
	public void setResendRefNumber(String resendRefNumber) {
		this.resendRefNumber = resendRefNumber;
	}
	
}
