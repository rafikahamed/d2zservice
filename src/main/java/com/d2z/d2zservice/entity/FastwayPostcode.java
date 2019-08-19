package com.d2z.d2zservice.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the PostcodeZones database table.
 * 
 */
@Entity
@Table(name="FastwayPostcode")
@NamedQuery(name="FastwayPostcode.findAll", query="SELECT p FROM FastwayPostcode p")
public class FastwayPostcode implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="Rowid")
	private int rowId;
	
	@EmbeddedId
	private FWPostCodeId fwPostCodeId;
	
	@Column(name="State")
	private String state;
	
	@Column(name="StateName")
	private String stateName;
	
	public int getRowId() {
		return rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	
	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public FWPostCodeId getFwPostCodeId() {
		return fwPostCodeId;
	}

	public void setFwPostCodeId(FWPostCodeId fwPostCodeId) {
		this.fwPostCodeId = fwPostCodeId;
	}
	
}
