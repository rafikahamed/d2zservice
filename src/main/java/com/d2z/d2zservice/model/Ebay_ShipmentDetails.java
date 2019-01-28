package com.d2z.d2zservice.model;

import java.util.List;

public class Ebay_ShipmentDetails {

	private String clientEbayToken;
	private List<Ebay_Shipment> shipment;
	public String getClientEbayToken() {
		return clientEbayToken;
	}
	public void setClientEbayToken(String clientEbayToken) {
		this.clientEbayToken = clientEbayToken;
	}
	public List<Ebay_Shipment> getShipment() {
		return shipment;
	}
	public void setShipment(List<Ebay_Shipment> shipment) {
		this.shipment = shipment;
	}
	
}
