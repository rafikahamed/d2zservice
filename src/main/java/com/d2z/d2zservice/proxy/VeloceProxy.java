package com.d2z.d2zservice.proxy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.d2z.d2zservice.model.veloce.ConsignmentResponse;
import com.d2z.d2zservice.model.veloce.ConsignmentRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@Service
public class VeloceProxy {

	@Value("${veloce.url}")
	private String url;
	
	public ConsignmentResponse makeCalltoVeloce(ConsignmentRequest request) {
		RestTemplate template = new RestTemplate();
		ConsignmentResponse response = new ConsignmentResponse();
		HttpEntity<ConsignmentRequest> requestEntity = new HttpEntity<ConsignmentRequest>(request);
		   ObjectWriter ow1 = new ObjectMapper().writer();
	        String jsonReq = null;
			try {
				jsonReq = ow1.writeValueAsString(request);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        System.out.println("Request :: " + jsonReq);
	        String jsonResponse="";
	        try {
		ResponseEntity<ConsignmentResponse> responseEntity = template.exchange(url, HttpMethod.POST, requestEntity, ConsignmentResponse.class);
		System.out.println(responseEntity.getStatusCode());
		 response = responseEntity.getBody();
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
	        return response;
	}

}
