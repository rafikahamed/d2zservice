package com.d2z.d2zservice.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="APIRates")
@NamedQuery(name="APIRates.findAll", query="SELECT a FROM APIRates a")
public class APIRates implements Serializable {

	private static final long serialVersionUID = 1L; 
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="RoWID")
    private int rowId; 
	
	@Column(name="user_id")
	private int userId;
	
	@Column(name="Postcode")
	private String postCode;
	
	@Column(name="Minweight")
	private Double minWeight;
	
	@Column(name="Maxweight")
	private Double maxWeight;
	
	@Column(name="Rate")
	private Double rate;
	
	@Column(name="ServiceType")
	private String serviceType;

	public int getRowId() {
		return rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}


	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getPostCode() {
		return postCode;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public Double getMinWeight() {
		return minWeight;
	}

	public void setMinWeight(Double minWeight) {
		this.minWeight = minWeight;
	}

	public Double getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(Double maxWeight) {
		this.maxWeight = maxWeight;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	
}
