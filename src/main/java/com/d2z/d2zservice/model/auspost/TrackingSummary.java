package com.d2z.d2zservice.model.auspost;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TrackingSummary {

	@JsonProperty(value="Sealed")
	private int sealed;

	public int getSealed() {
		return sealed;
	}

	public void setSealed(int sealed) {
		sealed = sealed;
	}
	
}
