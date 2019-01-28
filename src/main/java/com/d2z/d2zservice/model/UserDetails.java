package com.d2z.d2zservice.model;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class UserDetails {

	@NotEmpty(message = "Company Name is mandatory")
	private String companyName;
	private String contactName;
	private String address;
	private String suburb;
	private String state;
	private String postCode;
	@NotEmpty(message = "Country is mandatory")
	private String country;
	@NotEmpty(message = "Email Address is mandatory")
    @Email(message = "Invalid Email Address")
	private String emailAddress;
	@NotEmpty(message = "UserName is mandatory")
	private String userName;
	@NotEmpty(message = "Password is mandatory")
	private String password;
	@NotEmpty(message = "Service Type is mandatory")
	private List<String> serviceType;
	private String contactPhoneNumber;
	private int role_Id;
	private List<String> deletedServiceTypes;
	private int user_id;
	
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getRole_Id() {
		return role_Id;
	}
	public void setRole_Id(int role_Id) {
		this.role_Id = role_Id;
	}
	public List<String> getDeletedServiceTypes() {
		return deletedServiceTypes;
	}
	public void setDeletedServiceTypes(List<String> deletedServiceTypes) {
		this.deletedServiceTypes = deletedServiceTypes;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getContactPhoneNumber() {
		return contactPhoneNumber;
	}
	public void setContactPhoneNumber(String contactPhoneNumber) {
		this.contactPhoneNumber = contactPhoneNumber;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getSuburb() {
		return suburb;
	}
	public void setSuburb(String suburb) {
		this.suburb = suburb;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<String> getServiceType() {
		return serviceType;
	}
	public void setServiceType(List<String> serviceType) {
		this.serviceType = serviceType;
	}

}
