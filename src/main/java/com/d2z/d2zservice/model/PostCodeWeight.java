package com.d2z.d2zservice.model;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;

public class PostCodeWeight {
	@Digits( fraction =2, message = "Invalid Weight", integer = 10)
	@DecimalMax(value = "22.00", message = "Weight can not more than be 22")
	private double weight;
	@NotEmpty(message = "postcode cannot be empty")
	private String postcode;
	private double rate;
	
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
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
