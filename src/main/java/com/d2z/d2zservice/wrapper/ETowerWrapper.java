package com.d2z.d2zservice.wrapper;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.d2z.d2zservice.dao.ID2ZDao;
import com.d2z.d2zservice.entity.ETowerResponse;
import com.d2z.d2zservice.model.SenderData;
import com.d2z.d2zservice.model.etower.CreateShippingRequest;
import com.d2z.d2zservice.model.etower.CreateShippingResponse;
import com.d2z.d2zservice.model.etower.EtowerErrorResponse;
import com.d2z.d2zservice.model.etower.GainLabelsResponse;
import com.d2z.d2zservice.model.etower.LabelData;
import com.d2z.d2zservice.model.etower.ResponseData;
import com.d2z.d2zservice.proxy.ETowerProxy;

@Service
public class ETowerWrapper {

	@Autowired
    private ID2ZDao d2zDao;
	
	@Autowired
	private ETowerProxy proxy;
	
	public Map<String,LabelData> makeCallToCreateShippingOrder(List<SenderData> orderDetailsList) {
		GainLabelsResponse gainLabelResponse = null;
		CreateShippingResponse response = proxy.makeCallForCreateShippingOrder(populateETowerRequest(orderDetailsList));
		/*if(response.getStatus().equalsIgnoreCase("Success")) {
			List<String> referenceNumbers  = response.getData().stream().filter(obj->obj.getStatus().equalsIgnoreCase("Success")).map(obj->{
				return obj.getReferenceNo();}).collect(Collectors.toList());*
	}*/
		List<ETowerResponse> responseEntity = new ArrayList<ETowerResponse>();
		List<String> referenceNumbers = new ArrayList<String>();
		if(response!=null) {
			List<ResponseData> responseData = response.getData();
			for(ResponseData data : responseData) {
				if(data.getStatus().equalsIgnoreCase("Success")) {
					referenceNumbers.add(data.getReferenceNo());
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
			
			if(referenceNumbers.size()>0) {
				 gainLabelResponse = proxy.makeCallToGainLabels(referenceNumbers);

			}
		}
			
		
		return parseETowerResponse(gainLabelResponse);
		
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

	private List<CreateShippingRequest> populateETowerRequest(List<SenderData> orderDetailsList) {
		List<CreateShippingRequest> eTowerRequest = new ArrayList<CreateShippingRequest>();

		for(SenderData orderDetail : orderDetailsList) {
			CreateShippingRequest request = new CreateShippingRequest();
			request.setReferenceNo(orderDetail.getReferenceNumber());
			request.setRecipientCompany(orderDetail.getConsigneeCompany());
			request.setRecipientName(orderDetail.getConsigneeName());
			request.setAddressLine1(orderDetail.getConsigneeAddr1());
			request.setAddressLine2(orderDetail.getConsigneeAddr2());
			request.setCity(orderDetail.getConsigneeSuburb());
			request.setState(orderDetail.getConsigneeState());
			request.setPostcode(orderDetail.getConsigneePostcode());
			request.setServiceOption("Express-Post");
			request.setFacility(orderDetail.getInjectionState());
			request.setWeight(Double.valueOf(orderDetail.getWeight()));
			request.setInvoiceValue(orderDetail.getValue());
			eTowerRequest.add(request);
		}
		
		return eTowerRequest;
	}
}
