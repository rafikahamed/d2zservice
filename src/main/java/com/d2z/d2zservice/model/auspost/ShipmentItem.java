package com.d2z.d2zservice.model.auspost;

public class ShipmentItem {

	private double weight;
	private boolean authority_to_leave;
	private boolean safe_drop_enabled;
	private boolean allow_partial_delivery;
	private String item_id;
	private TrackingDetails tracking_details;
	private String product_id;
	private ItemSummary item_summary;
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public boolean isAuthority_to_leave() {
		return authority_to_leave;
	}
	public void setAuthority_to_leave(boolean authority_to_leave) {
		this.authority_to_leave = authority_to_leave;
	}
	public boolean isSafe_drop_enabled() {
		return safe_drop_enabled;
	}
	public void setSafe_drop_enabled(boolean safe_drop_enabled) {
		this.safe_drop_enabled = safe_drop_enabled;
	}
	public boolean isAllow_partial_delivery() {
		return allow_partial_delivery;
	}
	public void setAllow_partial_delivery(boolean allow_partial_delivery) {
		this.allow_partial_delivery = allow_partial_delivery;
	}
	public String getItem_id() {
		return item_id;
	}
	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}
	public TrackingDetails getTracking_details() {
		return tracking_details;
	}
	public void setTracking_details(TrackingDetails tracking_details) {
		this.tracking_details = tracking_details;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	public ItemSummary getItem_summary() {
		return item_summary;
	}
	public void setItem_summary(ItemSummary item_summary) {
		this.item_summary = item_summary;
	}
	
}
