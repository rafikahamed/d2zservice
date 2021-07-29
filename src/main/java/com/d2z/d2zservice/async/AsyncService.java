package com.d2z.d2zservice.async;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.d2z.d2zservice.dao.ID2ZDao;
import com.d2z.d2zservice.entity.SupplierEntity;
import com.d2z.d2zservice.entity.TrackEvents;
import com.d2z.d2zservice.model.PCACreateShippingResponse;
import com.d2z.d2zservice.model.PCATrackEventResponse;
import com.d2z.d2zservice.model.PFLTrackEvent;
import com.d2z.d2zservice.model.PFLTrackingResponse;
import com.d2z.d2zservice.model.PFLTrackingResponseDetails;
import com.d2z.d2zservice.model.PflTrackEventRequest;
import com.d2z.d2zservice.model.auspost.TrackingResponse;
import com.d2z.d2zservice.model.etower.TrackingEventResponse;
import com.d2z.d2zservice.model.veloce.VeloceTrackingResponse;
import com.d2z.d2zservice.proxy.AusPostProxy;
import com.d2z.d2zservice.proxy.ETowerProxy;
import com.d2z.d2zservice.proxy.PFLProxy;
import com.d2z.d2zservice.proxy.PcaProxy;
import com.d2z.d2zservice.supplier.EtowerSupplier;
import com.d2z.d2zservice.supplier.PFLSupplier;
import com.d2z.d2zservice.supplier.VeloceSupplier;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	
	@Autowired
	PFLSupplier pflSupplier;
	
	@Autowired
	EtowerSupplier etowerSupplier;
	
	@Autowired
	VeloceSupplier veloceSupplier;
	
	
	@Autowired
	ID2ZDao d2zDao;
	
	private static Logger log = LoggerFactory.getLogger(AsyncService.class);
	@Async("asyncExecutor")
	public CompletableFuture<List<TrackingEventResponse>> makeCalltoEtower(Map<String,List<String>> eTowerMap) throws InterruptedException 
    {
		log.info("Etower Tracking");
		List<TrackingEventResponse> response = new ArrayList<TrackingEventResponse>();
		eTowerMap.forEach((k,v) ->{
			TrackingEventResponse eTowerResponse = eTowerProxy.makeCallForTrackingEvents(v,k);
			response.add(eTowerResponse);

		});
		return CompletableFuture.completedFuture(response);
    }
	@Async("asyncExecutor")
	public CompletableFuture<List<PCATrackEventResponse>> makeCalltoPCA(List<String> articleIDs) throws InterruptedException 
    {
		log.info("PCA Tracking");
		List<PCATrackEventResponse> responseList = new ArrayList<PCATrackEventResponse>();
		for(String articleID : articleIDs) {
		String response = pcaproxy.trackingEvent(articleID);
		ObjectMapper mapper = new ObjectMapper();
		PCATrackEventResponse pcaResponse = null;
		try {
			pcaResponse = mapper.readValue(response, PCATrackEventResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		responseList.add(pcaResponse);
		}
		return CompletableFuture.completedFuture(responseList);
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
				event.setLocation(response.getLocation());
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
	public CompletableFuture<List<TrackEvents>> makeCalltoDB(List<String> d2zArticleIds) {
		List<TrackEvents> trackEvents = d2zDao.fetchEventsFromTrackEvents(d2zArticleIds);
		return CompletableFuture.completedFuture(trackEvents);
	}
	
	@Async("asyncExecutor")
	public CompletableFuture<TrackingEventResponse> makeCalltoEtower(List<String> articleIds,SupplierEntity config)  
    {
		log.info("Etower Tracking");
		TrackingEventResponse response = etowerSupplier.makeTrackingCall(articleIds,config);
		return CompletableFuture.completedFuture(response);
    }
	@Async("asyncExecutor")
	public CompletableFuture<List<PFLTrackingResponseDetails>> makeCalltoPFL(List<String> articleIds,SupplierEntity config)  
    {
		log.info("PFL Tracking");
		List<PFLTrackingResponseDetails> pflTrackingDetails = pflSupplier.makeTrackingCall(articleIds,config);
		return CompletableFuture.completedFuture(pflTrackingDetails);
    }
	@Async("asyncExecutor")
	public CompletableFuture<TrackingResponse> makeCalltoAuPost(List<String> articleIDs,SupplierEntity config)
    {
		log.info("AuPost Tracking");
		TrackingResponse response = ausPostProxy.trackingEvent(String.join(",", articleIDs));
		return CompletableFuture.completedFuture(response);
    }
	@Async("asyncExecutor")
	public CompletableFuture<VeloceTrackingResponse> makeCalltoVeloce(List<String> ids, SupplierEntity supplier) {
		log.info("Veloce Tracking");
		VeloceTrackingResponse response = veloceSupplier.makeTrackingCall(ids,supplier);
		return CompletableFuture.completedFuture(response);
	}
	
}
