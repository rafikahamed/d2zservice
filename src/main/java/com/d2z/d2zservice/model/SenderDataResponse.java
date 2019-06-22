package com.d2z.d2zservice.model;

public class SenderDataResponse {

	private String referenceNumber;
	private String barcodeLabelNumber;
	private String carrier;
	private String message;

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getBarcodeLabelNumber() {
		return barcodeLabelNumber;
	}

	public void setBarcodeLabelNumber(String barcodeLabelNumber) {
		this.barcodeLabelNumber = barcodeLabelNumber;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
