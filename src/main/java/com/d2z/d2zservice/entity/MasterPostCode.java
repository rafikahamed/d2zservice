package com.d2z.d2zservice.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="MasterPostcodeZones")
@NamedQuery(name="MasterPostCode.findAll", query="SELECT p FROM MasterPostCode p")
public class MasterPostCode implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private PostCodeId postcodeId;
	
	@Column(name="StateName")
	private String stateName;

	@Column(name="Zone")
	private String zone;
	
	@Column(name="PFLzone")
	private String pflZone;

	@Column(name="Fastwayzone")
	private String fastwayZone;
	
	@Column(name="FDMzone")
	private String fdmZone;
	
	@Column(name="MClogic")
	private String mcLogic;
	
	@Column(name="TollZone")
	private String tollZone;

	@Column(name="FDMRoute")
	private String fdmRoute;
	
	
	public String getFdmRoute() {
		return fdmRoute;
	}

	public void setFdmRoute(String fdmRoute) {
		this.fdmRoute = fdmRoute;
	}

	public String getTollZone() {
		return tollZone;
	}

	public void setTollZone(String tollZone) {
		this.tollZone = tollZone;
	}

	public PostCodeId getPostcodeId() {
		return postcodeId;
	}

	public void setPostcodeId(PostCodeId postcodeId) {
		this.postcodeId = postcodeId;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getPflZone() {
		return pflZone;
	}

	public void setPflZone(String pflZone) {
		this.pflZone = pflZone;
	}

	public String getFastwayZone() {
		return fastwayZone;
	}

	public void setFastwayZone(String fastwayZone) {
		this.fastwayZone = fastwayZone;
	}

	public String getFdmZone() {
		return fdmZone;
	}

	public void setFdmZone(String fdmZone) {
		this.fdmZone = fdmZone;
	}

	public String getMcLogic() {
		return mcLogic;
	}

	public void setMcLogic(String mcLogic) {
		this.mcLogic = mcLogic;
	}
	
	
}
