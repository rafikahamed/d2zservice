package com.d2z.d2zservice.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Bags {
	@JsonIgnore
	private double availableWeight;
	private String bagId;
	private List<Consignments> consignments;
	
	public Bags(double availableWeight) {
		this.availableWeight = availableWeight;
	}
	public String getBagId() {
		return bagId;
	}
	public void setBagId(String bagId) {
		this.bagId = bagId;
	}
	public List<Consignments> getConsignments() {
		if(null == consignments) {
			consignments = new ArrayList<Consignments>();
		}
		return consignments;
	}
	public void setConsignments(List<Consignments> consignments) {
		this.consignments = consignments;
	}
	public double getAvailableWeight() {
		return availableWeight;
	}
	public void setAvailableWeight(double availableWeight) {
		this.availableWeight = availableWeight;
	}

}
