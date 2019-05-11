package com.d2z.d2zservice.soapConnector;


import java.io.IOException;

import javax.xml.bind.JAXBElement;

import org.apache.commons.lang.ArrayUtils;
import org.datacontract.schemas._2004._07.lciservicedatacontract.Credentials;
import org.datacontract.schemas._2004._07.lciservicedatacontract.GetTrackingDetailsRequest;
import org.datacontract.schemas._2004._07.lciservicedatacontract.ObjectFactory;
import org.datacontract.schemas._2004._07.lciservicedatacontract.UploadManifestRequest;
import org.tempuri.UploadManifestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import org.tempuri.GetTrackingDetails;
import org.tempuri.GetTrackingDetailsResponse;
import org.tempuri.UploadManifest;

public class FreiPostConnector  extends WebServiceGatewaySupport {
 
    @SuppressWarnings("unchecked")
	public UploadManifestResponse uploadManifestService( UploadManifest uploadManifestRequest){
      	WebServiceTemplate webServiceTemplate = getWebServiceTemplate();
    	ClientInterceptor[] interceptors = webServiceTemplate.getInterceptors();
        
    	interceptors = (ClientInterceptor[]) ArrayUtils.add(interceptors, new ClientInterceptor() {
    	    @Override
    	    public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
    	        return true;
    	    }
    	 
    	    @Override
    	    public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
    	        return true;
    	    }
    	 
    	    @Override
    	    public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
    	        return true;
    	    }
    	 
    	    @Override
    	    public void afterCompletion(MessageContext messageContext, Exception ex) throws WebServiceClientException {
    	        try {
    	            System.out.println("Request :");
    	            messageContext.getRequest().writeTo(System.out);
    	            System.out.println("\nResponse : ");
    	            messageContext.getResponse().writeTo(System.out);
    	            System.out.println();
    	        } catch (IOException ignored) {
    	        }
    	    }
    	});
    	 
    	webServiceTemplate.setInterceptors(interceptors);
    	
    
    	SoapActionCallback soapAction = new SoapActionCallback("http://tempuri.org/IService/UploadManifest");
		UploadManifestResponse response = (UploadManifestResponse) webServiceTemplate.marshalSendAndReceive(uploadManifestRequest,soapAction);
    	return response;       
    }
    @SuppressWarnings("unchecked")
    public GetTrackingDetailsResponse trackingEventService(GetTrackingDetails trackingDetails){
    	
    	WebServiceTemplate webServiceTemplate = getWebServiceTemplate();
    	ClientInterceptor[] interceptors = webServiceTemplate.getInterceptors();
        
    	interceptors = (ClientInterceptor[]) ArrayUtils.add(interceptors, new ClientInterceptor() {
    	    @Override
    	    public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
    	        return true;
    	    }
    	 
    	    @Override
    	    public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
    	        return true;
    	    }
    	 
    	    @Override
    	    public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
    	        return true;
    	    }
    	 
    	    @Override
    	    public void afterCompletion(MessageContext messageContext, Exception ex) throws WebServiceClientException {
    	        try {
    	            System.out.println("Request :");
    	            messageContext.getRequest().writeTo(System.out);
    	            System.out.println("\nResponse : ");
    	            messageContext.getResponse().writeTo(System.out);
    	            System.out.println();
    	        } catch (IOException ignored) {
    	        }
    	    }
    	});
    	 
    	webServiceTemplate.setInterceptors(interceptors);
    	
    
    	SoapActionCallback soapAction = new SoapActionCallback("http://tempuri.org/IService/GetTrackingDetails");
		JAXBElement<GetTrackingDetailsResponse> response = (JAXBElement<GetTrackingDetailsResponse>)webServiceTemplate.marshalSendAndReceive(trackingDetails,soapAction);
    	return response.getValue();
    }
}