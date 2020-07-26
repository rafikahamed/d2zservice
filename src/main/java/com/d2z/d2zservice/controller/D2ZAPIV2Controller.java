package com.d2z.d2zservice.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.d2z.d2zservice.exception.FailureResponseException;
import com.d2z.d2zservice.exception.InvalidUserException;
import com.d2z.d2zservice.exception.MaxSizeCountException;
import com.d2z.d2zservice.exception.ReferenceNumberNotUniqueException;
import com.d2z.d2zservice.model.CreateConsignmentRequest;
import com.d2z.d2zservice.model.SenderDataResponse;
import com.d2z.d2zservice.service.ID2ZAPIService;
import com.d2z.d2zservice.service.ID2ZService;
import com.d2z.d2zservice.util.ValidationUtils;
import com.d2z.d2zservice.model.CreateConsignmentResponse;
import com.d2z.d2zservice.model.ErrorDetails;

@RestController
@RequestMapping(value = "/v2/d2z/api")
public class D2ZAPIV2Controller {
Logger logger = LoggerFactory.getLogger(D2ZAPIV2Controller.class);

@Autowired
private  ID2ZAPIService d2zApiService;

@Autowired
private  ID2ZService d2zService;

@RequestMapping(method = RequestMethod.POST, path = "/consignments-create")
public ResponseEntity<Object> createConsignments(@Valid @RequestBody CreateConsignmentRequest orderDetail,Errors errors) throws FailureResponseException {
	List<SenderDataResponse> responseList = new ArrayList<SenderDataResponse>();
	CreateConsignmentResponse response = new CreateConsignmentResponse();
	Map<String,List<ErrorDetails>> errorMap = new HashMap<String,List<ErrorDetails>>();
	HttpStatus status = null;
	if(errors.hasErrors()) {
		if(null!=errors.getFieldError("userName")) {
			FieldError error = errors.getFieldError("userName");
			response.setStatus("Failure");
			ErrorDetails errorDetails = new ErrorDetails();
			errorDetails.setValue(String.valueOf(error.getRejectedValue()));
			errorDetails.setErrorMessage(error.getDefaultMessage());
			response.setErrors(errorDetails);
			return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		}
		    errorMap =  ValidationUtils.populateErrorDetails(errors) ;
			
		    
	}
	
		try {
			List<String> autoShipRefNbrs = new ArrayList<String>();

		d2zApiService.createConsignments(orderDetail,responseList,errorMap,autoShipRefNbrs);
		List<String> invalidData = new ArrayList<String>();
		if(errorMap.size() > 0) {

		    for(String refNbr : errorMap.keySet()) {
		    	invalidData.add(refNbr);
		    	SenderDataResponse responseData = new SenderDataResponse();
		    	responseData.setReferenceNumber(refNbr);
		    	responseData.setErrorDetails(errorMap.get(refNbr));
				responseList.add(responseData);
			}
		}
		List<String> incomingRefNbr = responseList.stream()
    			.filter(obj -> null!=obj.getBarcodeLabelNumber())
    			.map(SenderDataResponse :: getReferenceNumber)
				.collect(Collectors.toList());
    	if(null!=incomingRefNbr && incomingRefNbr.size()>0) {
    		status = HttpStatus.OK;
    		response.setStatus("Success");
    		if(null!=autoShipRefNbrs && !autoShipRefNbrs.isEmpty()) {
    			System.out.println("Auto-Shipment Allocation");
    			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    			String shipmentNumber = orderDetail.getUserName()+simpleDateFormat.format(new Date());
    			try {
					d2zService.allocateShipment(String.join(",", autoShipRefNbrs), shipmentNumber);
				} catch (ReferenceNumberNotUniqueException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}        
            	
    		}
    	d2zApiService.makeCallToEtowerBasedonSupplierUI(incomingRefNbr);
    	}else {
    		response.setStatus("Failure");
    		status = HttpStatus.BAD_REQUEST;
    	}
		}
		catch(InvalidUserException e) {
			response.setStatus("Failure");
			ErrorDetails errorDetails = new ErrorDetails();
			errorDetails.setValue(e.getUserName());
			errorDetails.setErrorMessage(e.getMessage());
			response.setErrors(errorDetails);
			return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		}
		catch(MaxSizeCountException e) {
			response.setStatus("Failure");
			ErrorDetails errorDetails = new ErrorDetails();
			errorDetails.setErrorMessage(e.getMessage());
			response.setErrors(errorDetails);
			return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		}
	    

	response.setResponseData(responseList);
	return new ResponseEntity(response, status);
}	
}
