package com.d2z.d2zservice.wrapper;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.d2z.d2zservice.dao.ID2ZDao;
import com.d2z.d2zservice.entity.ETowerResponse;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.model.etower.CreateShippingRequest;
import com.d2z.d2zservice.model.etower.CreateShippingResponse;
import com.d2z.d2zservice.model.etower.EtowerErrorResponse;
import com.d2z.d2zservice.model.etower.GainLabelsResponse;
import com.d2z.d2zservice.model.etower.LabelData;
import com.d2z.d2zservice.model.etower.ResponseData;
import com.d2z.d2zservice.model.etower.TrackingEventResponse;
import com.d2z.d2zservice.proxy.ETowerProxy;

@Service
public class ETowerWrapper {

	@Autowired
    private ID2ZDao d2zDao;
	
	@Autowired
	private ETowerProxy proxy;
	
	public void makeCallToCreateShippingOrder(List<SenderdataMaster> orderDetailsList) {
		CreateShippingResponse response = proxy.makeCallForCreateShippingOrder(populateETowerRequest(orderDetailsList));
		List<String> trackingNbrs = new ArrayList<String>();
		List<ETowerResponse> responseEntity = new ArrayList<ETowerResponse>();
		if(response!=null) {
			List<ResponseData> responseData = response.getData();
			if(null!=responseData) {
			for(ResponseData data : responseData) {
			 	ETowerResponse errorResponse  = new ETowerResponse();
			 	errorResponse.setAPIName("Create Shipping Order");
			 	errorResponse.setStatus(data.getStatus());
			 	errorResponse.setOrderId(data.getOrderId());
		 		errorResponse.setReferenceNumber(data.getReferenceNo());
		 		errorResponse.setTrackingNo(data.getTrackingNo());
		 		trackingNbrs.add(data.getTrackingNo());
		 		errorResponse.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
				List<EtowerErrorResponse> errors = data.getErrors();
				 for(EtowerErrorResponse error : errors) {
				 		errorResponse.setErrorCode(error.getCode());
				 		errorResponse.setErrorMessage(error.getMessage());
				 		responseEntity.add(errorResponse);
				}
			}
			}
			
				d2zDao.logEtowerResponse(responseEntity);
			
		}
		
		
		
	}

	private Map<String,LabelData> parseETowerResponse(GainLabelsResponse response) {
		Map<String,LabelData> responseMap = null;
		List<ETowerResponse> responseEntity = new ArrayList<ETowerResponse>();
		if(response!=null) {
			responseMap = new HashMap<String,LabelData>();
			for(LabelData data : response.getData()) {
				if(null == data.getErrors()) {
				responseMap.put(data.getReferenceNo(), data);
				}
				else {
					 	List<EtowerErrorResponse> errors = data.getErrors();
					 	for(EtowerErrorResponse error : errors) {
						 	ETowerResponse errorResponse  = new ETowerResponse();
					 		errorResponse.setAPIName("Gain Labels");
					 		errorResponse.setOrderId(data.getOrderId());
					 		errorResponse.setReferenceNumber(data.getReferenceNo());
					 		errorResponse.setTrackingNo(data.getTrackingNo());
					 		errorResponse.setErrorCode(error.getCode());
					 		errorResponse.setErrorMessage(error.getMessage());
					 		errorResponse.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
					 		responseEntity.add(errorResponse);
					 	}
					}
				}
			if(responseEntity.size()>0) {
				d2zDao.logEtowerResponse(responseEntity);
			}
			}
		
		return responseMap;
	}

	private List<CreateShippingRequest> populateETowerRequest(List<SenderdataMaster> orderDetailsList) {
		List<CreateShippingRequest> eTowerRequest = new ArrayList<CreateShippingRequest>();

		for(SenderdataMaster orderDetail : orderDetailsList) {
			CreateShippingRequest request = new CreateShippingRequest();
			request.setTrackingNo(orderDetail.getArticleId());
			request.setReferenceNo("SW10"+orderDetail.getReference_number());
			request.setRecipientCompany(orderDetail.getConsigneeCompany());
			request.setRecipientName(orderDetail.getConsignee_name());
			request.setAddressLine1(orderDetail.getConsignee_addr1());
			request.setAddressLine2(orderDetail.getConsignee_addr2());
			request.setEmail(orderDetail.getConsignee_Email());
			request.setCity(orderDetail.getConsignee_Suburb());
			request.setState(orderDetail.getConsignee_State());
			request.setPostcode(orderDetail.getConsignee_Postcode());
			if(orderDetail.getCarrier().equalsIgnoreCase("Express")){
			request.setServiceOption("Express-Post");
			}
			else {
				request.setServiceOption("E-Parcel");

			}
			request.setFacility(orderDetail.getInjectionState());
			request.setWeight(Double.valueOf(orderDetail.getWeight()));
			request.setInvoiceValue(orderDetail.getValue());
			eTowerRequest.add(request);
		}
		
		return eTowerRequest;
	}

	public void foreCast(List<String> trackingNbrs) {
		CreateShippingResponse response = proxy.makeCallForForeCast(trackingNbrs);
		List<ETowerResponse> responseEntity = new ArrayList<ETowerResponse>();

		if(response!=null) {
			List<ResponseData> responseData = response.getData();
			if(null!=responseData) {
			for(ResponseData data : responseData) {
			 	ETowerResponse errorResponse  = new ETowerResponse();
			 	errorResponse.setAPIName("Forecast");
			 	errorResponse.setStatus(data.getStatus());
			 	errorResponse.setOrderId(data.getOrderId());
		 		errorResponse.setReferenceNumber(data.getReferenceNo());
		 		errorResponse.setTrackingNo(data.getTrackingNo());
		 		errorResponse.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
				List<EtowerErrorResponse> errors = data.getErrors();
				 for(EtowerErrorResponse error : errors) {
				 		errorResponse.setErrorCode(error.getCode());
				 		errorResponse.setErrorMessage(error.getMessage());
				 		responseEntity.add(errorResponse);
				}
			}
			}
			
				d2zDao.logEtowerResponse(responseEntity);
	}
		TrackingEventResponse trackEventresponse = proxy.makeCallForTrackingEvents(trackingNbrs);	
		d2zDao.insertTrackingDetails(trackEventresponse);
}
}
