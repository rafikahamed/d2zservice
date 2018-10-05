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
	
	@EmbeddedId
	private PostCodeId postcodeId;
	
	@Column(name="State")
	private String state;

	@Column(name="Zone")
	private String zone;

	public PostcodeZone() {
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public PostCodeId getPostcodeId() {
		return postcodeId;
	}

	public void setPostcodeId(PostCodeId postcodeId) {
		this.postcodeId = postcodeId;
	}

	public String getZone() {
		return this.zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}
	
}