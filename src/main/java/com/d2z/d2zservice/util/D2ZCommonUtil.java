package com.d2z.d2zservice.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class D2ZCommonUtil {

	public static String getCurrentTimestamp() {
		Date date = new Date();
		String currentTimestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Timestamp(date.getTime()));
		return currentTimestamp;
	}

	public static String hashPassword(String plainTextPassword) {
		return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
	}

	public static String formatDataMatrix(String datamatrix) {
		StringBuffer formattedDatamatrix = new StringBuffer(datamatrix);
		formattedDatamatrix.insert(0, '[');
		formattedDatamatrix.insert(3, ']');
		formattedDatamatrix.insert(18, '[');
		formattedDatamatrix.insert(21, ']');
		formattedDatamatrix.insert(43, '[');
		formattedDatamatrix.insert(47, ']');
		formattedDatamatrix.insert(52, '[');
		formattedDatamatrix.insert(57, ']');
		return formattedDatamatrix.toString();
	}
	
	public static String getAETCurrentTimestamp() {
		Date dt = new Date();
	    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	    format.setTimeZone(TimeZone.getTimeZone("Australia/Sydney"));
	    String parsedDate = format.format(dt);
		return parsedDate;
	}
	
	  
}
