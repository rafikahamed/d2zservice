package com.d2z.d2zservice.model;

import java.math.BigDecimal;
public class CurrencyDetails {

	private String country;
	private String currencyCode;
	private BigDecimal currencyRate;
	private BigDecimal audCurrencyRate;
	
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public BigDecimal getCurrencyRate() {
		return currencyRate;
	}
	public void setCurrencyRate(BigDecimal currencyRate) {
		this.currencyRate = currencyRate;
	}
	public BigDecimal getAudCurrencyRate() {
		return audCurrencyRate;
	}
	public void setAudCurrencyRate(BigDecimal audCurrencyRate) {
		this.audCurrencyRate = audCurrencyRate;
	}
	
	
}
