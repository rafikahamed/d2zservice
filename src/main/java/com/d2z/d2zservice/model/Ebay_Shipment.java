package com.d2z.d2zservice.model;

public class Ebay_Shipment {
	private String referenceNumber;
	private String shipmentTrackingNumber;
	private String carrierUsed;
	public String getReferenceNumber() {
		return referenceNumber;
	}
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
	public String getShipmentTrackingNumber() {
		return shipmentTrackingNumber;
	}
	public void setShipmentTrackingNumber(String shipmentTrackingNumber) {
		this.shipmentTrackingNumber = shipmentTrackingNumber;
	}
	public String getCarrierUsed() {
		return carrierUsed;
	}
	public void setCarrierUsed(String carrierUsed) {
		this.carrierUsed = carrierUsed;
	}
	
	
}
