package com.d2z.d2zservice.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the StarTrackPostcode database table.
 * 
 */
@Entity
@Table(name="StarTrackPostcode")
@NamedQuery(name="StarTrackPostcode.findAll", query="SELECT p FROM StarTrackPostcode p")
public class StarTrackPostcode implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="Rowid")
	private int rowId;
	
	@EmbeddedId
	private STPostCodeId stPostCodeId;
	
	@Column(name="StateName")
	private String stateName;

	public int getRowId() {
		return rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public STPostCodeId getStPostCodeId() {
		return stPostCodeId;
	}

	public void setStPostCodeId(STPostCodeId stPostCodeId) {
		this.stPostCodeId = stPostCodeId;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
