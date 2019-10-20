package com.d2z.d2zservice.proxy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import com.d2z.d2zservice.model.PFLCreateShippingResponse;
import com.d2z.d2zservice.model.PFLSubmitOrderRequest;
import com.d2z.d2zservice.model.PFLSubmitOrderResponse;
import com.d2z.d2zservice.model.PFLTrackingResponse;
import com.d2z.d2zservice.model.PflCreateShippingRequest;
import com.d2z.d2zservice.model.PflTrackEventRequest;
import com.d2z.d2zservice.security.HMACGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@Service
public class PFLProxy {

	String baseURL = "http://103.225.160.46";
	HMACGenerator hmacGenerator = new HMACGenerator();

	public PFLCreateShippingResponse makeCallForCreateShippingOrder(PflCreateShippingRequest request,String serviceType) {
		RestTemplate template = new RestTemplate();
		String jsonResponse = null;
		String SECRET_KEY = null;
		String Token = null;
		PFLCreateShippingResponse response = null;
		String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
		String url = baseURL + "/app/services/multicourier/createorder";
		SimpleDateFormat currentDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);
		currentDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		String currentDate = currentDateFormat.format(new Date());
		System.out.println("US: " + currentDateFormat.format(new Date()));
		if("FW".equalsIgnoreCase(serviceType)) {
			SECRET_KEY = "U00T659VKM1YBHJGFE9SC326EHFKWE7B";
			Token = "FVJMJGYLC74QIAGRPJREJBAHOQZ3H0LM";
		}else {
			SECRET_KEY = "3VXSOS7WUSF4DS6V5LXS8ER14KR2TMP6";
			Token = "QT6P9I85LHETLYP43G7J440GD6W77TFX";
		}
		String authorizationHeader = hmacGenerator.calculatePFLHMAC(SECRET_KEY,"/app/services/multicourier/createorder");
		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("SHIPMENTAPI-DATE", currentDate);
		headers.add("SHIPMENTAPI-AUTH", Token + ":" + authorizationHeader);

		System.out.println("Request Headers:: " + headers.toString());
		ObjectWriter ow1 = new ObjectMapper().writer();
		String jsonReq = null;
		try {
			jsonReq = ow1.writeValueAsString(request);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Request Body:: " + jsonReq);

		HttpEntity<String> httpEntity = new HttpEntity<String>(jsonReq, headers);
		try {
			ResponseEntity<PFLCreateShippingResponse> responseEntity = template.exchange(url, HttpMethod.POST,
					httpEntity, PFLCreateShippingResponse.class);
			System.out.println(responseEntity.getStatusCode());
			response = responseEntity.getBody();
			ObjectWriter ow = new ObjectMapper().writer();
			try {
				jsonResponse = ow.writeValueAsString(response);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		} catch (HttpStatusCodeException e) {
			System.out.println("error code :" + e.getStatusCode());
			jsonResponse = e.getResponseBodyAsString();
			System.out.println(jsonResponse);
		}
		System.out.println("Response :: " + jsonResponse);
		return response;
	}

	public PFLSubmitOrderResponse createSubmitOrderPFL(PFLSubmitOrderRequest orderIds) {
		String url = baseURL + "/app/services/multicourier/submit";
		RestTemplate template = new RestTemplate();
		String jsonResponse = null;
		PFLSubmitOrderResponse response = null;
		String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
		SimpleDateFormat currentDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);
		currentDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		String currentDate = currentDateFormat.format(new Date());
		System.out.println("US: " + currentDateFormat.format(new Date()));
		String SECRET_KEY = "3VXSOS7WUSF4DS6V5LXS8ER14KR2TMP6";
		String Token = "QT6P9I85LHETLYP43G7J440GD6W77TFX";
		String authorizationHeader = hmacGenerator.calculatePFLHMAC(SECRET_KEY,
				"/app/services/multicourier/submit");
		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("SHIPMENTAPI-DATE", currentDate);
		headers.add("SHIPMENTAPI-AUTH", Token + ":" + authorizationHeader);

		System.out.println("Request Headers:: " + headers.toString());
		ObjectWriter ow1 = new ObjectMapper().writer();
		String jsonReq = null;
		try {
			jsonReq = ow1.writeValueAsString(orderIds);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Request Body:: " + jsonReq);

		HttpEntity<String> httpEntity = new HttpEntity<String>(jsonReq, headers);
		try {
			ResponseEntity<PFLSubmitOrderResponse> responseEntity = template.exchange(url, HttpMethod.POST,
					httpEntity, PFLSubmitOrderResponse.class);
			System.out.println(responseEntity.getStatusCode());
			response = responseEntity.getBody();
			ObjectWriter ow = new ObjectMapper().writer();
			try {
				jsonResponse = ow.writeValueAsString(response);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		} catch (HttpStatusCodeException e) {
			System.out.println("error code :" + e.getStatusCode());
			jsonResponse = e.getResponseBodyAsString();
			System.out.println(jsonResponse);
		}
		System.out.println("Response :: " + jsonResponse);
		return response;
	}

	
	public PFLSubmitOrderResponse DeleteOrderPFL(PFLSubmitOrderRequest orderIds,String serviceType) {
		String url = baseURL + "/app/services/multicourier/deleteorder";
		RestTemplate template = new RestTemplate();
		String jsonResponse = null;
		String SECRET_KEY = null;
		String Token = null;
		PFLSubmitOrderResponse response = null;
		String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
		SimpleDateFormat currentDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);
		currentDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		String currentDate = currentDateFormat.format(new Date());
		System.out.println("US: " + currentDateFormat.format(new Date()));
		if("FW".equalsIgnoreCase(serviceType)) {
			SECRET_KEY = "U00T659VKM1YBHJGFE9SC326EHFKWE7B";
			Token = "FVJMJGYLC74QIAGRPJREJBAHOQZ3H0LM";
		}else {
			SECRET_KEY = "3VXSOS7WUSF4DS6V5LXS8ER14KR2TMP6";
			Token = "QT6P9I85LHETLYP43G7J440GD6W77TFX";
		}
		
		String authorizationHeader = hmacGenerator.calculatePFLHMAC(SECRET_KEY,
				"/app/services/multicourier/deleteorder");
		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("SHIPMENTAPI-DATE", currentDate);
		headers.add("SHIPMENTAPI-AUTH", Token + ":" + authorizationHeader);

		System.out.println("Request Headers:: " + headers.toString());
		ObjectWriter ow1 = new ObjectMapper().writer();
		String jsonReq = null;
		try {
			jsonReq = ow1.writeValueAsString(orderIds);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Request Body:: " + jsonReq);

		HttpEntity<String> httpEntity = new HttpEntity<String>(jsonReq, headers);
		try {
			ResponseEntity<PFLSubmitOrderResponse> responseEntity = template.exchange(url, HttpMethod.POST,
					httpEntity, PFLSubmitOrderResponse.class);
			System.out.println(responseEntity.getStatusCode());
			response = responseEntity.getBody();
			ObjectWriter ow = new ObjectMapper().writer();
			try {
				jsonResponse = ow.writeValueAsString(response);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		} catch (HttpStatusCodeException e) {
			System.out.println("error code :" + e.getStatusCode());
			jsonResponse = e.getResponseBodyAsString();
			System.out.println(jsonResponse);
		}
		System.out.println("Response :: " + jsonResponse);
		return response;
	}

	public PFLTrackingResponse trackingEvent(PflTrackEventRequest pflTrackEvent) {
		String url = baseURL + "/app/services/multicourier/track";
		RestTemplate template = new RestTemplate();
		String jsonResponse = null;
		PFLTrackingResponse response = null;
		String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
		SimpleDateFormat currentDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);
		currentDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		String currentDate = currentDateFormat.format(new Date());
		System.out.println("US: " + currentDateFormat.format(new Date()));
		String SECRET_KEY = "3VXSOS7WUSF4DS6V5LXS8ER14KR2TMP6";
		String Token = "QT6P9I85LHETLYP43G7J440GD6W77TFX";
		String authorizationHeader = hmacGenerator.calculatePFLHMAC(SECRET_KEY,
				"/app/services/multicourier/track");
		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("SHIPMENTAPI-DATE", currentDate);
		headers.add("SHIPMENTAPI-AUTH", Token + ":" + authorizationHeader);
		
		System.out.println("Request Headers:: " + headers.toString());
		ObjectWriter ow1 = new ObjectMapper().writer();
		String jsonReq = null;
		try {
			jsonReq = ow1.writeValueAsString(pflTrackEvent);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		System.out.println("Request Body:: " + jsonReq);

		HttpEntity<String> httpEntity = new HttpEntity<String>(jsonReq, headers);
		try {
			ResponseEntity<PFLTrackingResponse> responseEntity = template.exchange(url, HttpMethod.POST,
					httpEntity, PFLTrackingResponse.class);
			System.out.println("PFL Response code--->"+responseEntity.getStatusCode());
			response = responseEntity.getBody();
			System.out.println(response);
		} catch (HttpStatusCodeException e) {
			System.out.println("error code :" + e.getStatusCode());
			jsonResponse = e.getResponseBodyAsString();
		}
		return response;
	}
}
