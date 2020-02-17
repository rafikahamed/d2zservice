package com.d2z.d2zservice.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PCATrackEventResponse {

	private String ref;
	private List<List<String>> tracks;
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	public List<List<String>> getTracks() {
		return tracks;
	}
	public void setTracks(List<List<String>> tracks) {
		this.tracks = tracks;
	}
	
	
}
