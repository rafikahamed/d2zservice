package com.d2z.d2zservice.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

public class TrackEventId implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Column(name="ArticleID")
	private String articleID;

	public String getArticleID() {
		return articleID;
	}

	public void setArticleID(String articleID) {
		this.articleID = articleID;
	}
	
	@Column(name="TrackEventDateOccured")
	private String trackEventDateOccured;

	@Column(name="TrackEventDetails")
	private String trackEventDetails;
	
	public TrackEventId() {
		
	}
	public TrackEventId(String articleID, String trackEventDetails, String trackEventDateOccured) {
		this.articleID = articleID;
		this.trackEventDetails = trackEventDetails;
		this.trackEventDateOccured = trackEventDateOccured;
	}
	
	

	public String getTrackEventDateOccured() {
		return trackEventDateOccured;
	}

	public String getTrackEventDetails() {
		return trackEventDetails;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((articleID == null) ? 0 : articleID.hashCode());
		result = prime * result + ((trackEventDetails == null) ? 0 : trackEventDetails.hashCode());
		result = prime * result + ((trackEventDateOccured == null) ? 0 : trackEventDateOccured.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TrackEventId other = (TrackEventId) obj;
		if (articleID == null) {
			if (other.articleID != null)
				return false;
		} else if (!articleID.equals(other.articleID))
			return false;
		if (trackEventDetails == null) {
			if (other.trackEventDetails != null)
				return false;
		} else if (!trackEventDetails.equals(other.trackEventDetails))
			return false;
		if (trackEventDateOccured == null) {
			if (other.trackEventDateOccured != null)
				return false;
		} else if (!trackEventDateOccured.equals(other.trackEventDateOccured))
			return false;
		return true;
	}



}
