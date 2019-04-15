package com.d2z.d2zservice.model.auspost;

public class Shipments {

	private String shipment_id;
	private boolean email_tracking_enabled;
	private ShipmentSummary shipment_summary;
	public String getShipment_id() {
		return shipment_id;
	}
	public void setShipment_id(String shipment_id) {
		this.shipment_id = shipment_id;
	}
	public boolean isEmail_tracking_enabled() {
		return email_tracking_enabled;
	}
	public void setEmail_tracking_enabled(boolean email_tracking_enabled) {
		this.email_tracking_enabled = email_tracking_enabled;
	}
	public ShipmentSummary getShipment_summary() {
		return shipment_summary;
	}
	public void setShipment_summary(ShipmentSummary shipment_summary) {
		this.shipment_summary = shipment_summary;
	}
	
}
