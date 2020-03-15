package com.d2z.d2zservice.model;

public class PerformanceReportTrackingData {

	private String articleID;
	private String serviceType;
	public String getArticleID() {
		return articleID;
	}
	public void setArticleID(String articleID) {
		this.articleID = articleID;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	
	 public PerformanceReportTrackingData(Object[] row) {
		 articleID = (String)row[0];
		 serviceType = (String)row[1];
	    }
}
