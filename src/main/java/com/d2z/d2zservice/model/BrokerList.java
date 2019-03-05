package com.d2z.d2zservice.model;

import java.util.Set;

public class BrokerList {
	
	private DropDownModel brokerUserName;
	private int userId;
	private Set<DropDownModel> serviceType;
	private Set<DropDownModel> injectionType;
	
	public DropDownModel getBrokerUserName() {
		return brokerUserName;
	}
	public void setBrokerUserName(DropDownModel brokerUserName) {
		this.brokerUserName = brokerUserName;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Set<DropDownModel> getServiceType() {
		return serviceType;
	}
	public void setServiceType(Set<DropDownModel> serviceType) {
		this.serviceType = serviceType;
	}
	public Set<DropDownModel> getInjectionType() {
		return injectionType;
	}
	public void setInjectionType(Set<DropDownModel> injectionType) {
		this.injectionType = injectionType;
	}

}
