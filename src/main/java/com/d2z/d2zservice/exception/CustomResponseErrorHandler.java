package com.d2z.d2zservice.exception;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class CustomResponseErrorHandler implements ResponseErrorHandler {
	
	Logger logger = LoggerFactory.getLogger(CustomResponseErrorHandler.class);

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		HttpStatus status = response.getStatusCode();
		System.out.println("Http Status: "+status);
        return status.is4xxClientError() || status.is5xxServerError();
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		ObjectWriter ow = new ObjectMapper().writer();
		String jsonResponse = null;
		try {
			jsonResponse = ow.writeValueAsString(response.getBody());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		System.out.println("Http Status"+response.getStatusCode());
		System.out.println("Response"+jsonResponse);
        throw new FailureException("Shipment Error. Please contact us");		
	}

	static class FailureException extends IOException {
        public FailureException(String message) {
            super(message);
        }
    }
}
