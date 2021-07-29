package com.d2z.d2zservice.service;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.d2z.d2zservice.model.TrackParcelResponse;

public interface ConsignmentTracker {
	
	public List<TrackParcelResponse> trackParcels(List<String> trackingNos,String identifier);

}
