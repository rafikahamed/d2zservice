package com.d2z.d2zservice.model;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

public class APIRatesRequest {

	@NotEmpty(message = "UserName cannot be empty")
	private String userName;
	@NotEmpty(message = "Password cannot be empty")
	private String password;
	//@NotEmpty(message="Service type cannot be empty")
	/*private String servicetype;
	public String getServicetype() {
		return servicetype;
	}
	public void setServicetype(String servicetype) {
		this.servicetype = servicetype;
	}*/
	@Valid
	private List<PostCodeWeight> consignmentDetails;
	
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
	public List<PostCodeWeight> getConsignmentDetails() {
		return consignmentDetails;
	}
	public void setConsignmentDetails(List<PostCodeWeight> consignmentDetails) {
		this.consignmentDetails = consignmentDetails;
	}
	
	
	
}
