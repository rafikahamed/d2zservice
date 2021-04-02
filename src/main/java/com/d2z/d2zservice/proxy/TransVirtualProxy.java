package com.d2z.d2zservice.proxy;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.d2z.d2zservice.model.TransVirtual.TransVirtualRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@Service
public class TransVirtualProxy {
	
	
	public void makeCall(TransVirtualRequest request) {


		String url = "http://api.trans-virtual.com/Api/Consignment";
		
		System.out.println(url);
	
		String authorizationHeader = "177395|DLPDFTRZGQ";
		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", authorizationHeader);
		RestTemplate template = new RestTemplate();

		ObjectWriter ow1 = new ObjectMapper().writer();
		String jsonReq = null;
		try {
			jsonReq = ow1.writeValueAsString(request);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Request Body:: " + jsonReq);
		String response = null;
		HttpEntity<String> httpEntity = new HttpEntity<String>(jsonReq, headers);
		try {
			ResponseEntity<String> responseEntity = template.exchange(url, HttpMethod.POST,
					httpEntity, String.class);
			System.out.println(responseEntity.getStatusCode());
			response = responseEntity.getBody();
			
		} catch (HttpStatusCodeException e) {
			e.printStackTrace();
		}
		System.out.println("Response :: " + response);
	}

}
