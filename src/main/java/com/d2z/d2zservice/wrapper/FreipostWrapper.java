package com.d2z.d2zservice.wrapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.datacontract.schemas._2004._07.lciservicedatacontract.ArrayOfTrackingItemUpload;
import org.datacontract.schemas._2004._07.lciservicedatacontract.Consumer;
import org.datacontract.schemas._2004._07.lciservicedatacontract.Credentials;
import org.datacontract.schemas._2004._07.lciservicedatacontract.GetTrackingDetailsRequest;
import org.datacontract.schemas._2004._07.lciservicedatacontract.ObjectFactory;
import org.datacontract.schemas._2004._07.lciservicedatacontract.TrackingItemUpload;
import org.datacontract.schemas._2004._07.lciservicedatacontract.UploadManifestRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import org.tempuri.GetTrackingDetails;
import org.tempuri.GetTrackingDetailsResponse;
import org.tempuri.UploadManifest;
import org.tempuri.UploadManifestResponse;

import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.soapConfig.FreiPostSoapConfig;
import com.d2z.d2zservice.soapConnector.FreiPostConnector;

@Service
public class FreipostWrapper {

	@Autowired 
	private FreiPostConnector freipostConnector;
	 public UploadManifestResponse uploadManifestService(List<SenderdataMaster> senderMasterData) {
		/*AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
	        ctx.register(FreiPostSoapConfig.class);
	        ctx.refresh();
	      FreiPostConnector connector = ctx.getBean(FreiPostConnector.class);
	       */
		 
		 	ObjectFactory factory = new ObjectFactory();
	    	org.tempuri.ObjectFactory objFactory = new org.tempuri.ObjectFactory();
	    	
	    	Credentials credentials = factory.createCredentials();
	    	credentials.setUsername(factory.createCredentialsUsername("admin@d2z"));
	    	credentials.setPassword(factory.createCredentialsPassword("admin"));
	    	
	    	UploadManifest uploadManifest = objFactory.createUploadManifest();
	    	UploadManifestRequest uploadManifestRequest = new UploadManifestRequest();
	    	uploadManifestRequest.setAllocateTrackingNumber(true);
	    	uploadManifestRequest.setCredentials(factory.createUploadManifestRequestCredentials(credentials));
	    	ArrayOfTrackingItemUpload arrayofTrackingItemUpload = factory.createArrayOfTrackingItemUpload();
	    	 List<TrackingItemUpload> trackingItemUpload = new ArrayList<TrackingItemUpload>();
	    	 
	    	 
	    	 for(SenderdataMaster senderData : senderMasterData) {
	    		 TrackingItemUpload trackingItems = new TrackingItemUpload();
		    	 trackingItems.setABNARNNumber(factory.createTrackingItemUploadABNARNNumber("0"));
		    	 trackingItems.setBagName(factory.createTrackingItemUploadBagName("1"));
		    	 
		    	 Consumer consumer = new Consumer();
		    	 consumer.setAddress(factory.createConsumerAddress(senderData.getConsignee_addr1()));
		    	 consumer.setCountry(factory.createConsumerCountry("AUSTRALIA"));
		    	 consumer.setEmail(factory.createConsumerEmail("becattack@hotmail.com"));
		    	 consumer.setID(UUID.randomUUID().toString());
		    	 consumer.setName(factory.createConsumerName(senderData.getConsignee_name()));
		    	 consumer.setPostcode(factory.createConsumerPostcode(senderData.getConsignee_Postcode()));
		    	 consumer.setState(factory.createConsumerState(senderData.getConsignee_State()));
		    	 consumer.setSuburb(factory.createConsumerSuburb(senderData.getConsignee_Suburb()));
		    	 consumer.setTelephone(factory.createConsumerTelephone(senderData.getConsignee_Phone()));
		    	 trackingItems.setConsumer(factory.createConsumer(consumer));
		    	 
		    	 trackingItems.setCostFreight(factory.createTrackingItemUploadCostFreight(BigDecimal.valueOf(senderData.getValue())));
		    	 trackingItems.setCurrency(factory.createTrackingItemUploadCurrency(senderData.getCurrency()));
		    	 trackingItems.setDescription(factory.createTrackingItemUploadDescription(senderData.getProduct_Description()));
		    	 trackingItems.setInvoiceNumber(factory.createTrackingItemUploadInvoiceNumber(senderData.getReference_number()));
		    	 trackingItems.setInvoiceValue(BigDecimal.valueOf(senderData.getValue()));
		    	 trackingItems.setOriginCountry(factory.createTrackingItemUploadOriginCountry(getOriginCountry(senderData.getConsignee_State())));
		    	 trackingItems.setServiceType(factory.createTrackingItemUploadServiceType("APST"));
		    	 trackingItems.setTrackingNumber(factory.createTrackingItemUploadTrackingNumber(senderData.getBarcodelabelNumber()));
		    	 trackingItems.setWeight(senderData.getCubic_Weight());
		    	 trackingItemUpload.add(trackingItems);
	    	 }
	    	 arrayofTrackingItemUpload.getTrackingItemUpload().addAll(trackingItemUpload);
	    	 uploadManifestRequest.setManifest(factory.createUploadManifestRequestManifest(arrayofTrackingItemUpload));
	    	 uploadManifest.setRequest(objFactory.createUploadManifestRequest(uploadManifestRequest));
	        UploadManifestResponse response = freipostConnector.uploadManifestService(uploadManifest);
		return response;
		 
	        
	 }
	 
	 private String getOriginCountry(String consignee_State) {
		 if(consignee_State.equalsIgnoreCase("NSW") || consignee_State.equalsIgnoreCase("NSW") || consignee_State.equalsIgnoreCase("NSW")) {
			 return "O_SYD";
		 }else if(consignee_State.equalsIgnoreCase("VIC") || consignee_State.equalsIgnoreCase("SA") || consignee_State.equalsIgnoreCase("TAS")) {
			 return "O_MEL";
		 }else if (consignee_State.equalsIgnoreCase("QLD")){
			 return "O_BNE";
		 }else if (consignee_State.equalsIgnoreCase("WA")){
			 return "O_PER";
		 }else {
			 return "";
		 }
	}

	public GetTrackingDetailsResponse trackingEventService(String invoiceNumber) {
		 /*AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
	        ctx.register(FreiPostSoapConfig.class);
	        ctx.refresh();
	        FreiPostConnector connector = ctx.getBean(FreiPostConnector.class);
	        */
	    	ObjectFactory factory = new ObjectFactory();
	    	org.tempuri.ObjectFactory objFactory = new org.tempuri.ObjectFactory();
	    	Credentials credentials = factory.createCredentials();
	    	credentials.setUsername(factory.createCredentialsUsername("admin@d2z"));
	    	credentials.setPassword(factory.createCredentialsPassword("admin"));
	    	
	    	GetTrackingDetails trackingDetails = objFactory.createGetTrackingDetails();
	    	GetTrackingDetailsRequest trackingRequest = new GetTrackingDetailsRequest();
	    	trackingRequest.setInvoiceNumber(factory.createGetTrackingDetailsRequestInvoiceNumber(invoiceNumber));
	    	trackingRequest.setCredentials(factory.createGetTrackingDetailsRequestCredentials(credentials));
	    	trackingRequest.setTrackingNumber(factory.createGetTrackingDetailsRequestTrackingNumber(""));
	    	trackingDetails.setRequest(objFactory.createGetTrackingDetailsRequest(trackingRequest));
	    	
	        GetTrackingDetailsResponse response = freipostConnector.trackingEventService(trackingDetails);
		return response;
	        
		 
	 }
}
