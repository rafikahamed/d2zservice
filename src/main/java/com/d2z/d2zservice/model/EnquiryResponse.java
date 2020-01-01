package com.d2z.d2zservice.model;

import java.util.List;

public class EnquiryResponse {
	
	private List<String> ticketId;
	private String message;
	
	public List<String> getTicketId() {
		return ticketId;
	}
	public void setTicketId(List<String> ticketId) {
		this.ticketId = ticketId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
