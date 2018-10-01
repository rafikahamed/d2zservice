package com.d2z.d2zservice.model;

import java.util.List;

public class TrackParcel {

	private String referenceNumber;
	private String barcodelabelNumber;
	private String consignmentCreated;
	private String shipmentCreated;
	private String heldByCustoms;
	private String clearedCustoms;
	private String received;
	private String processedByFacility;
	private String inTransit;
	private String delivered;
	
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
	
	
}
