package com.d2z.d2zservice.model;

import java.sql.Timestamp;
import java.util.List;

public class TrackParcel {

	private String referenceNumber;
	private String barcodelabelNumber;
	private String eventDetails;
	private String consignmentCreated;
	private String shipmentCreated;
	private String heldByCustoms;
	private String clearedCustoms;
	private String received;
	private String processedByFacility;
	private String inTransit;
	private String delivered;
	private Timestamp trackEventDateOccured;
	private List<TrackingEvents> trackingEvents;
	
	public List<TrackingEvents> getTrackingEvents() {
		return trackingEvents;
	}
	public void setTrackingEvents(List<TrackingEvents> trackingEvents) {
		this.trackingEvents = trackingEvents;
	}
	public Timestamp getTrackEventDateOccured() {
		return trackEventDateOccured;
	}
	public void setTrackEventDateOccured(Timestamp trackEventDateOccured) {
		this.trackEventDateOccured = trackEventDateOccured;
	}
	
	public String getReferenceNumber() {
		return referenceNumber;
	}
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
	
	public String getBarcodelabelNumber() {
		return barcodelabelNumber;
	}
	public void setBarcodelabelNumber(String barcodelabelNumber) {
		this.barcodelabelNumber = barcodelabelNumber;
	}
	public String getConsignmentCreated() {
		return consignmentCreated;
	}
	public void setConsignmentCreated(String consignmentCreated) {
		this.consignmentCreated = consignmentCreated;
	}
	public String getShipmentCreated() {
		return shipmentCreated;
	}
	public void setShipmentCreated(String shipmentCreated) {
		this.shipmentCreated = shipmentCreated;
	}
	public String getHeldByCustoms() {
		return heldByCustoms;
	}
	public void setHeldByCustoms(String heldByCustoms) {
		this.heldByCustoms = heldByCustoms;
	}
	public String getClearedCustoms() {
		return clearedCustoms;
	}
	public void setClearedCustoms(String clearedCustoms) {
		this.clearedCustoms = clearedCustoms;
	}
	public String getReceived() {
		return received;
	}
	public void setReceived(String received) {
		this.received = received;
	}
	public String getProcessedByFacility() {
		return processedByFacility;
	}
	public void setProcessedByFacility(String processedByFacility) {
		this.processedByFacility = processedByFacility;
	}
	public String getInTransit() {
		return inTransit;
	}
	public void setInTransit(String inTransit) {
		this.inTransit = inTransit;
	}
	public String getDelivered() {
		return delivered;
	}
	public void setDelivered(String delivered) {
		this.delivered = delivered;
	}
	public String getEventDetails() {
		return eventDetails;
	}
	public void setEventDetails(String eventDetails) {
		this.eventDetails = eventDetails;
	}
	
}
