package com.d2z.d2zservice.model.TransVirtual;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Items {
	private String Barcode = "HA88332232343239480923758";

	@JsonProperty("Barcode")
	public String getBarcode() {
		return Barcode;
	}

	public void setBarcode(String barcode) {
		Barcode = barcode;
	}
	
	

}
