package com.d2z.d2zservice.model;

public class PflCreateShippingOrderInfo {
	
	private String recipientName;
	private String recipientCompany;
	private String phone;
	private String email;
	private String addressLine1;
	private String addressLine2;
	private String city ;
	private String state;
	private String postcode;
	private String country = "AU";
	private String custom_ref;
	private Double weight;
	private String description = "bags";
	private String delivery_instruction;
	
	public String getRecipientName() {
		return recipientName;
	}
	public String getDelivery_instruction() {
		return delivery_instruction;
	}
	public void setDelivery_instruction(String delivery_instruction) {
		this.delivery_instruction = delivery_instruction;
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
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddressLine1() {
		return addressLine1;
	}
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	public String getAddressLine2() {
		return addressLine2;
	}
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
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
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCustom_ref() {
		return custom_ref;
	}
	public void setCustom_ref(String custom_ref) {
		this.custom_ref = custom_ref;
	}

}
