package com.d2z.d2zservice.model;

import java.util.List;

public class CreateJobRequest {

	private String type;
	private List<DropDownModel> mlid;
	private String consignee;
	private String mawb;
	private String dest;
	private String weight;
	private String hawb;
	private String price;
	private String flight;
	private String eta;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<DropDownModel> getMlid() {
		return mlid;
	}
	public void setMlid(List<DropDownModel> mlid) {
		this.mlid = mlid;
	}
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
	public String getDest() {
		return dest;
	}
	public void setDest(String dest) {
		this.dest = dest;
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
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getFlight() {
		return flight;
	}
	public void setFlight(String flight) {
		this.flight = flight;
	}
	public String getEta() {
		return eta;
	}
	public void setEta(String eta) {
		this.eta = eta;
	}
	
	public String toString()
	{
		String output = getType()+"::"+getConsignee()+"::"+getMawb()+"::"+getDest()+"::"+getFlight()+"::"+getEta()+"::"+getWeight()+"::"+getPrice()+"::"+getHawb();
		return output ;
		
	}
	
}
