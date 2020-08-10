package com.d2z.d2zservice.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="NZPostcodes")
@NamedQuery(name="NZPostcodes.findAll", query="SELECT p FROM NZPostcodes p")
public class NZPostcodes {

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private PostCodeId postcodeId;
	
	
	public NZPostcodes() {
	}

	

	public PostCodeId getPostcodeId() {
		return postcodeId;
	}

	public void setPostcodeId(PostCodeId postcodeId) {
		this.postcodeId = postcodeId;
	}

}
