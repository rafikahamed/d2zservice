package com.d2z.d2zservice.model;

import java.math.BigDecimal;

public class ShipmentCharges {
	
	private String consignee;
	private String mawb;
	private String broker;
	private String pod;
	private String pcs;
	private String weight;
	private String hawb;
	private Double process;
	private Double pickUp;
	private Double docs;
	private Double airport;
	private Double total;
	
	public String getConsignee() {
		return consignee;
	}
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}
	public String getMawb() {
		return mawb;
	}
	public void setMawb(String mawb) {
		this.mawb = mawb;
	}
	public String getBroker() {
		return broker;
	}
	public void setBroker(String broker) {
		this.broker = broker;
	}
	public String getPod() {
		return pod;
	}
	public void setPod(String pod) {
		this.pod = pod;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getHawb() {
		return hawb;
	}
	public void setHawb(String hawb) {
		this.hawb = hawb;
	}
	public Double getProcess() {
		return process;
	}
	public void setProcess(Double process) {
		this.process = process;
	}
	public String getPcs() {
		return pcs;
	}
	public void setPcs(String pcs) {
		this.pcs = pcs;
	}
	public Double getPickUp() {
		return pickUp;
	}
	public void setPickUp(Double pickUp) {
		this.pickUp = pickUp;
	}
	public Double getDocs() {
		return docs;
	}
	public void setDocs(Double docs) {
		this.docs = docs;
	}
	public Double getAirport() {
		return airport;
	}
	public void setAirport(Double airport) {
		this.airport = airport;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	
}
