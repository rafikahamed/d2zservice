package com.d2z.d2zservice.service;

import java.util.List;

import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.entity.Trackandtrace;
import com.d2z.d2zservice.exception.ReferenceNumberNotUniqueException;
import com.d2z.d2zservice.model.ResponseMessage;

public interface ShipmentAllocator {
	
	public ResponseMessage allocateShipment(List<String> ids, String shipmentNumber, String identifier)
			throws ReferenceNumberNotUniqueException;

	void autoAllocate(List<SenderdataMaster> consignments, String shipmentNumber);

	List<Trackandtrace> insertSAintoTrackandTrace(List<SenderdataMaster> senderDataValue, String shipmentNumber);

}
