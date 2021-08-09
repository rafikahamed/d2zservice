package com.d2z.d2zservice.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.d2z.d2zservice.dto.ConsignmentDTO;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.model.ConsignmentConfig;
import com.d2z.d2zservice.model.ErrorDetails;
import com.d2z.d2zservice.model.SenderDataApi;

public interface ConsignmentCreator {
	
	public Map<String,ConsignmentConfig> fetchConfigDetails(Map<String,List<SenderDataApi>> request,int userId,String fileSeqId,Map<String, List<ErrorDetails>> errorMap);
		
	public List<SenderdataMaster> createConsignment(Map<String,List<SenderDataApi>> request,int userId,Map<String, List<ErrorDetails>> errorMap, List<String> autoShipRefNbrs);
	
	public List<SenderdataMaster> saveConsignment(List<ConsignmentDTO> request);
	
	public ResponseEntity<Object> sendResponse(List<SenderdataMaster> savedData,Map<String, List<ErrorDetails>> errorMap,int intialRequestSize,List<String> autoShipRefNbrs, String userName);
	
}
