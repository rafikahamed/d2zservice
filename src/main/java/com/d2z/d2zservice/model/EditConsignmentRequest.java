package com.d2z.d2zservice.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class EditConsignmentRequest {

	@NotEmpty(message = "Reference Number cannot be empty")
	private String referenceNumber;
	@NotNull(message = "Weight cannot be empty")
	private Double weight;
	public String getReferenceNumber() {
		return referenceNumber;
	}
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	
}
