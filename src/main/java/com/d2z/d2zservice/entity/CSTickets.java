package com.d2z.d2zservice.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="CSTickets")
public class CSTickets implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "TicketID")
	private String ticketID;
	
	@Column(name = "ArticleID")
	private String articleID;
	
	@Column(name = "ReferenceNumber")
	private String referenceNumber;
	
	@Column(name = "DeliveryEnquiry")
	private String deliveryEnquiry;
	
	@Column(name = "POD")
	private String pod;
	
	@Column(name = "Comments")
	private String comments;
	
	@Column(name = "D2ZComments")
	private String d2zComments;
	
	@Column(name = "TrackingStatus")
	private String trackingStatus;
	
	@Column(name = "TrackingEvent")
	private String trackingEvent;
	
	@Column(name = "TrackingEventDateOccured")
	private Timestamp trackingEventDateOccured;
	
	@Column(name = "ExpectedDeliveryDate")
	private Timestamp expectedDeliveryDate;
	
	@Column(name = "Status")
	private String status;
	
	@Column(name="userId")
	private int userId;
	
	@Column(name = "Consignee_name")
	private String consigneeName;
	
	@Column(name = "Carrier")
	private String carrier;
	
	@Column(name = "Attachment")
	private byte[] attachment;
	
	@Column(name = "SendUpdate")
	private String sendUpdate;
	
	@Column(name = "Consignee_addr1")
	private String consignee_addr1;
	
	@Column(name = "Consignee_Suburb")
	private String consignee_Suburb;
	
	@Column(name = "Consignee_State")
	private String consignee_State;
	
	@Column(name = "Consignee_Postcode")
	private String consignee_Postcode;
	
	@Column(name = "Product_Description")
	private String product_Description;
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getTicketID() {
		return ticketID;
	}

	public void setTicketID(String ticketID) {
		this.ticketID = ticketID;
	}

	public String getArticleID() {
		return articleID;
	}

	public void setArticleID(String articleID) {
		this.articleID = articleID;
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

	public String getTrackingStatus() {
		return trackingStatus;
	}

	public void setTrackingStatus(String trackingStatus) {
		this.trackingStatus = trackingStatus;
	}

	public String getTrackingEvent() {
		return trackingEvent;
	}

	public void setTrackingEvent(String trackingEvent) {
		this.trackingEvent = trackingEvent;
	}

	public Timestamp getTrackingEventDateOccured() {
		return trackingEventDateOccured;
	}

	public void setTrackingEventDateOccured(Timestamp trackingEventDateOccured) {
		this.trackingEventDateOccured = trackingEventDateOccured;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}
	
	public String getD2zComments() {
		return d2zComments;
	}

	public void setD2zComments(String d2zComments) {
		this.d2zComments = d2zComments;
	}

	public Timestamp getExpectedDeliveryDate() {
		return expectedDeliveryDate;
	}

	public void setExpectedDeliveryDate(Timestamp expectedDeliveryDate) {
		this.expectedDeliveryDate = expectedDeliveryDate;
	}

	public byte[] getAttachment() {
		return attachment;
	}

	public void setAttachment(byte[] attachment) {
		this.attachment = attachment;
	}

	public String getConsignee_addr1() {
		return consignee_addr1;
	}

	public String getSendUpdate() {
		return sendUpdate;
	}

	public void setSendUpdate(String sendUpdate) {
		this.sendUpdate = sendUpdate;
	}

	public void setConsignee_addr1(String consignee_addr1) {
		this.consignee_addr1 = consignee_addr1;
	}

	public String getConsignee_Suburb() {
		return consignee_Suburb;
	}

	public void setConsignee_Suburb(String consignee_Suburb) {
		this.consignee_Suburb = consignee_Suburb;
	}

	public String getConsignee_State() {
		return consignee_State;
	}

	public void setConsignee_State(String consignee_State) {
		this.consignee_State = consignee_State;
	}

	public String getConsignee_Postcode() {
		return consignee_Postcode;
	}

	public void setConsignee_Postcode(String consignee_Postcode) {
		this.consignee_Postcode = consignee_Postcode;
	}

	public String getProduct_Description() {
		return product_Description;
	}

	public void setProduct_Description(String product_Description) {
		this.product_Description = product_Description;
	}

}
