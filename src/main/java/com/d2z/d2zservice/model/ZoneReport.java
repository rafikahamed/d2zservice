package com.d2z.d2zservice.model;

public class ZoneReport {
	
	private String zone;
	private String category;
	private int categoryVal;
	private int zoneSumVal;
	private float zonePerc;
	private int catSumVal;
	private float catPerc;
	
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getCategoryVal() {
		return categoryVal;
	}
	public void setCategoryVal(int categoryVal) {
		this.categoryVal = categoryVal;
	}
	public int getZoneSumVal() {
		return zoneSumVal;
	}
	public void setZoneSumVal(int zoneSumVal) {
		this.zoneSumVal = zoneSumVal;
	}
	public float getZonePerc() {
		return zonePerc;
	}
	public void setZonePerc(float zonePerc) {
		this.zonePerc = zonePerc;
	}
	public int getCatSumVal() {
		return catSumVal;
	}
	public void setCatSumVal(int catSumVal) {
		this.catSumVal = catSumVal;
	}
	public float getCatPerc() {
		return catPerc;
	}
	public void setCatPerc(float catPerc) {
		this.catPerc = catPerc;
	}

}
