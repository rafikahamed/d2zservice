package com.d2z.d2zservice.model;

import java.util.List;

import javax.validation.constraints.NotEmpty;

public class CreateConsignmentRequest {

	@NotEmpty(message = "UserName is mandatory")
	private String userName;
	private List<SenderData> senderData;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public List<SenderData> getSenderData() {
		return senderData;
	}
	public void setSenderData(List<SenderData> senderData) {
		this.senderData = senderData;
	}
	
}
