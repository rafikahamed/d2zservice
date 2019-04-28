package com.d2z.d2zservice.model.auspost;

import java.util.List;

public class TrackingResults {

	private String tracking_id;
	private String status;
	private List<TrackableItems> trackable_items;
	public String getTracking_id() {
		return tracking_id;
	}
	public void setTracking_id(String tracking_id) {
		this.tracking_id = tracking_id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<TrackableItems> getTrackable_items() {
		return trackable_items;
	}
	public void setTrackable_items(List<TrackableItems> trackable_items) {
		this.trackable_items = trackable_items;
	}
	
}
