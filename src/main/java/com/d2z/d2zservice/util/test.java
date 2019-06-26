package com.d2z.d2zservice.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;
import java.util.TimeZone;

import org.springframework.web.client.RestTemplate;

import com.d2z.d2zservice.model.PFLCreateShippingResponse;
import com.d2z.d2zservice.security.HMACGenerator;

public class test {

	public static boolean isPalindrome(String word) {
		  // Please write your code here.
		  String reverse = "";
		  int len = word.length();
		  for(int i = len-1;i>=0;i--)
		  {
		    reverse = reverse + word.charAt(i);
		  }
		  System.out.println(reverse);
		  if(reverse.equalsIgnoreCase(word))
		  {
		    return true;
		  }
		  else
		  {
		    return false;
		  }
		}
	
	
	
		public static void main(String[] args) {
			String baseURL = "http://103.225.160.46";
			HMACGenerator hmacGenerator = new HMACGenerator();
			String url = baseURL + "/app/services/multicourier/deleteorder";
			
			String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
			SimpleDateFormat currentDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);
			currentDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
			String currentDate = currentDateFormat.format(new Date());
			System.out.println("US: " + currentDateFormat.format(new Date()));
			String SECRET_KEY = "3VXSOS7WUSF4DS6V5LXS8ER14KR2TMP6";
			String Token = "QT6P9I85LHETLYP43G7J440GD6W77TFX";
			String authorizationHeader = hmacGenerator.calculatePFLHMAC(SECRET_KEY,
					"/app/services/multicourier/deleteorder");
			System.out.println("US: " + authorizationHeader);
		
		
		}
}
