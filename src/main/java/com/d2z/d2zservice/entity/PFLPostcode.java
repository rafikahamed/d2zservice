package com.d2z.d2zservice.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="PFLPostcode")
@NamedQuery(name="PFLPostcode.findAll", query="SELECT p FROM PFLPostcode p")
public class PFLPostcode {

	
	private static final long serialVersionUID = 1L;
	
	@Column(name="Rowid")
	private int rowId;
	
	@EmbeddedId
	private FWPostCodeId fwPostCodeId;
	
	@Column(name="City")
	private String city;

	@Column(name="ZoneNo")
	private String zoneNo;
	
	public int getRowId() {
		return rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}
	


	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public FWPostCodeId getFwPostCodeId() {
		return fwPostCodeId;
	}

	public void setFwPostCodeId(FWPostCodeId fwPostCodeId) {
		this.fwPostCodeId = fwPostCodeId;
	}

	public String getZoneNo() {
		return zoneNo;
	}

	public void setZoneNo(String zoneNo) {
		this.zoneNo = zoneNo;
	}
	
	
	

}
