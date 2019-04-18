package com.d2z.d2zservice.model.auspost;

public class OrderSummary {

	private double total_cost;
	private double total_cost_ex_gst;
	private double total_gst;
	private String status;
	private int number_of_shipments;
	private TrackingSummary tracking_summary;
	private int number_of_pickups;
	private int number_of_items;
	private boolean dangerous_goods_included;
	private double total_weight;
	
	
	public int getNumber_of_pickups() {
		return number_of_pickups;
	}
	public void setNumber_of_pickups(int number_of_pickups) {
		this.number_of_pickups = number_of_pickups;
	}
	public int getNumber_of_items() {
		return number_of_items;
	}
	public void setNumber_of_items(int number_of_items) {
		this.number_of_items = number_of_items;
	}
	public boolean isDangerous_goods_included() {
		return dangerous_goods_included;
	}
	public void setDangerous_goods_included(boolean dangerous_goods_included) {
		this.dangerous_goods_included = dangerous_goods_included;
	}
	public double getTotal_weight() {
		return total_weight;
	}
	public void setTotal_weight(double total_weight) {
		this.total_weight = total_weight;
	}
	public double getTotal_cost() {
		return total_cost;
	}
	public void setTotal_cost(double total_cost) {
		this.total_cost = total_cost;
	}
	public double getTotal_cost_ex_gst() {
		return total_cost_ex_gst;
	}
	public void setTotal_cost_ex_gst(double total_cost_ex_gst) {
		this.total_cost_ex_gst = total_cost_ex_gst;
	}
	public double getTotal_gst() {
		return total_gst;
	}
	public void setTotal_gst(double total_gst) {
		this.total_gst = total_gst;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getNumber_of_shipments() {
		return number_of_shipments;
	}
	public void setNumber_of_shipments(int number_of_shipments) {
		this.number_of_shipments = number_of_shipments;
	}
	public TrackingSummary getTracking_summary() {
		return tracking_summary;
	}
	public void setTracking_summary(TrackingSummary tracking_summary) {
		this.tracking_summary = tracking_summary;
	}
	
	
}
