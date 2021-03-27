package com.d2z.d2zservice.model;

public class ManualInvoiceData {
	
	private String brokerName;
	private String trackingNumber;
	private String fuelSurcharge;
	private String postage;
	private String total;
	private String airwayBill;
	public String getBrokerName() {
		return brokerName;
	}
	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
	}
	public String getTrackingNumber() {
		return trackingNumber;
	}
	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}
	public String getFuelSurcharge() {
		return fuelSurcharge;
	}
	public void setFuelSurcharge(String fuelSurcharge) {
		this.fuelSurcharge = fuelSurcharge;
	}
	public String getPostage() {
		return postage;
	}
	public void setPostage(String postage) {
		this.postage = postage;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getAirwayBill() {
		return airwayBill;
	}
	public void setAirwayBill(String airwayBill) {
		this.airwayBill = airwayBill;
	}
	
	

}
