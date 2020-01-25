package com.d2z.d2zservice.async;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.d2z.d2zservice.model.PFLTrackEvent;
import com.d2z.d2zservice.model.PFLTrackingResponse;
import com.d2z.d2zservice.model.PFLTrackingResponseDetails;
import com.d2z.d2zservice.model.PflTrackEventRequest;
import com.d2z.d2zservice.model.auspost.TrackingResponse;
import com.d2z.d2zservice.model.etower.TrackingEventResponse;
import com.d2z.d2zservice.proxy.AusPostProxy;
import com.d2z.d2zservice.proxy.ETowerProxy;
import com.d2z.d2zservice.proxy.PFLProxy;
import com.d2z.d2zservice.proxy.PcaProxy;

@Service
public class AsyncService {
	
	@Autowired
	private ETowerProxy eTowerProxy;
	
	@Autowired
	private PcaProxy pcaproxy;
	
	@Autowired
	private PFLProxy pflProxy;
	
	@Autowired
	AusPostProxy ausPostProxy;
	
	private static Logger log = LoggerFactory.getLogger(AsyncService.class);
	@Async("asyncExecutor")
	public CompletableFuture<TrackingEventResponse> makeCalltoEtower(List<String> trackingNumber,String serviceType) throws InterruptedException 
    {
		log.info("Etower Tracking");
		TrackingEventResponse response = eTowerProxy.makeCallForTrackingEvents(trackingNumber,serviceType);
		return CompletableFuture.completedFuture(response);
    }
	@Async("asyncExecutor")
	public CompletableFuture<String> makeCalltoPCA(List<String> articleIDs) throws InterruptedException 
    {
		log.info("PCA Tracking");
		String response = null;
		pcaproxy.trackingEvent(articleIDs);
		return CompletableFuture.completedFuture(response);
    }
	@Async("asyncExecutor")
	public CompletableFuture<List<PFLTrackingResponseDetails>> makeCalltoPFL(List<String> articleIds) throws InterruptedException 
    {
		log.info("PFL Tracking");
		List<PFLTrackingResponseDetails> pflTrackingDetails = new ArrayList<PFLTrackingResponseDetails>();
		for(String pflValue:articleIds) {
			PflTrackEventRequest pflTrackEvent = new PflTrackEventRequest();
			PFLTrackingResponseDetails pflResp = new PFLTrackingResponseDetails();
			pflTrackEvent.setTracking_number(pflValue);
			PFLTrackingResponse pflTrackResp = pflProxy.trackingEvent(pflTrackEvent);
			if(pflTrackResp.getResult() != null) {
				pflResp.setBarcodeLabel(pflTrackEvent.getTracking_number());
				List<PFLTrackEvent> pflTrackEventList = new ArrayList<PFLTrackEvent>();
				for(PFLTrackingResponseDetails response:pflTrackResp.getResult()) {
				PFLTrackEvent event= new PFLTrackEvent(); 
				event.setStatus(response.getStatus());
				event.setStatus_code(response.getStatus_code());
				event.setDate(response.getDate());
				pflTrackEventList.add(event);
				}
				pflResp.setTrackEvent(pflTrackEventList);
				pflTrackingDetails.add(pflResp);
			}
		}
		return CompletableFuture.completedFuture(pflTrackingDetails);
    }
	@Async("asyncExecutor")
	public CompletableFuture<TrackingResponse> makeCalltoAuPost(List<String> articleIDs) throws InterruptedException 
    {
		log.info("AuPost Tracking");
		TrackingResponse response = ausPostProxy.trackingEvent(String.join(",", articleIDs));
		return CompletableFuture.completedFuture(response);
    }
	
}
