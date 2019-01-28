package com.d2z.d2zservice.model;

import java.util.List;

public class D2ZRatesData {

	private String MLID;
	private List<ZoneDetails> zone;
	private String GST;
	public String getMLID() {
		return MLID;
	}
	public void setMLID(String mLID) {
		MLID = mLID;
	}
	public List<ZoneDetails> getZone() {
		return zone;
	}
	public void setZone(List<ZoneDetails> zone) {
		this.zone = zone;
	}
	public String getGST() {
		return GST;
	}
	public void setGST(String gST) {
		GST = gST;
	}
	
	
}
