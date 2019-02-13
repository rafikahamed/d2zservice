package com.d2z.d2zservice.model;

import java.util.List;

public class BaggingResponse {

	private String responseMessage;
	private List<Bags> bags;
	
	

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public List<Bags> getBags() {
		return bags;
	}

	public void setBags(List<Bags> bags) {
		this.bags = bags;
	}
	
}
