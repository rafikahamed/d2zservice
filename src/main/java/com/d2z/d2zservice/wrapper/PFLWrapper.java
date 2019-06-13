package com.d2z.d2zservice.wrapper;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.d2z.d2zservice.dao.ID2ZDao;
import com.d2z.d2zservice.entity.ETowerResponse;
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
		logPflCreateResponse(pflResponse);
		if(pflResponse==null) {
			throw new EtowerFailureResponseException("Error in file – please contact customer support");
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
	
	public void createShippingOrderPFLUI(List<SenderData> incomingRequest,
			PflCreateShippingRequest PFLRequest, String userName, List<SenderDataResponse> senderDataResponseList) 
					throws EtowerFailureResponseException{
		Map<String, LabelData> barcodeMap = new HashMap<String, LabelData>();
		PFLCreateShippingResponse pflResponse = pflProxy.makeCallForCreateShippingOrder(PFLRequest);
		logPflCreateResponse(pflResponse);
		if(pflResponse==null) {
			throw new EtowerFailureResponseException("Error in file – please contact customer support");
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
	
	private void logPflCreateResponse(PFLCreateShippingResponse pflResponse) throws EtowerFailureResponseException {
		List<ETowerResponse> responseEntity = new ArrayList<ETowerResponse>();
		if(pflResponse != null) {
			for(PFLResponseData pflData: pflResponse.getResult()) {
				if(pflData.getCode() != null) {
					ETowerResponse errorResponse = new ETowerResponse();
					errorResponse.setAPIName("PFL - Create order");
					errorResponse.setErrorCode(pflData.getCode());
					errorResponse.setErrorMessage(pflData.getError());
					errorResponse.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
					errorResponse.setStatus("Error");
					responseEntity.add(errorResponse);
					d2zDao.logEtowerResponse(responseEntity);
					throw new EtowerFailureResponseException("Error in file – please contact customer support");
				}else {
					ETowerResponse errorResponse = new ETowerResponse();
					errorResponse.setAPIName("PFL - Create order");
					errorResponse.setReferenceNumber(pflData.getReference());
					errorResponse.setOrderId(pflData.getId());
					errorResponse.setTrackingNo(pflData.getTracking());
					errorResponse.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
					errorResponse.setStatus("Success");
					responseEntity.add(errorResponse);
				}
			}
			d2zDao.logEtowerResponse(responseEntity);
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
	
	public void createSubmitOrderPFL(List<String> orderIds) throws EtowerFailureResponseException {
		PFLSubmitOrderRequest pflSubmitOrder = new PFLSubmitOrderRequest();
		pflSubmitOrder.setIds(orderIds);
		PFLSubmitOrderResponse pflSubmitResponse = pflProxy.createSubmitOrderPFL(pflSubmitOrder);
		logPflSubmitResponse(pflSubmitResponse, orderIds);
		if(pflSubmitResponse==null) {
			throw new EtowerFailureResponseException("Error in file – please contact customer support");
		}else {
			if(pflSubmitResponse.getResult() != null) {
			}
		}
	}

	private void logPflSubmitResponse(PFLSubmitOrderResponse pflSubmitResponse, List<String> orderIds) throws EtowerFailureResponseException{
		List<ETowerResponse> responseEntity = new ArrayList<ETowerResponse>();
			if(pflSubmitResponse != null) {
				if(pflSubmitResponse.getError() != null) {
					for(String orderIdVal:pflSubmitResponse.getError_ids()) {
						ETowerResponse errorResponse = new ETowerResponse();
						errorResponse.setAPIName("PFL - Submit order");
						errorResponse.setOrderId(orderIdVal);
						errorResponse.setErrorCode(pflSubmitResponse.getCode());
						errorResponse.setErrorMessage(pflSubmitResponse.getError());
						errorResponse.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
						errorResponse.setStatus("Error");
						responseEntity.add(errorResponse);
					}
					d2zDao.logEtowerResponse(responseEntity);
					throw new EtowerFailureResponseException("Error in file – please contact customer support");
				}else {
					for(String successOrder:orderIds) {
						ETowerResponse errorResponse = new ETowerResponse();
						errorResponse.setAPIName("PFL - Submit order");
						errorResponse.setOrderId(successOrder);
						errorResponse.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
						errorResponse.setStatus("Success");
						responseEntity.add(errorResponse);
					}
					d2zDao.logEtowerResponse(responseEntity);
				}
			}
	}
	
}
