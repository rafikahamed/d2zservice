package com.d2z.d2zservice.proxy;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;

import org.apache.poi.util.SystemOutLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.d2z.d2zservice.model.auspost.CreateShippingRequest;
import com.d2z.d2zservice.model.auspost.CreateShippingResponse;
import com.d2z.d2zservice.model.auspost.TrackableItems;
import com.d2z.d2zservice.model.auspost.TrackingResponse;
import com.d2z.d2zservice.model.auspost.TrackingResults;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
@Service
public class AusPostProxy {
	
	public void createOrderIncludingShipments(CreateShippingRequest request) {
	    String url = "https://digitalapi.auspost.com.au/shipping/v1/orders";
		RestTemplate template = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("account-number", "0008433003");
		 String base64encodedString = null;
		try {
			base64encodedString = Base64.getEncoder().encodeToString("b38ca324-caf7-4d80-8c4a-e9862a4e5ba6:456hg4%tgC".getBytes("utf-8"));
			System.out.println("base64encodedString ::: "+ base64encodedString);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		headers.add("Authorization","Basic "+ base64encodedString);
		
		HttpEntity<CreateShippingRequest> requestEntity = new HttpEntity<>(request, headers);
		   ObjectWriter ow1 = new ObjectMapper().writer();
	        String jsonReq = null;
			try {
				jsonReq = ow1.writeValueAsString(request);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        System.out.println("Request :: " + jsonReq);
		ResponseEntity<CreateShippingResponse> responseEntity = template.exchange(url, HttpMethod.POST, requestEntity, CreateShippingResponse.class);
		System.out.println(responseEntity.getStatusCode());
		CreateShippingResponse response = responseEntity.getBody();
	        ObjectWriter ow = new ObjectMapper().writer();
	        String jsonResponse = null;
			try {
				jsonResponse = ow.writeValueAsString(response);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        System.out.println("Response :: " + jsonResponse);
	}
	
	public TrackingResponse trackingEvent(String articleIds) {
	    String url = "https://digitalapi.auspost.com.au/shipping/v1/track?tracking_ids="+articleIds;
		RestTemplate template = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("account-number", "0008433003");
		 String base64encodedString = null;
		try {
			base64encodedString = Base64.getEncoder().encodeToString("b38ca324-caf7-4d80-8c4a-e9862a4e5ba6:456hg4%tgC".getBytes("utf-8"));
			System.out.println("base64encodedString ::: "+ base64encodedString);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		headers.add("Authorization","Basic "+ base64encodedString);
		HttpEntity<String> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<TrackingResponse> responseEntity = template.exchange(url, HttpMethod.GET, requestEntity, TrackingResponse.class);
		System.out.println(responseEntity.getStatusCode());
		TrackingResponse response = responseEntity.getBody();
//	        ObjectWriter ow = new ObjectMapper().writer();
//	        String jsonResponse = null;
//			try {
//				jsonResponse = ow.writeValueAsString(response);
//			} catch (JsonProcessingException e) {
//				e.printStackTrace();
//			}
//	        System.out.println("Response :: " + jsonResponse);
	     return response;
	}
	
	
}
