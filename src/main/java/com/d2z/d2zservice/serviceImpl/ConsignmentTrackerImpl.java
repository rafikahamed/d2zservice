package com.d2z.d2zservice.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.d2z.d2zservice.async.AsyncService;
import com.d2z.d2zservice.dao.ID2ZDao;
import com.d2z.d2zservice.entity.SupplierEntity;
import com.d2z.d2zservice.entity.TrackEvents;
import com.d2z.d2zservice.model.PFLTrackEvent;
import com.d2z.d2zservice.model.PFLTrackingResponseDetails;
import com.d2z.d2zservice.model.TrackParcelResponse;
import com.d2z.d2zservice.model.TrackingEvents;
import com.d2z.d2zservice.model.auspost.TrackableItems;
import com.d2z.d2zservice.model.auspost.TrackingResponse;
import com.d2z.d2zservice.model.auspost.TrackingResults;
import com.d2z.d2zservice.model.etower.ETowerTrackingDetails;
import com.d2z.d2zservice.model.etower.TrackEventResponseData;
import com.d2z.d2zservice.model.etower.TrackingEventResponse;
import com.d2z.d2zservice.model.veloce.DeliveryStatus;
import com.d2z.d2zservice.model.veloce.VeloceTrackResponse;
import com.d2z.d2zservice.model.veloce.VeloceTrackingResponse;
import com.d2z.d2zservice.service.ConsignmentTracker;
import com.d2z.d2zservice.supplier.Tracker;

@Service
public class ConsignmentTrackerImpl implements ConsignmentTracker{

	@Autowired
	ID2ZDao dao;
	
	@Autowired
	AsyncService aysncService;
	
	@Autowired
	Tracker tracker;
	
	@Override
	public List<TrackParcelResponse> trackParcels(List<String> trackingNos,String identifier){
		return tracker.trackParcels(trackingNos,identifier);
	}
	/*
	 * @Override public List<TrackParcelResponse> trackParcels(List<String>
	 * trackingNos,String identifier){ List<TrackParcelResponse>
	 * trackPracelsResponse = new ArrayList<TrackParcelResponse>();
	 * 
	 * Map<String,List<String>> trackingMap =
	 * dao.fetchtrackingIdentifier(trackingNos, identifier);
	 * if(trackingMap.size()>0) { Map<SupplierEntity,List<String>>
	 * trackingSupplierMap = new HashMap<SupplierEntity,List<String>>();
	 * 
	 * trackingMap.forEach((supplierId,trackingIds) -> {
	 * if(Integer.parseInt(supplierId) == 0) { SupplierEntity supplier = new
	 * SupplierEntity(); supplier.setSupplierName("D2Z");
	 * trackingSupplierMap.put(supplier,trackingIds); } else
	 * if(Integer.parseInt(supplierId)>0) { SupplierEntity supplier =
	 * dao.fetchSupplierData(Integer.parseInt(supplierId));
	 * trackingSupplierMap.put(supplier,trackingIds); } });
	 * CompletableFuture<TrackingEventResponse> eTowerResponse = new
	 * CompletableFuture<TrackingEventResponse>();
	 * CompletableFuture<TrackingResponse> auPostResponse = new
	 * CompletableFuture<TrackingResponse>(); CompletableFuture<List<TrackEvents>>
	 * trackEvents = new CompletableFuture<List<TrackEvents>>();
	 * CompletableFuture<List<PFLTrackingResponseDetails>> pflResponse = new
	 * CompletableFuture<List<PFLTrackingResponseDetails>>();
	 * CompletableFuture<VeloceTrackingResponse> veloceResponse = new
	 * CompletableFuture<VeloceTrackingResponse>();
	 * 
	 * for (Map.Entry<SupplierEntity,List<String>> entry :
	 * trackingSupplierMap.entrySet()) { SupplierEntity supplier = entry.getKey();
	 * List<String> ids = entry.getValue();
	 * if(supplier.getSupplierName().contains("PFL")) { pflResponse =
	 * aysncService.makeCalltoPFL(ids,supplier); }else { pflResponse.complete(null);
	 * } if(supplier.getSupplierName().contains("ETOWER")) { eTowerResponse =
	 * aysncService.makeCalltoEtower(ids,supplier); }else {
	 * eTowerResponse.complete(null); }
	 * if(supplier.getSupplierName().contains("AUPOST")) { auPostResponse =
	 * aysncService.makeCalltoAuPost(ids,supplier); }else {
	 * auPostResponse.complete(null); }
	 * if(supplier.getSupplierName().contains("D2Z")) {
	 * System.out.println(supplier.getSupplierName()); trackEvents =
	 * aysncService.makeCalltoDB(ids); }else { trackEvents.complete(null); }
	 * if(supplier.getSupplierName().contains("VELOCE")) {
	 * System.out.println(supplier.getSupplierName()); veloceResponse =
	 * aysncService.makeCalltoVeloce(ids,supplier); }else {
	 * veloceResponse.complete(null); } }
	 * 
	 * CompletableFuture.allOf(eTowerResponse, auPostResponse, trackEvents,
	 * pflResponse).join();
	 * 
	 * try { aggreateTrackParcelResponse(eTowerResponse.get(), auPostResponse.get(),
	 * trackEvents.get(), pflResponse.get(),
	 * trackPracelsResponse,veloceResponse.get()); } catch (InterruptedException e)
	 * { // TODO Auto-generated catch block e.printStackTrace(); } catch
	 * (ExecutionException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } }
	 * 
	 * return trackPracelsResponse;
	 * 
	 * }
	 */

	private List<TrackParcelResponse> aggreateTrackParcelResponse(TrackingEventResponse eTowerResponse,
			TrackingResponse auPostResponse, List<TrackEvents> trackEvents,
			List<PFLTrackingResponseDetails> pflResponse, List<TrackParcelResponse> trackPracelResponse,VeloceTrackingResponse veloceResponse) {

		if (null != eTowerResponse) {
			parseEtowerTrackingResponse(trackPracelResponse, eTowerResponse);
		}

		if (null != auPostResponse) {
			parseAuPostTrackingResponse(trackPracelResponse, auPostResponse);
		}
		if (null != trackEvents) {
			parseTrackEventsResponse(trackPracelResponse, trackEvents);
		}
		if (null != pflResponse) {
			parsePFLResponse(trackPracelResponse, pflResponse);
		}
		if (null != veloceResponse) {
			parseVeloceTrackingResponse(trackPracelResponse, veloceResponse);
		}
		return trackPracelResponse;
	}


	private void parseTrackEventsResponse(List<TrackParcelResponse> trackPracelResponse,
			List<TrackEvents> trackEvents) {
		// TODO Auto-generated method stub
		Map<String,List<TrackEvents>> trackEventsMap = new HashMap<String,List<TrackEvents>>();
		trackEvents.forEach(obj->{
			if(trackEventsMap.containsKey(obj.getArticleID())) {
			trackEventsMap.get(obj.getArticleID()).add(obj);
			}else {
				List<TrackEvents> eventList = new ArrayList<TrackEvents>();
				eventList.add(obj);
				trackEventsMap.put(obj.getArticleID(), eventList);
			}
		});
		trackEventsMap.forEach((k,v)->{
			TrackParcelResponse parcelStatus = new TrackParcelResponse();
			parcelStatus.setArticleId(k);
			List<TrackingEvents> events = new ArrayList<TrackingEvents>();
			for (TrackEvents trackEvent : v) {
				TrackingEvents event = new TrackingEvents();
				event.setTrackEventDateOccured(trackEvent.getTrackEventDateOccured());
				event.setEventDetails(trackEvent.getTrackEventDetails());
				event.setStatusCode(trackEvent.getTrackEventCode());
				event.setLocation(trackEvent.getLocation());
				events.add(event);
			}
			parcelStatus.setTrackingEvents(events);
			trackPracelResponse.add(parcelStatus);
		});
	}

	private void parsePFLResponse(List<TrackParcelResponse> trackParcelResponse,
			List<PFLTrackingResponseDetails> pflResponse) {
		if (!pflResponse.isEmpty()) {
			for (PFLTrackingResponseDetails response : pflResponse) {
				TrackParcelResponse parcelStatus = new TrackParcelResponse();
				parcelStatus.setArticleId(response.getBarcodeLabel());
				List<TrackingEvents> events = new ArrayList<TrackingEvents>();
				for (PFLTrackEvent trackEvent : response.getTrackEvent()) {
					TrackingEvents event = new TrackingEvents();
					event.setTrackEventDateOccured(trackEvent.getDate());
					event.setEventDetails(trackEvent.getStatus());
					event.setStatusCode(trackEvent.getStatus_code());
					event.setLocation(trackEvent.getLocation());
					events.add(event);
				}

				parcelStatus.setTrackingEvents(events);
				trackParcelResponse.add(parcelStatus);

			}
		}
	}

	private void parseAuPostTrackingResponse(List<TrackParcelResponse> trackParcelResponse,
			TrackingResponse auPostResponse) {
		List<TrackingResults> trackingData = auPostResponse.getTracking_results();
		if (!trackingData.isEmpty()) {

			for (TrackingResults data : trackingData) {
				if (data != null && data.getTrackable_items() != null) {
					for (TrackableItems trackingLabel : data.getTrackable_items()) {
						TrackParcelResponse parcelStatus = new TrackParcelResponse();
						List<TrackingEvents> events = new ArrayList<TrackingEvents>();

						parcelStatus.setArticleId(trackingLabel.getArticle_id());
						if (trackingLabel != null && trackingLabel.getEvents() != null) {
							for (com.d2z.d2zservice.model.auspost.TrackingEvents trackingEvents : trackingLabel
									.getEvents()) {
								TrackingEvents event = new TrackingEvents();
								event.setTrackEventDateOccured(trackingEvents.getDate());
								event.setEventDetails(trackingEvents.getDescription());
								events.add(event);
							}
							parcelStatus.setTrackingEvents(events);
						}
						trackParcelResponse.add(parcelStatus);
					}
				}
			}
		}
	}

	private void parseEtowerTrackingResponse(List<TrackParcelResponse> trackParcelResponse,
			TrackingEventResponse eTowerResponse) {
		
			List<TrackEventResponseData> responseData = eTowerResponse.getData();

			if (responseData != null && !responseData.isEmpty()) {

				for (TrackEventResponseData data : responseData) {

					if (data != null) {

						List<TrackingEvents> trackingEvents = new ArrayList<TrackingEvents>();
						TrackParcelResponse parcelStatus = new TrackParcelResponse();
						parcelStatus.setArticleId(data.getOrderId());
						if (data.getEvents() != null) {
							for (ETowerTrackingDetails trackingDetails : data.getEvents()) {

								TrackingEvents event = new TrackingEvents();
								event.setTrackEventDateOccured(trackingDetails.getEventTime());
								event.setEventDetails(trackingDetails.getActivity());
								event.setStatusCode(trackingDetails.getEventCode());
								trackingEvents.add(event);
							}
						}
						parcelStatus.setTrackingEvents(trackingEvents);
						trackParcelResponse.add(parcelStatus);
					}
				}
			}
		}
	
	private void parseVeloceTrackingResponse(List<TrackParcelResponse> trackParcelResponse,
			VeloceTrackingResponse veloceResponse) {
		
			List<VeloceTrackResponse> responseData = veloceResponse.getBdr();

			if (responseData != null && !responseData.isEmpty()) {

				for (VeloceTrackResponse data : responseData) {

					if (data != null) {

						List<TrackingEvents> trackingEvents = new ArrayList<TrackingEvents>();
						TrackParcelResponse parcelStatus = new TrackParcelResponse();
						parcelStatus.setArticleId(data.getTracking_id());
						if (data.getDelivery_status() != null) {
							for (DeliveryStatus trackingDetails : data.getDelivery_status()) {

								TrackingEvents event = new TrackingEvents();
								event.setTrackEventDateOccured(trackingDetails.getEvent_date());
								event.setEventDetails(trackingDetails.getEvent_detail());
								event.setStatusCode(trackingDetails.getEvent_message());
								trackingEvents.add(event);
							}
						}
						parcelStatus.setTrackingEvents(trackingEvents);
						trackParcelResponse.add(parcelStatus);
					}
				}
			}
		}
	


}
