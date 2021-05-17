package com.d2z.d2zservice.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
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
		if(datamatrix.length()>63) {
			formattedDatamatrix.insert(45, '[');
			formattedDatamatrix.insert(49, ']');
			formattedDatamatrix.insert(54, '[');
			formattedDatamatrix.insert(59, ']');
		}else {
		formattedDatamatrix.insert(43, '[');
		formattedDatamatrix.insert(47, ']');
		formattedDatamatrix.insert(52, '[');
		formattedDatamatrix.insert(57, ']');
		}
		return formattedDatamatrix.toString();
	}
	
	public static String getAETCurrentTimestamp() {
		Date dt = new Date();
	    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    format.setTimeZone(TimeZone.getTimeZone("Australia/Sydney"));
	    String parsedDate = format.format(dt);
		return parsedDate;
	}

	public static String getday() {
		Date dt = new Date();
	    DateFormat format = new SimpleDateFormat("yyyyMMdd");
	    format.setTimeZone(TimeZone.getTimeZone("Australia/Sydney"));
	    String parsedDate = format.format(dt);
		return parsedDate;
	}

	public static String getIncreasedTime(String trackingEventDateOccured, String transitTime) {
		String[] timeStampSplitArray = trackingEventDateOccured.split(" ");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try{
		   c.setTime(sdf.parse(trackingEventDateOccured));
		}catch(ParseException e){
			e.printStackTrace();
		 }
		c.add(Calendar.DAY_OF_MONTH, Integer.parseInt(transitTime));  
		String newDate = sdf.format(c.getTime());  
		return newDate+" "+timeStampSplitArray[1];
	}

	public static String formatPCAMessage(String msg) {
		List<String> sysRefNbrs = new ArrayList<String>();
		String[] arr = msg.split(" ");
		for(String s : arr) {
			if(s.startsWith("CR")){
				sysRefNbrs.add(s);
			}
		}
		List<String> list = new ArrayList<String>(Arrays.asList(arr));
		list.removeAll(sysRefNbrs);
		String formattedMsg = String.join(" ", list);
		return formattedMsg;
	}
	
	public static List<String> apgData(){
		List<String> apgList = new ArrayList<String>();
		apgList.add("33UXT");
		apgList.add("33UXX");
		apgList.add("33UY6");
		apgList.add("33UYA");
		apgList.add("33XCR");
		apgList.add("33XCT");
		apgList.add("33XH7");
		apgList.add("33XH8");
		return apgList;
	}
	
	public static List<String> ubiData(){
		List<String> ubiList = new ArrayList<String>();
		ubiList.add("33G7K");
		ubiList.add("33G7L");
		ubiList.add("33G7M");
		ubiList.add("33G7N");
		ubiList.add("33G7P");
		ubiList.add("SJU");
		ubiList.add("33QU7");
		ubiList.add("ZK6");
		return ubiList;
	}
	
	public static List<String> fdmData(){
		List<String> fdmList = new ArrayList<String>();
		fdmList.add("33PE9");
		fdmList.add("33PET");
		fdmList.add("33PEN");
		fdmList.add("33PEH");
		return fdmList;
	}
	

    public static String sha1Encrpytion(String input)
    {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
          catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
	
	/*public static int generateTrackID() {
		Random rnd = new Random();
		int uniqueNumber = 100000 + rnd.nextInt(900000);
		return uniqueNumber;
	}*/
}
