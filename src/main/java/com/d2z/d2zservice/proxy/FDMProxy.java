package com.d2z.d2zservice.proxy;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Collections;

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

@Service
public class FDMProxy {


	public FDMManifestResponse makeCallToFDMManifestMapping(FDMManifestRequest request) 
	{
		String url = "https://my.fdm.com.au/TestWebAPI/api/ManifestMessaging";
		ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
		RestTemplate template = new RestTemplate(factory);
		template.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_XML);
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
		
		HttpEntity<FDMManifestRequest> requestEntity = new HttpEntity<>(request, headers);
		
		ResponseEntity<FDMManifestResponse> responseEntity = template.exchange(url, HttpMethod.POST, requestEntity, FDMManifestResponse.class);
		System.out.println(responseEntity.getStatusCode());
		FDMManifestResponse response = responseEntity.getBody();
	    
		return response;
	}
}

	


