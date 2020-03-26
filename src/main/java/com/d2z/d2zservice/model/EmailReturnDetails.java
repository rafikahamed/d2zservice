package com.d2z.d2zservice.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class EmailReturnDetails {
	
	private String articleId;
	private String referenceNumber;
	private String consigneeName;
	private String clientName;
	private String returnReason;
	private String airwaybill;
	private String returnsCreatedDate;
	private String userId;
	private String client_Broker_Id;
	private String emailAddress;
	
	public EmailReturnDetails(Object[] obj) {
		articleId = (String)obj[0];
		referenceNumber = (String)obj[1];
		consigneeName = (String)obj[2];
		clientName = (String)obj[3];
		returnReason = (String)obj[4];
		airwaybill = (String)obj[5];
		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		returnsCreatedDate = df2.format(obj[6]);
		userId = Integer.toString((int) obj[7]);
		client_Broker_Id = Integer.toString((int) obj[8]);
		emailAddress = (String)obj[9];
	}

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getReturnReason() {
		return returnReason;
	}

	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}

	public String getAirwaybill() {
		return airwaybill;
	}

	public void setAirwaybill(String airwaybill) {
		this.airwaybill = airwaybill;
	}

	public String getReturnsCreatedDate() {
		return returnsCreatedDate;
	}

	public void setReturnsCreatedDate(String returnsCreatedDate) {
		this.returnsCreatedDate = returnsCreatedDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getClient_Broker_Id() {
		return client_Broker_Id;
	}

	public void setClient_Broker_Id(String client_Broker_Id) {
		this.client_Broker_Id = client_Broker_Id;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
}
