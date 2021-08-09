package com.d2z.d2zservice.supplier;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.d2z.d2zservice.entity.FFResponse;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.entity.SupplierEntity;
import com.d2z.d2zservice.model.FDMManifestDetails;
import com.d2z.d2zservice.model.fdm.ArrayOfConsignment;
import com.d2z.d2zservice.model.fdm.ArrayofDetail;
import com.d2z.d2zservice.model.fdm.Consignment;
import com.d2z.d2zservice.model.fdm.FDMManifestRequest;
import com.d2z.d2zservice.model.fdm.FDMManifestResponse;
import com.d2z.d2zservice.model.fdm.Line;
import com.d2z.d2zservice.repository.FFResponseRepository;
import com.d2z.d2zservice.repository.InvoicingZonesRepository;
import com.d2z.d2zservice.util.D2ZCommonUtil;

public class FDMSupplier {

	
	@Autowired
	FFResponseRepository ffresponseRepository;
	
	@Autowired
	InvoicingZonesRepository zonesRepository;
	
	@Value("${fdm.baseUrl}")
	private String baseURL; 
	
	@Value("${jasypt.encryptor.password}")
	private String encryptionPassword;
	
	@Autowired
	private SupplierInterface supplier;
	
	public void allocateShipment(List<SenderdataMaster> senderData,SupplierEntity config) {

		
			List<List<SenderdataMaster>> senderDataList = ListUtils.partition(senderData, 2000);
			senderDataList.forEach((data)->{
				Date dNow = new Date();
				SimpleDateFormat ft = new SimpleDateFormat("yyyyMMddhhmm");
				String orderRef = ft.format(dNow);
				ResponseEntity<FDMManifestResponse> responseEntity = supplier.makeCall(HttpMethod.POST, baseURL+config.getSupplierAllocateUri(),
						constructRequest(data,orderRef), constructHeader(config), FDMManifestResponse.class);
				saveFFResponse(""+responseEntity.getStatusCodeValue(), orderRef);
				
			});
	}
	
	private HttpHeaders constructHeader(SupplierEntity config) {
		StringEncryptor stringEncryptor = D2ZCommonUtil.stringEncryptor(encryptionPassword);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		 String base64encodedString = null;
		try {
			String auth = stringEncryptor.decrypt(config.getSupplierKey())+":"+stringEncryptor.decrypt(config.getSupplierToken());
			base64encodedString = Base64.getEncoder().encodeToString(auth.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		headers.add("Authorization","Basic "+ base64encodedString);
		return headers;
	}
 private String	constructRequest(List<SenderdataMaster> senderData,String orderRef){

		FDMManifestRequest request = new FDMManifestRequest();
		Date dNow = new Date();

		FDMManifestDetails fdmDetails = new FDMManifestDetails();
		fdmDetails.setMessage_no(orderRef);
		fdmDetails.setCustomer_id("D2Z");
		List<FFResponse> FFResponseList = new ArrayList<FFResponse>();

		ArrayOfConsignment consignmentsArray = new ArrayOfConsignment();
		List<Consignment> consignments = new ArrayList<Consignment>();
		for (SenderdataMaster data : senderData) {
			Consignment consignment = new Consignment();
			FFResponse ffresponse = new FFResponse();
			ffresponse.setMessage(orderRef);
			ffresponse.setBarcodelabelnumber(data.getBarcodelabelNumber());
			ffresponse.setWeight(String.valueOf(data.getWeight()));
			ffresponse.setArticleid(data.getArticleId());
			ffresponse.setReferencenumber(data.getReference_number());
			ffresponse.setTimestamp(Timestamp.valueOf(D2ZCommonUtil.getAETCurrentTimestamp()));
			ffresponse.setSupplier("FDM");
			ffresponse.setResponse("Pending");
			if(data.getArticleId().length()>=20) {
			consignment.setConnote_no(data.getArticleId().substring(3,19));
			}else{
				consignment.setConnote_no(data.getArticleId());
			}
			consignment.setTracking_connote(data.getArticleId());

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
			consignment.setConnote_date(format.format(dNow));
			
			
			consignment.setCustname(data.getConsignee_name());
			consignment.setCust_street1(data.getConsignee_addr1());
			consignment.setCust_street2(data.getConsignee_addr2());
			
			String state = data.getConsignee_State().trim().toUpperCase();
			String suburb = data.getConsignee_Suburb().trim().toUpperCase();
			String postcode = data.getConsignee_Postcode().trim();
			consignment.setCust_suburb(suburb);
			consignment.setCust_pcode(postcode);
			consignment.setCust_state(state);
			consignment.setRoute_code(zonesRepository.fdmRoute(suburb, state, postcode));
			consignment.setCust_country("AU");
			consignment.setCust_ph(data.getConsignee_Phone());
			consignment.setCust_email(data.getConsignee_Email());
			consignment.setInstruction(data.getDeliveryInstructions());
			consignment.setCustomer_code("D2Z");
			consignment.setCarrier("AUSPOST");
			consignment.setVendor_name("D2Z");
			if(state.equalsIgnoreCase("VIC")) {
			consignment.setVendor_street1("5 Buckland Street");
			consignment.setVendor_suburb("CLAYTON SOUTH");
			consignment.setVendor_pcode("3168");
			consignment.setVendor_state("VIC");
			}else {
				consignment.setVendor_street1("PO Box 6566");
				consignment.setVendor_suburb("Wetherill Park");
				consignment.setVendor_pcode("2164");
				consignment.setVendor_state("NSW");
			}
			consignment.setVendor_country("AU");
			consignment.setTotal_weight(String.valueOf(data.getWeight()));
			ArrayofDetail details = new ArrayofDetail();
			List<Line> itemList = new ArrayList<Line>();
			Line lineItem = new Line();
			lineItem.setBarcode(data.getBarcodelabelNumber());

			lineItem.setArticle_no(data.getArticleId());
			lineItem.setDescription(data.getProduct_Description());
			lineItem.setWeight(String.valueOf(data.getWeight()));
			lineItem.setDim_height(
					data.getDimensions_Height() == null ? "" : data.getDimensions_Height().toString());
			lineItem.setDim_length(
					data.getDimensions_Length() == null ? "" : data.getDimensions_Length().toString());
			lineItem.setDim_width(
					data.getDimensions_Width() == null ? "" : data.getDimensions_Width().toString());
			itemList.add(lineItem);
			details.setLine(itemList);
			consignment.setDetails(details);
			consignments.add(consignment);
			FFResponseList.add(ffresponse);
		}
		consignmentsArray.setConsignment(consignments);
		fdmDetails.setConsignments(consignmentsArray);
		request.setManifest(fdmDetails);

		
	
		 ffresponseRepository.saveAll(FFResponseList);
		 return D2ZCommonUtil.convertToJsonString(request);
	}
 
    public void saveFFResponse(String response,String orderRef) {
    	 List <FFResponse> FFresponsequery = ffresponseRepository.findByMessageNoIs(orderRef); 
		 List <FFResponse>	 FFResponseUpdaList = new ArrayList<FFResponse>(); 
		 for (FFResponse temp :FFresponsequery) { 
			 temp.setResponse(response);
			 FFResponseUpdaList .add(temp);
			 } 
		 ffresponseRepository.saveAll(FFResponseUpdaList);
    }
}
