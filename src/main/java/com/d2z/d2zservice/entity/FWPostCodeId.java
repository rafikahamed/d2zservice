package com.d2z.d2zservice.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class FWPostCodeId implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="Postcode")
	private String postcode;
	
	@Column(name="Suburb")
	private String suburb;
	
	@Column(name="State")
	private String state;
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getSuburb() {
		return suburb;
	}

	public void setSuburb(String suburb) {
		this.suburb = suburb;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((postcode == null) ? 0 : postcode.hashCode());
		result = prime * result + ((suburb == null) ? 0 : suburb.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
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
		FWPostCodeId other = (FWPostCodeId) obj;
		if (postcode == null) {
			if (other.postcode != null)
				return false;
		} else if (!postcode.equals(other.postcode))
			return false;
		if (suburb == null) {
			if (other.suburb != null)
				return false;
		} else if (!suburb.equals(other.suburb))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}

}
