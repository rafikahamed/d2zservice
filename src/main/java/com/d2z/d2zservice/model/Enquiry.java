package com.d2z.d2zservice.model;

import java.util.List;

public class Enquiry {
	
	private List<CreateEnquiryRequest> enquiryDetails;
	private String userName;
	
	public List<CreateEnquiryRequest> getEnquiryDetails() {
		return enquiryDetails;
	}
	public void setEnquiryDetails(List<CreateEnquiryRequest> enquiryDetails) {
		this.enquiryDetails = enquiryDetails;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
