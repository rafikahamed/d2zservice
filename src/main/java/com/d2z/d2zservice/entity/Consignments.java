package com.d2z.d2zservice.entity;

public class Consignments {
	private String referenceNumber; 
	private String stateCode;
	private double weight;
	

	public Consignments(String referenceNumber, String stateCode, double weight) {
		this.referenceNumber = referenceNumber;
		this.stateCode = stateCode;
		this.weight = weight;
	}
	public String getReferenceNumber() {
		return referenceNumber;
	}
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
	public String getStateCode() {
		return stateCode;
	}
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	@Override
	   public String toString() {
	        return ("{"+this.referenceNumber+","+this.stateCode+","+this.weight+"}");
	   }
}


