package com.d2z.d2zservice.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


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
	
	
	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	@Column(name="StateName")
	private String stateName;


	@Column(name="Zone")
	private String zone;

	public PostcodeZone() {
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