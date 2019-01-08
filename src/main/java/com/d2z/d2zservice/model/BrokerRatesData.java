package com.d2z.d2zservice.model;

import java.util.List;

public class BrokerRatesData {
private String brokerUserName;
private String injectionType;
private String serviceType;
private List<ZoneDetails> zone;
private String GST;
public String getBrokerUserName() {
	return brokerUserName;
}
public void setBrokerUserName(String brokerUserName) {
	this.brokerUserName = brokerUserName;
}
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

public List<ZoneDetails> getZone() {
	return zone;
}
public void setZone(List<ZoneDetails> zone) {
	this.zone = zone;
}
public String getGST() {
	return GST;
}
public void setGST(String gST) {
	GST = gST;
}

}
