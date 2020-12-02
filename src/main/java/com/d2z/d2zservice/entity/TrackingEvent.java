package com.d2z.d2zservice.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="TrackingEvent")
public class TrackingEvent implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Id
	@Column(name="order_id")
	private String orderId;

	@Column(name="event_date_occured")
	private String trackEventDateOccured;

	@Column(name="event_detail")
	private String trackEventDetails;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTrackEventDateOccured() {
		return trackEventDateOccured;
	}

	public void setTrackEventDateOccured(String trackEventDateOccured) {
		this.trackEventDateOccured = trackEventDateOccured;
	}

	public String getTrackEventDetails() {
		return trackEventDetails;
	}

	public void setTrackEventDetails(String trackEventDetails) {
		this.trackEventDetails = trackEventDetails;
	}
	
	


}
