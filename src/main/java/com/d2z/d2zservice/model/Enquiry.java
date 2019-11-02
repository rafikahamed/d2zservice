package com.d2z.d2zservice.model;

import java.util.List;

public class Enquiry {
	
	private List<CreateEnquiryRequest> enquiryDetails;
	private int userId;
	
	public List<CreateEnquiryRequest> getEnquiryDetails() {
		return enquiryDetails;
	}
	public void setEnquiryDetails(List<CreateEnquiryRequest> enquiryDetails) {
		this.enquiryDetails = enquiryDetails;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
}
