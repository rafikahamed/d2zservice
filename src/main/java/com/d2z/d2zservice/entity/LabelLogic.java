package com.d2z.d2zservice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="LabelLogic")
@NamedQuery(name="LabelLogic.findAll", query="SELECT t FROM LabelLogic t")
public class LabelLogic {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="Rowid")
	private int rowId;
			
	@Column(name="ServiceType")
	private String serviceType;
	
	@Column(name="Carrier")
	private String carrier;

	@Column(name="LabelName")
	private String labelName;
	
	@Column(name="tracking_identifier")
	private String trackingIdentifier;

	@Column(name="allocation_identifier")
	private String allocationIdentifier;

	public int getRowId() {
		return rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public String getTrackingIdentifier() {
		return trackingIdentifier;
	}

	public void setTrackingIdentifier(String trackingIdentifier) {
		this.trackingIdentifier = trackingIdentifier;
	}

	public String getAllocationIdentifier() {
		return allocationIdentifier;
	}

	public void setAllocationIdentifier(String allocationIdentifier) {
		this.allocationIdentifier = allocationIdentifier;
	}

	
}
