package com.d2z.d2zservice.model;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

public class CreateConsignmentRequest {

	@NotEmpty(message = "UserName is mandatory")
	private String userName;
	@Valid
	private List<SenderDataApi> consignmentData;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public List<SenderDataApi> getConsignmentData() {
		return consignmentData;
	}
	public void setConsignmentData(List<SenderDataApi> consignmentData) {
		this.consignmentData = consignmentData;
	}
	
}
