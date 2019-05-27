package com.d2z.d2zservice.interceptor;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import com.d2z.d2zservice.security.HMACGenerator;

public class ETowerHeaderRequestInterceptor implements ClientHttpRequestInterceptor {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private String SECRET_KEY;
    private String token;
    
    
	public ETowerHeaderRequestInterceptor(String sECRET_KEY, String token) {
		super();
		SECRET_KEY = sECRET_KEY;
		this.token = token;
	}

	HMACGenerator hmacGenerator=  new HMACGenerator();
    
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
		String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
		SimpleDateFormat currentDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);
		currentDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		String currentDate = currentDateFormat.format(new Date());
		System.out.println("US: "+currentDateFormat.format(new Date()));
		System.out.println("key "+SECRET_KEY);
		System.out.println("token "+token);
	//	String SECRET_KEY = "79db9e5OEeOpvgAVXUFWSD";
		//String SECRET_KEY = "zwmaAqqPaIHHQLecRmtSoA";
       //String authorizationHeader = "WallTech test5AdbzO5OEeOpvgAVXUFE0A:" + hmacGenerator.calculateHMAC(SECRET_KEY,request.getURI().toString(),request.getMethod().toString());
		 String authorizationHeader = "WallTech "+token+":" + hmacGenerator.calculateHMAC(SECRET_KEY,request.getURI().toString(),request.getMethod().toString());
    	System.out.println("Before calling");
    	 HttpHeaders headers = request.getHeaders();
         headers.add("X-WallTech-Date", currentDate);
         headers.add("Authorization", authorizationHeader);
    	System.out.println("Header: "+request.getHeaders());
    	System.out.println("Request Body: "+ new String(body, "UTF-8"));
        logRequest(request, body);
        ClientHttpResponse response =  execution.execute(request, body);
       // System.out.println("Response Body: "+StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()));
        logResponse(response);
        return response;
        
    }
    private void logRequest(HttpRequest request, byte[] body) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("===========================request begin================================================");
            log.debug("URI         : {}", request.getURI());
            log.debug("Method      : {}", request.getMethod());
            log.debug("Headers     : {}", request.getHeaders());
            log.debug("Request body: {}", new String(body, "UTF-8"));
            log.debug("==========================request end================================================");
        }
    }
 
    private void logResponse(ClientHttpResponse response) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("============================response begin==========================================");
            log.debug("Status code  : {}", response.getStatusCode());
            log.debug("Status text  : {}", response.getStatusText());
            log.debug("Headers      : {}", response.getHeaders());
            log.debug("Response body: {}", StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()));
            log.debug("=======================response end=================================================");
        }
}
}
