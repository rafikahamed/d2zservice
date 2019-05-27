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
    	System.out.println("Calling FreiPost - Update Manifest");
    	SoapActionCallback soapAction = new SoapActionCallback("http://tempuri.org/IService/UploadManifest");
		UploadManifestResponse response = (UploadManifestResponse) getWebServiceTemplate().marshalSendAndReceive(uploadManifestRequest,soapAction);
    	return response;       
    }
    @SuppressWarnings("unchecked")
    public GetTrackingDetailsResponse trackingEventService(GetTrackingDetails trackingDetails){
    	System.out.println("Calling FreiPost - Tracking Event");
    	SoapActionCallback soapAction = new SoapActionCallback("http://tempuri.org/IService/GetTrackingDetails");
		JAXBElement<GetTrackingDetailsResponse> response = (JAXBElement<GetTrackingDetailsResponse>)getWebServiceTemplate().marshalSendAndReceive(trackingDetails,soapAction);
    	return response.getValue();
    }
}