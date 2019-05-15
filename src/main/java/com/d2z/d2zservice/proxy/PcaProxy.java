package com.d2z.d2zservice.proxy;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class PcaProxy {
	
	
	public  void trackingEvent(List<String> articleIds) {
	    String url = "https://s1.pcaex.com/api/tracking";
		RestTemplate template = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		String ArticleData = "{\"connote\":";
		
		if(articleIds.size()>1)
		{
			String ID = "[";
			for(String item : articleIds){
				ID= ID+"\""+item+"\""+",";
				//ID=ID+item+",";
			}
			//articleIds.forEach(item->ID=ID+item+",");
			ID = ID.substring(0, ID.length()-1);
			ID = ID+"]";
			ArticleData = ArticleData+ID+"}";
		}
		else
		{
			ArticleData = ArticleData+articleIds.get(0)+"}";
		}
		System.out.println(ArticleData);
		
		
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("api_id", "api_test");
		formData.add("data" , ArticleData);
		String sign = calculatesign(ArticleData);
		formData.add("sign", sign.toUpperCase());
		
		
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);
		 MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		    converter.setSupportedMediaTypes(Collections.singletonList(MediaType.TEXT_HTML));
		    template.getMessageConverters().add(converter);
		ResponseEntity<Object> response = template.exchange(url,HttpMethod.POST,requestEntity,Object.class);
                
		System.out.println(response.getStatusCode());
		System.out.println(response.getBody().toString());
		
		byte ptext[];
		try {
			ptext = response.getBody().toString().getBytes("ISO-8859-1");
			String value = new String(ptext, "UTF-8"); 
			System.out.println(value);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	
		
	}
	
	public  String  calculatesign(String data)
	{
		String ApiKey = "6366b2bb8b8d7407a3d46f6fa79a9af3472e1381442b49e637dfc96a0b0d6808";
		String Data = "api_idapi_testdata"+data;
		
		String Finaloutput = ApiKey+Data+ApiKey;
		 String hashtext="" ;
			  
	         // Static getInstance method is called with hashing MD5 
	         MessageDigest md;
			try {
				md = MessageDigest.getInstance("MD5");
			
	         // digest() method is called to calculate message digest 
	         //  of an input digest() return array of byte 
	         byte[] messageDigest = md.digest(Finaloutput.getBytes()); 

	         // Convert byte array into signum representation 
	         BigInteger no = new BigInteger(1, messageDigest); 

	         // Convert message digest into hex value 
	          hashtext = no.toString(16); 
	         while (hashtext.length() < 32) { 
	             hashtext = "0" + hashtext; 
	         } 
	         
	         System.out.println(hashtext);
	}
	         catch (NoSuchAlgorithmException e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 		} 
return hashtext;
	    
	}
}
			

