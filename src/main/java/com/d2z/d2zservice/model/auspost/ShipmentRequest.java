package com.d2z.d2zservice.model.auspost;

import java.util.List;


public class ShipmentRequest {

	private String sender_references;
	private boolean email_tracking_enabled;
	private FromAddress from;
	private ToAddress to;
	private List<Items> items;
	public String getSender_references() {
		return sender_references;
	}
	public void setSender_references(String sender_references) {
		this.sender_references = sender_references;
	}
	public boolean isEmail_tracking_enabled() {
		return email_tracking_enabled;
	}
	public void setEmail_tracking_enabled(boolean email_tracking_enabled) {
		this.email_tracking_enabled = email_tracking_enabled;
	}
	public FromAddress getFrom() {
		return from;
	}
	public void setFrom(FromAddress from) {
		this.from = from;
	}
	public ToAddress getTo() {
		return to;
	}
	public void setTo(ToAddress to) {
		this.to = to;
	}
	public List<Items> getItems() {
		return items;
	}
	public void setItems(List<Items> items) {
		this.items = items;
	}
	

	
	
	
}
