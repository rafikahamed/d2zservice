package com.d2z.d2zservice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="PostcodeLogic")
@NamedQuery(name="PostcodeLogic.findAll", query="SELECT t FROM PostcodeLogic t")
public class PostcodeLogic {

	@Id
	@Column(name="Service_type")
	private String serviceType;
	
	@Column(name="Postcode_logic")
	private String postcodeLogic;

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getPostcodeLogic() {
		return postcodeLogic;
	}

	public void setPostcodeLogic(String postcodeLogic) {
		this.postcodeLogic = postcodeLogic;
	}
	
	
}
