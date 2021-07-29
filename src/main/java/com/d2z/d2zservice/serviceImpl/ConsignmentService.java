package com.d2z.d2zservice.serviceImpl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.d2z.d2zservice.dao.ID2ZDao;
import com.d2z.d2zservice.model.CreateConsignmentRequest;
import com.d2z.d2zservice.model.ErrorDetails;
import com.d2z.d2zservice.model.SenderDataApi;
import com.d2z.d2zservice.model.TrackParcelResponse;
import com.d2z.d2zservice.service.ConsignmentCreator;
import com.d2z.d2zservice.service.ConsignmentLabelGenerator;
import com.d2z.d2zservice.service.ConsignmentTracker;
import com.d2z.d2zservice.service.IConsignmentService;
import com.d2z.d2zservice.validation.D2ZValidator;
@Service
public class ConsignmentService  implements IConsignmentService{

	@Autowired
	D2ZValidator validator;
	
	@Autowired 
	ConsignmentCreator consignmentCreator;
	
	@Autowired
	ConsignmentLabelGenerator labelGenerator;
	
	@Autowired
	ConsignmentTracker tracker;
	
	@Autowired
	ID2ZDao dao;
	
	@Override
	public ResponseEntity<Object> createConsignments(CreateConsignmentRequest orderDetail, Map<String,List<ErrorDetails>>  errorMap) {
		int intialRequestSize = orderDetail.getConsignmentData().size();
		int userId = validator.validateUser(orderDetail.getUserName());
		D2ZValidator.maxOrderValidation(orderDetail.getConsignmentData());
		validator.validationReferenceNumber(orderDetail,errorMap);
		if(orderDetail.getConsignmentData().size()>0) {
		Map<String,List<SenderDataApi>> consignmentServiceTypeMap = validator.validateServiceType(orderDetail,errorMap);
		consignmentCreator.createConsignment(consignmentServiceTypeMap, userId, errorMap);
		}
		return consignmentCreator.sendResponse(orderDetail.getConsignmentData(),errorMap,intialRequestSize);
	}

	@Override
	public byte[] generateLabel(List<String> refBarNumArray,String identifier) {	
		return labelGenerator.generateLabel(refBarNumArray,identifier);		
	}
	
	
	@Override
	public List<TrackParcelResponse> trackParcels(List<String> trackingNos,String identifier) {	
		return tracker.trackParcels(trackingNos,identifier);		
	}
	
	


}
