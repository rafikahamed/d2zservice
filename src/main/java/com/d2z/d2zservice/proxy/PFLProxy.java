package com.d2z.d2zservice.proxy;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.d2z.d2zservice.interceptor.PflHeaderRequestInterceptor;
import com.d2z.d2zservice.model.PFLCreateShippingResponse;
import com.d2z.d2zservice.model.PflCreateShippingRequest;
import com.d2z.d2zservice.security.HMACGenerator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
@Service
public class PFLProxy {

	String baseURL = "http://103.225.160.46";
	   HMACGenerator hmacGenerator = new HMACGenerator();
	public String makeCallForCreateShippingOrder(List<PflCreateShippingRequest> request) {

		String url =  baseURL+"/app/services/multicourier/createorder/";
		RestTemplate template = new RestTemplate();
		String jsonResponse = null;
		
		String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
		SimpleDateFormat currentDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);
		currentDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		String currentDate = currentDateFormat.format(new Date());
		System.out.println("US: "+currentDateFormat.format(new Date()));
		String SECRET_KEY = "3VXSOS7WUSF4DS6V5LXS8ER14KR2TMP6";
		String Token ="QT6P9I85LHETLYP43G7J440GD6W77TFX";
		String authorizationHeader = hmacGenerator.calculatePFLHMAC(SECRET_KEY,"/app/services/multicourier/createorder");
		HttpHeaders headers = new HttpHeaders();
		
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("SHIPMENTAPI-DATE", currentDate);
		headers.set("SHIPMENTAPI-AUTH", Token+":"+authorizationHeader);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<List<PflCreateShippingRequest>> httpEntity = new HttpEntity<List<PflCreateShippingRequest>>(request,headers);

         System.out.println("Request Headers:: " + headers.toString());
		//HttpEntity<CreateShippingRequest> requestEntity = new HttpEntity<>(request, headers);
		   ObjectWriter ow1 = new ObjectMapper().writer();
	        String jsonReq = null;
			try {
				jsonReq = ow1.writeValueAsString(request);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        System.out.println("Request Body:: " + jsonReq);
	        try {
		ResponseEntity<PFLCreateShippingResponse> responseEntity = template.exchange(url, HttpMethod.POST, httpEntity, PFLCreateShippingResponse.class);
		System.out.println(responseEntity.getStatusCode());
		PFLCreateShippingResponse response = responseEntity.getBody();
	        ObjectWriter ow = new ObjectMapper().writer();
	        
			try {
				jsonResponse = ow.writeValueAsString(response);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        }
	        catch(HttpStatusCodeException e)
	        {
	        	System.out.println("error code :"+ e.getStatusCode());
	        	jsonResponse = e.getResponseBodyAsString();
	        }
	        System.out.println("Response :: " + jsonResponse);
	        return jsonResponse;
	}
	
}
