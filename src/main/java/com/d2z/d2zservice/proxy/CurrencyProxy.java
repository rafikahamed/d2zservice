package com.d2z.d2zservice.proxy;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.d2z.d2zservice.entity.Currency;
import com.d2z.d2zservice.model.CurrencyDetails;
import com.d2z.d2zservice.model.CurrencyPojo;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CurrencyProxy {
	
	public List<CurrencyDetails> currencyRate() {
System.out.println("in currencyrate");
		List<CurrencyDetails> currencyList = null;
		try {
		Map<String, String> countryMap = new HashMap<String, String>();
		countryMap.put("USD","United States dollar");
		countryMap.put("CNY","Chinese renminbi");
		countryMap.put("JPY","Japanese yen");
		countryMap.put("EUR","European euro");
		countryMap.put("KRW","South Korean won");
		countryMap.put("SGD","Singapore dollar");
		countryMap.put("NZD","New Zealand dollar");
		countryMap.put("GBP","UK pound sterling");
		countryMap.put("MYR","Malaysian ringgit");
		countryMap.put("THB","Thai baht");
		countryMap.put("IDR","Indonesian rupiah");
		countryMap.put("INR","Indian rupee");
		countryMap.put("TWD","New Taiwan dollar");
		countryMap.put("VND","Vietnamese dong");
		countryMap.put("HKD","Hong Kong dollar");
		countryMap.put("PGK","Papua New Guinea kina");
		countryMap.put("CHF","Swiss franc");
		countryMap.put("AED","United Arab Emirates dirham");
		countryMap.put("CAD","Canadian dollar");
		countryMap.put("AUD", "Australian Dollar");

		   URL obj = new URL("http://data.fixer.io/api/latest?access_key=7015a49ee4ba2344ba512f7026c5f7f2&base=EUR");
		   HttpURLConnection con = (HttpURLConnection)obj.openConnection();
		   con.setRequestMethod("GET");
		  
		   int responseCode = con.getResponseCode();
		  System.out.println("GET Response Code :: " + responseCode);
		if (responseCode == 200){
		     BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		    
		    StringBuffer response = new StringBuffer();
		    String inputLine;
		     while ((inputLine = in.readLine()) != null){
		       response.append(inputLine);
		     }
		     in.close();
		     ObjectMapper mapper = new ObjectMapper();
		     CurrencyPojo currPojo = (CurrencyPojo)mapper.readValue(response.toString(), CurrencyPojo.class);
		     Double fl = ((Double)currPojo.getRates().get("AUD"));
		     currencyList = new ArrayList<CurrencyDetails>();
		     CurrencyDetails currency = null;
		     List<String> listOfCurrency = listOfCurrency();
		     for (Map.Entry<String, Double> entry : currPojo.getRates().entrySet()){
		   	if(listOfCurrency.contains(entry.getKey())) {
		   	currency = new CurrencyDetails();
		       Double flot = ((Double)entry.getValue());
		       if(entry.getKey().equalsIgnoreCase("AUD")) {
		       	currency.setAudCurrencyRate(BigDecimal.valueOf(1));
		       }else {
		       currency.setAudCurrencyRate(BigDecimal.valueOf(flot/fl).setScale(2, RoundingMode.HALF_EVEN));
		       }
		       currency.setCountry(countryMap.get(entry.getKey()));
		       currency.setCurrencyCode(entry.getKey());
		       currencyList.add(currency);
		   	}
		     }
		   }
		   else{
		     System.out.println("GET request not worked");
		   }
		}catch(Exception ex) {

		}
		
		return currencyList;
		}

		
	private List<String> listOfCurrency() {
		List<String> profileList = new ArrayList<String>();
		profileList.add("USD");
		profileList.add("CNY");
		profileList.add("JPY");
		profileList.add("EUR");
		profileList.add("KRW");
		profileList.add("SGD");
		profileList.add("NZD");
		profileList.add("GBP");
		profileList.add("MYR");
		profileList.add("THB");
		profileList.add("IDR");
		profileList.add("INR");
		profileList.add("TWD");
		profileList.add("VND");
		profileList.add("HKD");
		profileList.add("PGK");
		profileList.add("CHF");
		profileList.add("AED");
		profileList.add("CAD");
		profileList.add("AUD");
		return profileList;
	}


}
