package com.d2z.d2zservice.entity;

import java.io.Serializable;
import java.lang.reflect.Method;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="MasterPostcode")
@NamedQuery(name="MasterPostcode.findAll", query="SELECT p FROM MasterPostcode p")
public class MasterPostcode implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private PostCodeId postcodeId;
	
	@Column(name="StateName")
	private String stateName;

	@Column(name="Zone")
	private String zone;
	
	@Column(name="PFLZone")
	private String pflZone;

	@Column(name="FastwayZone")
	private String fastwayZone;
	
	@Column(name="TollZone")
	private String tollZone;

	@Column(name="FDMRoute")
	private String fdmRoute;

	@Column(name="RCZone")
	private String rcZone;
	
	@Column(name="MCSZone")
	private String mcsZone;
	
	@Column(name="VCZone")
	private String vcZone;
	
	@Column(name="PostZone")
	private String postZone;
	
	@Column(name="MC2Zone")
	private String mc2Zone;
	
	public String getMc2Zone() {
		return mc2Zone;
	}

	public void setMc2Zone(String mc2Zone) {
		this.mc2Zone = mc2Zone;
	}

	public String getValueFromObject(Object object, String fieldName) {
        // get class
        Class clazz = object.getClass() ;
        if (clazz == null) {
          return null;
        }
     Object valueObject =null;
        // get object value using reflection
        String getterName = "get" + fieldName;
        try {
          Method method = clazz.getDeclaredMethod(getterName);
          valueObject = method.invoke(object,(Object[]) null);
        } catch (Exception e) {
          e.printStackTrace();
        }
        return valueObject != null ? valueObject.toString() : "0";
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

	public String getTollZone() {
		return tollZone;
	}

	public void setTollZone(String tollZone) {
		this.tollZone = tollZone;
	}

	public String getFdmRoute() {
		return fdmRoute;
	}

	public void setFdmRoute(String fdmRoute) {
		this.fdmRoute = fdmRoute;
	}

	public String getRcZone() {
		return rcZone;
	}

	public void setRcZone(String rcZone) {
		this.rcZone = rcZone;
	}

	public String getMcsZone() {
		return mcsZone;
	}

	public void setMcsZone(String mcsZone) {
		this.mcsZone = mcsZone;
	}

	public String getVcZone() {
		return vcZone;
	}

	public void setVcZone(String vcZone) {
		this.vcZone = vcZone;
	}

	public String getPostZone() {
		return postZone;
	}

	public void setPostZone(String postZone) {
		this.postZone = postZone;
	}
	
}
