package com.d2z.d2zservice.model;

import java.sql.Timestamp;

public class TrackParcel {

	private String referenceNumber;
	private String barcodelabelNumber;
	private Timestamp consignmentCreated;
	private Timestamp shipmentCreated;
	private Timestamp heldByCustoms;
	private Timestamp clearedCustoms;
	private Timestamp received;
	private Timestamp processedByFacility;
	private Timestamp inTransit;
	private Timestamp delivered;
	
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
	public Timestamp getConsignmentCreated() {
		return consignmentCreated;
	}
	public void setConsignmentCreated(Timestamp consignmentCreated) {
		this.consignmentCreated = consignmentCreated;
	}
	public Timestamp getShipmentCreated() {
		return shipmentCreated;
	}
	public void setShipmentCreated(Timestamp shipmentCreated) {
		this.shipmentCreated = shipmentCreated;
	}
	public Timestamp getHeldByCustoms() {
		return heldByCustoms;
	}
	public void setHeldByCustoms(Timestamp heldByCustoms) {
		this.heldByCustoms = heldByCustoms;
	}
	public Timestamp getClearedCustoms() {
		return clearedCustoms;
	}
	public void setClearedCustoms(Timestamp clearedCustoms) {
		this.clearedCustoms = clearedCustoms;
	}
	public Timestamp getReceived() {
		return received;
	}
	public void setReceived(Timestamp received) {
		this.received = received;
	}
	public Timestamp getProcessedByFacility() {
		return processedByFacility;
	}
	public void setProcessedByFacility(Timestamp processedByFacility) {
		this.processedByFacility = processedByFacility;
	}
	public Timestamp getInTransit() {
		return inTransit;
	}
	public void setInTransit(Timestamp inTransit) {
		this.inTransit = inTransit;
	}
	public Timestamp getDelivered() {
		return delivered;
	}
	public void setDelivered(Timestamp delivered) {
		this.delivered = delivered;
	}
	
}
