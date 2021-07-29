package com.d2z.d2zservice.supplier;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.d2z.d2zservice.dao.ID2ZDao;
import com.d2z.d2zservice.dto.ConsignmentDTO;
import com.d2z.d2zservice.entity.ETowerResponse;
import com.d2z.d2zservice.entity.SupplierEntity;
import com.d2z.d2zservice.entity.Veloce;
import com.d2z.d2zservice.model.veloce.Consignment;
import com.d2z.d2zservice.model.veloce.ConsignmentRequest;
import com.d2z.d2zservice.model.veloce.ConsignmentResponse;
import com.d2z.d2zservice.model.veloce.ConsignmentStatus;
import com.d2z.d2zservice.model.veloce.Courier;
import com.d2z.d2zservice.model.veloce.RequestHeader;
import com.d2z.d2zservice.model.veloce.Tracking;
import com.d2z.d2zservice.model.veloce.VeloceTrackingRequest;
import com.d2z.d2zservice.model.veloce.VeloceTrackingResponse;
import com.d2z.d2zservice.util.D2ZCommonUtil;

@Service
public class VeloceSupplier {
	
	@Value("${veloce.baseUrl}")
	private String baseURL; 
	
	@Value("${jasypt.encryptor.password}")
	private String encryptionPassword;
	@Autowired
	private SupplierInterface supplier;

	@Autowired
	private ID2ZDao d2zDao;

	public void createOrder(List<ConsignmentDTO> incomingRequest,SupplierEntity config){
		Map<String, String> systemRefNbrMap = new HashMap<String, String>();
		ResponseEntity<ConsignmentResponse> responeEntity = supplier.makeCall(HttpMethod.POST,
				baseURL + config.getSupplierCreateUri(), constructCreateOrderRequest(incomingRequest,config),
				null, ConsignmentResponse.class);
		ConsignmentResponse response = responeEntity.getBody();
		logResponse(response);
		String carrier = D2ZCommonUtil.getCarrierName(config.getSupplierName());	
	}

	private void logResponse(ConsignmentResponse response) {

		List<ETowerResponse> responseEntity = new ArrayList<ETowerResponse>();
		for(ConsignmentStatus responseData : response.getBdr().getConsignment_status())
		{
			ETowerResponse errorResponse = new ETowerResponse();
			errorResponse.setAPIName("Veloce");
			errorResponse.setStatus(responseData.getSubmit_status());
			errorResponse.setOrderId(responseData.getTracking_id());
			errorResponse.setTrackingNo(responseData.getTracking_id());
			errorResponse.setReferenceNumber(responseData.getTracking_id());
			errorResponse.setTimestamp(Timestamp.valueOf(D2ZCommonUtil.getAETCurrentTimestamp()));
			responseEntity.add(errorResponse);
		}
		
		d2zDao.logEtowerResponse(responseEntity);	}
	
	private String constructCreateOrderRequest(List<ConsignmentDTO> incomingRequest,SupplierEntity config) {
		StringEncryptor stringEncryptor = D2ZCommonUtil.stringEncryptor(encryptionPassword);
		ConsignmentRequest request = new ConsignmentRequest();
		
		request.setHdr(constructHeader(stringEncryptor.decrypt(config.getSupplierToken()),stringEncryptor.decrypt(config.getSupplierKey())));
		
		List<Consignment> bdr = new ArrayList<Consignment>();
		for(ConsignmentDTO data : incomingRequest) {
			Consignment consignment = new Consignment();
			Veloce veloce = d2zDao.findVeloceValues(data.getServicetype());
			if(veloce == null) {
				consignment.setCourier(data.getCourier());
				consignment.setShipper_country(data.getShipperCountry());

			}else{
				consignment.setCourier(veloce.getCourier());
				consignment.setRecipient_country(veloce.getReceiverCountry());
				consignment.setShipper_country(veloce.getShipperCountry());
			}
			consignment.setCurrency(data.getCurrency());
			consignment.setHeight(data.getDimensionsHeight()==null?"":data.getDimensionsHeight().toString());
			consignment.setItem_desc(data.getProductDescription());
			consignment.setLength(data.getDimensionsLength()==null?"":data.getDimensionsLength().toString());
			consignment.setRecipient_address1(data.getConsigneeAddr1());
			consignment.setRecipient_address2(data.getConsigneeAddr2()==null?"":data.getConsigneeAddr2());
			consignment.setRecipient_city(data.getConsigneeSuburb());
			consignment.setRecipient_email(data.getConsigneeEmail()==null?"":data.getConsigneeEmail());
			consignment.setRecipient_name(data.getConsigneeName());
			consignment.setRecipient_phone(data.getConsigneePhone());
			consignment.setRecipient_postcode(data.getConsigneePostcode());
			consignment.setRecipient_state_name(data.getConsigneeState());
			consignment.setShipper_address1(data.getShipperAddr1()==null?"":data.getShipperAddr1());
			consignment.setShipper_city(data.getShipperCity()==null?"":data.getShipperCity());
			consignment.setShipper_state_name(data.getShipperState()==null?"":data.getShipperState());
			consignment.setShipper_name(data.getShipperName()==null?"":data.getShipperName());
			consignment.setShipper_postcode(data.getShipperPostcode()==null?"":data.getShipperPostcode());
			consignment.setShipper_ref(data.getReferenceNumber());
			consignment.setTracking_id(data.getBarcodelabelNumber());
			consignment.setWarehouse_code("VELOCE_WAREHOUSE");
			consignment.setWeight(String.valueOf(data.getWeight()));
			consignment.setValue(String.valueOf(data.getValue()));
			consignment.setWidth(data.getDimensionsWidth()==null?"":data.getDimensionsWidth().toString());
			bdr.add(consignment);
		}
		request.setBdr(bdr);
		return D2ZCommonUtil.convertToJsonString(request);
	
	}

	private RequestHeader constructHeader(String clientId,String key) {
		RequestHeader hdr = new RequestHeader();
		hdr.setClient_id(clientId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String formattedDate = sdf.format(new Date());
		hdr.setRequest_date(formattedDate);
		String sign = clientId.concat(key).concat(formattedDate);
		hdr.setRequest_sign(D2ZCommonUtil.sha1Encrpytion(sign)); 
		return hdr;
	}

	public byte[] printLabel(List<String> refBarNums,SupplierEntity supplierDetails) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );

		refBarNums.forEach(articleId -> {
			ResponseEntity<byte[]> response = supplier.makeGetCall(baseURL+supplierDetails.getSupplierLabelUri()+articleId,byte[].class);
			byte[] bytes = response.getBody();
			System.out.println(bytes);
			try {
				outputStream.write(bytes);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		return outputStream.toByteArray();
		
	}

	public VeloceTrackingResponse makeTrackingCall(List<String> ids, SupplierEntity config) {

		ResponseEntity<VeloceTrackingResponse> responeEntity = supplier.makeCall(HttpMethod.GET,
				baseURL + config.getSupplierTrackingUri(), constructTrackingRequest(ids,config),
				null, VeloceTrackingResponse.class);
		VeloceTrackingResponse response = responeEntity.getBody();
		return response;	
	}

	private String constructTrackingRequest(List<String> ids, SupplierEntity config) {
		StringEncryptor stringEncryptor = D2ZCommonUtil.stringEncryptor(encryptionPassword);
		VeloceTrackingRequest request = new VeloceTrackingRequest();
		request.setHdr(constructHeader(stringEncryptor.decrypt(config.getSupplierToken()),stringEncryptor.decrypt(config.getSupplierKey())));
		List<Tracking> trackRequest = new ArrayList<Tracking>();
		ids.forEach(id -> {
			Tracking trackingId = new Tracking();
			trackingId.setTRACKING_ID(id);
			trackRequest.add(trackingId);
		});
		request.setBdr(trackRequest);
		
		return D2ZCommonUtil.convertToJsonString(request);
	}
}
