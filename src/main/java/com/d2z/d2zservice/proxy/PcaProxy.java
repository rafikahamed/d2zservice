package com.d2z.d2zservice.proxy;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.d2z.d2zservice.model.PCACancelRequest;
import com.d2z.d2zservice.model.PCACreateShipmentRequest;
import com.d2z.d2zservice.model.PCACreateShippingResponse;
import com.d2z.d2zservice.model.PCATrackEventRequest;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PcaProxy {
	
	@Value("${pca.url}")
	private String url;
	
	@Value("${pca.apiKey}")
	private String apiKey;
	
	@Value("${pca.apiId}")
	private String apiId;
	
	public String trackingEvent(String articleId) {
		ObjectMapper mapper = new ObjectMapper();
		PCATrackEventRequest pcaTracking = new PCATrackEventRequest();
		pcaTracking.setConnote(articleId.substring(0,10));
		String jsonString = null;
		try {
			jsonString = mapper.writeValueAsString(pcaTracking);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}
		System.out.println("PCA Request ------>");
		System.out.println(jsonString);
		String response = executePost("http://api.pcaexpress.com.au/tracking", constructParamWithoutMethod(jsonString));
		System.out.println("PCA Response--->"+response);
		return response;
	}

//	public void trackingEvent(List<String> articleIds) {
//		String url = "https://s1.pcaex.com/api/tracking";
//		RestTemplate template = new RestTemplate();
//		HttpHeaders headers = new HttpHeaders();
//		String ArticleData = "{\"connote\":";
//
//		if (articleIds.size() > 1) {
//			String ID = "[";
//			for (String item : articleIds) {
//				ID = ID + "\"" + item + "\"" + ",";
//			}
//			ID = ID.substring(0, ID.length() - 1);
//			ID = ID + "]";
//			ArticleData = ArticleData + ID + "}";
//		} else {
//			ArticleData = ArticleData + articleIds.get(0) + "}";
//		}
//		System.out.println(ArticleData);
//
//		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//
//		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
//		formData.add("api_id", apiId);
//		formData.add("data", ArticleData);
//		String sign = calculatesign(ArticleData);
//		formData.add("sign", sign.toUpperCase());
//
//		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);
//		System.out.println("requestEntity---->"+requestEntity);
//		System.out.println(url);
////		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
////		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.TEXT_HTML));
////		template.getMessageConverters().add(converter);
//		ResponseEntity<Object> response = template.exchange(url, HttpMethod.POST, requestEntity, Object.class);
//
//		System.out.println(response.getStatusCode());
//		System.out.println(response.getBody().toString());
//
//		byte ptext[];
//		try {
//			ptext = response.getBody().toString().getBytes("ISO-8859-1");
//			String value = new String(ptext, "UTF-8");
//			System.out.println(value);
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
	
	public String calculatesign(String data) {
		String ApiKey = "6366b2bb8b8d7407a3d46f6fa79a9af3472e1381442b49e637dfc96a0b0d6808";
		String Data = "api_idapi_testdata" + data;

		String Finaloutput = ApiKey + Data + ApiKey;
		String hashtext = "";
		// Static getInstance method is called with hashing MD5
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			// digest() method is called to calculate message digest
			// of an input digest() return array of byte
			byte[] messageDigest = md.digest(Finaloutput.getBytes());
			// Convert byte array into signum representation
			BigInteger no = new BigInteger(1, messageDigest);
			// Convert message digest into hex value
			hashtext = no.toString(16);
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			System.out.println(hashtext);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hashtext;

	}

	public List<PCACreateShippingResponse> makeCallForCreateShippingOrder(PCACreateShipmentRequest pcaRequest) {
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = mapper.writeValueAsString(pcaRequest);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}
		System.out.println("PCA Request ------>");
		System.out.println(jsonString);
		String response = executePost(url, constructParam(jsonString, "create"));
		System.out.println(response);
		List<PCACreateShippingResponse> pcaResponse = null;
		try {
			pcaResponse = mapper.readValue(response, new TypeReference<List<PCACreateShippingResponse>>(){});
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pcaResponse;
	}

	public String constructParam(String pcaRequest, String method) {
		String requestData="";
		try {
			requestData = "api_id=" + URLEncoder.encode(apiId, "UTF-8") + "&method="
					+ URLEncoder.encode(method, "UTF-8") + "&data=" + URLEncoder.encode(pcaRequest.toString(), "UTF-8") + "&sign="
					+ URLEncoder.encode(getSign(pcaRequest.toString(),method), "UTF-8");
			System.out.println(requestData);
			return requestData;
			
		} catch (Exception e) {
			e.printStackTrace();
			return requestData;
    	}
	}
	
	public String constructParamWithoutMethod(String pcaRequest) {
		String requestData="";
		try {
			requestData = "api_id=" + URLEncoder.encode(apiId, "UTF-8") + "&data=" + URLEncoder.encode(pcaRequest.toString(), "UTF-8") + "&sign="
					+ URLEncoder.encode(getSignWithoutMethod(pcaRequest.toString()), "UTF-8");
			System.out.println(requestData);
			return requestData;
			
		} catch (Exception e) {
			e.printStackTrace();
			return requestData;
    	}
	}
	
	private String getSignWithoutMethod(String data) {
		Map<String, String> collect = new TreeMap<>();
		collect.put("api_id", apiId);
		collect.put("data", data);
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(apiKey);
		for (Map.Entry<String, String> entry : collect.entrySet()) {
			sBuilder.append(entry.getKey());
			sBuilder.append(entry.getValue());
		}
		sBuilder.append(apiKey);
		return getMd5(sBuilder.toString());
	}
	
	private String getSign(String data, String method) {
		Map<String, String> collect = new TreeMap<>();
		collect.put("api_id", apiId);
		collect.put("method", method);
		collect.put("data", data);
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(apiKey);
		for (Map.Entry<String, String> entry : collect.entrySet()) {
			sBuilder.append(entry.getKey());
			sBuilder.append(entry.getValue());
		}
		sBuilder.append(apiKey);
		return getMd5(sBuilder.toString());
	}

	private String getMd5(String text) {
		StringBuilder sb = new StringBuilder();
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			for (byte b : messageDigest.digest(text.getBytes())) {
				sb.append(String.format("%02X", b));
			}
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		return sb.toString();
	}

	private String executePost(String targetUrl, String UrlParameters) {
		URL url;
		HttpURLConnection connection = null;
		try {
			url = new URL(targetUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length", Integer.toString(UrlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(UrlParameters);
			wr.flush();
			wr.close();
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			
			String line;
			StringBuffer reponse = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				reponse.append(line);
				reponse.append('\r');
			}
			rd.close();
			System.out.println(reponse);
			return reponse.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
	private byte[] executeLabel(String targetUrl, String UrlParameters) {
		URL url;
		HttpURLConnection connection = null;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		try {
			url = new URL(targetUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length", Integer.toString(UrlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(UrlParameters);
			wr.flush();
			wr.close();
			InputStream is = connection.getInputStream();
			
			 byte[] chunk = new byte[4096];
		        int bytesRead;
		      
		        while ((bytesRead = is.read(chunk)) > 0) {
		        	
		        	outputStream.write(chunk, 0, bytesRead);
		        }
		            
			
			return outputStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	public PCACreateShippingResponse makeCallForCancelShipment(PCACancelRequest pcaReq) {
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = mapper.writeValueAsString(pcaReq);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}
		System.out.println("PCA Cancel Request ------>");
		System.out.println(jsonString);
		String response = executePost(url, constructParam(jsonString, "cancel"));
		System.out.println(response);
		PCACreateShippingResponse pcaResponse = null;
		try {
			pcaResponse = mapper.readValue(response, new TypeReference<PCACreateShippingResponse>(){});
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pcaResponse;
	}
	public byte[] makeCallForLabelShipment(PCACancelRequest pcaReq) {
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = mapper.writeValueAsString(pcaReq);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}
		System.out.println("PCA Label Request ------>");
		System.out.println(jsonString);
		byte output[] = executeLabel(url,constructParam(jsonString, "label"));
		
		
		return output;
	}
	private String constructParamCancel(PCACancelRequest pcaReq, String string) {
		return null;
	}
	
}
