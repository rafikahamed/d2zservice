package com.d2z.d2zservice.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

/**
 * The persistent class for the UserRole database table.
 * 
 */
@Entity
@NamedQuery(name="ServiceTypeList.findAll", query="SELECT s FROM ServiceTypeList s")
public class ServiceTypeList implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Column(name="InjectionType")
	private String injectionType;
	
	@Id
	@Column(name="ServiceType")
	private String serviceType;

	public String getInjectionType() {
		return injectionType;
	}

	public void setInjectionType(String injectionType) {
		this.injectionType = injectionType;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	
}
