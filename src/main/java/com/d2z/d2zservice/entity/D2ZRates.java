package com.d2z.d2zservice.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity
@Table(name="D2ZRates")
@NamedQuery(name="D2ZRates.findAll", query="SELECT d FROM D2ZRates d")
public class D2ZRates implements Serializable {

	private static final long serialVersionUID = 1L; 
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="RoWID")
    private int rowId;
	
	
	@Column(name="MLID")
	private String MLID;
	
	
	@Column(name="ZoneID")
	private String zoneID;
	
	
	@Column(name="Minweight")
	private Double minWeight;
	
	
	@Column(name="Maxweight")
	private Double maxWeight;

	@Column(name="Rate")
	private Double rate;
	
	@Column(name="FuelSurcharge")
	private String fuelSurcharge;

	@Column(name="GST")
	private String GST;
	
	@Column(name="Timestamp")
	private String timestamp;

	@Column(name="BackupInd")
	private String backupInd;
	
	 
	
	public String getMLID() {
		return MLID;
	}

	public void setMLID(String mLID) {
		MLID = mLID;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public int getRowId() {
		return rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public String getBackupInd() {
		return backupInd;
	}

	public void setBackupInd(String backupInd) {
		this.backupInd = backupInd;
	}

	public String getZoneID() {
		return zoneID;
	}

	public void setZoneID(String zoneID) {
		this.zoneID = zoneID;
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

	public String getFuelSurcharge() {
		return fuelSurcharge;
	}

	public void setFuelSurcharge(String fuelSurcharge) {
		this.fuelSurcharge = fuelSurcharge;
	}

	public String getGST() {
		return GST;
	}

	public void setGST(String gST) {
		GST = gST;
	}
	
	
}
