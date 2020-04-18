package com.d2z.d2zservice.model;

public class PFLSubmitOrderData {

	private String orderId;
	private String serviceType;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	
	 public PFLSubmitOrderData(Object[] row) {
		 orderId = (String)row[0];
		 serviceType = (String)row[1];
	    }
}
