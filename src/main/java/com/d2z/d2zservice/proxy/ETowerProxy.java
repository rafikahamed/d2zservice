package com.d2z.d2zservice.proxy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.d2z.d2zservice.interceptor.ETowerHeaderRequestInterceptor;
import com.d2z.d2zservice.model.etower.CreateShippingRequest;
import com.d2z.d2zservice.model.etower.CreateShippingResponse;
import com.d2z.d2zservice.model.etower.GainLabelsResponse;
import com.d2z.d2zservice.model.etower.TrackingEventResponse;
import com.d2z.d2zservice.security.HMACGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@Service
public class ETowerProxy {
	
	
	String baseURL = "http://qa-cn.etowertech.com/";
	public TrackingEventResponse makeCallForTrackingEvents(List<String> trackingNumber) {


		String url = baseURL+"/services/shipper/trackingEvents/";
		//Prod URL
		//SSL cert issue fix
		//String url = "http://au.etowertech.com/services/integration/shipper/trackingEvents/";
			//"https://au.etowertech.com/services/integration/shipper/trackingEvents/";
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setOutputStreaming(false);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.setInterceptors(Collections.singletonList(new ETowerHeaderRequestInterceptor()));
	
		HttpEntity<List<String>> httpEntity = new HttpEntity<List<String>>(trackingNumber);

        System.out.println("Making call to etower");
        //ResponseEntity<List<List<ETowerTrackingDetails>>> response = restTemplate.exchange(url,HttpMethod.POST,httpEntity,new ParameterizedTypeReference<List<List<ETowerTrackingDetails>>>() {});
        //TrackingEventResponse response = restTemplate.postForObject(url, httpEntity, TrackingEventResponse.class); 
        ResponseEntity<TrackingEventResponse> response = restTemplate.exchange(url,HttpMethod.POST,httpEntity,TrackingEventResponse.class);
        TrackingEventResponse responseList = response.getBody();
        ObjectWriter ow = new ObjectMapper().writer();
        String jsonResponse = null;
		try {
			jsonResponse = ow.writeValueAsString(responseList);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("Response :: " + jsonResponse);
        return responseList;
	}
	
/*	public List<List<ETowerTrackingDetails>> stubETower() {
		ObjectMapper objectMapper = new ObjectMapper();
		String respJson =  "[[{\"trackingNo\":\"2RB653554501000931509\",\"eventTime\":1540980455000,\"eventCode\":\"SCN\",\"activity\":\"PREPARING TO DISPATCH\",\"location\":\"\",\"referenceTrackingNo\":null,\"destCountry\":\"AU\",\"timeZone\":\"+0800\",\"timestamp\":\"2018-10-31 18:07:35\"},{\"trackingNo\":\"2RB653554501000931509\",\"eventTime\":1540975440000,\"eventCode\":\"RCV\",\"activity\":\"ARRIVED AT DESTINATION AIRPORT\",\"location\":\"\",\"referenceTrackingNo\":null,\"destCountry\":\"AU\",\"timeZone\":\"+0800\",\"timestamp\":\"2018-10-31 16:44:00\"},{\"trackingNo\":\"2RB653554501000931509\",\"eventTime\":1540975351000,\"eventCode\":\"INF\",\"activity\":\"SHIPPING INFORMATION RECEIVED\",\"location\":\"\",\"referenceTrackingNo\":null,\"destCountry\":\"AU\",\"timeZone\":\"+0800\",\"timestamp\":\"2018-10-31 16:42:31\"},{\"trackingNo\":\"2RB653554501000931509\",\"eventTime\":1540995551000,\"eventCode\":\"DVR\",\"activity\":\"COLLECTED FROM AIRPORT TERMINAL\",\"location\":\"\",\"referenceTrackingNo\":null,\"destCountry\":\"AU\",\"timeZone\":\"+0800\",\"timestamp\":\"2018-10-31 20:42:31\"}]]";
		TypeReference<List<List<ETowerTrackingDetails>>> mapType = new TypeReference<List<List<ETowerTrackingDetails>>>() {};
		List<List<ETowerTrackingDetails>> jsonToList = null ;
		try {
			jsonToList = objectMapper.readValue(respJson, mapType);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonToList;
	}*/
	public CreateShippingResponse makeCallForCreateShippingOrder(List<CreateShippingRequest> request) {


		String url = baseURL+"services/shipper/orders/";
		//Prod URL
	//String url = "http://au.etowertech.com/services/shipper/orders/";
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setOutputStreaming(false);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.setInterceptors(Collections.singletonList(new ETowerHeaderRequestInterceptor()));
    
		HttpEntity<List<CreateShippingRequest>> httpEntity = new HttpEntity<List<CreateShippingRequest>>(request);

        System.out.println("Making call to etower");
        ResponseEntity<CreateShippingResponse> response = restTemplate.exchange(url,HttpMethod.POST,httpEntity,CreateShippingResponse.class);
        CreateShippingResponse createShippingResponse = response.getBody();
        ObjectWriter ow = new ObjectMapper().writer();
        String jsonResponse = null;
		try {
			jsonResponse = ow.writeValueAsString(createShippingResponse);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("Response :: " + jsonResponse);
        return createShippingResponse;
	}
	
	/*public GainLabelsResponse makeCallToGainLabels(List<String> referenceNumbers) {


		String url = "http://qa-cn.etowertech.com/services/shipper/labelSpecs/";
		//Prod URL
		//SSL cert issue fix
		//String url = "http://au.etowertech.com/services/shipper/labelSpecs/";
			//"https://au.etowertech.com/services/integration/shipper/trackingEvents/";
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setOutputStreaming(false);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.setInterceptors(Collections.singletonList(new ETowerHeaderRequestInterceptor()));
        HttpEntity<List<String>> httpEntity = new HttpEntity<List<String>>(referenceNumbers);

        System.out.println("Making call to etower");
        ResponseEntity<GainLabelsResponse> response = restTemplate.exchange(url,HttpMethod.POST,httpEntity,GainLabelsResponse.class);
        GainLabelsResponse gainLabelsresponse = response.getBody();
        return gainLabelsresponse;
	}*/
	public CreateShippingResponse makeCallForForeCast(List<String> trackingNumber) {


		String url = baseURL+"services/shipper/manifests/";
		//Prod URL
		//SSL cert issue fix
		//String url = "http://au.etowertech.com/services/shipper/manifests/";
			//"https://au.etowertech.com/services/integration/shipper/trackingEvents/";
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setOutputStreaming(false);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.setInterceptors(Collections.singletonList(new ETowerHeaderRequestInterceptor()));
     
		HttpEntity<List<String>> httpEntity = new HttpEntity<List<String>>(trackingNumber);

        System.out.println("Making call to etower");
        ResponseEntity<CreateShippingResponse> response = restTemplate.exchange(url,HttpMethod.POST,httpEntity,CreateShippingResponse.class);
        CreateShippingResponse responseList = response.getBody();
        ObjectWriter ow = new ObjectMapper().writer();
        String jsonResponse = null;
		try {
			jsonResponse = ow.writeValueAsString(responseList);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("Response :: " + jsonResponse);
        return responseList;
	}
}
