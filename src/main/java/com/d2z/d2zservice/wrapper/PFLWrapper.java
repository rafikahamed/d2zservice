package com.d2z.d2zservice.wrapper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.d2z.d2zservice.dao.ID2ZDao;
import com.d2z.d2zservice.exception.EtowerFailureResponseException;
import com.d2z.d2zservice.model.PFLCreateShippingResponse;
import com.d2z.d2zservice.model.PFLResponseData;
import com.d2z.d2zservice.model.PFLSubmitOrderRequest;
import com.d2z.d2zservice.model.PFLSubmitOrderResponse;
import com.d2z.d2zservice.model.PflCreateShippingRequest;
import com.d2z.d2zservice.model.SenderData;
import com.d2z.d2zservice.model.SenderDataApi;
import com.d2z.d2zservice.model.SenderDataResponse;
import com.d2z.d2zservice.model.etower.LabelData;
import com.d2z.d2zservice.proxy.PFLProxy;

@Service
public class PFLWrapper {
	
	@Autowired
	private PFLProxy pflProxy;
	
	@Autowired
	private ID2ZDao d2zDao;
	
	public void createShippingOrderPFL(List<SenderDataApi> incomingRequest,
			PflCreateShippingRequest PFLRequest, String userName, List<SenderDataResponse> senderDataResponseList) 
						throws EtowerFailureResponseException {
		Map<String, LabelData> barcodeMap = new HashMap<String, LabelData>();
		PFLCreateShippingResponse pflResponse = pflProxy.makeCallForCreateShippingOrder(PFLRequest);
		if(pflResponse==null) {
			throw new EtowerFailureResponseException("Failed. Please contact D2Z");
		}else {
			if(pflResponse.getResult() != null) {
				processLabelsResponse(pflResponse, barcodeMap);
				int userId =d2zDao.fetchUserIdbyUserName(userName);
				String senderFileID = d2zDao.createConsignments(incomingRequest, userId, userName, barcodeMap);
				List<String> insertedOrder = d2zDao.fetchBySenderFileID(senderFileID);
				Iterator itr = insertedOrder.iterator();
				while (itr.hasNext()) {
					Object[] obj = (Object[]) itr.next();
					SenderDataResponse senderDataresponse = new SenderDataResponse();
					senderDataresponse.setReferenceNumber(obj[0].toString());
					senderDataresponse.setBarcodeLabelNumber(obj[3] != null ? obj[3].toString() : "");
					senderDataResponseList.add(senderDataresponse);
				}
			}
		}
	}
	
	private Map<String, LabelData> processLabelsResponse(PFLCreateShippingResponse pflResponse, Map<String, LabelData> barcodeMap) {
		for (PFLResponseData data : pflResponse.getResult()) {
			LabelData labelData = new LabelData();
			labelData.setReferenceNo(data.getReference());
			labelData.setArticleId(data.getId());
			labelData.setTrackingNo(data.getTracking());
			labelData.setHub(data.getHub());
			labelData.setMatrix(data.getMatrix());
			labelData.setProvider("PFL");
			barcodeMap.put(data.getReference(), labelData);
		}
		return barcodeMap;
	}

	public void createShippingOrderPFLUI(List<SenderData> incomingRequest,
			PflCreateShippingRequest PFLRequest, String userName, List<SenderDataResponse> senderDataResponseList) 
					throws EtowerFailureResponseException{
		Map<String, LabelData> barcodeMap = new HashMap<String, LabelData>();
		PFLCreateShippingResponse pflResponse = pflProxy.makeCallForCreateShippingOrder(PFLRequest);
		if(pflResponse==null) {
			throw new EtowerFailureResponseException("Failed. Please contact D2Z");
		}else {
			if(pflResponse.getResult() != null) {
				processLabelsResponse(pflResponse, barcodeMap);
				String senderFileID = d2zDao.exportParcel(incomingRequest,barcodeMap);
				List<String> insertedOrder = d2zDao.fetchBySenderFileID(senderFileID);
				Iterator itr = insertedOrder.iterator();
				while (itr.hasNext()) {
					Object[] obj = (Object[]) itr.next();
					SenderDataResponse senderDataresponse = new SenderDataResponse();
					senderDataresponse.setReferenceNumber(obj[0].toString());
					senderDataresponse.setBarcodeLabelNumber(obj[3] != null ? obj[3].toString() : "");
					senderDataresponse.setCarrier(obj[4] != null ? obj[4].toString() : "");
					senderDataResponseList.add(senderDataresponse);
				}
			}
		}
	}
	
	public void createSubmitOrderPFL(List<String> orderIds) throws EtowerFailureResponseException {
		PFLSubmitOrderRequest pflSubmitOrder = new PFLSubmitOrderRequest();
		pflSubmitOrder.setIds(orderIds);
		PFLSubmitOrderResponse pflResponse = pflProxy.createSubmitOrderPFL(pflSubmitOrder);
		if(pflResponse==null) {
			throw new EtowerFailureResponseException("Failed. Please contact D2Z");
		}else {
			if(pflResponse.getResult() != null) {
				
			}
		}
	}
	
}
