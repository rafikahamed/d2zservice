package com.d2z.d2zservice.model.etower;

import java.util.ArrayList;
import java.util.List;

public class CreateShippingRequest {
	private String trackingNo;
	private String referenceNo;
	private String addressLine1;
	private String addressLine2;
	private String recipientName;
	private String recipientCompany;
	private String city ;
	private String state;
	private String postcode;
	private String country = "AU";
	private String serviceCode = "UBI.AU2AU.AUPOST";
	private String serviceOption;
	private String facility;
	private Double weight;
	private Double invoiceValue;
	private String invoiceCurrency = "AUD";
	private String description = "Bags";
	private String nativeDescription = "Bags";
	private String shipperName = "Test company";
	private String shipperAddressLine1 = "1 fake st";
	private String shipperCity = "Petaling Jaya";
	private String shipperState = "Selangor";
	private String shipperCountry = "MY";
	private String email;
	private String returnOption;
	private boolean authorityToleave;
	private String vendorid;
	
	public String getNativeDescription() {
		return nativeDescription;
	}

	public void setNativeDescription(String nativeDescription) {
		this.nativeDescription = nativeDescription;
	}

	public String getVendorid() {
		return vendorid;
	}

	public void setVendorid(String vendorid) {
		this.vendorid = vendorid;
	}

	public String getReturnOption() {
		return returnOption;
	}

	public void setReturnOption(String returnOption) {
		this.returnOption = returnOption;
	}

	public boolean isAuthorityToleave() {
		return authorityToleave;
	}

	public void setAuthorityToleave(boolean authorityToleave) {
		this.authorityToleave = authorityToleave;
	}
	private boolean dangerousGoods;
	
	public boolean isDangerousGoods() {
		return dangerousGoods;
	}

	public void setDangerousGoods(boolean dangerousGoods) {
		this.dangerousGoods = dangerousGoods;
	}

	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	private List<OrderItems> orderItems;
	
	
	public String getServiceOption() {
		return serviceOption;
	}
	public void setServiceOption(String serviceOption) {
		this.serviceOption = serviceOption;
	}
	public String getAddressLine2() {
		return addressLine2;
	}
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}
	public String getTrackingNo() {
		return trackingNo;
	}
	public void setTrackingNo(String trackingNo) {
		this.trackingNo = trackingNo;
	}
	public String getReferenceNo() {
		return referenceNo;
	}
	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}
	public String getAddressLine1() {
		return addressLine1;
	}
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	public String getRecipientName() {
		return recipientName;
	}
	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}
	public String getRecipientCompany() {
		return recipientCompany;
	}
	public void setRecipientCompany(String recipientCompany) {
		this.recipientCompany = recipientCompany;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getFacility() {
		return facility;
	}
	public void setFacility(String facility) {
		this.facility = facility;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public Double getInvoiceValue() {
		return invoiceValue;
	}
	public void setInvoiceValue(Double invoiceValue) {
		this.invoiceValue = invoiceValue;
	}
	public String getInvoiceCurrency() {
		return invoiceCurrency;
	}
	public void setInvoiceCurrency(String invoiceCurrency) {
		this.invoiceCurrency = invoiceCurrency;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getShipperName() {
		return shipperName;
	}
	public void setShipperName(String shipperName) {
		this.shipperName = shipperName;
	}
	public String getShipperAddressLine1() {
		return shipperAddressLine1;
	}
	public void setShipperAddressLine1(String shipperAddressLine1) {
		this.shipperAddressLine1 = shipperAddressLine1;
	}
	public String getShipperCity() {
		return shipperCity;
	}
	public void setShipperCity(String shipperCity) {
		this.shipperCity = shipperCity;
	}
	public String getShipperState() {
		return shipperState;
	}
	public void setShipperState(String shipperState) {
		this.shipperState = shipperState;
	}
	public String getShipperCountry() {
		return shipperCountry;
	}
	public void setShipperCountry(String shipperCountry) {
		this.shipperCountry = shipperCountry;
	}
	public List<OrderItems> getOrderItems() {
		if(null == orderItems) {
			this.orderItems = new ArrayList<OrderItems>();
			this.orderItems.add(new OrderItems());
		}
		return orderItems;
	}
	public void setOrderItems(List<OrderItems> orderItems) {
		this.orderItems = orderItems;
	}
	
	
	
	
	
}
