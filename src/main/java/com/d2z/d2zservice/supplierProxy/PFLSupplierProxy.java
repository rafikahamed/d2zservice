package com.d2z.d2zservice.supplierProxy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.d2z.d2zservice.exception.FailureResponseException;
import com.d2z.d2zservice.model.PFLCreateShippingResponse;
import com.d2z.d2zservice.model.PflCreateShippingRequest;
import com.d2z.d2zservice.security.HMACGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
@Service
public class PFLSupplierProxy {

	@Value("${pfl.url}")
	private String baseURL;  
	HMACGenerator hmacGenerator = new HMACGenerator();

	public PFLCreateShippingResponse makeCallForCreateShippingOrder(PflCreateShippingRequest request,String serviceType,String key,String token) throws FailureResponseException {
		RestTemplate template = new RestTemplate();
		String jsonResponse = null;
		String SECRET_KEY = null;
		String Token = null;
		PFLCreateShippingResponse response = null;
		String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
		String uri = "/app/services/multicourier/createorder";
		
		SimpleDateFormat currentDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);
		currentDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		String currentDate = currentDateFormat.format(new Date());
		System.out.println("US: " + currentDateFormat.format(new Date()));
		
		if("1PS4".equalsIgnoreCase(serviceType)){
			uri = "/app/services/eparcels/createorder";
		}
		
		String url = baseURL + uri;
		String authorizationHeader = hmacGenerator.calculatePFLHMAC(key,uri,token);
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
			throw new FailureResponseException("Shipment Error. Please contact us");
		}
		System.out.println("Response :: " + jsonResponse);
		return response;
	}
}
