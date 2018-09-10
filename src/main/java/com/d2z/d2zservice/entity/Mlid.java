package com.d2z.d2zservice.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the MLID database table.
 * 
 */
@Entity
@Table(name="MLID")
@NamedQuery(name="Mlid.findAll", query="SELECT m FROM Mlid m")
public class Mlid implements Serializable {
	private static final long serialVersionUID = 1L;

	private String destinationzone;

	@Column(name="Maxweight")
	private String maxweight;

	@Column(name="Minweight")
	private String minweight;
	
	@Column(name="MLID")
	private String mlid;

	@Column(name="Service_type")
	private String service_type;

	@Id
	@Column(name="ZoneID")
	private String zoneID;

	public Mlid() {
	}

	public String getDestinationzone() {
		return this.destinationzone;
	}

	public void setDestinationzone(String destinationzone) {
		this.destinationzone = destinationzone;
	}

	public String getMaxweight() {
		return this.maxweight;
	}

	public void setMaxweight(String maxweight) {
		this.maxweight = maxweight;
	}

	public String getMinweight() {
		return this.minweight;
	}

	public void setMinweight(String minweight) {
		this.minweight = minweight;
	}

	public String getMlid() {
		return this.mlid;
	}

	public void setMlid(String mlid) {
		this.mlid = mlid;
	}

	public String getService_type() {
		return this.service_type;
	}

	public void setService_type(String service_type) {
		this.service_type = service_type;
	}

	public String getZoneID() {
		return this.zoneID;
	}

	public void setZoneID(String zoneID) {
		this.zoneID = zoneID;
	}

}