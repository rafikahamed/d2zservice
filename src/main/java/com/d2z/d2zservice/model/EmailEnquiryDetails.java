package com.d2z.d2zservice.model;

public class EmailEnquiryDetails {
	
	private String ticketId;
	private String articleId;
	private String referenceNumber;
	private String deliveryEnquiry;
	private String pod;
	private String comments;
	private String d2zComments;
	private String consignee_Name;
	private String trackingEvent;
	private String trackingEventDateOccured;
	private String userId;
	private String client_Broker_Id;
	private String emailAddress;
	
	public EmailEnquiryDetails(Object[] obj) {
		ticketId = (String)obj[0];
		articleId = (String)obj[1];
		referenceNumber = (String)obj[2];
		deliveryEnquiry = (String)obj[3];
		pod = (String)obj[4];
		comments = (String)obj[5];
		d2zComments = (String)obj[6];
		consignee_Name = (String)obj[7];
		trackingEvent = (String)obj[8];
		trackingEventDateOccured = (String)obj[9];
		userId = Integer.toString((int) obj[10]);
		client_Broker_Id = Integer.toString((int) obj[11]);
		emailAddress = (String)obj[12];
	}

	public String getTicketId() {
		return ticketId;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getDeliveryEnquiry() {
		return deliveryEnquiry;
	}

	public void setDeliveryEnquiry(String deliveryEnquiry) {
		this.deliveryEnquiry = deliveryEnquiry;
	}

	public String getPod() {
		return pod;
	}

	public void setPod(String pod) {
		this.pod = pod;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getD2zComments() {
		return d2zComments;
	}

	public void setD2zComments(String d2zComments) {
		this.d2zComments = d2zComments;
	}

	public String getConsignee_Name() {
		return consignee_Name;
	}

	public void setConsignee_Name(String consignee_Name) {
		this.consignee_Name = consignee_Name;
	}

	public String getTrackingEvent() {
		return trackingEvent;
	}

	public void setTrackingEvent(String trackingEvent) {
		this.trackingEvent = trackingEvent;
	}

	public String getTrackingEventDateOccured() {
		return trackingEventDateOccured;
	}

	public void setTrackingEventDateOccured(String trackingEventDateOccured) {
		this.trackingEventDateOccured = trackingEventDateOccured;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getClient_Broker_Id() {
		return client_Broker_Id;
	}

	public void setClient_Broker_Id(String client_Broker_Id) {
		this.client_Broker_Id = client_Broker_Id;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
}
