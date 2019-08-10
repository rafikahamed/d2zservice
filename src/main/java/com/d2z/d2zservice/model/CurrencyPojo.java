package com.d2z.d2zservice.model;

import java.util.Map;

public class CurrencyPojo{
	
  private boolean success;
  private int timestamp;
  private String base;
  private String date;
  private Map<String, Double> rates;
 
  public boolean isSuccess(){
    return this.success;
  }
 
  public void setSuccess(boolean success)
  {
    this.success = success;
  }
 
  public int getTimestamp()
  {
    return this.timestamp;
  }
 
  public void setTimestamp(int timestamp)
  {
    this.timestamp = timestamp;
  }
 
  public String getBase()
  {
    return this.base;
  }
 
  public void setBase(String base)
  {
    this.base = base;
  }
 
  public String getDate()
  {
    return this.date;
  }
 
  public void setDate(String date)
  {
    this.date = date;
  }

public Map<String, Double> getRates() {
	return rates;
}

public void setRates(Map<String, Double> rates) {
	this.rates = rates;
}

}

