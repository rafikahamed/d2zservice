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
import com.d2z.d2zservice.model.etower.CreateShippingResponse;
import com.d2z.d2zservice.model.etower.EtowerErrorResponse;
import com.d2z.d2zservice.model.etower.GainLabelsResponse;
import com.d2z.d2zservice.model.etower.LabelData;
import com.d2z.d2zservice.model.etower.ResponseData;
import com.d2z.d2zservice.model.etower.TrackingEventResponse;
import com.d2z.d2zservice.security.HMACGenerator;
import com.d2z.d2zservice.util.D2ZCommonUtil;
import com.d2z.singleton.SingletonCounter;

@Service
public class EtowerSupplier {

	@Value("${eTower.url}")
	private String baseURL; 
	
	@Value("${jasypt.encryptor.password}")
	private String encryptionPassword;
	
	@Autowired
	private SupplierInterface supplier;

	@Autowired
	private ID2ZDao d2zDao;
	
	public List<ConsignmentDTO> createOrder(List<ConsignmentDTO> incomingRequest,SupplierEntity config) throws FailureResponseException {
		Map<String, String> systemRefNbrMap = new HashMap<String, String>();
		ResponseEntity<CreateShippingResponse> responeEntity = supplier.makeCall(HttpMethod.POST,
				baseURL + config.getSupplierCreateUri(), constructCreateOrderRequest(incomingRequest, systemRefNbrMap),
				constructHeader(config, baseURL+config.getSupplierCreateUri(),HttpMethod.POST), CreateShippingResponse.class);
		CreateShippingResponse etowerResponse = responeEntity.getBody();
		
		List<String> gainLabelTrackingNo = logEtowerCreateResponse(etowerResponse, incomingRequest);
				processCreateOrderResponse(etowerResponse, incomingRequest, systemRefNbrMap);

		if(gainLabelTrackingNo.size()>0) {
			GainLabelsResponse gainLabelResponse = makeCallToGainLabels(gainLabelTrackingNo,config);
			logGainLabelsResponse(gainLabelResponse);
			processGainLabelsResponse(gainLabelResponse, incomingRequest,systemRefNbrMap);
		}
		return incomingRequest;
	}
	
	private void logGainLabelsResponse(GainLabelsResponse response) {
		List<ETowerResponse> responseEntity = new ArrayList<ETowerResponse>();
		if (response != null) {
			List<LabelData> responseData = response.getData();
			if (responseData == null && null != response.getErrors()) {
				for (EtowerErrorResponse error : response.getErrors()) {
					ETowerResponse errorResponse = new ETowerResponse();
					errorResponse.setAPIName("Gain Labels");
					errorResponse.setStatus(response.getStatus());
					errorResponse.setErrorCode(error.getCode());
					errorResponse.setErrorMessage(error.getMessage());
					responseEntity.add(errorResponse);
				}
			}
			else {
			for (LabelData data : responseData) {
				List<EtowerErrorResponse> errors = data.getErrors();
				if (null == errors) {
					ETowerResponse errorResponse = new ETowerResponse();
					errorResponse.setAPIName("Gain Labels");
					errorResponse.setStatus(data.getStatus());
					errorResponse.setOrderId(data.getOrderId());
					errorResponse.setReferenceNumber(data.getReferenceNo());
					errorResponse.setTrackingNo(data.getTrackingNo());
					errorResponse.setTimestamp(Timestamp.valueOf(D2ZCommonUtil.getAETCurrentTimestamp()));
					responseEntity.add(errorResponse);
				} else {
					for (EtowerErrorResponse error : errors) {
						ETowerResponse errorResponse = new ETowerResponse();
						errorResponse.setAPIName("Gain Labels");
						errorResponse.setStatus(response.getStatus());
						errorResponse.setStatus(data.getStatus());
						errorResponse.setOrderId(data.getOrderId());
						errorResponse.setReferenceNumber(data.getReferenceNo());
						errorResponse.setTrackingNo(data.getTrackingNo());
						errorResponse.setTimestamp(Timestamp.valueOf(D2ZCommonUtil.getAETCurrentTimestamp()));
						errorResponse.setErrorCode(error.getCode());
						errorResponse.setErrorMessage(error.getMessage());
						responseEntity.add(errorResponse);
					}
				}
			}
			}
		}
		d2zDao.logEtowerResponse(responseEntity);
	}

	private void processGainLabelsResponse(GainLabelsResponse response, List<ConsignmentDTO> incomingRequest,
			Map<String, String> systemRefNbrMap) {

		incomingRequest.stream().forEach(obj -> {
			LabelData resp = response.getData().stream()
					.filter(data -> systemRefNbrMap.get(data.getReferenceNo()).equals(obj.getReferenceNumber()))
					.findFirst().orElse(null);
			if (resp != null) {
			obj.setBarcodelabelNumber(resp.getBarCode());
			obj.setDatamatrix(D2ZCommonUtil.formatDataMatrix(resp.getBarCode2D().replaceAll("\\(|\\)|\u001d", "")));
			}
		});
			
	}

	private void processCreateOrderResponse(CreateShippingResponse response, List<ConsignmentDTO> incomingRequest, 
			Map<String, String> systemRefNbrMap) {
		incomingRequest.stream().forEach(obj -> {
			ResponseData resp = response.getData().stream()
					.filter(data -> systemRefNbrMap.get(data.getReferenceNo()).equals(obj.getReferenceNumber()))
					.findFirst().orElse(null);
			if (resp != null) {
				obj.setBarcodelabelNumber(resp.getTrackingNo());
				obj.setArticleId(resp.getTrackingNo());
				obj.setMlid(obj.getArticleId().substring(0, 5));
				obj.setInjectionState("SYD");
				obj.setCarrier("eParcel");
			}
		});
	}

	private GainLabelsResponse makeCallToGainLabels(List<String> gainLabelTrackingNo,SupplierEntity config) {
		ResponseEntity<GainLabelsResponse> responeEntity = supplier.makeCall(HttpMethod.POST,
				baseURL + "services/shipper/labelSpecs/", gainLabelTrackingNo,
				constructHeader(config, baseURL+"services/shipper/labelSpecs/",HttpMethod.POST), GainLabelsResponse.class);
		return responeEntity.getBody();
	}

	private List<String> logEtowerCreateResponse(CreateShippingResponse response, List<ConsignmentDTO> incomingRequest)
			throws FailureResponseException {

		List<String> gainLabelTrackingNo = new ArrayList<String>();

		List<ETowerResponse> responseEntity = new ArrayList<ETowerResponse>();

		if (response == null) {
			throw new FailureResponseException("Failed. Please contact us");
		} else {
			if (null != response.getData()) {
				for (ResponseData data : response.getData()) {
					List<EtowerErrorResponse> errors = data.getErrors();
					if (null == errors) {
						ETowerResponse errorResponse = new ETowerResponse();
						errorResponse.setAPIName("Create Shipping Order");
						errorResponse.setStatus(data.getStatus());
						errorResponse.setOrderId(data.getOrderId());
						errorResponse.setReferenceNumber(data.getReferenceNo());
						errorResponse.setTrackingNo(data.getTrackingNo());
						errorResponse.setTimestamp(Timestamp.valueOf(D2ZCommonUtil.getAETCurrentTimestamp()));
						responseEntity.add(errorResponse);
						gainLabelTrackingNo.add(data.getTrackingNo());
					} else {
						for (EtowerErrorResponse error : errors) {
							ETowerResponse errorResponse = new ETowerResponse();
							errorResponse.setAPIName("Create Shipping Order");
							errorResponse.setStatus(response.getStatus());
							errorResponse.setStatus(data.getStatus());
							errorResponse.setOrderId(data.getOrderId());
							errorResponse.setReferenceNumber(data.getReferenceNo());
							errorResponse.setTrackingNo(data.getTrackingNo());
							errorResponse.setTimestamp(Timestamp.valueOf(D2ZCommonUtil.getAETCurrentTimestamp()));
							errorResponse.setErrorCode(error.getCode());
							errorResponse.setErrorMessage(error.getMessage());
							responseEntity.add(errorResponse);
						}
						throw new FailureResponseException("Failed. Please contact us");
					}

				}
				d2zDao.logEtowerResponse(responseEntity);
			}

		}

		return gainLabelTrackingNo;

	}

	private HttpHeaders constructHeader(SupplierEntity config, String uri,HttpMethod verb) {

		StringEncryptor stringEncryptor = D2ZCommonUtil.stringEncryptor(encryptionPassword);
		System.out.println("Decrypted Key :"+stringEncryptor.decrypt(config.getSupplierKey()));
		System.out.println("Decrypted Token :"+stringEncryptor.decrypt(config.getSupplierToken()));
		 String authorizationHeader = "WallTech "+stringEncryptor.decrypt(config.getSupplierToken())+":" + HMACGenerator.calculateHMAC(stringEncryptor.decrypt(config.getSupplierKey()), uri, verb.toString());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("X-WallTech-Date", D2ZCommonUtil.formatDate());
        headers.add("Authorization", authorizationHeader);
		return headers;
	
	}

	private String constructCreateOrderRequest(List<ConsignmentDTO> incomingRequest,
			Map<String, String> systemRefNbrMap) {

		List<com.d2z.d2zservice.model.etower.CreateShippingRequest> eTowerRequest = new ArrayList<com.d2z.d2zservice.model.etower.CreateShippingRequest>();

		incomingRequest.forEach(orderDetail -> {
			com.d2z.d2zservice.model.etower.CreateShippingRequest request = new com.d2z.d2zservice.model.etower.CreateShippingRequest();
			int uniqueNumber = SingletonCounter.getInstance().getEtowerCount();
			request.setReferenceNo("SW11A" + uniqueNumber);
			systemRefNbrMap.put(request.getReferenceNo(), orderDetail.getReferenceNumber());
			request.setRecipientCompany(orderDetail.getConsigneeCompany());
			String recpName = orderDetail.getConsigneeName().length() > 34
					? orderDetail.getConsigneeName().substring(0, 34)
					: orderDetail.getConsigneeName();
			recpName = recpName.replaceAll("[^:a-zA-Z0-9\\s+]", "");
			request.setRecipientName(recpName);
			String address = (orderDetail.getConsigneeAddr1()).replaceAll("[^:a-zA-Z0-9\\s+]", "");
			address = address.length() > 39 ? address.substring(0, 39) : address;
			request.setAddressLine1(address);
			if (null != orderDetail.getConsigneeAddr2()) {
				String address2 = (orderDetail.getConsigneeAddr2()).replaceAll("[^:a-zA-Z0-9\\s+]", "");
				address2 = address2.length() > 60 ? address2.substring(0, 60) : address2;
				request.setAddressLine2(address2);
			}
			if (null != orderDetail.getProductDescription()) {
				String description = (orderDetail.getProductDescription()).replaceAll("[^a-zA-Z0-9\\s+]", "");
				description = description.length() > 50 ? description.substring(0, 50) : description;
				request.setDescription(description);
			}
			request.setPhone(orderDetail.getConsigneePhone());
			request.setEmail(orderDetail.getConsigneeEmail());
			request.setCity(orderDetail.getConsigneeSuburb());
			request.setState(orderDetail.getConsigneeState());
			request.setPostcode(orderDetail.getConsigneePostcode());
			request.setReturnOption("Return");
			request.setVendorid(orderDetail.getVendorId());
			request.setAuthorityToleave(false);
			Double weight = Double.valueOf(orderDetail.getWeight());
			request.setFacility("SYD2");
			orderDetail.setInjectionType("SYD2");
			request.setServiceOption("E-Parcel");
			request.setWeight(weight);
			request.setInvoiceValue(orderDetail.getValue());
			request.getOrderItems().get(0).setUnitValue(orderDetail.getValue());
			eTowerRequest.add(request);

		});
		return D2ZCommonUtil.convertToJsonString(eTowerRequest);

	}

	public byte[] printLabel(List<String> ids,SupplierEntity config) {
		ResponseEntity<byte[]> response = supplier.makeCall(HttpMethod.POST,
				baseURL + config.getSupplierLabelUri(), D2ZCommonUtil.convertToJsonString(ids),
				constructHeader(config, baseURL+config.getSupplierLabelUri(),HttpMethod.POST), byte[].class);
		return response.getBody();
	}

	public TrackingEventResponse makeTrackingCall(List<String> articleIds, SupplierEntity config) {
		ResponseEntity<TrackingEventResponse> responeEntity = supplier.makeCall(HttpMethod.POST,
				baseURL + config.getSupplierTrackingUri(), articleIds,
				constructHeader(config, baseURL+config.getSupplierTrackingUri(),HttpMethod.POST), TrackingEventResponse.class);
		return responeEntity.getBody();
	}
}
