package com.d2z.d2zservice.supplier;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.d2z.d2zservice.dao.ID2ZDao;
import com.d2z.d2zservice.dto.ConsignmentDTO;
import com.d2z.d2zservice.entity.ETowerResponse;
import com.d2z.d2zservice.entity.SupplierEntity;
import com.d2z.d2zservice.exception.FailureResponseException;
import com.d2z.d2zservice.model.PFLCreateShippingResponse;
import com.d2z.d2zservice.model.PFLResponseData;
import com.d2z.d2zservice.model.PFLSubmitOrderRequest;
import com.d2z.d2zservice.model.PFLSubmitOrderResponse;
import com.d2z.d2zservice.model.PFLTrackEvent;
import com.d2z.d2zservice.model.PFLTrackingResponse;
import com.d2z.d2zservice.model.PFLTrackingResponseDetails;
import com.d2z.d2zservice.model.PflCreateShippingOrderInfo;
import com.d2z.d2zservice.model.PflCreateShippingRequest;
import com.d2z.d2zservice.model.PflPrintLabelRequest;
import com.d2z.d2zservice.model.PflTrackEventRequest;
import com.d2z.d2zservice.security.HMACGenerator;
import com.d2z.d2zservice.util.D2ZCommonUtil;
import com.d2z.singleton.SingletonCounter;


@Service
public class PFLSupplier {
	
	@Value("${pfl.url}")
	private String baseURL; 
	
	@Value("${jasypt.encryptor.password}")
	private String encryptionPassword;
	
	@Autowired
	private SupplierInterface supplier;

	@Autowired
	private ID2ZDao d2zDao;
	
	public List<ConsignmentDTO> createOrder(List<ConsignmentDTO> incomingRequest,SupplierEntity config) throws FailureResponseException {
		Map<String, String> systemRefNbrMap = new HashMap<String, String>();
		ResponseEntity<PFLCreateShippingResponse> responeEntity = supplier.makeCall(HttpMethod.POST,
				baseURL + config.getSupplierCreateUri(), constructCreateOrderRequest(incomingRequest, systemRefNbrMap),
				constructHeader(config, config.getSupplierCreateUri()), PFLCreateShippingResponse.class);
		PFLCreateShippingResponse pflResponse = responeEntity.getBody();
		logPflCreateResponse(pflResponse, systemRefNbrMap);
		String carrier = D2ZCommonUtil.getCarrierName(config.getSupplierName());	
		processCreateOrderResponse(pflResponse, incomingRequest, systemRefNbrMap, carrier);

		return incomingRequest;
	}
	public HttpHeaders constructHeader(SupplierEntity config,String uri) {
		StringEncryptor stringEncryptor = D2ZCommonUtil.stringEncryptor(encryptionPassword);
		System.out.println("Decrypted Key :"+stringEncryptor.decrypt(config.getSupplierKey()));
		System.out.println("Decrypted Token :"+stringEncryptor.decrypt(config.getSupplierToken()));
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		header.add("SHIPMENTAPI-DATE", D2ZCommonUtil.formatDate());
		header.add("SHIPMENTAPI-AUTH", stringEncryptor.decrypt(config.getSupplierToken()) + ":" 
		+HMACGenerator.calculatePFLHMAC(stringEncryptor.decrypt(config.getSupplierKey()), uri, stringEncryptor.decrypt(config.getSupplierToken())));
		return header;
	}
	private String constructCreateOrderRequest(List<ConsignmentDTO> data,
			Map<String, String> systemRefNbrMap) {

		PflCreateShippingRequest pflRequest = new PflCreateShippingRequest();
		List<PflCreateShippingOrderInfo> pflOrderInfoRequest = new ArrayList<PflCreateShippingOrderInfo>();
		
		for (ConsignmentDTO orderDetail : data) {
			PflCreateShippingOrderInfo request = new PflCreateShippingOrderInfo();
			int uniqueNumber = SingletonCounter.getInstance().getPFLCount();
			String sysRefNbr = "RTFGA" + uniqueNumber;
			request.setCustom_ref(sysRefNbr);
			systemRefNbrMap.put(request.getCustom_ref(), orderDetail.getReferenceNumber());
			request.setRecipientCompany(orderDetail.getConsigneeCompany());
			String recpName = orderDetail.getConsigneeName().length() > 34
					? orderDetail.getConsigneeName().substring(0, 34)
					: orderDetail.getConsigneeName();
			request.setRecipientName(recpName);
			request.setAddressLine1(orderDetail.getConsigneeAddr1());
			request.setAddressLine2(orderDetail.getConsigneeAddr2());
			request.setEmail(orderDetail.getConsigneeEmail());
			request.setPhone(orderDetail.getConsigneePhone());
			request.setCity(orderDetail.getConsigneeSuburb());
			request.setState(orderDetail.getConsigneeState());
			request.setPostcode(orderDetail.getConsigneePostcode());
			request.setCountry("AU");
			request.setWeight(orderDetail.getCubicWeight().doubleValue());
			pflOrderInfoRequest.add(request);
		}
		pflRequest.setOrderinfo(pflOrderInfoRequest);
		return D2ZCommonUtil.convertToJsonString(pflRequest);
	}

	private void logPflCreateResponse(PFLCreateShippingResponse pflResponse, Map<String, String> systemRefNbrMap) throws FailureResponseException {
		System.out.println("Log PFL Response");
		List<ETowerResponse> responseEntity = new ArrayList<ETowerResponse>();
		if(pflResponse != null) {
			if(null !=pflResponse.getError()) {
				String addInfo = pflResponse.getAdditional_info();
				if(addInfo.length()>0) {
					String[] message = addInfo.split(":");
					String errMsg = message[1];
					String refNbr = message[0].split("-")[1].trim();
					ETowerResponse errorResponse = new ETowerResponse();
					errorResponse.setAPIName("PFL - Create order");
					errorResponse.setReferenceNumber(systemRefNbrMap.get(refNbr));
					errorResponse.setErrorMessage(errMsg.trim());
					errorResponse.setStatus("Error");
					responseEntity.add(errorResponse);
					d2zDao.logEtowerResponse(responseEntity);
					throw new FailureResponseException(errMsg.trim());
					
				}
				
			}
			if(null!=pflResponse.getResult()) {
			for(PFLResponseData pflData: pflResponse.getResult()) {
				if(pflData.getCode() != null) {
					ETowerResponse errorResponse = new ETowerResponse();
					errorResponse.setAPIName("PFL - Create order");
					errorResponse.setErrorCode(pflData.getCode());
					errorResponse.setErrorMessage(pflData.getError());
					errorResponse.setTimestamp(Timestamp.valueOf(D2ZCommonUtil.getAETCurrentTimestamp()));
					errorResponse.setStatus("Error");
					responseEntity.add(errorResponse);
					d2zDao.logEtowerResponse(responseEntity);
					throw new FailureResponseException("Shipment Error. Please contact us");
				}else {
					ETowerResponse errorResponse = new ETowerResponse();
					errorResponse.setAPIName("PFL - Create order");
					errorResponse.setReferenceNumber(pflData.getReference());
					errorResponse.setOrderId(pflData.getId());
					errorResponse.setTrackingNo(pflData.getTracking());
					errorResponse.setTimestamp(Timestamp.valueOf(D2ZCommonUtil.getAETCurrentTimestamp()));
					errorResponse.setStatus("Success");
					responseEntity.add(errorResponse);
				}
			}}else {
				throw new FailureResponseException("Shipment Error. Please contact us");
			}
			
		
			d2zDao.logEtowerResponse(responseEntity);
		}else {
			throw new FailureResponseException("Shipment Error. Please contact us");
		}
	}
	private void processCreateOrderResponse(PFLCreateShippingResponse pflResponse, List<ConsignmentDTO> incomingRequest, 
			Map<String, String> systemRefNbrMap, String carrier) {
		incomingRequest.stream().forEach(obj -> {
			PFLResponseData pflResp = pflResponse.getResult().stream()
					.filter(data -> systemRefNbrMap.get(data.getReference()).equals(obj.getReferenceNumber()))
					.findFirst().orElse(null);
			if (pflResp != null) {
				System.out.println(pflResp.getTracking());
				obj.setInjectionState(pflResp.getHub());
				obj.setBarcodelabelNumber(pflResp.getTracking());
				obj.setArticleId(pflResp.getTracking());
				obj.setMlid(pflResp.getId());
				obj.setDatamatrix(pflResp.getMatrix());
			    obj.setCarrier(carrier);
				obj.setD2zRate(pflResp.getHeader());
				obj.setBrokerRate(pflResp.getFooter());
			}
		});
	}
	public byte[] printLabel(List<String> mlidList, SupplierEntity config) {

		PflPrintLabelRequest request = new PflPrintLabelRequest();
		request.setIds(mlidList);
		
			ResponseEntity<byte[]> response =  supplier.makeCall(HttpMethod.POST,
					baseURL + config.getSupplierLabelUri(), D2ZCommonUtil.convertToJsonString(request),
					constructHeader(config, config.getSupplierLabelUri()), byte[].class);
		
		return response.getBody();
	
	}
	public List<PFLTrackingResponseDetails> makeTrackingCall(List<String> articleIds, SupplierEntity config) {
		List<PFLTrackingResponseDetails> pflTrackingDetails = new ArrayList<PFLTrackingResponseDetails>();

		for(String pflValue:articleIds) {
		PflTrackEventRequest pflTrackEvent = new PflTrackEventRequest();
		PFLTrackingResponseDetails pflResp = new PFLTrackingResponseDetails();
		pflTrackEvent.setTracking_number(pflValue);
		
		ResponseEntity<PFLTrackingResponse> responseEntity =  supplier.makeCall(HttpMethod.POST,
				baseURL + config.getSupplierTrackingUri(), D2ZCommonUtil.convertToJsonString(pflTrackEvent),
				constructHeader(config, config.getSupplierTrackingUri()), PFLTrackingResponse.class);
		
		PFLTrackingResponse pflTrackResp = responseEntity.getBody();
		if(pflTrackResp.getResult() != null) {
			pflResp.setBarcodeLabel(pflTrackEvent.getTracking_number());
			List<PFLTrackEvent> pflTrackEventList = new ArrayList<PFLTrackEvent>();
			for(PFLTrackingResponseDetails response:pflTrackResp.getResult()) {
			PFLTrackEvent event= new PFLTrackEvent(); 
			event.setStatus(response.getStatus());
			event.setStatus_code(response.getStatus_code());
			event.setDate(response.getDate());
			event.setLocation(response.getLocation());
			pflTrackEventList.add(event);
			}
			pflResp.setTrackEvent(pflTrackEventList);
			pflTrackingDetails.add(pflResp);
		}
		}		
		return pflTrackingDetails;
	}
	
	public void allocateShipment(List<String> orderIds, SupplierEntity config) throws FailureResponseException {
		
		ResponseEntity<PFLSubmitOrderResponse> responeEntity = supplier.makeCall(HttpMethod.POST,
				baseURL + config.getSupplierAllocateUri(), constructSubmitOrderRequest(orderIds),
				constructHeader(config, config.getSupplierAllocateUri()), PFLSubmitOrderResponse.class);
		PFLSubmitOrderResponse pflResponse = responeEntity.getBody();
		logPflSubmitResponse(pflResponse,orderIds);
		if(pflResponse==null) {
			throw new FailureResponseException("Error in file – please contact customer support");
		}	
	}

	private String constructSubmitOrderRequest(List<String> orderIds) {
		PFLSubmitOrderRequest pflSubmitOrder = new PFLSubmitOrderRequest();
		pflSubmitOrder.setIds(orderIds);
		return D2ZCommonUtil.convertToJsonString(pflSubmitOrder);
	}
	
	private void logPflSubmitResponse(PFLSubmitOrderResponse pflSubmitResponse, List<String> orderIds) throws FailureResponseException{
		List<ETowerResponse> responseEntity = new ArrayList<ETowerResponse>();
			if(pflSubmitResponse != null) {
				if(pflSubmitResponse.getError() != null) {
					if(pflSubmitResponse.getError_ids()!=null) {
				
					for(String orderIdVal:pflSubmitResponse.getError_ids()) {
						ETowerResponse errorResponse = new ETowerResponse();
						errorResponse.setAPIName("PFL - Submit order");
						errorResponse.setOrderId(orderIdVal);
						errorResponse.setErrorCode(pflSubmitResponse.getCode());
						errorResponse.setErrorMessage(pflSubmitResponse.getError());
						errorResponse.setTimestamp(Timestamp.valueOf(D2ZCommonUtil.getAETCurrentTimestamp()));
						errorResponse.setStatus("Error");
						responseEntity.add(errorResponse);
					}
					
					d2zDao.logEtowerResponse(responseEntity);
					throw new FailureResponseException("Error in file – please contact customer support");
					}
				}else {
					for(String successOrder:orderIds) {
						ETowerResponse errorResponse = new ETowerResponse();
						errorResponse.setAPIName("PFL - Submit order");
						errorResponse.setOrderId(successOrder);
						errorResponse.setTimestamp(Timestamp.valueOf(D2ZCommonUtil.getAETCurrentTimestamp()));
						errorResponse.setStatus("Success");
						responseEntity.add(errorResponse);
					}
					d2zDao.logEtowerResponse(responseEntity);
				}
			}
	}
}
