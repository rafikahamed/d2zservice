package com.d2z.d2zservice.model;

public class ReconcileData {
	
	private String articleNo;
	private double articleActualWeight;
	private double normalRateParcel;
	private String supplierType;
	private String refrenceNumber;
	private double chargedWeight;
	private double cost;
	private String postCode;
	private String barcodeLabelNumber;
	
	public String getArticleNo() {
		return articleNo;
	}
	public void setArticleNo(String articleNo) {
		this.articleNo = articleNo;
	}
	public double getArticleActualWeight() {
		return articleActualWeight;
	}
	public void setArticleActualWeight(double articleActualWeight) {
		this.articleActualWeight = articleActualWeight;
	}
	public double getNormalRateParcel() {
		return normalRateParcel;
	}
	public void setNormalRateParcel(double normalRateParcel) {
		this.normalRateParcel = normalRateParcel;
	}
	public String getSupplierType() {
		return supplierType;
	}
	public void setSupplierType(String supplierType) {
		this.supplierType = supplierType;
	}
	
	public double getChargedWeight() {
		return chargedWeight;
	}
	public void setChargedWeight(double chargedWeight) {
		this.chargedWeight = chargedWeight;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public String getRefrenceNumber() {
		return refrenceNumber;
	}
	public void setRefrenceNumber(String refrenceNumber) {
		this.refrenceNumber = refrenceNumber;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public String getBarcodeLabelNumber() {
		return barcodeLabelNumber;
	}
	public void setBarcodeLabelNumber(String barcodeLabelNumber) {
		this.barcodeLabelNumber = barcodeLabelNumber;
	}
	
}
