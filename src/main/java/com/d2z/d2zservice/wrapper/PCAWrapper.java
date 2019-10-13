package com.d2z.d2zservice.wrapper;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.d2z.d2zservice.dao.ID2ZDao;
import com.d2z.d2zservice.entity.ETowerResponse;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.exception.EtowerFailureResponseException;
import com.d2z.d2zservice.model.PCACancelRequest;
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
import com.d2z.d2zservice.util.D2ZCommonUtil;

@Service
public class PCAWrapper {

	@Autowired
	private PcaProxy pcaProxy;

	@Value("${pca.chargeCodeFW}")
	private String chargeCodeFW;
	
	@Value("${pca.chargeCodeST}")
	private String chargeCodeST;

	@Autowired
	private ID2ZDao d2zDao;

	public void makeCreateShippingOrderPFACall(List<SenderDataApi> consignmentData,
			List<SenderDataResponse> senderDataResponseList, String userName, String serviceType) throws EtowerFailureResponseException {
		System.out.println("comes in pfa call");
		Map<String, String> systemRefNbrMap = new HashMap<String, String>();
		PCACreateShipmentRequest pcaRequest = new PCACreateShipmentRequest();
		List<PCACreateShipmentRequestInfo> pcaOrderInfoRequest = new ArrayList<PCACreateShipmentRequestInfo>();
		String chargeType = "FastwayS";
		Map<String, String> matrixMap = new HashMap<String, String>();
		for (SenderDataApi orderDetail : consignmentData) {
			PCACreateShipmentRequestInfo request = new PCACreateShipmentRequestInfo();
			 Random rnd = new Random();
			 int uniqueNumber = 1000000 + rnd.nextInt(9000000);
    		 String sysRefNbr = "CR"+uniqueNumber;
    		 request.setCust_ref(sysRefNbr);
 			systemRefNbrMap.put(request.getCust_ref(), orderDetail.getReferenceNumber());

			//request.setCust_ref(orderDetail.getReferenceNumber());
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
			/*pcaReceiver.setPhone(orderDetail.getConsigneePhone());*/
			pcaReceiver.setPhone("040040040");
			pcaReceiver.setEmail(orderDetail.getConsigneeEmail());
			request.setReceiver(pcaReceiver);

			PCAConsignee pcaConsignee = new PCAConsignee();
			pcaConsignee.setName(orderDetail.getConsigneeName());
			pcaConsignee.setAddress(orderDetail.getConsigneeAddr1());
			pcaConsignee.setState(orderDetail.getConsigneeState());
			pcaConsignee.setCity(orderDetail.getConsigneeSuburb());
			pcaConsignee.setSuburb(orderDetail.getConsigneeSuburb());
			pcaConsignee.setPostcode(orderDetail.getConsigneePostcode());
			//pcaConsignee.setPhone(orderDetail.getConsigneePhone());
			pcaConsignee.setPhone("040040040");
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
			if("STS-Sub".equalsIgnoreCase(serviceType)) {
				request.setChargecode(chargeCodeST);
				chargeType = "StarTrack";
			}else {
				request.setChargecode(chargeCodeFW);
			}
			pcaOrderInfoRequest.add(request);
			matrixMap.put(orderDetail.getReferenceNumber(), orderDetail.getConsigneeName()+"||"+orderDetail.getConsigneeAddr1()+"||"+
					orderDetail.getConsigneeSuburb()+"||"+orderDetail.getConsigneePostcode()+"|"+orderDetail.getConsigneePhone()+"||"+
					orderDetail.getConsigneeName());
		}
		pcaRequest.setShipments(pcaOrderInfoRequest);
		createShippingOrderPCA(consignmentData, pcaRequest, userName, senderDataResponseList, chargeType, matrixMap,systemRefNbrMap);
	}
	public void makeCreateShippingOrderPCACall(List<SenderdataMaster> consignmentData) throws EtowerFailureResponseException {
		System.out.println("comes in pfa call");
		PCACreateShipmentRequest pcaRequest = new PCACreateShipmentRequest();
		List<PCACreateShipmentRequestInfo> pcaOrderInfoRequest = new ArrayList<PCACreateShipmentRequestInfo>();
		//String chargeType = "FastwayS";
	
		for (SenderdataMaster orderDetail : consignmentData) {
			PCACreateShipmentRequestInfo request = new PCACreateShipmentRequestInfo();
			request.setNo(orderDetail.getArticleId().substring(0, 10));
			request.setDirect("1");
			 Random rnd = new Random();
			 int uniqueNumber = 1000000 + rnd.nextInt(9000000);
    		 String sysRefNbr = "CR"+uniqueNumber;
    		 request.setCust_ref(sysRefNbr);

			//request.setCust_ref(orderDetail.getReference_number());
			request.setType("10");
			request.setPacks("1");

			List<PCAPackages> pcaPackageList = new ArrayList<PCAPackages>();
			PCAPackages pcaPackages = new PCAPackages();
			pcaPackages.setWeight(new BigDecimal(orderDetail.getWeight()));
			pcaPackages.setHeight(orderDetail.getDimensions_Height());
			pcaPackages.setWidth(orderDetail.getDimensions_Width());
			pcaPackages.setLength(orderDetail.getDimensions_Length());
			pcaPackageList.add(pcaPackages);
			request.setPackages(pcaPackageList);

			request.setWeight(String.valueOf(orderDetail.getWeight()));
			request.setCbm(new BigDecimal(orderDetail.getWeight()));
			request.setCurrency(orderDetail.getCurrency());

			PCAShipper pcaShipper = new PCAShipper();
			pcaShipper.setName(orderDetail.getShipper_Name());
			pcaShipper.setAddress(orderDetail.getShipper_Addr1());
			pcaShipper.setState(orderDetail.getStatus());
			pcaShipper.setCity(orderDetail.getShipper_City());
			pcaShipper.setSuburb(orderDetail.getShipper_City());
			pcaShipper.setPostcode(orderDetail.getShipper_Postcode());
			request.setShipper(pcaShipper);

			PCAReceiver pcaReceiver = new PCAReceiver();
			pcaReceiver.setName(orderDetail.getConsignee_name());
			pcaReceiver.setAddress(orderDetail.getConsignee_addr1());
			pcaReceiver.setState(orderDetail.getConsignee_State());
			pcaReceiver.setCity(orderDetail.getConsignee_Suburb());
			pcaReceiver.setSuburb(orderDetail.getConsignee_Suburb());
			pcaReceiver.setPostcode(orderDetail.getConsignee_Postcode());
			//pcaReceiver.setPhone(orderDetail.getConsignee_Phone());
			pcaReceiver.setPhone("040040040");
			pcaReceiver.setEmail(orderDetail.getConsignee_Email());
			request.setReceiver(pcaReceiver);

			PCAConsignee pcaConsignee = new PCAConsignee();
			pcaConsignee.setName(orderDetail.getConsignee_name());
			pcaConsignee.setAddress(orderDetail.getConsignee_addr1());
			pcaConsignee.setState(orderDetail.getConsignee_State());
			pcaConsignee.setCity(orderDetail.getConsignee_Suburb());
			pcaConsignee.setSuburb(orderDetail.getConsignee_Suburb());
			pcaConsignee.setPostcode(orderDetail.getConsignee_Postcode());
			//pcaConsignee.setPhone(orderDetail.getConsignee_Phone());
			pcaConsignee.setPhone("040040040");
			pcaConsignee.setEmail(orderDetail.getConsignee_Email());
			request.setConsignee(pcaConsignee);

			List<PCAItems> PCAItemList = new ArrayList<PCAItems>();
			PCAItems pcaItems = new PCAItems();
			pcaItems.setType("O");
			pcaItems.setName(orderDetail.getProduct_Description());
			pcaItems.setQty("1");
			pcaItems.setPrice(String.valueOf(orderDetail.getValue()));
			pcaItems.setSku(orderDetail.getSku());
			PCAItemList.add(pcaItems);
			request.setItems(PCAItemList);
			request.setChargecode("2816");
			
			pcaOrderInfoRequest.add(request);
		
		}
		pcaRequest.setShipments(pcaOrderInfoRequest);
		createShippingOrderPCA( pcaRequest);
	}

	private void createShippingOrderPCA(PCACreateShipmentRequest pcaRequest) throws EtowerFailureResponseException {
		List<PCACreateShippingResponse> pcaResponse = pcaProxy.makeCallForCreateShippingOrder(pcaRequest);
		logPcaCreateResponse(pcaResponse);
		
	}
	private void createShippingOrderPCA(List<SenderDataApi> consignmentData, PCACreateShipmentRequest pcaRequest,
			String userName, List<SenderDataResponse> senderDataResponseList, String chargeType, Map<String, String> matrixMap,Map<String, String> systemRefNbrMap) throws EtowerFailureResponseException {
		System.out.print("comes in pcs");
		Map<String, LabelData> barcodeMap = new HashMap<String, LabelData>();
		List<PCACreateShippingResponse> pcaResponse = pcaProxy.makeCallForCreateShippingOrder(pcaRequest);
		logPcaCreateResponse(pcaResponse);
		List<PCACreateShippingResponse> pcaResponseSuccess  = new ArrayList<PCACreateShippingResponse>();
		List<String> referenceNumber = new ArrayList<String>();
		for(PCACreateShippingResponse pcaData: pcaResponse) {
			if(!pcaData.getMsg().equalsIgnoreCase("Success")) {
				SenderDataResponse senderDataresponse = new SenderDataResponse();
				//senderDataresponse.setReferenceNumber(pcaData.getMsg().split("-")[1].split(" ")[2]);
				senderDataresponse.setMessage(D2ZCommonUtil.formatPCAMessage(pcaData.getMsg()));
				senderDataResponseList.add(senderDataresponse);
				referenceNumber.add(pcaData.getMsg().split("-")[1].split(" ")[2]);
			}else {
				pcaResponseSuccess.add(pcaData);
			}
		}
		
		if(referenceNumber.size() > 0) {
			for(PCACreateShipmentRequestInfo pcaRequestData: pcaRequest.getShipments()) {
				PCACancelRequest pcaReq = new PCACancelRequest();
				pcaReq.setCustref(pcaRequestData.getCust_ref());
				pcaProxy.makeCallForCancelShipment(pcaReq);
			}
			return;
		}
		
//		List<SenderDataApi> pflSenderData = new ArrayList<SenderDataApi>();
//		PFLSenderDataRequest pflRequest = new PFLSenderDataRequest();
//		if(referenceNumber.size() > 0) {
//			consignmentData.forEach(obj -> {
//				if(!referenceNumber.contains(obj.getReferenceNumber())) {
//					pflSenderData.add(obj);
//				}
//			});
//			pflRequest.setPflSenderDataApi(pflSenderData);
//		}else {
//			pflRequest.setPflSenderDataApi(consignmentData);
//		}
		
		if(pcaResponseSuccess.size() > 0) {
			processPcaLabelsResponse(pcaResponseSuccess, barcodeMap, chargeType, matrixMap,systemRefNbrMap);
			int userId = d2zDao.fetchUserIdbyUserName(userName);
			String senderFileID = d2zDao.createConsignments(consignmentData, userId, userName, barcodeMap);
			
			List<String> insertedOrder = d2zDao.fetchBySenderFileID(senderFileID);
			Iterator itr = insertedOrder.iterator();
			while (itr.hasNext()) {
				Object[] obj = (Object[]) itr.next();
				SenderDataResponse senderDataresponse = new SenderDataResponse();
				senderDataresponse.setReferenceNumber(obj[0].toString());
				senderDataresponse.setDatamatrix(obj[1].toString());
				String barcode = obj[1].toString();
				if(chargeType.equals("FastwayS"))
				{
					senderDataresponse.setBarcodeLabelNumber(obj[3] != null ? obj[3].toString() : "");
				}
				else
				{
				senderDataresponse.setBarcodeLabelNumber("]d2".concat(barcode.replaceAll("\\[|\\]", "")));
				}
				//
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

	private Map<String, LabelData> processPcaLabelsResponse(List<PCACreateShippingResponse> pcaResponse, Map<String, LabelData> barcodeMap, 
				String chargeType, Map<String, String> matrixMap,Map<String, String> systemRefNbrMap) {
			for (PCACreateShippingResponse data : pcaResponse) {
				if(data.getMsg().equalsIgnoreCase("Success")) {
					LabelData labelData = new LabelData();
					labelData.setReferenceNo(systemRefNbrMap.get(data.getCustref()));
					labelData.setArticleId(data.getConnote());
					labelData.setTrackingNo(data.getRef());
					labelData.setHub(data.getDepot());
					labelData.setMatrix(matrixMap.get(labelData.getReferenceNo()));
					labelData.setProvider("PCA");
					if("StarTrack".equalsIgnoreCase(chargeType)) {
						labelData.setCarrier("StarTrack");
					}else {
						labelData.setCarrier("FastwayS");
					}
					barcodeMap.put(labelData.getReferenceNo(), labelData);
				}
			}
		return barcodeMap;
	}

	public void makeCreateShippingOrderFilePCACall(List<SenderData> orderDetailList,
			List<SenderDataResponse> senderDataResponseList, String userName, String serviceType) throws EtowerFailureResponseException {
		PCACreateShipmentRequest pcaRequest = new PCACreateShipmentRequest();
		Map<String, String> systemRefNbrMap = new HashMap<String, String>();
		List<PCACreateShipmentRequestInfo> pcaOrderInfoRequest = new ArrayList<PCACreateShipmentRequestInfo>();
		String chargeType = "FastwayS";
		Map<String, String> matrixMap = new HashMap<String, String>();
		for (SenderData orderDetail : orderDetailList) {
			PCACreateShipmentRequestInfo request = new PCACreateShipmentRequestInfo();
			Random rnd = new Random();
			 int uniqueNumber = 1000000 + rnd.nextInt(9000000);
			 String sysRefNbr = "CR"+uniqueNumber;
			 request.setCust_ref(sysRefNbr);
			systemRefNbrMap.put(request.getCust_ref(), orderDetail.getReferenceNumber());

			//request.setCust_ref(orderDetail.getReferenceNumber());
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
			//pcaReceiver.setPhone(orderDetail.getConsigneePhone());
			pcaReceiver.setPhone("040040040");
			pcaReceiver.setEmail(orderDetail.getConsigneeEmail());
			request.setReceiver(pcaReceiver);

			PCAConsignee pcaConsignee = new PCAConsignee();
			pcaConsignee.setName(orderDetail.getConsigneeName());
			pcaConsignee.setAddress(orderDetail.getConsigneeAddr1());
			pcaConsignee.setState(orderDetail.getConsigneeState());
			pcaConsignee.setCity(orderDetail.getConsigneeSuburb());
			pcaConsignee.setSuburb(orderDetail.getConsigneeSuburb());
			pcaConsignee.setPostcode(orderDetail.getConsigneePostcode());
			//pcaConsignee.setPhone(orderDetail.getConsigneePhone());
			pcaConsignee.setPhone("040040040");
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
			if("STS-Sub".equalsIgnoreCase(serviceType)) {
				request.setChargecode(chargeCodeST);
				chargeType = "StarTrack";
			}else {
				request.setChargecode(chargeCodeFW);
			}
			pcaOrderInfoRequest.add(request);
			matrixMap.put(orderDetail.getReferenceNumber(), orderDetail.getConsigneeName()+"||"+orderDetail.getConsigneeAddr1()+"||"+
					orderDetail.getConsigneeSuburb()+"||"+orderDetail.getConsigneePostcode()+"|"+orderDetail.getConsigneePhone()+"||"+
					orderDetail.getConsigneeName());
		}
		pcaRequest.setShipments(pcaOrderInfoRequest);
		createShippingOrderFilePCA(orderDetailList, pcaRequest, userName, senderDataResponseList, chargeType, matrixMap,systemRefNbrMap);
	}

	private void createShippingOrderFilePCA(List<SenderData> orderDetailList, PCACreateShipmentRequest pcaRequest,
			String userName, List<SenderDataResponse> senderDataResponseList, String chargeType, Map<String, String> matrixMap, Map<String, String> systemRefNbrMap) throws EtowerFailureResponseException {
		Map<String, LabelData> barcodeMap = new HashMap<String, LabelData>();
		List<PCACreateShippingResponse> pcaResponse = pcaProxy.makeCallForCreateShippingOrder(pcaRequest);
		logPcaCreateResponse(pcaResponse);
		List<PCACreateShippingResponse> pcaFileResponseSuccess  = new ArrayList<PCACreateShippingResponse>();
		List<String> referenceNumber = new ArrayList<String>();
		for(PCACreateShippingResponse pcaData: pcaResponse) {
			if(!pcaData.getMsg().equalsIgnoreCase("Success")) {
				SenderDataResponse senderDataresponse = new SenderDataResponse();
				//senderDataresponse.setReferenceNumber(pcaData.getMsg().split("-")[1].split(" ")[2]);
				senderDataresponse.setMessage(D2ZCommonUtil.formatPCAMessage(pcaData.getMsg()));
				senderDataResponseList.add(senderDataresponse);
				referenceNumber.add(pcaData.getMsg().split("-")[1].split(" ")[2]);
			}else {
				pcaFileResponseSuccess.add(pcaData);
			}
		}
		
		if(referenceNumber.size() > 0) {
			for(PCACreateShipmentRequestInfo pcaRequestData: pcaRequest.getShipments()) {
				PCACancelRequest pcaReq = new PCACancelRequest();
				pcaReq.setCustref(pcaRequestData.getCust_ref());
				pcaProxy.makeCallForCancelShipment(pcaReq);
			}
			return;
		}
		
//		List<SenderData> pflSenderData = new ArrayList<SenderData>();
//		PFLSenderDataFileRequest pflRequest = new PFLSenderDataFileRequest();
//		if(referenceNumber.size() > 0) {
//			orderDetailList.forEach(obj -> {
//				if(!referenceNumber.contains(obj.getReferenceNumber())) {
//					pflSenderData.add(obj);
//				}
//			});
//			pflRequest.setPflSenderDataApi(pflSenderData);
//		}else {
//			pflRequest.setPflSenderDataApi(orderDetailList);
//		}
//		
		if(pcaFileResponseSuccess.size() > 0) {
			processPcaLabelsResponse(pcaFileResponseSuccess, barcodeMap, chargeType, matrixMap,systemRefNbrMap);
			String senderFileID = d2zDao.exportParcel(orderDetailList, barcodeMap);
			List<String> insertedOrder = d2zDao.fetchBySenderFileID(senderFileID);
			Iterator itr = insertedOrder.iterator();
			while (itr.hasNext()) {
				Object[] obj = (Object[]) itr.next();
				SenderDataResponse senderDataresponse = new SenderDataResponse();
				senderDataresponse.setReferenceNumber(obj[0].toString());
				senderDataresponse.setDatamatrix(obj[1].toString());
				senderDataresponse.setBarcodeLabelNumber(obj[3] != null ? obj[3].toString() : "");
				senderDataresponse.setCarrier(obj[4] != null ? obj[4].toString() : "");
				senderDataresponse.setInjectionPort(obj[5] != null ? obj[5].toString() : "");
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
					errorResponse.setAPIName("NEX - Create order");
					errorResponse.setErrorCode(String.valueOf(pcaData.getStatus()));
					errorResponse.setErrorMessage(pcaData.getMsg());
					errorResponse.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
					errorResponse.setStatus("Error");
					responseEntity.add(errorResponse);
				}else {
					ETowerResponse errorResponse = new ETowerResponse();
					errorResponse.setAPIName("NEX - Create order");
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
	
	public byte[] pcalabel(String refnum){
		PCACancelRequest pcaReq = new PCACancelRequest();
		pcaReq.setCustref(refnum);
		return pcaProxy.makeCallForLabelShipment(pcaReq);
	}

	public void deletePcaOrder(List<String> pcaArticleid) {
		pcaArticleid.stream().forEach((refrenceNumber) -> {
			System.out.println(refrenceNumber);
			PCACancelRequest pcaReq = new PCACancelRequest();
			pcaReq.setCustref(refrenceNumber);
			PCACreateShippingResponse pcaDeleteResponse = pcaProxy.makeCallForCancelShipment(pcaReq);
		});
	}

}
