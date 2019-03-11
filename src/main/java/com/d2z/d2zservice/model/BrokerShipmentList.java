package com.d2z.d2zservice.model;

import java.util.Set;

public class BrokerShipmentList {

	private DropDownModel brokerUserName;
	private int userId;
	private Set<DropDownModel> shipmentNumber;
	
	public DropDownModel getBrokerUserName() {
		return brokerUserName;
	}
	public void setBrokerUserName(DropDownModel brokerUserName) {
		this.brokerUserName = brokerUserName;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Set<DropDownModel> getShipmentNumber() {
		return shipmentNumber;
	}
	public void setShipmentNumber(Set<DropDownModel> shipmentNumber) {
		this.shipmentNumber = shipmentNumber;
	}
	
	
}
