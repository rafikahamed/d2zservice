package com.d2z.d2zservice.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.d2z.d2zservice.exception.ReferenceNumberNotUniqueException;
import com.d2z.d2zservice.model.CreateConsignmentRequest;
import com.d2z.d2zservice.model.ErrorDetails;
import com.d2z.d2zservice.model.ResponseMessage;
import com.d2z.d2zservice.model.TrackParcelResponse;

public interface IConsignmentService {
	public ResponseEntity<Object> createConsignments(CreateConsignmentRequest orderDetail,Map<String,List<ErrorDetails>> errorMap );

	public byte[] generateLabel(List<String> refBarNumArray,String identifier);

	List<TrackParcelResponse> trackParcels(List<String> trackingNo, String identifier);

	public ResponseMessage allocateShipment(List<String> ids, String identifier, String shipmentNumber) throws ReferenceNumberNotUniqueException;
}
