package com.d2z.d2zservice.model.auspost;

public class ItemSummary {

	private double total_cost;
	private double total_cost_ex_gst;
	private double total_gst;
	private String status;
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
	
}
