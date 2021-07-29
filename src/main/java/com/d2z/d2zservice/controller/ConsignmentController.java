package com.d2z.d2zservice.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.d2z.d2zservice.exception.FailureResponseException;
import com.d2z.d2zservice.exception.InvalidUserException;
import com.d2z.d2zservice.exception.MaxSizeCountException;
import com.d2z.d2zservice.exception.PCAlabelException;
import com.d2z.d2zservice.model.CreateConsignmentRequest;
import com.d2z.d2zservice.model.CreateConsignmentResponse;
import com.d2z.d2zservice.model.ErrorDetails;
import com.d2z.d2zservice.model.TrackParcelResponse;
import com.d2z.d2zservice.service.IConsignmentService;
import com.d2z.d2zservice.util.ValidationUtils;

@RestController
@RequestMapping(value = "/v3/d2z/api")
public class ConsignmentController {
	
	@Autowired
	private  IConsignmentService service;

	@RequestMapping(method = RequestMethod.POST, path = "/consignments-create")
	public ResponseEntity<Object> createConsignments(@Valid @RequestBody CreateConsignmentRequest orderDetail,Errors errors) throws FailureResponseException {
		CreateConsignmentResponse response = new CreateConsignmentResponse();
		HttpStatus status = null;
		Map<String,List<ErrorDetails>> errorMap = new HashMap<String,List<ErrorDetails>>();
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
			ResponseEntity<Object> responseEntity = service.createConsignments(orderDetail,errorMap);
			return responseEntity;
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
	}
	@RequestMapping( method = RequestMethod.POST, path = "/label/{identifier}")
    public ResponseEntity<byte[]> trackingLabel(@RequestBody String ids,@PathVariable String identifier) {
    	List<String> refBarNumArray =
    			  Stream.of(ids.split(","))
    			  .collect(Collectors.toList());
		byte[] bytes = service.generateLabel(refBarNumArray,identifier);
	    return ResponseEntity
	      .ok()
	      .header("Content-Type", "application/pdf; charset=UTF-8")
	      .header("Content-Disposition", "inline; filename=\"Label.pdf\"")
	      .body(bytes);
	}
    
    @RequestMapping( method = RequestMethod.POST, path = "/trackParcels/{identifier}")
    public List<TrackParcelResponse> trackParcels(@RequestBody List<String> articleIds,@PathVariable String identifier) {
		List<TrackParcelResponse> trackParcelResponse = new ArrayList<TrackParcelResponse>();
		trackParcelResponse = service.trackParcels(articleIds,identifier);
		return trackParcelResponse;
    }
}
