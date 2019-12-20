package com.d2z.d2zservice.model;

import java.util.List;

public class Zone {
	
	private List<ZoneResponse> ZoneResponse;
	private CategoryResponse categoryResponse;
	
	public List<ZoneResponse> getZoneResponse() {
		return ZoneResponse;
	}
	public void setZoneResponse(List<ZoneResponse> zoneResponse) {
		ZoneResponse = zoneResponse;
	}
	public CategoryResponse getCategoryResponse() {
		return categoryResponse;
	}
	public void setCategoryResponse(CategoryResponse categoryResponse) {
		this.categoryResponse = categoryResponse;
	}
	
}
