package com.d2z.d2zservice.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the NonD2ZData database table.
 * 
 */
@Entity
@NamedQuery(name="NonD2ZData.findAll", query="SELECT n FROM NonD2ZData n")
@NamedStoredProcedureQueries({
	   @NamedStoredProcedureQuery(name = "RatesND", 
		  procedureName = "RatesND",
		  parameters = {
		      @StoredProcedureParameter(mode = ParameterMode.IN, name = "ArticleId", type = String.class)
		  })
	}
)

public class NonD2ZData implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="Address")
	private String address;

	@Column(name="ArticleId")
	private String articleId;

	@Column(name="Billed")
	private String billed;

	@Column(name="BrokerRate")
	private String brokerRate;

	@Column(name="ConsigneeName")
	private String consigneeName;

	@Column(name="D2Zrate")
	private String d2Zrate;

	@Column(name="FuelSurcharge")
	private String fuelSurcharge;

	@Column(name="Invoiced")
	private String invoiced;

	@Column(name="Postcode")
	private String postcode;

	@Column(name="Reference_number")
	private String reference_number;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="RowId")
	private int rowId;

	@Column(name="ServiceType")
	private String serviceType;

	@Column(name="Suburb")
	private String suburb;

	@Column(name="Weight")
	private double weight;

	public NonD2ZData() {
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getArticleId() {
		return this.articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public String getBilled() {
		return this.billed;
	}

	public void setBilled(String billed) {
		this.billed = billed;
	}

	public String getBrokerRate() {
		return this.brokerRate;
	}

	public void setBrokerRate(String brokerRate) {
		this.brokerRate = brokerRate;
	}

	public String getConsigneeName() {
		return this.consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

	public String getD2Zrate() {
		return this.d2Zrate;
	}

	public void setD2Zrate(String d2Zrate) {
		this.d2Zrate = d2Zrate;
	}

	public String getFuelSurcharge() {
		return this.fuelSurcharge;
	}

	public void setFuelSurcharge(String fuelSurcharge) {
		this.fuelSurcharge = fuelSurcharge;
	}

	public String getInvoiced() {
		return this.invoiced;
	}

	public void setInvoiced(String invoiced) {
		this.invoiced = invoiced;
	}

	public String getPostcode() {
		return this.postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getReference_number() {
		return this.reference_number;
	}

	public void setReference_number(String reference_number) {
		this.reference_number = reference_number;
	}

	public int getRowId() {
		return this.rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public String getServiceType() {
		return this.serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getSuburb() {
		return this.suburb;
	}

	public void setSuburb(String suburb) {
		this.suburb = suburb;
	}

	public double getWeight() {
		return this.weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}


}