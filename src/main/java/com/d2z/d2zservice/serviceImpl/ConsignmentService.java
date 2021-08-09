package com.d2z.d2zservice.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.d2z.d2zservice.dao.ID2ZDao;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.exception.ReferenceNumberNotUniqueException;
import com.d2z.d2zservice.model.CreateConsignmentRequest;
import com.d2z.d2zservice.model.ErrorDetails;
import com.d2z.d2zservice.model.ResponseMessage;
import com.d2z.d2zservice.model.SenderDataApi;
import com.d2z.d2zservice.model.TrackParcelResponse;
import com.d2z.d2zservice.service.ConsignmentCreator;
import com.d2z.d2zservice.service.ConsignmentLabelGenerator;
import com.d2z.d2zservice.service.ConsignmentTracker;
import com.d2z.d2zservice.service.IConsignmentService;
import com.d2z.d2zservice.service.ShipmentAllocator;
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
	ShipmentAllocator allocator;
	
	@Autowired
	ID2ZDao dao;
	
	@Override
	public ResponseEntity<Object> createConsignments(CreateConsignmentRequest orderDetail, Map<String,List<ErrorDetails>>  errorMap) {
		int intialRequestSize = orderDetail.getConsignmentData().size();
		int userId = validator.validateUser(orderDetail.getUserName());
		D2ZValidator.maxOrderValidation(orderDetail.getConsignmentData());
		validator.validationReferenceNumber(orderDetail,errorMap);
		List<String> autoShipRefNbrs = new ArrayList<String>();
		List<SenderdataMaster> savedData = new ArrayList<SenderdataMaster>();
		if(orderDetail.getConsignmentData().size()>0) {
		Map<String,List<SenderDataApi>> consignmentServiceTypeMap = validator.validateServiceType(orderDetail,errorMap);
		savedData=consignmentCreator.createConsignment(consignmentServiceTypeMap, userId, errorMap,autoShipRefNbrs);
		}
		return consignmentCreator.sendResponse(savedData,errorMap,intialRequestSize,autoShipRefNbrs,orderDetail.getUserName());
	}

	@Override
	public byte[] generateLabel(List<String> refBarNumArray,String identifier) {	
		return labelGenerator.generateLabel(refBarNumArray,identifier);		
	}
	
	
	@Override
	public List<TrackParcelResponse> trackParcels(List<String> trackingNos,String identifier) {	
		return tracker.trackParcels(trackingNos,identifier);		
	}

	@Override
	public ResponseMessage allocateShipment(List<String> ids, String identifier, String shipmentNumber) throws ReferenceNumberNotUniqueException {
		return allocator.allocateShipment(ids, identifier, shipmentNumber);
	}
	
	


}
