package com.d2z.d2zservice.model;

public class OpenEnquiryResponse {
	
	private String userName;
	private String ticketNumber;
	private int userId;
	private int client_broker_id;
	private String articleID;
	private String trackingEventDateOccured;
	private String trackingDeliveryDate;
	private String consigneeName;
	private String status;
	private String comments;
	private String d2zComments;
	private String attachment;
	private String consigneeaddr1;
	private String consigneeSuburb;
	private String consigneeState;
	private String consigneePostcode;
	private String productDescription;
	private String sendUpdate;
	
	public String getSendUpdate() {
		return sendUpdate;
	}
	public void setSendUpdate(String sendUpdate) {
		this.sendUpdate = sendUpdate;
	}
	public String getD2zComments() {
		return d2zComments;
	}
	public void setD2zComments(String d2zComments) {
		this.d2zComments = d2zComments;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getTicketNumber() {
		return ticketNumber;
	}
	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getClient_broker_id() {
		return client_broker_id;
	}
	public void setClient_broker_id(int client_broker_id) {
		this.client_broker_id = client_broker_id;
	}
	public String getArticleID() {
		return articleID;
	}
	public void setArticleID(String articleID) {
		this.articleID = articleID;
	}
	public String getTrackingEventDateOccured() {
		return trackingEventDateOccured;
	}
	public void setTrackingEventDateOccured(String trackingEventDateOccured) {
		this.trackingEventDateOccured = trackingEventDateOccured;
	}
	public String getTrackingDeliveryDate() {
		return trackingDeliveryDate;
	}
	public void setTrackingDeliveryDate(String trackingDeliveryDate) {
		this.trackingDeliveryDate = trackingDeliveryDate;
	}
	public String getConsigneeName() {
		return consigneeName;
	}
	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public String getConsigneeaddr1() {
		return consigneeaddr1;
	}
	public void setConsigneeaddr1(String consigneeaddr1) {
		this.consigneeaddr1 = consigneeaddr1;
	}
	public String getConsigneeSuburb() {
		return consigneeSuburb;
	}
	public void setConsigneeSuburb(String consigneeSuburb) {
		this.consigneeSuburb = consigneeSuburb;
	}
	public String getConsigneeState() {
		return consigneeState;
	}
	public void setConsigneeState(String consigneeState) {
		this.consigneeState = consigneeState;
	}
	public String getConsigneePostcode() {
		return consigneePostcode;
	}
	public void setConsigneePostcode(String consigneePostcode) {
		this.consigneePostcode = consigneePostcode;
	}
	public String getProductDescription() {
		return productDescription;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	
}
