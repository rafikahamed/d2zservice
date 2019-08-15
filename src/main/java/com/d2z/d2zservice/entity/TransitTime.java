package com.d2z.d2zservice.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The persistent class for the Users database table.
 * 
 */
@Entity
@Table(name="TransitTime")
public class TransitTime implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="Postcode")
	private String postcode;

	@Column(name="TransitTime")
	private String transitTime;

	@Column(name="Destination")
	private String destination;

	@Id
	@Column(name="Rowid")
	private int rowId;

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getTransitTime() {
		return transitTime;
	}

	public void setTransitTime(String transitTime) {
		this.transitTime = transitTime;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public int getRowId() {
		return rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}