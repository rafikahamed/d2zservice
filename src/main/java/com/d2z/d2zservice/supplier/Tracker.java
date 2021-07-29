package com.d2z.d2zservice.supplier;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.model.TrackParcelResponse;
import com.d2z.d2zservice.model.TrackingRequest;
import com.d2z.d2zservice.security.HMACGenerator;
import com.d2z.d2zservice.util.D2ZCommonUtil;

@Service
public class Tracker {
	
	@Value("${trackServer.baseUrl}")
	private String url; 

	@Autowired
	SupplierInterface supplier;
	
	public void saveData(List<SenderdataMaster> savedData) {
		supplier.makeCall(HttpMethod.POST, url+"/track", constructRequest(savedData), constructHeader(), String.class);
	}

	private HttpHeaders constructHeader() {
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
	    return header;		
	}

	private String constructRequest(List<SenderdataMaster> savedData) {
		List<TrackingRequest> list = new ArrayList<TrackingRequest>();
		savedData.forEach(data -> {
			TrackingRequest request = new TrackingRequest();
			request.setReference_number(data.getReference_number());
			request.setUser_Id(data.getUser_ID());
			request.setServiceType(data.getServicetype());
			request.setCarrier(data.getCarrier());
			request.setBarcodelabelNumber(data.getBarcodelabelNumber());
			request.setArticleID(data.getArticleId());
			request.setFileName(data.getFilename());
			request.setIsDeleted(data.getIsDeleted());
			request.setIsDelivered("N");
			request.setCreateTimestamp(data.getTimestamp());
			request.setModifiedTimestamp(data.getTimestamp());
			list.add(request);
			
		});
		return D2ZCommonUtil.convertToJsonString(list);
	}

	public void createEvents(List<TrackParcelResponse> request) {
		supplier.makeCall(HttpMethod.POST, url+"/trackEvents",D2ZCommonUtil.convertToJsonString(request), constructHeader(), String.class);
	}

}
