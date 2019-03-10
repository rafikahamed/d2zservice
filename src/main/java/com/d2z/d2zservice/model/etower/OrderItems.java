package com.d2z.d2zservice.model.etower;

public class OrderItems {

	private String itemNo = "1";
	private String sku = "1";
	private String originCountry = "MY";
	private Double unitValue;
	private String itemCount = "1";
	public String getItemNo() {
		return itemNo;
	}
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getOriginCountry() {
		return originCountry;
	}
	public void setOriginCountry(String originCountry) {
		this.originCountry = originCountry;
	}
	
	public Double getUnitValue() {
		return unitValue;
	}
	public void setUnitValue(Double unitValue) {
		this.unitValue = unitValue;
	}
	public String getItemCount() {
		return itemCount;
	}
	public void setItemCount(String itemCount) {
		this.itemCount = itemCount;
	}
	
	
}
