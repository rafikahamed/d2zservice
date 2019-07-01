package com.d2z.d2zservice.wrapper;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.d2z.d2zservice.dao.ID2ZDao;
import com.d2z.d2zservice.entity.ETowerResponse;
import com.d2z.d2zservice.exception.EtowerFailureResponseException;
import com.d2z.d2zservice.model.PCAConsignee;
import com.d2z.d2zservice.model.PCACreateShipmentRequest;
import com.d2z.d2zservice.model.PCACreateShipmentRequestInfo;
import com.d2z.d2zservice.model.PCACreateShippingResponse;
import com.d2z.d2zservice.model.PCAItems;
import com.d2z.d2zservice.model.PCAPackages;
import com.d2z.d2zservice.model.PCAReceiver;
import com.d2z.d2zservice.model.PCAShipper;
import com.d2z.d2zservice.model.PFLSenderDataFileRequest;
import com.d2z.d2zservice.model.PFLSenderDataRequest;
import com.d2z.d2zservice.model.SenderData;
import com.d2z.d2zservice.model.SenderDataApi;
import com.d2z.d2zservice.model.SenderDataResponse;
import com.d2z.d2zservice.model.etower.LabelData;
import com.d2z.d2zservice.proxy.PcaProxy;

@Service
public class PCAWrapper {

	@Autowired
	private PcaProxy pcaProxy;

	@Value("${pca.chargeCodeFW}")
	private String chargeCodeFW;
	
	@Value("${pca.chargeCodeST}")
	private String chargeCodeST;

	@Value("${pca.cancel}")
	private String cancel;

	@Autowired
	private ID2ZDao d2zDao;

	public void makeCreateShippingOrderPFACall(List<SenderDataApi> consignmentData,
			List<SenderDataResponse> senderDataResponseList, String userName, String serviceType) throws EtowerFailureResponseException {
		PCACreateShipmentRequest pcaRequest = new PCACreateShipmentRequest();
		List<PCACreateShipmentRequestInfo> pcaOrderInfoRequest = new ArrayList<PCACreateShipmentRequestInfo>();
		String chargeType = "FastwayS";
		for (SenderDataApi orderDetail : consignmentData) {
			PCACreateShipmentRequestInfo request = new PCACreateShipmentRequestInfo();
			request.setCust_ref(orderDetail.getReferenceNumber());
			request.setType("10");
			request.setPacks("1");

			List<PCAPackages> pcaPackageList = new ArrayList<PCAPackages>();
			PCAPackages pcaPackages = new PCAPackages();
			pcaPackages.setWeight(new BigDecimal(orderDetail.getWeight()));
			pcaPackages.setHeight(orderDetail.getDimensionsHeight());
			pcaPackages.setWidth(orderDetail.getDimensionsWidth());
			pcaPackages.setLength(orderDetail.getDimensionsLength());
			pcaPackageList.add(pcaPackages);
			request.setPackages(pcaPackageList);

			request.setWeight(orderDetail.getWeight());
			request.setCbm(new BigDecimal(orderDetail.getWeight()));
			request.setCurrency(orderDetail.getCurrency());

			PCAShipper pcaShipper = new PCAShipper();
			pcaShipper.setName(orderDetail.getShipperName());
			pcaShipper.setAddress(orderDetail.getShipperAddr1());
			pcaShipper.setState(orderDetail.getStatus());
			pcaShipper.setCity(orderDetail.getShipperCity());
			pcaShipper.setSuburb(orderDetail.getShipperCity());
			pcaShipper.setPostcode(orderDetail.getShipperPostcode());
			request.setShipper(pcaShipper);

			PCAReceiver pcaReceiver = new PCAReceiver();
			pcaReceiver.setName(orderDetail.getConsigneeName());
			pcaReceiver.setAddress(orderDetail.getConsigneeAddr1());
			pcaReceiver.setState(orderDetail.getConsigneeState());
			pcaReceiver.setCity(orderDetail.getConsigneeSuburb());
			pcaReceiver.setSuburb(orderDetail.getConsigneeSuburb());
			pcaReceiver.setPostcode(orderDetail.getConsigneePostcode());
			pcaReceiver.setPhone(orderDetail.getConsigneePhone());
			pcaReceiver.setEmail(orderDetail.getConsigneeEmail());
			request.setReceiver(pcaReceiver);

			PCAConsignee pcaConsignee = new PCAConsignee();
			pcaConsignee.setName(orderDetail.getConsigneeName());
			pcaConsignee.setAddress(orderDetail.getConsigneeAddr1());
			pcaConsignee.setState(orderDetail.getConsigneeState());
			pcaConsignee.setCity(orderDetail.getConsigneeSuburb());
			pcaConsignee.setSuburb(orderDetail.getConsigneeSuburb());
			pcaConsignee.setPostcode(orderDetail.getConsigneePostcode());
			pcaConsignee.setPhone(orderDetail.getConsigneePhone());
			pcaConsignee.setEmail(orderDetail.getConsigneeEmail());
			request.setConsignee(pcaConsignee);

			List<PCAItems> PCAItemList = new ArrayList<PCAItems>();
			PCAItems pcaItems = new PCAItems();
			pcaItems.setType("O");
			pcaItems.setName(orderDetail.getProductDescription());
			pcaItems.setQty("1");
			pcaItems.setPrice(String.valueOf(orderDetail.getValue()));
			pcaItems.setSku(orderDetail.getSku());
			PCAItemList.add(pcaItems);
			request.setItems(PCAItemList);
			if("STS".equalsIgnoreCase(serviceType)) {
				request.setChargecode(chargeCodeFW);
			}else {
				request.setChargecode(chargeCodeST);
				chargeType = "StarTrack";
			}
			pcaOrderInfoRequest.add(request);
		}
		pcaRequest.setShipments(pcaOrderInfoRequest);
		createShippingOrderPCA(consignmentData, pcaRequest, userName, senderDataResponseList, chargeType);
	}

	private void createShippingOrderPCA(List<SenderDataApi> consignmentData, PCACreateShipmentRequest pcaRequest,
			String userName, List<SenderDataResponse> senderDataResponseList, String chargeType) throws EtowerFailureResponseException {
		Map<String, LabelData> barcodeMap = new HashMap<String, LabelData>();
		List<PCACreateShippingResponse> pcaResponse = pcaProxy.makeCallForCreateShippingOrder(pcaRequest);
		logPcaCreateResponse(pcaResponse);
		List<PCACreateShippingResponse> pcaResponseSuccess  = new ArrayList<PCACreateShippingResponse>();
		List<String> referenceNumber = new ArrayList<String>();
		for(PCACreateShippingResponse pcaData: pcaResponse) {
			if(!pcaData.getMsg().equalsIgnoreCase("Success")) {
				SenderDataResponse senderDataresponse = new SenderDataResponse();
				senderDataresponse.setReferenceNumber(pcaData.getMsg().split("-")[1].split(" ")[2]);
				senderDataresponse.setMessage(pcaData.getMsg());
				senderDataResponseList.add(senderDataresponse);
				referenceNumber.add(pcaData.getMsg().split("-")[1].split(" ")[2]);
			}else {
				pcaResponseSuccess.add(pcaData);
			}
		}
		
//		if(referenceNumber.size() > 0) {
//			String pcaRequestCancel = " {\"no\":\"ECN1656036385\", \"ref\":\"ML0000731779\", \"cust_ref\":\"2017062600112345\"}";
//			List<PCACreateShippingResponse> pcaResponseCancel = pcaProxy.makeCallForCancelShipment(pcaRequestCancel);
//		}
		
		List<SenderDataApi> pflSenderData = new ArrayList<SenderDataApi>();
		PFLSenderDataRequest pflRequest = new PFLSenderDataRequest();
		if(referenceNumber.size() > 0) {
			consignmentData.forEach(obj -> {
				if(!referenceNumber.contains(obj.getReferenceNumber())) {
					pflSenderData.add(obj);
				}
			});
			pflRequest.setPflSenderDataApi(pflSenderData);
		}else {
			pflRequest.setPflSenderDataApi(consignmentData);
		}
		
		if(pcaResponseSuccess.size() > 0) {
			processPcaLabelsResponse(pcaResponseSuccess, barcodeMap, chargeType);
			int userId = d2zDao.fetchUserIdbyUserName(userName);
			String senderFileID = d2zDao.createConsignments(pflRequest.getPflSenderDataApi(), userId, userName, barcodeMap);
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

	private Map<String, LabelData> processPcaLabelsResponse(List<PCACreateShippingResponse> pcaResponse, Map<String, LabelData> barcodeMap, 
				String chargeType) {
			for (PCACreateShippingResponse data : pcaResponse) {
				if(data.getMsg().equalsIgnoreCase("Success")) {
					LabelData labelData = new LabelData();
					labelData.setReferenceNo(data.getCustref());
					labelData.setArticleId(data.getRef());
					labelData.setTrackingNo(data.getRef());
					labelData.setHub(data.getDepot());
					labelData.setMatrix(data.getConnote());
					labelData.setProvider("PCA");
					if("StarTrack".equalsIgnoreCase(chargeType)) {
						labelData.setCarrier("StarTrack");
					}else {
						labelData.setCarrier("FastwayS");
					}
					barcodeMap.put(data.getCustref(), labelData);
				}
			}
		return barcodeMap;
	}

	public void makeCreateShippingOrderFilePCACall(List<SenderData> orderDetailList,
			List<SenderDataResponse> senderDataResponseList, String userName, String serviceType) throws EtowerFailureResponseException {
		PCACreateShipmentRequest pcaRequest = new PCACreateShipmentRequest();
		List<PCACreateShipmentRequestInfo> pcaOrderInfoRequest = new ArrayList<PCACreateShipmentRequestInfo>();
		String chargeType = "FastwayS";
		for (SenderData orderDetail : orderDetailList) {
			PCACreateShipmentRequestInfo request = new PCACreateShipmentRequestInfo();
			request.setCust_ref(orderDetail.getReferenceNumber());
			request.setType("10");
			request.setPacks("1");

			List<PCAPackages> pcaPackageList = new ArrayList<PCAPackages>();
			PCAPackages pcaPackages = new PCAPackages();
			pcaPackages.setWeight(new BigDecimal(orderDetail.getWeight()));
			pcaPackages.setHeight(orderDetail.getDimensionsHeight());
			pcaPackages.setWidth(orderDetail.getDimensionsWidth());
			pcaPackages.setLength(orderDetail.getDimensionsLength());
			pcaPackageList.add(pcaPackages);
			request.setPackages(pcaPackageList);

			request.setWeight(orderDetail.getWeight());
			request.setCbm(new BigDecimal(orderDetail.getWeight()));
			request.setCurrency(orderDetail.getCurrency());

			PCAShipper pcaShipper = new PCAShipper();
			pcaShipper.setName(orderDetail.getShipperName());
			pcaShipper.setAddress(orderDetail.getShipperAddr1());
			pcaShipper.setState(orderDetail.getStatus());
			pcaShipper.setCity(orderDetail.getShipperCity());
			pcaShipper.setSuburb(orderDetail.getShipperCity());
			pcaShipper.setPostcode(orderDetail.getShipperPostcode());
			request.setShipper(pcaShipper);

			PCAReceiver pcaReceiver = new PCAReceiver();
			pcaReceiver.setName(orderDetail.getConsigneeName());
			pcaReceiver.setAddress(orderDetail.getConsigneeAddr1());
			pcaReceiver.setState(orderDetail.getConsigneeState());
			pcaReceiver.setCity(orderDetail.getConsigneeSuburb());
			pcaReceiver.setSuburb(orderDetail.getConsigneeSuburb());
			pcaReceiver.setPostcode(orderDetail.getConsigneePostcode());
			pcaReceiver.setPhone(orderDetail.getConsigneePhone());
			pcaReceiver.setEmail(orderDetail.getConsigneeEmail());
			request.setReceiver(pcaReceiver);

			PCAConsignee pcaConsignee = new PCAConsignee();
			pcaConsignee.setName(orderDetail.getConsigneeName());
			pcaConsignee.setAddress(orderDetail.getConsigneeAddr1());
			pcaConsignee.setState(orderDetail.getConsigneeState());
			pcaConsignee.setCity(orderDetail.getConsigneeSuburb());
			pcaConsignee.setSuburb(orderDetail.getConsigneeSuburb());
			pcaConsignee.setPostcode(orderDetail.getConsigneePostcode());
			pcaConsignee.setPhone(orderDetail.getConsigneePhone());
			pcaConsignee.setEmail(orderDetail.getConsigneeEmail());
			request.setConsignee(pcaConsignee);

			List<PCAItems> PCAItemList = new ArrayList<PCAItems>();
			PCAItems pcaItems = new PCAItems();
			pcaItems.setType("O");
			pcaItems.setName(orderDetail.getProductDescription());
			pcaItems.setQty("1");
			pcaItems.setPrice(String.valueOf(orderDetail.getValue()));
			pcaItems.setSku(orderDetail.getSku());
			PCAItemList.add(pcaItems);
			request.setItems(PCAItemList);
			if("STS".equalsIgnoreCase(serviceType)) {
				request.setChargecode(chargeCodeFW);
			}else {
				request.setChargecode(chargeCodeST);
				chargeType = "StarTrack";
			}
			pcaOrderInfoRequest.add(request);
		}
		pcaRequest.setShipments(pcaOrderInfoRequest);
		createShippingOrderFilePCA(orderDetailList, pcaRequest, userName, senderDataResponseList, chargeType);
	}

	private void createShippingOrderFilePCA(List<SenderData> orderDetailList, PCACreateShipmentRequest pcaRequest,
			String userName, List<SenderDataResponse> senderDataResponseList, String chargeType) throws EtowerFailureResponseException {
		Map<String, LabelData> barcodeMap = new HashMap<String, LabelData>();
		List<PCACreateShippingResponse> pcaResponse = pcaProxy.makeCallForCreateShippingOrder(pcaRequest);
		logPcaCreateResponse(pcaResponse);
		List<PCACreateShippingResponse> pcaFileResponseSuccess  = new ArrayList<PCACreateShippingResponse>();
		List<String> referenceNumber = new ArrayList<String>();
		for(PCACreateShippingResponse pcaData: pcaResponse) {
			if(!pcaData.getMsg().equalsIgnoreCase("Success")) {
				SenderDataResponse senderDataresponse = new SenderDataResponse();
				senderDataresponse.setReferenceNumber(pcaData.getMsg().split("-")[1].split(" ")[2]);
				senderDataresponse.setMessage(pcaData.getMsg());
				senderDataResponseList.add(senderDataresponse);
				referenceNumber.add(pcaData.getMsg().split("-")[1].split(" ")[2]);
			}else {
				pcaFileResponseSuccess.add(pcaData);
			}
		}
		
		List<SenderData> pflSenderData = new ArrayList<SenderData>();
		PFLSenderDataFileRequest pflRequest = new PFLSenderDataFileRequest();
		if(referenceNumber.size() > 0) {
			orderDetailList.forEach(obj -> {
				if(!referenceNumber.contains(obj.getReferenceNumber())) {
					pflSenderData.add(obj);
				}
			});
			pflRequest.setPflSenderDataApi(pflSenderData);
		}else {
			pflRequest.setPflSenderDataApi(orderDetailList);
		}
		
		if(pcaFileResponseSuccess.size() > 0) {
			processPcaLabelsResponse(pcaFileResponseSuccess, barcodeMap, chargeType);
			String senderFileID = d2zDao.exportParcel(pflRequest.getPflSenderDataApi(), barcodeMap);
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
	
	private void logPcaCreateResponse(List<PCACreateShippingResponse> pcaResponse)throws EtowerFailureResponseException {
		List<ETowerResponse> responseEntity = new ArrayList<ETowerResponse>();
		if(pcaResponse != null) {
			for(PCACreateShippingResponse pcaData: pcaResponse) {
				if(!pcaData.getMsg().equalsIgnoreCase("Success")) {
					ETowerResponse errorResponse = new ETowerResponse();
					errorResponse.setAPIName("PCA - Create order");
					errorResponse.setErrorCode(String.valueOf(pcaData.getStatus()));
					errorResponse.setErrorMessage(pcaData.getMsg());
					errorResponse.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
					errorResponse.setStatus("Error");
					responseEntity.add(errorResponse);
				}else {
					ETowerResponse errorResponse = new ETowerResponse();
					errorResponse.setAPIName("PCA - Create order");
					errorResponse.setReferenceNumber(pcaData.getCustref());
					errorResponse.setOrderId(pcaData.getConnote());
					errorResponse.setTrackingNo(pcaData.getRef());
					errorResponse.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
					errorResponse.setStatus("Success");
					responseEntity.add(errorResponse);
				}
			}
			d2zDao.logEtowerResponse(responseEntity);
		}
	}

}
