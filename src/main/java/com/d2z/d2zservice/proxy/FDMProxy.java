package com.d2z.d2zservice.proxy;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Collections;

import org.json.JSONObject;
import org.json.XML;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.d2z.d2zservice.interceptor.RequestResponseLoggingInterceptor;
import com.d2z.d2zservice.model.fdm.FDMManifestRequest;
import com.d2z.d2zservice.model.fdm.FDMManifestResponse;
import com.google.gson.Gson;

@Service
public class FDMProxy {


	public String makeCallToFDMManifestMapping(FDMManifestRequest request) 
	{
		Gson gson = new Gson();
		String jsonStr = gson.toJson(request); 
		JSONObject json = new JSONObject(jsonStr);
		String requestXml = XML.toString(json);
		System.out.println("FDM Request XML --> "+requestXml); 
	//	String url = "https://my.fdm.com.au/TestWebAPI/api/ManifestMessaging"; //test url
		String url = "https://my.fdm.com.au/MyFdmWebAPI/api/ManifestMessaging";//prod url
		ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
		RestTemplate template = new RestTemplate(factory);
		template.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_XML);
		 String base64encodedString = null;
		try {
			String userName = "D2Z";
			String password = "khdgh780EFW8ge34Dv";
			String auth = userName+":"+password;
			base64encodedString = Base64.getEncoder().encodeToString(auth.getBytes("utf-8"));
			System.out.println("base64encodedString ::: "+ base64encodedString);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		headers.add("Authorization","Basic "+ base64encodedString);
		
		HttpEntity<String> requestEntity = new HttpEntity<>(requestXml, headers);
		
		ResponseEntity<FDMManifestResponse> responseEntity = template.exchange(url, HttpMethod.POST, requestEntity, FDMManifestResponse.class);
		System.out.println("FDM Status Code --->"+responseEntity.getStatusCode());
		FDMManifestResponse response = responseEntity.getBody();
	    //re
		return ""+responseEntity.getStatusCodeValue();
	}
}

	


