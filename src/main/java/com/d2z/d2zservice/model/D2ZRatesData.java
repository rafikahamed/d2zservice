package com.d2z.d2zservice.model;

import java.util.List;

public class D2ZRatesData {

	private String serviceType;
	private List<ZoneDetails> zone;
	private String fuelSurcharge;
	
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public List<ZoneDetails> getZone() {
		return zone;
	}
	public void setZone(List<ZoneDetails> zone) {
		this.zone = zone;
	}
	public String getFuelSurcharge() {
		return fuelSurcharge;
	}
	public void setFuelSurcharge(String fuelSurcharge) {
		this.fuelSurcharge = fuelSurcharge;
	}
	
}
