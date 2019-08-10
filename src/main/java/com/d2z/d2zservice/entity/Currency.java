package com.d2z.d2zservice.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
@Entity
@Table(name="currency")
@NamedQuery(name="Currency.findAll", query="SELECT c FROM Currency c")
public class Currency implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String country;

	@Column(name="AUD_currency_rate")
	private double audCurrencyRate;

	@Column(name="currency_code")
	private String currencyCode;

	@Column(name="currency_rate")
	private double currencyRate;

	@Column(name="last_updated")
	private Date lastUpdated;

	public Currency() {
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public double getAudCurrencyRate() {
		return this.audCurrencyRate;
	}

	public void setAudCurrencyRate(double audCurrencyRate) {
		this.audCurrencyRate = audCurrencyRate;
	}

	public String getCurrencyCode() {
		return this.currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public double getCurrencyRate() {
		return this.currencyRate;
	}

	public void setCurrencyRate(double currencyRate) {
		this.currencyRate = currencyRate;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
