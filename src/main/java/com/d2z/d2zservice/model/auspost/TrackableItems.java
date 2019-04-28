package com.d2z.d2zservice.model.auspost;

import java.util.List;


public class TrackableItems {

	private String article_id;
	private String product_type;
	private List<TrackingEvents> events;
	private String status;
	public String getArticle_id() {
		return article_id;
	}
	public void setArticle_id(String article_id) {
		this.article_id = article_id;
	}
	public String getProduct_type() {
		return product_type;
	}
	public void setProduct_type(String product_type) {
		this.product_type = product_type;
	}
	public List<TrackingEvents> getEvents() {
		return events;
	}
	public void setEvents(List<TrackingEvents> events) {
		this.events = events;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
