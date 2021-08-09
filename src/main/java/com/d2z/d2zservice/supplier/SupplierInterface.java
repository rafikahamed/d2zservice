package com.d2z.d2zservice.supplier;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.d2z.d2zservice.model.TrackParcelResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@Service
public class SupplierInterface {
	Logger logger = LoggerFactory.getLogger(SupplierInterface.class);
	
	@Autowired 
	RestTemplate template;

	public <T> ResponseEntity makeCall(HttpMethod httpMethod,String url,String request,HttpHeaders header,Class<T> responseType) {
		System.out.println("URL"+url);
		System.out.println("Request Body"+request);
		HttpEntity<String> httpEntity = new HttpEntity<String>(request, header);
		System.out.println(httpEntity.toString());
		ResponseEntity<T> response  = template.exchange(url, httpMethod,httpEntity, responseType);
		ObjectWriter ow = new ObjectMapper().writer();
		String jsonResponse = null;
		try {
			jsonResponse = ow.writeValueAsString(response.getBody());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		System.out.println("Http Status"+response.getStatusCode());
		System.out.println("Response"+jsonResponse);
		return response;
	}
	public <T> ResponseEntity makeCall(HttpMethod httpMethod,String url,List<String> request,HttpHeaders header,Class<T> responseType) {
		System.out.println("URL"+url);
		System.out.println("Request Header"+header.toString());
		System.out.println("Request Body"+request);
		HttpEntity<List<String>> httpEntity = new HttpEntity<List<String>>(request, header);
		System.out.println(httpEntity.toString());
		ResponseEntity<T> response  = template.exchange(url, httpMethod,httpEntity, responseType);
		ObjectWriter ow = new ObjectMapper().writer();
		String jsonResponse = null;
		try {
			jsonResponse = ow.writeValueAsString(response.getBody());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		System.out.println("Http Status"+response.getStatusCode());
		System.out.println("Response"+jsonResponse);
		return response;
	}
	public <T> ResponseEntity makeGetCall(String url, Class<T> responseType) {

		System.out.println("URL"+url);
		ResponseEntity<T> response  = template.getForEntity(url, responseType);
		ObjectWriter ow = new ObjectMapper().writer();
		String jsonResponse = null;
		try {
			jsonResponse = ow.writeValueAsString(response.getBody());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		System.out.println("Http Status"+response.getStatusCode());
		System.out.println("Response"+jsonResponse);
		return response;
	
	}
	public <T> ResponseEntity makeCall(HttpMethod httpMethod, String url, List<String> request,
			HttpHeaders header,
			ParameterizedTypeReference<T> parameterizedTypeReference) {

		System.out.println("URL"+url);
		System.out.println("Request Header"+header.toString());
		System.out.println("Request Body"+request);
		HttpEntity<List<String>> httpEntity = new HttpEntity<List<String>>(request, header);
		System.out.println(httpEntity.toString());
		ResponseEntity<T> response  = template.exchange(url, httpMethod,httpEntity, parameterizedTypeReference);
		ObjectWriter ow = new ObjectMapper().writer();
		String jsonResponse = null;
		try {
			jsonResponse = ow.writeValueAsString(response.getBody());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		System.out.println("Http Status"+response.getStatusCode());
		System.out.println("Response"+jsonResponse);
		return response;
	
	}

}
