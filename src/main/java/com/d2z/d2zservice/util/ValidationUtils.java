package com.d2z.d2zservice.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import com.d2z.d2zservice.model.CreateConsignmentRequest;
import com.d2z.d2zservice.model.ErrorDetails;
import com.d2z.d2zservice.model.SenderDataApi;

public class ValidationUtils {

	public static Map<String,List<ErrorDetails>> populateErrorDetails(Errors errors) {

	    Map<String,List<ErrorDetails>> errorMap = new HashMap<String,List<ErrorDetails>>();
	    for (final FieldError error : errors.getFieldErrors()) {
	    	String message = error.getDefaultMessage();
	    	String[] messageArray = message.split(",");
          if(errorMap.containsKey(messageArray[0])) {
        	  List<ErrorDetails> errorList = errorMap.get(messageArray[0]);
        	  ErrorDetails errorDetails = new ErrorDetails();
        	  errorDetails.setValue(messageArray[1]);
        	  errorDetails.setErrorMessage(messageArray[2]);
        	  errorList.add(errorDetails);
          }
          else {
        	  List<ErrorDetails> errorList = new ArrayList<ErrorDetails>();
        	  ErrorDetails errorDetails = new ErrorDetails();
        	  errorDetails.setValue(messageArray[1]);
        	  errorDetails.setErrorMessage(messageArray[2]);
        	  errorList.add(errorDetails);
        	  errorMap.put(messageArray[0],errorList);
          }
        }
		return errorMap;
	}

	public static void populateErrorDetails(String referenceNumber, String value, String message,
			Map<String, List<ErrorDetails>> errorMap) {
		if(errorMap.containsKey(referenceNumber)) {
      	  List<ErrorDetails> errorList = errorMap.get(referenceNumber);
      	  ErrorDetails errorDetails = new ErrorDetails();
      	  errorDetails.setValue(value);
      	  errorDetails.setErrorMessage(message);
      	  errorList.add(errorDetails);
        }
        else {
      	  List<ErrorDetails> errorList = new ArrayList<ErrorDetails>();
      	  ErrorDetails errorDetails = new ErrorDetails();
      	 errorDetails.setValue(value);
     	  errorDetails.setErrorMessage(message);
      	  errorList.add(errorDetails);
      	  errorMap.put(referenceNumber,errorList);
        }
		
	}

	public static void removeInvalidconsignments(CreateConsignmentRequest orderDetail,
			Map<String, List<ErrorDetails>> errorMap) {
		 Set<String> incorrectRefNbrs = errorMap.keySet();
		 List<SenderDataApi> data = orderDetail.getConsignmentData();
		 data.removeIf(obj -> incorrectRefNbrs.contains(obj.getReferenceNumber()));
		 orderDetail.setConsignmentData(data);
		
	}

}
