package com.d2z.d2zservice.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
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
	private String expectedDeliveryDate;
	
	@Column(name = "EnquiryOpenDate")
	private Timestamp enquiryOpenDate;
	
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
	private String consigneeaddr1;
	
	@Column(name = "Consignee_Suburb")
	private String consigneeSuburb;
	
	@Column(name = "Consignee_State")
	private String consigneeState;
	
	@Column(name = "Consignee_Postcode")
	private String consigneePostcode;
	
	@Column(name = "Product_Description")
	private String productDescription;
	
	@Column(name="BarcodelabelNumber")
	private String barcodelabelNumber;
	
	@Lob
	@Column(name="proof")
	private byte[] proof;
	
	@Column(name="fileName")
	private String fileName;
	
	@Column(name="Client_Broker_id")
	private int clientBrokerId;
	
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

	public String getD2zComments() {
		return d2zComments;
	}

	public void setD2zComments(String d2zComments) {
		this.d2zComments = d2zComments;
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

	public String getExpectedDeliveryDate() {
		return expectedDeliveryDate;
	}

	public void setExpectedDeliveryDate(String expectedDeliveryDate) {
		this.expectedDeliveryDate = expectedDeliveryDate;
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

	public byte[] getAttachment() {
		return attachment;
	}

	public void setAttachment(byte[] attachment) {
		this.attachment = attachment;
	}

	public String getSendUpdate() {
		return sendUpdate;
	}

	public void setSendUpdate(String sendUpdate) {
		this.sendUpdate = sendUpdate;
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

	public String getBarcodelabelNumber() {
		return barcodelabelNumber;
	}

	public void setBarcodelabelNumber(String barcodelabelNumber) {
		this.barcodelabelNumber = barcodelabelNumber;
	}

	public Timestamp getEnquiryOpenDate() {
		return enquiryOpenDate;
	}

	public void setEnquiryOpenDate(Timestamp enquiryOpenDate) {
		this.enquiryOpenDate = enquiryOpenDate;
	}

	public byte[] getProof() {
		return proof;
	}

	public void setProof(byte[] blob) {
		this.proof = blob;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getClientBrokerId() {
		return clientBrokerId;
	}

	public void setClientBrokerId(int clientBrokerId) {
		this.clientBrokerId = clientBrokerId;
	}

}
