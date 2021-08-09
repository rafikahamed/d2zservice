package com.d2z.d2zservice.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="InvoicingZones")
@NamedQuery(name="InvoicingZones.findAll", query="SELECT i FROM InvoicingZones i")
public class InvoicingZones implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private PostCodeId postcodeId;
	
	@Column(name="MCS65UL")
	private String mcs65ul;
	
	@Column(name="MCS3CNE")
	private String mcs3cne;
	
	@Column(name="MCS5AMSC")
	private String mcs5amsc;
	
	@Column(name="RCINVZone")
	private String rcInvZone;
	
	@Column(name="FDMZone")
	private String fdmZone;
	
	@Column(name="FastwayZoneId")
	private String fastwayZoneId;
	
	@Column(name="MCSGSX")
	private String mcsgsx;
	
	@Column(name="FDMVC1")
	private String fdmVc1;
	
	@Column(name="PostZone")
	private String postZone;
	
	@Column(name="MCSD2Z")
	private String mcsD2Z;
	
	@Column(name="VCD2Z")
	private String vcD2Z;
	
	@Column(name="MCS7EQ")
	private String mcs7EQ;
	
	@Column(name="FDMRoute")
	private String fdmRoute;
	
	@Column(name="VCSortCode")
	private String vcSortCode;

	public String getPostZone() {
		return postZone;
	}

	public void setPostZone(String postZone) {
		this.postZone = postZone;
	}

	public String getMcsD2Z() {
		return mcsD2Z;
	}

	public void setMcsD2Z(String mcsD2Z) {
		this.mcsD2Z = mcsD2Z;
	}

	public String getVcD2Z() {
		return vcD2Z;
	}

	public void setVcD2Z(String vcD2Z) {
		this.vcD2Z = vcD2Z;
	}

	public String getMcs7EQ() {
		return mcs7EQ;
	}

	public void setMcs7EQ(String mcs7eq) {
		mcs7EQ = mcs7eq;
	}

	public String getFdmRoute() {
		return fdmRoute;
	}

	public void setFdmRoute(String fdmRoute) {
		this.fdmRoute = fdmRoute;
	}

	public String getVcSortCode() {
		return vcSortCode;
	}

	public void setVcSortCode(String vcSortCode) {
		this.vcSortCode = vcSortCode;
	}

	public PostCodeId getPostcodeId() {
		return postcodeId;
	}

	public void setPostcodeId(PostCodeId postcodeId) {
		this.postcodeId = postcodeId;
	}

	public String getMcs65ul() {
		return mcs65ul;
	}

	public void setMcs65ul(String mcs65ul) {
		this.mcs65ul = mcs65ul;
	}

	public String getMcs3cne() {
		return mcs3cne;
	}

	public void setMcs3cne(String mcs3cne) {
		this.mcs3cne = mcs3cne;
	}

	public String getMcs5amsc() {
		return mcs5amsc;
	}

	public void setMcs5amsc(String mcs5amsc) {
		this.mcs5amsc = mcs5amsc;
	}

	public String getRcInvZone() {
		return rcInvZone;
	}

	public void setRcInvZone(String rcInvZone) {
		this.rcInvZone = rcInvZone;
	}

	public String getFdmZone() {
		return fdmZone;
	}

	public void setFdmZone(String fdmZone) {
		this.fdmZone = fdmZone;
	}

	public String getFastwayZoneId() {
		return fastwayZoneId;
	}

	public void setFastwayZoneId(String fastwayZoneId) {
		this.fastwayZoneId = fastwayZoneId;
	}

	public String getMcsgsx() {
		return mcsgsx;
	}

	public void setMcsgsx(String mcsgsx) {
		this.mcsgsx = mcsgsx;
	}

	public String getFdmVc1() {
		return fdmVc1;
	}

	public void setFdmVc1(String fdmVc1) {
		this.fdmVc1 = fdmVc1;
	}
	
	
	
	
	
}
