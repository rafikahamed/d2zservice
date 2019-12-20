package com.d2z.d2zservice.model;

public class DownloadInvice {
	
	private String brokerName;
	private String trackingNumber;
	private String referenceNuber;
	private String postcode;
	private String weight;
	private String postage;
	private String fuelsurcharge;
	private String total;
	private String serviceType;
	private String airwaybill;
	
	public String getBrokerName() {
		return brokerName;
	}
	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
	}
	public String getAirwaybill() {
		return airwaybill;
	}
	public void setAirwaybill(String airwaybill) {
		this.airwaybill = airwaybill;
	}
	public String getTrackingNumber() {
		return trackingNumber;
	}
	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}
	public String getReferenceNuber() {
		return referenceNuber;
	}
	public void setReferenceNuber(String referenceNuber) {
		this.referenceNuber = referenceNuber;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getPostage() {
		return postage;
	}
	public void setPostage(String postage) {
		this.postage = postage;
	}
	public String getFuelsurcharge() {
		return fuelsurcharge;
	}
	public void setFuelsurcharge(String fuelsurcharge) {
		this.fuelsurcharge = fuelsurcharge;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	
}
