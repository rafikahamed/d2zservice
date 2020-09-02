package com.d2z.d2zservice.model.auspost;

import java.util.List;

public class Items {

	private String length;
	private String height;
	private String width;
	private String weight;
	private String item_description;
	private String product_id  ="3D85";
	private TrackingDetails tracking_details;
	
	

	public TrackingDetails getTracking_details() {
		return tracking_details;
	}
	public void setTracking_details(TrackingDetails tracking_details) {
		this.tracking_details = tracking_details;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getItem_description() {
		return item_description;
	}
	public void setItem_description(String item_description) {
		this.item_description = item_description;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	
}
