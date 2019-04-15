package com.d2z.d2zservice.model.auspost;

import java.util.List;

public class CreateShippingRequest {

	private String order_reference;
	private String payment_method;
	private List<ShipmentRequest> shipments;
	public String getOrder_reference() {
		return order_reference;
	}
	public void setOrder_reference(String order_reference) {
		this.order_reference = order_reference;
	}
	public String getPayment_method() {
		return payment_method;
	}
	public void setPayment_method(String payment_method) {
		this.payment_method = payment_method;
	}
	public List<ShipmentRequest> getShipments() {
		return shipments;
	}
	public void setShipments(List<ShipmentRequest> shipments) {
		this.shipments = shipments;
	}
	
	
}
