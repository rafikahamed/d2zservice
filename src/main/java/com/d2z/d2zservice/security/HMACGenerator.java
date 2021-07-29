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

import com.d2z.d2zservice.util.D2ZCommonUtil;
@Service
public class HMACGenerator {

	private final static String HMAC_SHA1_ALGORITHM = "HmacSHA1";
	public static String calculateHMAC(String key,String url,String verb) {
		System.out.println(key+"::::"+url+"::::"+verb);
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
		String currentDate = D2ZCommonUtil.formatDate();
		String toSignIn = ":" + currentDate + ":" + url;
		String result = "";
		try {
			SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
			Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
			mac.init(signingKey);
			result = Base64.encodeBase64String(mac.doFinal(toSignIn.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
}
}
