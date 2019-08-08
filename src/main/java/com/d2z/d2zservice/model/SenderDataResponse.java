package com.d2z.d2zservice.model;

public class SenderDataResponse {

	private String referenceNumber;
	private String barcodeLabelNumber;
	private String carrier;
	private String message;
	private String datamatrix;
	private String soccode;
	public String getSoccode() {
		return soccode;
	}

	public void setSoccode(String soccode) {
		this.soccode = soccode;
	}

	public String getDatamatrix() {
		return datamatrix;
	}

	public void setDatamatrix(String datamatrix) {
		this.datamatrix = datamatrix;
	}

	public String getInjectionPort() {
		return injectionPort;
	}

	public void setInjectionPort(String injectionPort) {
		this.injectionPort = injectionPort.length()>3?injectionPort.substring(0,3):injectionPort;
	}

	private String injectionPort;

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
