package com.d2z.d2zservice.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the PostcodeZones database table.
 * 
 */
@Entity
@Table(name="PostcodeZones")
@NamedQuery(name="PostcodeZone.findAll", query="SELECT p FROM PostcodeZone p")
public class PostcodeZone implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="Postcode")
	private String postcode;

	@Column(name="State")
	private String state;
	
	@Column(name="Suburb")
	private String suburb;

	@Column(name="Zone")
	private String zone;

	public PostcodeZone() {
	}

	public String getPostcode() {
		return this.postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getSuburb() {
		return this.suburb;
	}

	public void setSuburb(String suburb) {
		this.suburb = suburb;
	}

	public String getZone() {
		return this.zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	@Override
	public String toString() {
		return "PostcodeZone [postcode=" + postcode + ", state=" + state + ", suburb=" + suburb + ", zone=" + zone
				+ "]";
	}
	
}