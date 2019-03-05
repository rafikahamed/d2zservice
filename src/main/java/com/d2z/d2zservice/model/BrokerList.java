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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((brokerUserName == null) ? 0 : brokerUserName.hashCode());
		result = prime * result + ((injectionType == null) ? 0 : injectionType.hashCode());
		result = prime * result + ((serviceType == null) ? 0 : serviceType.hashCode());
		result = prime * result + userId;
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
		BrokerList other = (BrokerList) obj;
		if (brokerUserName == null) {
			if (other.brokerUserName != null)
				return false;
		} else if (!brokerUserName.equals(other.brokerUserName))
			return false;
		if (injectionType == null) {
			if (other.injectionType != null)
				return false;
		} else if (!injectionType.equals(other.injectionType))
			return false;
		if (serviceType == null) {
			if (other.serviceType != null)
				return false;
		} else if (!serviceType.equals(other.serviceType))
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}
	
}
