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
@Table(name="CSTickets")
@NamedQuery(name="CSTickets.findAll", query="SELECT a FROM CSTickets a")
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
	
	@Column(name = "TrackingStatus")
	private String trackingStatus;
	
	@Column(name = "TrackingEvent")
	private String trackingEvent;
	
	@Column(name = "TrackingEventDateOccured")
	private Timestamp trackingEventDateOccured;
	
	@Column(name = "Status")
	private String status;
	
	@Column(name="userId")
	private int userId;

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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
