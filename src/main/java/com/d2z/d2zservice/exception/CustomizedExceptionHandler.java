package com.d2z.d2zservice.exception;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.d2z.d2zservice.model.ErrorResponse;

@ControllerAdvice
@RestController
public class CustomizedExceptionHandler extends ResponseEntityExceptionHandler {

	 @ExceptionHandler({ ConstraintViolationException.class })
	  protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex,
	       WebRequest request) {
		 
			    List<String> errors = new ArrayList<String>();
			    for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
			        errors.add(violation.getPropertyPath() + ": " + violation.getMessage());
			    }
			 
			    ErrorResponse errorResponse = 
			      new ErrorResponse(HttpStatus.BAD_REQUEST, "VALIDATION FAILED", errors);
			    return new ResponseEntity<Object>(
			    		errorResponse, new HttpHeaders(), errorResponse.getStatus());
	  } 
	 
	 @ExceptionHandler(InvalidSuburbPostcodeException.class)
	  public final ResponseEntity<Object> handleInvalidSuburbException(InvalidSuburbPostcodeException ex, WebRequest request) {
		 ErrorResponse errorResponse = 
			      new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getReferenceNumbers());
			    return new ResponseEntity<Object>(
			    		errorResponse, new HttpHeaders(), errorResponse.getStatus());
	  }
	 
	 @ExceptionHandler(ReferenceNumberNotUniqueException.class)
	  public final ResponseEntity<Object> handleReferenceNumberNotUniqueException(ReferenceNumberNotUniqueException ex, WebRequest request) {
		 ErrorResponse errorResponse = 
			      new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getReferenceNumbers());
			    return new ResponseEntity<Object>(
			    		errorResponse, new HttpHeaders(), errorResponse.getStatus());
	  }
	 @ExceptionHandler(InvalidServiceTypeException.class)
	  public final ResponseEntity<Object> handleInvalidServiceTypeException(InvalidServiceTypeException ex, WebRequest request) {
		 ErrorResponse errorResponse = 
			      new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getReferenceNumbers());
			    return new ResponseEntity<Object>(
			    		errorResponse, new HttpHeaders(), errorResponse.getStatus());
	  }
}

