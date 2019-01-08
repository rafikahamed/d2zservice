package com.d2z.d2zservice.model;

import java.util.List;

public class ZoneDetails {

	private String zoneID;
	private List<ZoneRates> rates;
	public String getZoneID() {
		return zoneID;
	}
	public void setZoneID(String zoneID) {
		this.zoneID = zoneID;
	}
	public List<ZoneRates> getRates() {
		return rates;
	}
	public void setRates(List<ZoneRates> rates) {
		this.rates = rates;
	}

	
}
