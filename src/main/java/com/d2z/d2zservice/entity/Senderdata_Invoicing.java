package com.d2z.d2zservice.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;

@Entity
@Table(name="Senderdata_Invoicing")
@NamedQuery(name="Senderdata_Invoicing.findAll", query="SELECT s FROM Senderdata_Invoicing s")
@NamedStoredProcedureQueries({
		   @NamedStoredProcedureQuery(name = "InvoiceUpdate", 
			  procedureName = "InvoiceUpdate",
			  parameters = {
			      @StoredProcedureParameter(mode = ParameterMode.IN, name = "Indicator", type = String.class),
			      @StoredProcedureParameter(mode = ParameterMode.IN, name = "Airwaybill", type = String.class)
			  })
})

public class Senderdata_Invoicing implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="User_Id")
	private int user_Id;
	
	@Id
	@Column(name="Reference_number")
	private String reference_number;
	
	@Column(name="ArticleId")
	private String articleId;
	
	@Column(name="Consignee_Suburb")
	private String consignee_Suburb;
	
	@Column(name="Consignee_Postcode")
	private String consignee_Postcode;
	
	@Column(name="Weight")
	private double weight;
	
	@Column(name="Servicetype")
	private String servicetype;
	
	@Column(name="InjectionState")
	private String injectionState;
	
	@Column(name="InjectionType")
	private String injectionType;
	
	@Column(name="BrokerRate")
	private BigDecimal brokerRate;
	
	@Column(name="D2ZRate")
	private BigDecimal d2zRate;
	
	@Column(name="FuelSurcharge")
	private BigDecimal fuelSurcharge;
	
	@Column(name="Invoiced")
	private String invoiced;
	
	@Column(name="Billed")
	private String billed;
	
	@Column(name="AirwayBill")
	private String airwayBill;

	@Column(name="BarcodelabelNumber")
	private String barcodelabelNumber;
	
	@Column(name="Timestamp")
	private String timestamp;

	public int getUser_Id() {
		return user_Id;
	}

	public void setUser_Id(int user_Id) {
		this.user_Id = user_Id;
	}

	public String getReference_number() {
		return reference_number;
	}

	public void setReference_number(String reference_number) {
		this.reference_number = reference_number;
	}

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public String getConsignee_Suburb() {
		return consignee_Suburb;
	}

	public void setConsignee_Suburb(String consignee_Suburb) {
		this.consignee_Suburb = consignee_Suburb;
	}

	public String getConsignee_Postcode() {
		return consignee_Postcode;
	}

	public void setConsignee_Postcode(String consignee_Postcode) {
		this.consignee_Postcode = consignee_Postcode;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getServicetype() {
		return servicetype;
	}

	public void setServicetype(String servicetype) {
		this.servicetype = servicetype;
	}

	public String getInjectionState() {
		return injectionState;
	}

	public void setInjectionState(String injectionState) {
		this.injectionState = injectionState;
	}

	public String getInjectionType() {
		return injectionType;
	}

	public void setInjectionType(String injectionType) {
		this.injectionType = injectionType;
	}

	public BigDecimal getBrokerRate() {
		return brokerRate;
	}

	public void setBrokerRate(BigDecimal brokerRate) {
		this.brokerRate = brokerRate;
	}

	public BigDecimal getD2zRate() {
		return d2zRate;
	}

	public void setD2zRate(BigDecimal d2zRate) {
		this.d2zRate = d2zRate;
	}

	public BigDecimal getFuelSurcharge() {
		return fuelSurcharge;
	}

	public void setFuelSurcharge(BigDecimal fuelSurcharge) {
		this.fuelSurcharge = fuelSurcharge;
	}

	public String getInvoiced() {
		return invoiced;
	}

	public void setInvoiced(String invoiced) {
		this.invoiced = invoiced;
	}

	public String getBilled() {
		return billed;
	}

	public void setBilled(String billed) {
		this.billed = billed;
	}

	public String getAirwayBill() {
		return airwayBill;
	}

	public void setAirwayBill(String airwayBill) {
		this.airwayBill = airwayBill;
	}

	public String getBarcodelabelNumber() {
		return barcodelabelNumber;
	}

	public void setBarcodelabelNumber(String barcodelabelNumber) {
		this.barcodelabelNumber = barcodelabelNumber;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
}
