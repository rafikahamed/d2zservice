package com.d2z.d2zservice.exception;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.d2z.d2zservice.model.ErrorResponse;

@ControllerAdvice
@RestController
public class CustomizedExceptionHandler extends ResponseEntityExceptionHandler {
	 @Override
	  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
	      HttpHeaders headers, HttpStatus status, WebRequest request) {
		    List<String> errors = new ArrayList<String>();
		    for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
	           // errors.add(error.getField() + ": " + error.getDefaultMessage());
		    	 errors.add(error.getDefaultMessage());
	        }
		    ErrorResponse errorDetails = new ErrorResponse(HttpStatus.BAD_REQUEST, "Validation Failed",
	        errors);
	    return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
	  } 
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
	 @ExceptionHandler(MaxSizeCountException.class)
	  public final ResponseEntity<Object> handleMaxSizeCountException(MaxSizeCountException ex, WebRequest request) {
		 List<String> error = new ArrayList<String>();
		 ErrorResponse errorResponse = 
			      new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), error);
			    return new ResponseEntity<Object>(
			    		errorResponse, new HttpHeaders(), errorResponse.getStatus());
	  }
	 @ExceptionHandler(InvalidUserException.class)
	  public final ResponseEntity<Object> handleInvalidUserException(InvalidUserException ex, WebRequest request) {
		 List<String> error = new ArrayList<String>();
		 error.add(ex.getUserName());
		 ErrorResponse errorResponse = 
			      new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), error);
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
	 @ExceptionHandler(InvalidDateException.class)
	  public final ResponseEntity<Object> handleInvalidDateException(InvalidDateException ex, WebRequest request) {
		 ErrorResponse errorResponse = 
			      new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
			    return new ResponseEntity<Object>(
			    		errorResponse, new HttpHeaders(), errorResponse.getStatus());
	  }
}

