package com.d2z.d2zservice.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class D2ZCommonUtil {
	
		public static String getCurrentTimestamp() {
			Date date= new Date();
			String currentTimestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Timestamp(date.getTime()));
			return currentTimestamp;
		}
		
		public static String hashPassword(String plainTextPassword){
		    return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
		}

}
