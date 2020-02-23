package com.d2z.d2zservice.model;

import java.math.BigDecimal;

public class ProfitLossReport {
	
	private String articleId;
	private String broker;
	private BigDecimal brokerRate;
	private BigDecimal revenue;
	private int parcel;
	private BigDecimal profit;
	private BigDecimal profitPerParcel;
	private BigDecimal d2zRate;
	private BigDecimal shipmentCharge;
	
	public String getBroker() {
		return broker;
	}
	public void setBroker(String broker) {
		this.broker = broker;
	}
	public BigDecimal getBrokerRate() {
		return brokerRate;
	}
	public void setBrokerRate(BigDecimal brokerRate) {
		this.brokerRate = brokerRate;
	}
	public BigDecimal getRevenue() {
		return revenue;
	}
	public void setRevenue(BigDecimal revenue) {
		this.revenue = revenue;
	}
	public int getParcel() {
		return parcel;
	}
	public void setParcel(int parcel) {
		this.parcel = parcel;
	}
	public BigDecimal getProfit() {
		return profit;
	}
	public void setProfit(BigDecimal profit) {
		this.profit = profit;
	}
	public BigDecimal getProfitPerParcel() {
		return profitPerParcel;
	}
	public void setProfitPerParcel(BigDecimal profitPerParcel) {
		this.profitPerParcel = profitPerParcel;
	}
	public BigDecimal getD2zRate() {
		return d2zRate;
	}
	public void setD2zRate(BigDecimal d2zRate) {
		this.d2zRate = d2zRate;
	}
	public String getArticleId() {
		return articleId;
	}
	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
	public BigDecimal getShipmentCharge() {
		return shipmentCharge;
	}
	public void setShipmentCharge(BigDecimal shipmentCharge) {
		this.shipmentCharge = shipmentCharge;
	}
	
}
