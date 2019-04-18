package com.d2z.d2zservice.wrapper;

import org.datacontract.schemas._2004._07.lciservicedatacontract.UploadManifestRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import org.tempuri.GetTrackingDetailsResponse;
import org.tempuri.UploadManifestResponse;

import com.d2z.d2zservice.soapConfig.FreiPostSoapConfig;
import com.d2z.d2zservice.soapConnector.FreiPostConnector;

@Service
public class FreipostWrapper {

	@Autowired 
	private FreiPostConnector freipostConnector;
	 public UploadManifestResponse uploadManifestService() {
		  AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
	        ctx.register(FreiPostSoapConfig.class);
	        ctx.refresh();
	      FreiPostConnector connector = ctx.getBean(FreiPostConnector.class);
	        UploadManifestRequest uploadManifestRequest = new UploadManifestRequest();
	        UploadManifestResponse response = connector.uploadManifestService(uploadManifestRequest);
		return response;
		 
	        
	 }
	 
	 public GetTrackingDetailsResponse trackingEventService() {
		  AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
	        ctx.register(FreiPostSoapConfig.class);
	        ctx.refresh();
	        FreiPostConnector connector = ctx.getBean(FreiPostConnector.class);
	        GetTrackingDetailsResponse response = freipostConnector.trackingEventService("4536");
		return response;
	        
		 
	 }
}
