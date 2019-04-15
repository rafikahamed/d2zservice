package com.d2z.d2zservice.model.auspost;

import java.util.List;

public class Order {

	private String order_id;
	private String order_reference;
	private String order_creation_date;
	private OrderSummary order_summary;
	private List<Shipments> shipments;
	private String payment_method;
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getOrder_reference() {
		return order_reference;
	}
	public void setOrder_reference(String order_reference) {
		this.order_reference = order_reference;
	}
	public String getOrder_creation_date() {
		return order_creation_date;
	}
	public void setOrder_creation_date(String order_creation_date) {
		this.order_creation_date = order_creation_date;
	}
	public OrderSummary getOrder_summary() {
		return order_summary;
	}
	public void setOrder_summary(OrderSummary order_summary) {
		this.order_summary = order_summary;
	}
	
	public List<Shipments> getShipments() {
		return shipments;
	}
	public void setShipments(List<Shipments> shipments) {
		this.shipments = shipments;
	}
	public String getPayment_method() {
		return payment_method;
	}
	public void setPayment_method(String payment_method) {
		this.payment_method = payment_method;
	}
	
	
}
