package com.d2z.d2zservice.model;

import java.util.List;

public class ResponseMessage {

	private String responseMessage;
	private List<String> messageDetail;
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	public List<String> getMessageDetail() {
		return messageDetail;
	}
	public void setMessageDetail(List<String> messageDetail) {
		this.messageDetail = messageDetail;
	}
		
}
