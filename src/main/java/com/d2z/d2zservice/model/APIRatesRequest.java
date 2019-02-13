package com.d2z.d2zservice.model;

import javax.validation.constraints.NotEmpty;

public class APIRatesRequest {

	@NotEmpty(message = "UserName cannot be empty")
	private String userName;
	@NotEmpty(message = "Password cannot be empty")
	private String password;
	private double weight;
	private String postcode;
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
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	
	
}
