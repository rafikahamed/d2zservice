package com.d2z.d2zservice.wrapper;

import java.math.BigDecimal;
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
import com.d2z.d2zservice.model.SenderData;
import com.d2z.d2zservice.model.SenderDataApi;
import com.d2z.d2zservice.model.SenderDataResponse;
import com.d2z.d2zservice.model.etower.LabelData;
import com.d2z.d2zservice.proxy.PcaProxy;

@Service
public class PCAWrapper {

	@Autowired
	private PcaProxy pcaProxy;

	@Value("${pca.chargeCode}")
	private String chargeCode;

	@Autowired
	private ID2ZDao d2zDao;

	public void makeCreateShippingOrderPFACall(List<SenderDataApi> consignmentData,
			List<SenderDataResponse> senderDataResponseList, String userName) throws EtowerFailureResponseException {
		PCACreateShipmentRequest pcaRequest = new PCACreateShipmentRequest();
		List<PCACreateShipmentRequestInfo> pcaOrderInfoRequest = new ArrayList<PCACreateShipmentRequestInfo>();
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
			request.setChargecode(chargeCode);
			pcaOrderInfoRequest.add(request);
		}
		pcaRequest.setShipments(pcaOrderInfoRequest);
		createShippingOrderPCA(consignmentData, pcaRequest, userName, senderDataResponseList);
	}

	private void createShippingOrderPCA(List<SenderDataApi> consignmentData, PCACreateShipmentRequest pcaRequest,
			String userName, List<SenderDataResponse> senderDataResponseList) throws EtowerFailureResponseException {
		Map<String, LabelData> barcodeMap = new HashMap<String, LabelData>();
		PCACreateShippingResponse pcaResponse = pcaProxy.makeCallForCreateShippingOrder(pcaRequest);
		logPcaCreateResponse(pcaResponse);
		if (!pcaResponse.getMsg().equalsIgnoreCase("Success")) {
			throw new EtowerFailureResponseException("Error in file – please contact customer support");
		} else {
			if (pcaResponse.getMsg().equalsIgnoreCase("Success")) {
				processPcaLabelsResponse(pcaResponse, barcodeMap);
				int userId = d2zDao.fetchUserIdbyUserName(userName);
				String senderFileID = d2zDao.createConsignments(consignmentData, userId, userName, barcodeMap);
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

	private Map<String, LabelData> processPcaLabelsResponse(PCACreateShippingResponse pcaResponse,
			Map<String, LabelData> barcodeMap) {
	//		for (PFLResponseData data : pflResponse.getResult()) {
	//			LabelData labelData = new LabelData();
	//			labelData.setReferenceNo(data.getReference());
	//			labelData.setArticleId(data.getId());
	//			labelData.setTrackingNo(data.getTracking());
	//			labelData.setHub(data.getHub());
	//			labelData.setMatrix(data.getMatrix());
	//			labelData.setProvider("PCA");
	//			barcodeMap.put(data.getReference(), labelData);
	//		}
		return barcodeMap;
	}

	public void makeCreateShippingOrderFilePCACall(List<SenderData> orderDetailList,
			List<SenderDataResponse> senderDataResponseList, String userName) throws EtowerFailureResponseException {
		PCACreateShipmentRequest pcaRequest = new PCACreateShipmentRequest();
		List<PCACreateShipmentRequestInfo> pcaOrderInfoRequest = new ArrayList<PCACreateShipmentRequestInfo>();
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
			request.setChargecode(chargeCode);
			pcaOrderInfoRequest.add(request);
		}
		pcaRequest.setShipments(pcaOrderInfoRequest);
		createShippingOrderFilePCA(orderDetailList, pcaRequest, userName, senderDataResponseList);
	}

	private void createShippingOrderFilePCA(List<SenderData> orderDetailList, PCACreateShipmentRequest pcaRequest,
			String userName, List<SenderDataResponse> senderDataResponseList) throws EtowerFailureResponseException {
		Map<String, LabelData> barcodeMap = new HashMap<String, LabelData>();
		PCACreateShippingResponse pcaResponse = pcaProxy.makeCallForCreateShippingOrder(pcaRequest);
		logPcaCreateResponse(pcaResponse);
		if (!pcaResponse.getMsg().equalsIgnoreCase("Success")) {
			throw new EtowerFailureResponseException("Error in file – please contact customer support");
		} else {
			if (pcaResponse != null) {
				processPcaLabelsResponse(pcaResponse, barcodeMap);
				String senderFileID = d2zDao.exportParcel(orderDetailList, barcodeMap);
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
	
	private void logPcaCreateResponse(PCACreateShippingResponse pcaResponse)throws EtowerFailureResponseException {
		List<ETowerResponse> responseEntity = new ArrayList<ETowerResponse>();
		if(pcaResponse != null) {
//			for(PFLResponseData pflData: pflResponse.getResult()) {
//				if(pflData.getCode() != null) {
//					ETowerResponse errorResponse = new ETowerResponse();
//					errorResponse.setAPIName("PFL - Create order");
//					errorResponse.setErrorCode(pflData.getCode());
//					errorResponse.setErrorMessage(pflData.getError());
//					errorResponse.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
//					errorResponse.setStatus("Error");
//					responseEntity.add(errorResponse);
//					d2zDao.logEtowerResponse(responseEntity);
//					throw new EtowerFailureResponseException("Error in file – please contact customer support");
//				}else {
//					ETowerResponse errorResponse = new ETowerResponse();
//					errorResponse.setAPIName("PFL - Create order");
//					errorResponse.setReferenceNumber(pflData.getReference());
//					errorResponse.setOrderId(pflData.getId());
//					errorResponse.setTrackingNo(pflData.getTracking());
//					errorResponse.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
//					errorResponse.setStatus("Success");
//					responseEntity.add(errorResponse);
//				}
//			}
			d2zDao.logEtowerResponse(responseEntity);
		}
	}

}
