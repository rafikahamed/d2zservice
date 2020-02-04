package com.d2z.d2zservice.model;

public class NotBilled {
	
	private String userName;
	private String airwayBill;
	private String articleId;
	private String referenceNumber;
	private Double d2zRate;
	private String dateAllocated;
	private String weight;
	
	
	
	

	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getDateAllocated() {
		return dateAllocated;
	}
	public void setDateAllocated(String dateAllocated) {
		this.dateAllocated = dateAllocated;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getAirwayBill() {
		return airwayBill;
	}
	public void setAirwayBill(String airwayBill) {
		this.airwayBill = airwayBill;
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
	public Double getD2zRate() {
		return d2zRate;
	}
	public void setD2zRate(Double d2zRate) {
		this.d2zRate = d2zRate;
	}
	
	

}
