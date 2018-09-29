package com.d2z.d2zservice.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class D2ZCommonUtil {

	
		// TODO Auto-generated constructor stub
		public static String getCurrentTimestamp() {
		
			Date date= new Date();
			String currentTimestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Timestamp(date.getTime()));
			return currentTimestamp;
		}
	

}
