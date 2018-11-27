package com.d2z.d2zservice.proxy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.d2z.d2zservice.interceptor.ETowerHeaderRequestInterceptor;
import com.d2z.d2zservice.model.ETowerTrackingDetails;

@Service
public class ETowerProxy {
	


	public List<List<ETowerTrackingDetails>> makeCallForTrackingEvents(List<String> trackingNumber) {


		String url = "http://qa-cn.etowertech.com/services/integration/shipper/trackingEvents/";

		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setOutputStreaming(false);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.setInterceptors(Collections.singletonList(new ETowerHeaderRequestInterceptor()));
       /* List<String> trackingNo = new ArrayList<>();
       // trackingNo = trackingNumber;
        trackingNo.add("2MB653554501000931509");
        trackingNo.add("BRG406312001000935100");
*/		HttpEntity<List<String>> httpEntity = new HttpEntity<List<String>>(trackingNumber);

        System.out.println("Making call to etower");
        ResponseEntity<List<List<ETowerTrackingDetails>>> response = restTemplate.exchange(url,HttpMethod.POST,httpEntity,new ParameterizedTypeReference<List<List<ETowerTrackingDetails>>>() {});
        List<List<ETowerTrackingDetails>> responseList = response.getBody();
        return responseList;
	}
}
