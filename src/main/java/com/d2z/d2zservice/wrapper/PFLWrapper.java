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
import com.d2z.d2zservice.exception.FailureResponseException;
import com.d2z.d2zservice.model.PFLCreateShippingResponse;
import com.d2z.d2zservice.model.PFLResponseData;
import com.d2z.d2zservice.model.PFLSubmitOrderRequest;
import com.d2z.d2zservice.model.PFLSubmitOrderResponse;
import com.d2z.d2zservice.model.PFLTrackingResponse;
import com.d2z.d2zservice.model.PflCreateShippingRequest;
import com.d2z.d2zservice.model.PflPrintLabelRequest;
import com.d2z.d2zservice.model.PflTrackEventRequest;
import com.d2z.d2zservice.model.SenderData;
import com.d2z.d2zservice.model.SenderDataApi;
import com.d2z.d2zservice.model.SenderDataResponse;
import com.d2z.d2zservice.model.TrackingEvents;
import com.d2z.d2zservice.model.etower.LabelData;
import com.d2z.d2zservice.proxy.PFLProxy;
import com.d2z.d2zservice.util.D2ZCommonUtil;

@Service
public class PFLWrapper {
	
	@Autowired
	private PFLProxy pflProxy;
	
	@Autowired
	private ID2ZDao d2zDao;
	
	public void createShippingOrderPFL(List<SenderDataApi> incomingRequest,
			PflCreateShippingRequest PFLRequest, String userName, List<SenderDataResponse> senderDataResponseList, String serviceType, Map<String, String> systemRefNbrMap) 
						throws FailureResponseException {
		Map<String, LabelData> barcodeMap = new HashMap<String, LabelData>();
		PFLCreateShippingResponse pflResponse = pflProxy.makeCallForCreateShippingOrder(PFLRequest, serviceType);
		logPflCreateResponse(pflResponse);
		if(pflResponse==null) {
			throw new FailureResponseException("Error in file – please contact customer support");
		}else {
			if(pflResponse.getResult() != null) {
				processLabelsResponse(pflResponse, barcodeMap,systemRefNbrMap);
				int userId =d2zDao.fetchUserIdbyUserName(userName);
				String senderFileID = d2zDao.createConsignments(incomingRequest, userId, userName, barcodeMap);
				List<String> insertedOrder = d2zDao.fetchBySenderFileID(senderFileID);
				Iterator itr = insertedOrder.iterator();
				while (itr.hasNext()) {
					Object[] obj = (Object[]) itr.next();
					SenderDataResponse senderDataresponse = new SenderDataResponse();
					senderDataresponse.setReferenceNumber(obj[0].toString());
					senderDataresponse.setDatamatrix(obj[1] != null ?obj[1].toString():"");
					senderDataresponse.setBarcodeLabelNumber(obj[3] != null ? obj[3].toString() : "");
					senderDataresponse.setCarrier(obj[4] != null ? obj[4].toString() : "");
					senderDataresponse.setInjectionPort(obj[5] != null ? obj[5].toString() : "");
					if(senderDataresponse.getInjectionPort().equals("SYD") ||senderDataresponse.getInjectionPort().equals("MEL")||senderDataresponse.getInjectionPort().equals("BNE")||senderDataresponse.getInjectionPort().equals("ADL") ||senderDataresponse.getInjectionPort().equals("PER"))
					{
						senderDataresponse.setSortcode(senderDataresponse.getInjectionPort());
					}
					else
					{
						senderDataresponse.setSortcode("OTH");
					}
					senderDataResponseList.add(senderDataresponse);
				}
			}
		}
	}
	
	public void createShippingOrderPFLUI(List<SenderData> incomingRequest,
			PflCreateShippingRequest PFLRequest, String userName, List<SenderDataResponse> senderDataResponseList, String serviceType, Map<String, String> systemRefNbrMap) 
					throws FailureResponseException{
		Map<String, LabelData> barcodeMap = new HashMap<String, LabelData>();
		PFLCreateShippingResponse pflResponse = pflProxy.makeCallForCreateShippingOrder(PFLRequest, serviceType);
		logPflCreateResponse(pflResponse);
		if(pflResponse==null) {
			throw new FailureResponseException("Error in file – please contact customer support");
		}else {
			if(pflResponse.getResult() != null) {
				processLabelsResponse(pflResponse, barcodeMap,systemRefNbrMap);
				String senderFileID = d2zDao.exportParcel(incomingRequest,barcodeMap);
				List<String> insertedOrder = d2zDao.fetchBySenderFileID(senderFileID);
				Iterator itr = insertedOrder.iterator();
				while (itr.hasNext()) {
					Object[] obj = (Object[]) itr.next();
					SenderDataResponse senderDataresponse = new SenderDataResponse();
					senderDataresponse.setReferenceNumber(obj[0].toString());
					senderDataresponse.setBarcodeLabelNumber(obj[2] != null ? obj[2].toString() : "");
					senderDataresponse.setCarrier(obj[4] != null ? obj[4].toString() : "");
					senderDataresponse.setInjectionPort(obj[5] != null ? obj[5].toString() : "");
					senderDataResponseList.add(senderDataresponse);
				}
			}
		}
	}
	
	private void logPflCreateResponse(PFLCreateShippingResponse pflResponse) throws FailureResponseException {
		List<ETowerResponse> responseEntity = new ArrayList<ETowerResponse>();
		if(pflResponse != null) {
			if(null==pflResponse.getResult()) {
				throw new FailureResponseException("Shipment Error. Please contact us");
			}else {
			for(PFLResponseData pflData: pflResponse.getResult()) {
				if(pflData.getCode() != null) {
					ETowerResponse errorResponse = new ETowerResponse();
					errorResponse.setAPIName("PFL - Create order");
					errorResponse.setErrorCode(pflData.getCode());
					errorResponse.setErrorMessage(pflData.getError());
					errorResponse.setTimestamp(Timestamp.valueOf(D2ZCommonUtil.getAETCurrentTimestamp()));
					errorResponse.setStatus("Error");
					responseEntity.add(errorResponse);
					d2zDao.logEtowerResponse(responseEntity);
					throw new FailureResponseException("Error in file – please contact customer support");
				}else {
					ETowerResponse errorResponse = new ETowerResponse();
					errorResponse.setAPIName("PFL - Create order");
					errorResponse.setReferenceNumber(pflData.getReference());
					errorResponse.setOrderId(pflData.getId());
					errorResponse.setTrackingNo(pflData.getTracking());
					errorResponse.setTimestamp(Timestamp.valueOf(D2ZCommonUtil.getAETCurrentTimestamp()));
					errorResponse.setStatus("Success");
					responseEntity.add(errorResponse);
				}
			}}
			d2zDao.logEtowerResponse(responseEntity);
		}
	}
	
	private Map<String, LabelData> processLabelsResponse(PFLCreateShippingResponse pflResponse, Map<String, LabelData> barcodeMap, Map<String, String> systemRefNbrMap) {
		for (PFLResponseData data : pflResponse.getResult()) {
			LabelData labelData = new LabelData();
			labelData.setReferenceNo(systemRefNbrMap.get(data.getReference()));
			labelData.setArticleId(data.getId());
			labelData.setTrackingNo(data.getTracking());
			labelData.setHub(data.getHub());
			labelData.setMatrix(data.getMatrix());
			labelData.setProvider("PFL");
			barcodeMap.put(labelData.getReferenceNo(), labelData);
		}
		return barcodeMap;
	}
	
	public void createSubmitOrderPFL(List<String> orderIds, String serviceType) throws FailureResponseException {
		PFLSubmitOrderRequest pflSubmitOrder = new PFLSubmitOrderRequest();
		pflSubmitOrder.setIds(orderIds);
		PFLSubmitOrderResponse pflSubmitResponse = pflProxy.createSubmitOrderPFL(pflSubmitOrder,serviceType);
		logPflSubmitResponse(pflSubmitResponse, orderIds);
		if(pflSubmitResponse==null) {
			throw new FailureResponseException("Error in file – please contact customer support");
		}else {
			if(pflSubmitResponse.getResult() != null) {
			}
		}
	}
	
	public void DeleteOrderPFL(List<String> orderIds, String ServiceType) throws FailureResponseException {
		PFLSubmitOrderRequest pflSubmitOrder = new PFLSubmitOrderRequest();
		pflSubmitOrder.setIds(orderIds);
		PFLSubmitOrderResponse pflSubmitResponse = pflProxy.DeleteOrderPFL(pflSubmitOrder,ServiceType);
		logPflDeleteResponse(pflSubmitResponse, orderIds);
		if(pflSubmitResponse==null) {
			throw new FailureResponseException("Error in file – please contact customer support");
		}else {
			if(pflSubmitResponse.getResult() != null) {
			}
		}
	}

	private void logPflSubmitResponse(PFLSubmitOrderResponse pflSubmitResponse, List<String> orderIds) throws FailureResponseException{
		List<ETowerResponse> responseEntity = new ArrayList<ETowerResponse>();
			if(pflSubmitResponse != null) {
				if(pflSubmitResponse.getError() != null) {
					for(String orderIdVal:pflSubmitResponse.getError_ids()) {
						ETowerResponse errorResponse = new ETowerResponse();
						errorResponse.setAPIName("PFL - Submit order");
						errorResponse.setOrderId(orderIdVal);
						errorResponse.setErrorCode(pflSubmitResponse.getCode());
						errorResponse.setErrorMessage(pflSubmitResponse.getError());
						errorResponse.setTimestamp(Timestamp.valueOf(D2ZCommonUtil.getAETCurrentTimestamp()));
						errorResponse.setStatus("Error");
						responseEntity.add(errorResponse);
					}
					d2zDao.logEtowerResponse(responseEntity);
					throw new FailureResponseException("Error in file – please contact customer support");
				}else {
					for(String successOrder:orderIds) {
						ETowerResponse errorResponse = new ETowerResponse();
						errorResponse.setAPIName("PFL - Submit order");
						errorResponse.setOrderId(successOrder);
						errorResponse.setTimestamp(Timestamp.valueOf(D2ZCommonUtil.getAETCurrentTimestamp()));
						errorResponse.setStatus("Success");
						responseEntity.add(errorResponse);
					}
					d2zDao.logEtowerResponse(responseEntity);
				}
			}
	}
	
	private void logPflDeleteResponse(PFLSubmitOrderResponse pflSubmitResponse, List<String> orderIds) throws FailureResponseException{
		List<ETowerResponse> responseEntity = new ArrayList<ETowerResponse>();
			if(pflSubmitResponse != null) {
				if(pflSubmitResponse.getError() != null) {
					for(String orderIdVal:pflSubmitResponse.getError_ids()) {
						ETowerResponse errorResponse = new ETowerResponse();
						errorResponse.setAPIName("PFL - Delete order");
						errorResponse.setOrderId(orderIdVal);
						errorResponse.setErrorCode(pflSubmitResponse.getCode());
						errorResponse.setErrorMessage(pflSubmitResponse.getError());
						errorResponse.setTimestamp(Timestamp.valueOf(D2ZCommonUtil.getAETCurrentTimestamp()));
						errorResponse.setStatus("Error");
						responseEntity.add(errorResponse);
					}
					d2zDao.logEtowerResponse(responseEntity);
					throw new FailureResponseException("Error in file – please contact customer support");
				}else {
					for(String successOrder:orderIds) {
						ETowerResponse errorResponse = new ETowerResponse();
						errorResponse.setAPIName("PFL - Delete order");
						errorResponse.setOrderId(successOrder);
						errorResponse.setTimestamp(Timestamp.valueOf(D2ZCommonUtil.getAETCurrentTimestamp()));
						errorResponse.setStatus("Success");
						responseEntity.add(errorResponse);
					}
					d2zDao.logEtowerResponse(responseEntity);
				}
			}
	}

	public void makeTrackingEventCall(List<String> trackingNbrs,Map<String,TrackingEvents> trackingDataMap) {
		for(String pflValue:trackingNbrs) {
			PflTrackEventRequest pflTrackEvent = new PflTrackEventRequest();
			pflTrackEvent.setTracking_number(pflValue);
			PFLTrackingResponse pflTrackResp = pflProxy.trackingEvent(pflTrackEvent);
			if(pflTrackResp.getResult() != null) {
				TrackingEvents event = new TrackingEvents();
				event.setEventDetails(pflTrackResp.getResult().get(0).getStatus());
				event.setTrackEventDateOccured(pflTrackResp.getResult().get(0).getDate());
				d2zDao.saveTrackingEvents(pflTrackEvent.getTracking_number(),event);
				trackingDataMap.put(pflTrackEvent.getTracking_number(),event);

			}
		}
	}

	public byte[] printLabel(List<String> artileIDList) {
		PflPrintLabelRequest request = new PflPrintLabelRequest();
		request.setIds(artileIDList);
		byte[] bytes = null;
		try {
			bytes =  pflProxy.makeCallForPrintLabel(request);
		} catch (FailureResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bytes;
	}
	
	
}
