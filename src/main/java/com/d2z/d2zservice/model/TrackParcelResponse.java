package com.d2z.d2zservice.model;

import java.util.List;

public class TrackParcelResponse {

	private String articleId;
	private List<TrackingEvents> trackingEvents;
	public String getArticleId() {
		return articleId;
	}
	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
	public List<TrackingEvents> getTrackingEvents() {
		return trackingEvents;
	}
	public void setTrackingEvents(List<TrackingEvents> trackingEvents) {
		this.trackingEvents = trackingEvents;
	}

}
