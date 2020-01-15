package com.d2z.d2zservice.security;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;
@Service
public class HMACGenerator {

	private final static String HMAC_SHA1_ALGORITHM = "HmacSHA1";
	public String calculateHMAC(String key,String url,String verb) {
		String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
		SimpleDateFormat currentDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);
		currentDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		String currentDate = currentDateFormat.format(new Date());
		
		String toSignIn = verb+'\n'+currentDate+'\n'+url;
		System.out.println(toSignIn);
		String result = "";
		try {
		SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(),HMAC_SHA1_ALGORITHM);
		Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
		mac.init(signingKey);
		//byte[] rawHmac=mac.doFinal(toSignIn.getBytes());
	     result = Base64.encodeBase64String(mac.doFinal(toSignIn.getBytes()));
	     System.out.println(Base64.decodeBase64(result));
		} catch (Exception e) {
		e.printStackTrace();
		}
	return result;
		}
	
	
	public static String calculatePFLHMAC(String key,String url,String Token) {
		String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
		//String Token ="QT6P9I85LHETLYP43G7J440GD6W77TFX";
		
	/*	 ZoneId singaporeZoneId = ZoneId.of("Australia/Sydney");
	        System.out.println("TimeZone : " + singaporeZoneId);
	        LocalDateTime ldt = LocalDateTime.now();

	        //LocalDateTime + ZoneId = ZonedDateTime
	        ZonedDateTime asiaZonedDateTime = ldt.atZone(singaporeZoneId);*/
		SimpleDateFormat currentDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);
		currentDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		//currentDateFormat.T
		String currentDate = currentDateFormat.format(new Date());
		
		//String currentDate = "Mon, 23 Jan 2017 23:11:09 +0000";
		String toSignIn =":"+ currentDate+":"+url;
		System.out.println(toSignIn);
		String result = "";
		try {
		SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(),HMAC_SHA1_ALGORITHM);
		Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
		mac.init(signingKey);
		//byte[] rawHmac=mac.doFinal(toSignIn.getBytes());
	     result = Base64.encodeBase64String(mac.doFinal(toSignIn.getBytes()));
	     System.out.println((result));
	     System.out.println(Token+":"+result);
		}catch (Exception e){
			
		}
		return result;
}
}
