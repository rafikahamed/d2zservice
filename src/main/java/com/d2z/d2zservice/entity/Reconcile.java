package com.d2z.d2zservice.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;

/**
 * The persistent class for the Reconcile database table.
 * 
 */
@Entity
@NamedQuery(name="Reconcile.findAll", query="SELECT r FROM Reconcile r")
public class Reconcile implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="Airwaybill")
	private String airwaybill;

	@Column(name="ArticleId")
	private String articleId;

	@Column(name="BrokerUserName")
	private String brokerUserName;

	@Column(name="ChargeDifference")
	private BigDecimal chargeDifference;

	@Column(name="CorrectAmount")
	private BigDecimal correctAmount;

	@Column(name="CostDifference")
	private BigDecimal costDifference;

	@Column(name="D2ZCost")
	private BigDecimal d2ZCost;

	@Column(name="D2ZWeight")
	private double d2ZWeight;

	@Column(name="InvoicedAmount")
	private BigDecimal invoicedAmount;

	@Column(name="Reference_number")
	private String reference_number;

	@Column(name="SupplierCharge")
	private BigDecimal supplierCharge;

	@Column(name="SupplierWeight")
	private double supplierWeight;

	@Column(name="WeightDifference")
	private double weightDifference;

	public Reconcile() {
	}

	public String getAirwaybill() {
		return this.airwaybill;
	}

	public void setAirwaybill(String airwaybill) {
		this.airwaybill = airwaybill;
	}

	public String getArticleId() {
		return this.articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public String getBrokerUserName() {
		return this.brokerUserName;
	}

	public void setBrokerUserName(String brokerUserName) {
		this.brokerUserName = brokerUserName;
	}

	public BigDecimal getChargeDifference() {
		return this.chargeDifference;
	}

	public void setChargeDifference(BigDecimal chargeDifference) {
		this.chargeDifference = chargeDifference;
	}

	public BigDecimal getCorrectAmount() {
		return this.correctAmount;
	}

	public void setCorrectAmount(BigDecimal correctAmount) {
		this.correctAmount = correctAmount;
	}

	public BigDecimal getCostDifference() {
		return this.costDifference;
	}

	public void setCostDifference(BigDecimal costDifference) {
		this.costDifference = costDifference;
	}

	public BigDecimal getD2ZCost() {
		return this.d2ZCost;
	}

	public void setD2ZCost(BigDecimal d2ZCost) {
		this.d2ZCost = d2ZCost;
	}

	public double getD2ZWeight() {
		return this.d2ZWeight;
	}

	public void setD2ZWeight(double d2ZWeight) {
		this.d2ZWeight = d2ZWeight;
	}

	public BigDecimal getInvoicedAmount() {
		return this.invoicedAmount;
	}

	public void setInvoicedAmount(BigDecimal invoicedAmount) {
		this.invoicedAmount = invoicedAmount;
	}

	public String getReference_number() {
		return this.reference_number;
	}

	public void setReference_number(String reference_number) {
		this.reference_number = reference_number;
	}

	public BigDecimal getSupplierCharge() {
		return this.supplierCharge;
	}

	public void setSupplierCharge(BigDecimal supplierCharge) {
		this.supplierCharge = supplierCharge;
	}

	public double getSupplierWeight() {
		return this.supplierWeight;
	}

	public void setSupplierWeight(double supplierWeight) {
		this.supplierWeight = supplierWeight;
	}

	public double getWeightDifference() {
		return this.weightDifference;
	}

	public void setWeightDifference(double weightDifference) {
		this.weightDifference = weightDifference;
	}

}