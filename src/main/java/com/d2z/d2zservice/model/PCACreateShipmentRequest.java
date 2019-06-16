package com.d2z.d2zservice.model;

import java.util.List;

public class PCACreateShipmentRequest {
	
	private List<PCACreateShipmentRequestInfo> shipments;

	public List<PCACreateShipmentRequestInfo> getShipments() {
		return shipments;
	}

	public void setShipments(List<PCACreateShipmentRequestInfo> shipments) {
		this.shipments = shipments;
	}

}
