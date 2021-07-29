package com.d2z.d2zservice.util;

import java.math.BigDecimal;
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
import java.util.Locale;
import java.util.TimeZone;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

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
            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }
          catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

	public static String convertToJsonString(Object request) {
		ObjectWriter writer = new ObjectMapper().writer();
		String jsonRequest = null;
		try {
			jsonRequest = writer.writeValueAsString(request);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return jsonRequest;
	}

	public static BigDecimal calculateCubicWeight(String serviceType, Double weight) {
		Double modifiedWeight = weight;
		if(serviceType.equalsIgnoreCase("VC1")) {
		if(weight>0.5 && weight<=1) {
			modifiedWeight = 0.5;
		}else if(weight>1 && weight <=2) {
			modifiedWeight = weight - 0.5;
		}else if( weight>2) {
			modifiedWeight = weight - 1;
		}
		}
		return BigDecimal.valueOf(modifiedWeight);
	}
	
	public static String formatDate() {
		String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
		SimpleDateFormat currentDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);
		currentDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		String currentDate = currentDateFormat.format(new Date());
		return currentDate;
	}
	/*public static int generateTrackID() {
		Random rnd = new Random();
		int uniqueNumber = 100000 + rnd.nextInt(900000);
		return uniqueNumber;
	}*/
	
		public static StringEncryptor stringEncryptor(String encryptionPassword) {
			System.out.println(encryptionPassword);
		    PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		    SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		    config.setPassword(encryptionPassword);
		    config.setAlgorithm("PBEWithMD5AndDES");
		    config.setKeyObtentionIterations("1000");
		    config.setPoolSize("1");
		    config.setProviderName("SunJCE");
		    config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
		    config.setIvGeneratorClassName("org.jasypt.iv.NoIvGenerator");
		    config.setStringOutputType("base64");
		    encryptor.setConfig(config);
		    return encryptor;
		}

		public static String getCarrierName(String supplierName) {
			String carrier = "";
			String[] supplierArr = supplierName.split("-");
			if (supplierArr.length > 1) {
				carrier = supplierArr[1];
			}
			return carrier;			
		}
}
