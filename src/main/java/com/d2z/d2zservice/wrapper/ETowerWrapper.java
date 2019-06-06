package com.d2z.d2zservice.wrapper;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.d2z.d2zservice.dao.ID2ZDao;
import com.d2z.d2zservice.entity.ETowerResponse;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.exception.EtowerFailureResponseException;
import com.d2z.d2zservice.model.CreateConsignmentRequest;
import com.d2z.d2zservice.model.SenderData;
import com.d2z.d2zservice.model.SenderDataApi;
import com.d2z.d2zservice.model.SenderDataResponse;
import com.d2z.d2zservice.model.etower.CreateShippingRequest;
import com.d2z.d2zservice.model.etower.CreateShippingResponse;
import com.d2z.d2zservice.model.etower.EtowerErrorResponse;
import com.d2z.d2zservice.model.etower.GainLabelsResponse;
import com.d2z.d2zservice.model.etower.LabelData;
import com.d2z.d2zservice.model.etower.ResponseData;
import com.d2z.d2zservice.proxy.ETowerProxy;
import com.d2z.singleton.D2ZSingleton;

@Service
public class ETowerWrapper {

	@Autowired
	private ID2ZDao d2zDao;

	@Autowired
	private ETowerProxy eTowerProxy;

	public void makeCreateShippingOrderEtowerCallForAPIData(CreateConsignmentRequest data,
			List<SenderDataResponse> senderDataResponseList) throws EtowerFailureResponseException {
		List<com.d2z.d2zservice.model.etower.CreateShippingRequest> eTowerRequest = constructEtowerRequestWithAPIData(data);
		String status = null;
		Map<String, LabelData> barcodeMap = new HashMap<String, LabelData>();
		List<String> gainLabelTrackingNo = new ArrayList<String>();

		if (!eTowerRequest.isEmpty()) {
			CreateShippingResponse response = eTowerProxy.makeCallForCreateShippingOrder(eTowerRequest);
			status = parseCreateShippingOrderResponse(response, senderDataResponseList, barcodeMap,
					gainLabelTrackingNo);
		}

		if (gainLabelTrackingNo.size() == data.getConsignmentData().size()) {

			Runnable r = new Runnable() {
				public void run() {
					GainLabelsResponse gainLabelResponse = eTowerProxy.makeCallToGainLabels(gainLabelTrackingNo);
					processGainLabelsResponse(gainLabelResponse, barcodeMap);

					int userId = d2zDao.fetchUserIdbyUserName(data.getUserName());
					d2zDao.createConsignments(data.getConsignmentData(), userId, data.getUserName(), barcodeMap);

				}
			};
			new Thread(r).start();
		} else {
			if (null != status) {
				GainLabelsResponse gainLabelResponse = eTowerProxy.makeCallToGainLabels(gainLabelTrackingNo);
				processGainLabelsResponse(gainLabelResponse, barcodeMap);
			}
			int userId = d2zDao.fetchUserIdbyUserName(data.getUserName());
			String senderFileID = d2zDao.createConsignments(data.getConsignmentData(), userId, data.getUserName(),
					barcodeMap);

			List<String> insertedOrder = d2zDao.fetchBySenderFileID(senderFileID);
			senderDataResponseList.clear();
			Iterator itr = insertedOrder.iterator();
			while (itr.hasNext()) {
				Object[] obj = (Object[]) itr.next();
				SenderDataResponse senderDataresponse = new SenderDataResponse();
				senderDataresponse.setReferenceNumber(obj[0].toString());
				senderDataresponse.setBarcodeLabelNumber(obj[2] != null ? obj[2].toString() : "");
				senderDataResponseList.add(senderDataresponse);
			}
		}

	}

	public void makeCreateShippingOrderEtowerCallForFileData(List<SenderData> data,
			List<SenderDataResponse> senderDataResponseList) throws EtowerFailureResponseException {
		List<com.d2z.d2zservice.model.etower.CreateShippingRequest> eTowerRequest = constructEtowerRequestWithFileData(
				data);
		String status = null;
		Map<String, LabelData> barcodeMap = new HashMap<String, LabelData>();
		List<String> gainLabelTrackingNo = new ArrayList<String>();

		if (!eTowerRequest.isEmpty()) {
			CreateShippingResponse response = eTowerProxy.makeCallForCreateShippingOrder(eTowerRequest);
			status = parseCreateShippingOrderResponse(response, senderDataResponseList, barcodeMap,
					gainLabelTrackingNo);
		}

		if (gainLabelTrackingNo.size() == data.size()) {

			Runnable r = new Runnable() {
				public void run() {
					GainLabelsResponse gainLabelResponse = eTowerProxy.makeCallToGainLabels(gainLabelTrackingNo);
					processGainLabelsResponse(gainLabelResponse, barcodeMap);

					d2zDao.exportParcel(data, barcodeMap);

				}
			};
			new Thread(r).start();
		} else {
			if (("Partial Success").equalsIgnoreCase(status)) {
				GainLabelsResponse gainLabelResponse = eTowerProxy.makeCallToGainLabels(gainLabelTrackingNo);
				processGainLabelsResponse(gainLabelResponse, barcodeMap);
			}
			String senderFileID = d2zDao.exportParcel(data, barcodeMap);

			List<String> insertedOrder = d2zDao.fetchBySenderFileID(senderFileID);
			senderDataResponseList.clear();
			Iterator itr = insertedOrder.iterator();
			while (itr.hasNext()) {
				Object[] obj = (Object[]) itr.next();
				SenderDataResponse senderDataresponse = new SenderDataResponse();
				senderDataresponse.setReferenceNumber(obj[0].toString());
				senderDataresponse.setBarcodeLabelNumber(obj[2] != null ? obj[2].toString() : "");
				senderDataResponseList.add(senderDataresponse);
			}
		}

	}

	private List<CreateShippingRequest> constructEtowerRequestWithFileData(List<SenderData> data) {
		List<com.d2z.d2zservice.model.etower.CreateShippingRequest> eTowerRequest = new ArrayList<com.d2z.d2zservice.model.etower.CreateShippingRequest>();
		Map<String, String> postCodeZoneMap = D2ZSingleton.getInstance().getPostCodeZoneMap();
		ListIterator<SenderData> iterator = data.listIterator();
		// for(SenderData orderDetail : data) {
		while (iterator.hasNext()) {
			SenderData orderDetail = iterator.next();
			com.d2z.d2zservice.model.etower.CreateShippingRequest request = new com.d2z.d2zservice.model.etower.CreateShippingRequest();

			request.setReferenceNo(orderDetail.getReferenceNumber());
			request.setRecipientCompany(orderDetail.getConsigneeCompany());
			String recpName = orderDetail.getConsigneeName().length() > 34
					? orderDetail.getConsigneeName().substring(0, 34)
					: orderDetail.getConsigneeName();
			request.setRecipientName(recpName);
			request.setAddressLine1(orderDetail.getConsigneeAddr1());
			request.setAddressLine2(orderDetail.getConsigneeAddr2());
			request.setEmail(orderDetail.getConsigneeEmail());
			request.setCity(orderDetail.getConsigneeSuburb());
			request.setState(orderDetail.getConsigneeState());
			request.setPostcode(orderDetail.getConsigneePostcode());
			Double weight = Double.valueOf(orderDetail.getWeight());
			if ("1PM3E".equalsIgnoreCase(orderDetail.getServiceType())) {
				request.setFacility("MEL3");

				orderDetail.setInjectionState("MEL3");
				orderDetail.setCarrier("Express");
			} else if ("1PS2".equalsIgnoreCase(orderDetail.getServiceType())) {
				request.setFacility("SYD2");
				orderDetail.setInjectionState("SYD2");

			} else if ("1PS3".equalsIgnoreCase(orderDetail.getServiceType())) {
				String zoneId = postCodeZoneMap
						.get(orderDetail.getConsigneeSuburb().trim().concat(orderDetail.getConsigneePostcode().trim()));
				if (weight > 3) {
					List<String> bneDestination = Stream.of("Q0", "Q1", "Q2", "Q3", "Q4", "Q5")
							.collect(Collectors.toList());
					boolean containsDest = bneDestination.stream().anyMatch(zoneId::equalsIgnoreCase);
					if (containsDest) {
						request.setFacility("BNE");
						orderDetail.setInjectionState("BNE");

					} else {
						request.setFacility("SYD");
						orderDetail.setInjectionState("SYD");

					}
				} else {
					List<String> sydDestination = Stream.of("CB", "GF", "N0", "N1", "N2", "N3", "N4", "NC", "WG")
							.collect(Collectors.toList());
					boolean containsDest = sydDestination.stream().anyMatch(zoneId::equalsIgnoreCase);
					if (containsDest) {
						request.setFacility("SYD");
						orderDetail.setInjectionState("SYD");

					} else {
						iterator.set(orderDetail);
						continue;
					}
				}
			}
			else if("1PS5".equalsIgnoreCase(orderDetail.getServiceType())) {
				String zoneId = postCodeZoneMap
						.get(orderDetail.getConsigneeSuburb().trim().concat(orderDetail.getConsigneePostcode().trim()));
				List<String> eTowerZoneID = Stream.of("CB", "GF", "N0", "N1", "N3", "N4", "NC", "Q0" , "S0", "V0", "W0", "WG")
						.collect(Collectors.toList());
				boolean containsDest = eTowerZoneID.stream().anyMatch(zoneId::equalsIgnoreCase);
				if (containsDest) {
					request.setFacility("SYD2");
					orderDetail.setInjectionState("SYD2");

				} else {
					iterator.set(orderDetail);
					continue;
				}
			}
			if (("Express").equalsIgnoreCase(orderDetail.getCarrier())) {
				request.setServiceOption("Express-Post");
			} else {
				request.setServiceOption("E-Parcel");

			}
			request.setWeight(weight);
			request.setInvoiceValue(orderDetail.getValue());
			request.getOrderItems().get(0).setUnitValue(orderDetail.getValue());
			iterator.set(orderDetail);
			eTowerRequest.add(request);

		}
		return eTowerRequest;

	}

	private String parseCreateShippingOrderResponse(CreateShippingResponse response,
			List<SenderDataResponse> senderDataResponseList, Map<String, LabelData> barcodeMap,
			List<String> gainLabelTrackingNo) throws EtowerFailureResponseException {

		List<ETowerResponse> responseEntity = new ArrayList<ETowerResponse>();

		if (response == null) {
			throw new EtowerFailureResponseException("Failed. Please contact D2Z");
		} else {
			if (response.getStatus().equalsIgnoreCase("Success")) {
				for (ResponseData data : response.getData()) {
					ETowerResponse errorResponse = new ETowerResponse();
					errorResponse.setAPIName("Create Shipping Order");
					errorResponse.setStatus(data.getStatus());
					errorResponse.setOrderId(data.getOrderId());
					errorResponse.setReferenceNumber(data.getReferenceNo());
					errorResponse.setTrackingNo(data.getTrackingNo());
					errorResponse.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
					responseEntity.add(errorResponse);

					gainLabelTrackingNo.add(data.getTrackingNo());
					SenderDataResponse senderDataresponse = new SenderDataResponse();
					senderDataresponse.setReferenceNumber(data.getReferenceNo());
					senderDataresponse.setBarcodeLabelNumber(data.getTrackingNo());
					senderDataResponseList.add(senderDataresponse);

				}
				d2zDao.logEtowerResponse(responseEntity);

			} else if (response.getStatus().equalsIgnoreCase("Partial Success")) {
				for (ResponseData data : response.getData()) {
					List<EtowerErrorResponse> errors = data.getErrors();
					if (null == errors) {
						ETowerResponse errorResponse = new ETowerResponse();
						errorResponse.setAPIName("Create Shipping Order");
						errorResponse.setStatus(data.getStatus());
						errorResponse.setOrderId(data.getOrderId());
						errorResponse.setReferenceNumber(data.getReferenceNo());
						errorResponse.setTrackingNo(data.getTrackingNo());
						errorResponse.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
						responseEntity.add(errorResponse);
						gainLabelTrackingNo.add(data.getTrackingNo());
					} else {
						for (EtowerErrorResponse error : errors) {
							ETowerResponse errorResponse = new ETowerResponse();
							errorResponse.setAPIName("Create Shipping Order");
							errorResponse.setStatus(response.getStatus());
							errorResponse.setStatus(data.getStatus());
							errorResponse.setOrderId(data.getOrderId());
							errorResponse.setReferenceNumber(data.getReferenceNo());
							errorResponse.setTrackingNo(data.getTrackingNo());
							errorResponse.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
							errorResponse.setErrorCode(error.getCode());
							errorResponse.setErrorMessage(error.getMessage());
							responseEntity.add(errorResponse);
						}
					}
				}
				d2zDao.logEtowerResponse(responseEntity);

			} else if (response.getStatus().equalsIgnoreCase("Failure")) {
				for (ResponseData data : response.getData()) {
					List<EtowerErrorResponse> errors = data.getErrors();
					for (EtowerErrorResponse error : errors) {
						ETowerResponse errorResponse = new ETowerResponse();
						errorResponse.setAPIName("Create Shipping Order");
						errorResponse.setStatus(response.getStatus());
						errorResponse.setStatus(data.getStatus());
						errorResponse.setOrderId(data.getOrderId());
						errorResponse.setReferenceNumber(data.getReferenceNo());
						errorResponse.setTrackingNo(data.getTrackingNo());
						errorResponse.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
						errorResponse.setErrorCode(error.getCode());
						errorResponse.setErrorMessage(error.getMessage());
						responseEntity.add(errorResponse);
					}
				}
				d2zDao.logEtowerResponse(responseEntity);
				throw new EtowerFailureResponseException("Internal Server Error. Please contact D2Z");
			}

		}

		return response.getStatus();
	}

	private List<CreateShippingRequest> constructEtowerRequestWithAPIData(CreateConsignmentRequest data) {
		List<com.d2z.d2zservice.model.etower.CreateShippingRequest> eTowerRequest = new ArrayList<com.d2z.d2zservice.model.etower.CreateShippingRequest>();
		Map<String, String> postCodeZoneMap = D2ZSingleton.getInstance().getPostCodeZoneMap();
		 
		List<SenderDataApi> updatedOrderDetail = new ArrayList<SenderDataApi>();
		for (SenderDataApi orderDetail : data.getConsignmentData()) {
			com.d2z.d2zservice.model.etower.CreateShippingRequest request = new com.d2z.d2zservice.model.etower.CreateShippingRequest();

			request.setReferenceNo(orderDetail.getReferenceNumber());
			request.setRecipientCompany(orderDetail.getConsigneeCompany());
			String recpName = orderDetail.getConsigneeName().length() > 34
					? orderDetail.getConsigneeName().substring(0, 34)
					: orderDetail.getConsigneeName();
			request.setRecipientName(recpName);
			request.setAddressLine1(orderDetail.getConsigneeAddr1());
			request.setAddressLine2(orderDetail.getConsigneeAddr2());
			request.setEmail(orderDetail.getConsigneeEmail());
			request.setCity(orderDetail.getConsigneeSuburb());
			request.setState(orderDetail.getConsigneeState());
			request.setPostcode(orderDetail.getConsigneePostcode());
			Double weight = Double.valueOf(orderDetail.getWeight());
			if ("1PM3E".equalsIgnoreCase(orderDetail.getServiceType())) {
				request.setFacility("MEL3");
				orderDetail.setInjectionType("MEL3");
				orderDetail.setCarrier("Express");
			} else if ("1PS2".equalsIgnoreCase(orderDetail.getServiceType())) {
				request.setFacility("SYD2");
				orderDetail.setInjectionType("SYD2");
			} else if ("1PS3".equalsIgnoreCase(orderDetail.getServiceType())) {
				String zoneId = postCodeZoneMap
						.get(orderDetail.getConsigneeSuburb().trim().concat(orderDetail.getConsigneePostcode().trim()));
				if (weight > 3) {
					List<String> bneDestination = Stream.of("Q0", "Q1", "Q2", "Q3", "Q4", "Q5")
							.collect(Collectors.toList());
					boolean containsDest = bneDestination.stream().anyMatch(zoneId::equalsIgnoreCase);
					if (containsDest) {
						request.setFacility("BNE");
						orderDetail.setInjectionType("BNE");
					} else {
						request.setFacility("SYD");
						orderDetail.setInjectionType("SYD");
					}
				} else {
					List<String> sydDestination = Stream.of("CB", "GF", "N0", "N1", "N2", "N3", "N4", "NC", "WG")
							.collect(Collectors.toList());
					boolean containsDest = sydDestination.stream().anyMatch(zoneId::equalsIgnoreCase);
					if (containsDest) {
						request.setFacility("SYD");
						orderDetail.setInjectionType("SYD");
					} else {
						updatedOrderDetail.add(orderDetail);
						continue;
					}
				}
			}else if("1PS5".equalsIgnoreCase(orderDetail.getServiceType())) {
				String zoneId = postCodeZoneMap
						.get(orderDetail.getConsigneeSuburb().trim().concat(orderDetail.getConsigneePostcode().trim()));
				List<String> eTowerZoneID = Stream.of("CB", "GF", "N0", "N1", "N3", "N4", "NC", "Q0" , "S0", "V0", "W0", "WG")
						.collect(Collectors.toList());
				boolean containsDest = eTowerZoneID.stream().anyMatch(zoneId::equalsIgnoreCase);
				if (containsDest) {
					request.setFacility("SYD2");
					orderDetail.setInjectionState("SYD2");

				} else {
					updatedOrderDetail.add(orderDetail);
					continue;
				}
			}
			if (("Express").equalsIgnoreCase(orderDetail.getCarrier())) {
				request.setServiceOption("Express-Post");
			} else {
				request.setServiceOption("E-Parcel");

			}
			request.setWeight(weight);
			request.setInvoiceValue(orderDetail.getValue());
			request.getOrderItems().get(0).setUnitValue(orderDetail.getValue());
			eTowerRequest.add(request);
			updatedOrderDetail.add(orderDetail);
		}
		data.setConsignmentData(updatedOrderDetail);
		return eTowerRequest;
	}

	private Map<String, LabelData> processGainLabelsResponse(GainLabelsResponse response,
			Map<String, LabelData> barcodeMap) {
		List<ETowerResponse> responseEntity = new ArrayList<ETowerResponse>();
		if (response != null) {
			List<LabelData> responseData = response.getData();
			if (responseData == null && null != response.getErrors()) {
				for (EtowerErrorResponse error : response.getErrors()) {
					ETowerResponse errorResponse = new ETowerResponse();
					errorResponse.setAPIName("Gain Labels");
					errorResponse.setStatus(response.getStatus());
					errorResponse.setErrorCode(error.getCode());
					errorResponse.setErrorMessage(error.getMessage());
					responseEntity.add(errorResponse);
				}
			}

			for (LabelData data : responseData) {
				List<EtowerErrorResponse> errors = data.getErrors();
				if (null == errors) {
					ETowerResponse errorResponse = new ETowerResponse();
					errorResponse.setAPIName("Gain Labels");
					errorResponse.setStatus(data.getStatus());
					errorResponse.setOrderId(data.getOrderId());
					errorResponse.setReferenceNumber(data.getReferenceNo());
					errorResponse.setTrackingNo(data.getTrackingNo());
					errorResponse.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
					responseEntity.add(errorResponse);
					data.setProvider("Etower");
					barcodeMap.put(data.getReferenceNo(), data);
				} else {
					for (EtowerErrorResponse error : errors) {
						ETowerResponse errorResponse = new ETowerResponse();
						errorResponse.setAPIName("Gain Labels");
						errorResponse.setStatus(response.getStatus());
						errorResponse.setStatus(data.getStatus());
						errorResponse.setOrderId(data.getOrderId());
						errorResponse.setReferenceNumber(data.getReferenceNo());
						errorResponse.setTrackingNo(data.getTrackingNo());
						errorResponse.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
						errorResponse.setErrorCode(error.getCode());
						errorResponse.setErrorMessage(error.getMessage());
						responseEntity.add(errorResponse);
					}
				}
			}
		}
		d2zDao.logEtowerResponse(responseEntity);
		return barcodeMap;
	}

	public void makeCalltoEtower(List<SenderdataMaster> eTowerOrders) {
		System.out.println("Background Thread created.....");
		System.out.println(eTowerOrders.size());
		if (!eTowerOrders.isEmpty()) {
			List<CreateShippingRequest> eTowerRequest = new ArrayList<CreateShippingRequest>();

			for (SenderdataMaster orderDetail : eTowerOrders) {
				CreateShippingRequest request = new CreateShippingRequest();
				System.out.println(orderDetail.getArticleId());
				request.setTrackingNo(orderDetail.getArticleId());
				request.setReferenceNo("SW10" + orderDetail.getReference_number());
				request.setRecipientCompany(orderDetail.getConsigneeCompany());
				String recpName = orderDetail.getConsignee_name().length() > 34
						? orderDetail.getConsignee_name().substring(0, 34)
						: orderDetail.getConsignee_name();
				request.setRecipientName(recpName);
				request.setAddressLine1(orderDetail.getConsignee_addr1());
				request.setAddressLine2(orderDetail.getConsignee_addr2());
				request.setEmail(orderDetail.getConsignee_Email());
				request.setCity(orderDetail.getConsignee_Suburb());
				request.setState(orderDetail.getConsignee_State());
				request.setPostcode(orderDetail.getConsignee_Postcode());
				if (orderDetail.getCarrier().equalsIgnoreCase("Express")) {
					request.setServiceOption("Express-Post");
				} else {
					request.setServiceOption("E-Parcel");

				}
				request.setFacility(orderDetail.getInjectionState());
				request.setWeight(Double.valueOf(orderDetail.getWeight()));
				request.setInvoiceValue(orderDetail.getValue());
				request.getOrderItems().get(0).setUnitValue(orderDetail.getValue());
				eTowerRequest.add(request);
			}
			CreateShippingResponse response = eTowerProxy.makeCallForCreateShippingOrder(eTowerRequest);
			parseEtowerCreateShippingResponse(response);

		}
	}

	private void parseEtowerCreateShippingResponse(CreateShippingResponse response) {
		List<ETowerResponse> responseEntity = new ArrayList<ETowerResponse>();
		if (response != null) {
			List<ResponseData> responseData = response.getData();
			if (responseData == null && null != response.getErrors()) {
				for (EtowerErrorResponse error : response.getErrors()) {
					ETowerResponse errorResponse = new ETowerResponse();
					errorResponse.setAPIName("Create Shipping Order");
					errorResponse.setStatus(response.getStatus());
					errorResponse.setErrorCode(error.getCode());
					errorResponse.setErrorMessage(error.getMessage());
					responseEntity.add(errorResponse);
				}
			}

			for (ResponseData data : responseData) {
				List<EtowerErrorResponse> errors = data.getErrors();
				if (null == errors) {
					ETowerResponse errorResponse = new ETowerResponse();
					errorResponse.setAPIName("Create Shipping Order");
					errorResponse.setStatus(data.getStatus());
					errorResponse.setOrderId(data.getOrderId());
					errorResponse.setReferenceNumber(data.getReferenceNo());
					errorResponse.setTrackingNo(data.getTrackingNo());
					errorResponse.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
					responseEntity.add(errorResponse);

				} else {
					for (EtowerErrorResponse error : errors) {
						ETowerResponse errorResponse = new ETowerResponse();
						errorResponse.setAPIName("Create Shipping Order");
						errorResponse.setStatus(response.getStatus());
						errorResponse.setStatus(data.getStatus());
						errorResponse.setOrderId(data.getOrderId());
						errorResponse.setReferenceNumber(data.getReferenceNo());
						errorResponse.setTrackingNo(data.getTrackingNo());
						errorResponse.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
						errorResponse.setErrorCode(error.getCode());
						errorResponse.setErrorMessage(error.getMessage());
						responseEntity.add(errorResponse);
					}
				}
			}
		}
		d2zDao.logEtowerResponse(responseEntity);

	}

	public void makeEtowerForecastCall(List<String> articleIDS) {

		List<List<String>> trackingNbrList = ListUtils.partition(articleIDS, 300);
		for (List<String> trackingNumbers : trackingNbrList) {
			CreateShippingResponse response = eTowerProxy.makeCallForForeCast(trackingNumbers);
			List<ETowerResponse> responseEntity = new ArrayList<ETowerResponse>();

			if (response != null) {
				List<ResponseData> responseData = response.getData();
				if (responseData == null && null != response.getErrors()) {
					for (EtowerErrorResponse error : response.getErrors()) {
						ETowerResponse errorResponse = new ETowerResponse();
						errorResponse.setAPIName("Forecast");
						errorResponse.setStatus(response.getStatus());
						errorResponse.setErrorCode(error.getCode());
						errorResponse.setErrorMessage(error.getMessage());
						responseEntity.add(errorResponse);
					}
				}

				for (ResponseData data : responseData) {
					List<EtowerErrorResponse> errors = data.getErrors();
					if (null == errors) {
						ETowerResponse errorResponse = new ETowerResponse();
						errorResponse.setAPIName("Forecast");
						errorResponse.setStatus(data.getStatus());
						errorResponse.setOrderId(data.getOrderId());
						errorResponse.setReferenceNumber(data.getReferenceNo());
						errorResponse.setTrackingNo(data.getTrackingNo());
						errorResponse.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
						responseEntity.add(errorResponse);
					} else {
						for (EtowerErrorResponse error : errors) {
							ETowerResponse errorResponse = new ETowerResponse();
							errorResponse.setAPIName("Forecast");
							errorResponse.setStatus(response.getStatus());
							errorResponse.setStatus(data.getStatus());
							errorResponse.setOrderId(data.getOrderId());
							errorResponse.setReferenceNumber(data.getReferenceNo());
							errorResponse.setTrackingNo(data.getTrackingNo());
							errorResponse.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
							errorResponse.setErrorCode(error.getCode());
							errorResponse.setErrorMessage(error.getMessage());
							responseEntity.add(errorResponse);
						}
					}
				}
			}
			d2zDao.logEtowerResponse(responseEntity);

			// TrackingEventResponse trackEventresponse =
			// eTowerProxy.makeCallForTrackingEvents(trackingNumbers);
			// insertTrackingDetails(trackEventresponse);
		}

	}

}
