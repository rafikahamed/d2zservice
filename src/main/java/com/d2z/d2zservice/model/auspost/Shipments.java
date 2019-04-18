package com.d2z.d2zservice.model.auspost;

import java.util.List;

public class Shipments {

	private String shipment_id;
	private String shipment_creation_date;
	private List<ShipmentItem> items;
	private boolean email_tracking_enabled;
	private ShipmentSummary shipment_summary;
	private String movement_type;
	private String charge_to_account;
	
	
	
	public String getMovement_type() {
		return movement_type;
	}
	public void setMovement_type(String movement_type) {
		this.movement_type = movement_type;
	}
	public String getCharge_to_account() {
		return charge_to_account;
	}
	public void setCharge_to_account(String charge_to_account) {
		this.charge_to_account = charge_to_account;
	}
	public String getShipment_creation_date() {
		return shipment_creation_date;
	}
	public void setShipment_creation_date(String shipment_creation_date) {
		this.shipment_creation_date = shipment_creation_date;
	}
	public List<ShipmentItem> getItems() {
		return items;
	}
	public void setItems(List<ShipmentItem> items) {
		this.items = items;
	}
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
