package com.d2z.d2zservice.model;

import java.util.Map;

public class ResponseMessage {

	private String responseMessage;
	private Map<String,String> messageDetail;
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	public Map<String, String> getMessageDetail() {
		return messageDetail;
	}
	public void setMessageDetail(Map<String, String> messageDetail) {
		this.messageDetail = messageDetail;
	}
	
	
	
	
}
