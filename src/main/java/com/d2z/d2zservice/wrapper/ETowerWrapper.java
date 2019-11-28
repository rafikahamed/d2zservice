package com.d2z.d2zservice.wrapper;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
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
import com.d2z.d2zservice.model.etower.DeleteShippingResponse;
import com.d2z.d2zservice.model.etower.EtowerErrorResponse;
import com.d2z.d2zservice.model.etower.Facility;
import com.d2z.d2zservice.model.etower.GainLabelsResponse;
import com.d2z.d2zservice.model.etower.LabelData;
import com.d2z.d2zservice.model.etower.ResponseData;
import com.d2z.d2zservice.proxy.ETowerProxy;
import com.d2z.singleton.D2ZSingleton;
import com.d2z.singleton.SingletonCounter;

@Service
public class ETowerWrapper {

	@Autowired
	private ID2ZDao d2zDao;

	@Autowired
	private ETowerProxy eTowerProxy;

	public void makeCreateShippingOrderEtowerCallForAPIData(CreateConsignmentRequest data,
			List<SenderDataResponse> senderDataResponseList) throws EtowerFailureResponseException {
		Map<String, String> systemRefNbrMap = new HashMap<String, String>();
		List<com.d2z.d2zservice.model.etower.CreateShippingRequest> eTowerRequest = constructEtowerRequestWithAPIData(data,systemRefNbrMap);
		String status = null;
		Map<String, LabelData> barcodeMap = new HashMap<String, LabelData>();
		
		List<String> gainLabelTrackingNo = new ArrayList<String>();
System.out.println("ttt"+eTowerRequest.isEmpty());
		if (!eTowerRequest.isEmpty()) {
			
			CreateShippingResponse response = eTowerProxy.makeCallForCreateShippingOrder(eTowerRequest);
			status = parseCreateShippingOrderResponse(response, senderDataResponseList, barcodeMap,
					gainLabelTrackingNo);
		}

		/*if (gainLabelTrackingNo.size() == data.getConsignmentData().size()) {

			Runnable r = new Runnable() {
				public void run() {
					GainLabelsResponse gainLabelResponse = eTowerProxy.makeCallToGainLabels(gainLabelTrackingNo);
					processGainLabelsResponse(gainLabelResponse, barcodeMap);

					int userId = d2zDao.fetchUserIdbyUserName(data.getUserName());
					d2zDao.createConsignments(data.getConsignmentData(), userId, data.getUserName(), barcodeMap);

				}
			};
			new Thread(r).start();
		} else {*/
			if (null != status) {
				GainLabelsResponse gainLabelResponse = eTowerProxy.makeCallToGainLabels(gainLabelTrackingNo);
				processGainLabelsResponse(gainLabelResponse, barcodeMap,systemRefNbrMap);
			}
			int userId = d2zDao.fetchUserIdbyUserName(data.getUserName());
			System.out.println("Barcode Map-->"+barcodeMap.size());
			String senderFileID = d2zDao.createConsignments(data.getConsignmentData(), userId, data.getUserName(),
					barcodeMap);

			List<String> insertedOrder = d2zDao.fetchBySenderFileID(senderFileID);
			senderDataResponseList.clear();
			Iterator itr = insertedOrder.iterator();
			while (itr.hasNext()) {
				Object[] obj = (Object[]) itr.next();
				SenderDataResponse senderDataresponse = new SenderDataResponse();
				senderDataresponse.setReferenceNumber(obj[0].toString());
				//senderDataresponse.setBarcodeLabelNumber(obj[2] != null ? obj[2].toString() : "");
				String barcode = obj[1].toString();
				senderDataresponse.setBarcodeLabelNumber("]d2".concat(barcode.replaceAll("\\[|\\]", "")));
				senderDataresponse.setInjectionPort(obj[5] != null ? obj[5].toString() : "");
				senderDataResponseList.add(senderDataresponse);
			}
		//}

	}

	public void makeCreateShippingOrderEtowerCallForFileData(List<SenderData> data,
			List<SenderDataResponse> senderDataResponseList) throws EtowerFailureResponseException {
		Map<String, String> systemRefNbrMap = new HashMap<String, String>();
		List<com.d2z.d2zservice.model.etower.CreateShippingRequest> eTowerRequest = constructEtowerRequestWithFileData(
				data,systemRefNbrMap);
		String status = null;
		
		Map<String, LabelData> barcodeMap = new HashMap<String, LabelData>();
		List<String> gainLabelTrackingNo = new ArrayList<String>();

		if (!eTowerRequest.isEmpty()) {
			CreateShippingResponse response = eTowerProxy.makeCallForCreateShippingOrder(eTowerRequest);
			status = parseCreateShippingOrderResponse(response, senderDataResponseList, barcodeMap,
					gainLabelTrackingNo);
		}
		/*if (gainLabelTrackingNo.size() == data.size()) {

			Runnable r = new Runnable() {
				public void run() {
					GainLabelsResponse gainLabelResponse = eTowerProxy.makeCallToGainLabels(gainLabelTrackingNo);
					processGainLabelsResponse(gainLabelResponse, barcodeMap);

					d2zDao.exportParcel(data, barcodeMap);

				}
			};
			new Thread(r).start();
		} else {*/
			if (null != status) {
				GainLabelsResponse gainLabelResponse = eTowerProxy.makeCallToGainLabels(gainLabelTrackingNo);
				processGainLabelsResponse(gainLabelResponse, barcodeMap,systemRefNbrMap);
			}
			String senderFileID = d2zDao.exportParcel(data, barcodeMap);

			List<String> insertedOrder = d2zDao.fetchBySenderFileID(senderFileID);
			senderDataResponseList.clear();
			Iterator itr = insertedOrder.iterator();
			while (itr.hasNext()) {
				Object[] obj = (Object[]) itr.next();
				SenderDataResponse senderDataresponse = new SenderDataResponse();
				senderDataresponse.setReferenceNumber(obj[0].toString());
				senderDataresponse.setBarcodeLabelNumber(obj[3] != null ? obj[3].toString() : "");
				//senderDataresponse.setBarcodeLabelNumber(obj[2] != null ? obj[2].toString() : "");
				senderDataresponse.setCarrier(obj[4] != null ? obj[4].toString() : "");
				senderDataresponse.setInjectionPort(obj[5] != null ? obj[5].toString() : "");
				senderDataResponseList.add(senderDataresponse);
			}
		//}

	}

	private List<CreateShippingRequest> constructEtowerRequestWithFileData(List<SenderData> data,Map<String,String> systemRefNbrMap) {
		List<com.d2z.d2zservice.model.etower.CreateShippingRequest> eTowerRequest = new ArrayList<com.d2z.d2zservice.model.etower.CreateShippingRequest>();
		Map<String, String> postCodeZoneMap = D2ZSingleton.getInstance().getPostCodeZoneMap();
		ListIterator<SenderData> iterator = data.listIterator();
		// for(SenderData orderDetail : data) {
		while (iterator.hasNext()) {
			SenderData orderDetail = iterator.next();
			com.d2z.d2zservice.model.etower.CreateShippingRequest request = new com.d2z.d2zservice.model.etower.CreateShippingRequest();
			//Random rnd = new Random();
			int uniqueNumber = SingletonCounter.getInstance().getEtowerCount();
			request.setReferenceNo("SW10A" + uniqueNumber);
			systemRefNbrMap.put(request.getReferenceNo(), orderDetail.getReferenceNumber());
			//request.setReferenceNo(orderDetail.getReferenceNumber());
			request.setRecipientCompany(orderDetail.getConsigneeCompany());
			
					String recpName = orderDetail.getConsigneeName().length() > 34
							? orderDetail.getConsigneeName().substring(0, 34)
							: orderDetail.getConsigneeName();
							recpName = recpName.replaceAll("[^a-zA-Z0-9\\s+]", "");
							request.setRecipientName(recpName);
					String address = (orderDetail.getConsigneeAddr1()).replaceAll("[^a-zA-Z0-9\\s+]", "");
							address = address.length()>39 ? address.substring(0, 39):address ;
							request.setAddressLine1(address);
					String address2 = (orderDetail.getConsigneeAddr2()).replaceAll("[^a-zA-Z0-9\\s+]", "");
							address2 = address2.length()>60 ? address2.substring(0, 60):address2 ;
							request.setAddressLine2(address2);
			 		String description = (orderDetail.getProductDescription()).replaceAll("[^a-zA-Z0-9\\s+]", "");
							description = description.length()>50 ? description.substring(0, 50):description ;
							request.setDescription(description);
			request.setDangerousGoods(orderDetail.isDangerousGoods());
			request.setEmail(orderDetail.getConsigneeEmail());
			request.setCity(orderDetail.getConsigneeSuburb());
			request.setState(orderDetail.getConsigneeState());
			request.setPostcode(orderDetail.getConsigneePostcode());
			request.setShipperName(orderDetail.getShipperName());
			request.setShipperAddressLine1(orderDetail.getShipperAddr1());
			request.setShipperCity(orderDetail.getShipperCity());
			request.setShipperState(orderDetail.getShipperState());
			request.setShipperCountry(orderDetail.getShipperCountry());
			request.setReturnOption("Return");
			request.setAuthorityToleave(false);
			Double weight = Double.valueOf(orderDetail.getWeight());
			if ("1PM3E".equalsIgnoreCase(orderDetail.getServiceType())) {
				request.setFacility("MEL3");

				orderDetail.setInjectionState("MEL3");
				orderDetail.setCarrier("Express");
			} /*else if ("1PS2".equalsIgnoreCase(orderDetail.getServiceType())) {
				request.setFacility("SYD2");
				orderDetail.setInjectionState("SYD2");

			} */else if ("1PS3".equalsIgnoreCase(orderDetail.getServiceType())) {
				String zoneId = postCodeZoneMap
						.get(orderDetail.getConsigneeSuburb().trim().toUpperCase().concat(orderDetail.getConsigneePostcode().trim()));
				if (weight > 3) {
					List<String> bneDestination = Stream.of("Q0", "Q1", "Q2", "Q3", "Q4", "Q5")
							.collect(Collectors.toList());
					boolean containsDest = bneDestination.stream().anyMatch(zoneId::equalsIgnoreCase);
					if (containsDest) {
						request.setFacility("BNE");
						orderDetail.setInjectionState("BNE");

					} else {
					/*	request.setFacility("SYD2");
						orderDetail.setInjectionState("SYD2");
						*/
						iterator.set(orderDetail);
						continue;

					}
				} else {
					/*List<String> sydDestination = Stream.of("CB", "GF", "N0", "N1", "N2", "N3", "N4", "NC", "WG")
							.collect(Collectors.toList());
					boolean containsDest = sydDestination.stream().anyMatch(zoneId::equalsIgnoreCase);
					if (containsDest) {
						request.setFacility("SYD2");
						orderDetail.setInjectionState("SYD2");

					} else {*/
						iterator.set(orderDetail);
						continue;
					//}
				}
			}
			/*else if("1PS5".equalsIgnoreCase(orderDetail.getServiceType())) {
				String zoneId = postCodeZoneMap
						.get(orderDetail.getConsigneeSuburb().trim().toUpperCase().concat(orderDetail.getConsigneePostcode().trim()));
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
			else if("2PSP".equalsIgnoreCase(orderDetail.getServiceType())) {
				String zoneId = postCodeZoneMap
						.get(orderDetail.getConsigneeSuburb().trim().toUpperCase().concat(orderDetail.getConsigneePostcode().trim()));
				List<String> eTowerZoneID = Stream.of("N0", "Q0" , "S0", "V0")
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
			else if("1PS".equalsIgnoreCase(orderDetail.getServiceType())) {
				String zoneId = postCodeZoneMap
						.get(orderDetail.getConsigneeSuburb().trim().toUpperCase().concat(orderDetail.getConsigneePostcode().trim()));
				if (weight > 3) {
						request.setFacility("SYD2");
						orderDetail.setInjectionState("SYD2");
				} else {
					List<String> sydDestination = Stream.of("CB", "GF", "N0", "N1", "N2", "N3", "N4", "NC", "WG")
							.collect(Collectors.toList());
					boolean containsDest = sydDestination.stream().anyMatch(zoneId::equalsIgnoreCase);
					if (containsDest) {
						request.setFacility("SYD2");
						orderDetail.setInjectionState("SYD2");

					} else {
						iterator.set(orderDetail);
						continue;
					}
				}
			}
*/			else if("1PM5".equalsIgnoreCase(orderDetail.getServiceType())) {
				String zoneId = postCodeZoneMap
						.get(orderDetail.getConsigneeSuburb().trim().toUpperCase().concat(orderDetail.getConsigneePostcode().trim()));
				if(weight<=0.5) {
				List<String> destination = Stream.of("BR", "GL", "N0", "Q0", "S0", "V0", "V1", "V3", "W0")
						.collect(Collectors.toList());
				boolean containsDest = destination.stream().anyMatch(zoneId::equalsIgnoreCase);
				if (containsDest) {
					request.setFacility("MEL3");
					orderDetail.setInjectionState("MEL3");

				} else {
					iterator.set(orderDetail);
					continue;
				}
				}else {
					List<String> destination = Stream.of("N0", "Q0", "S0", "V0", "W0")
							.collect(Collectors.toList());
					boolean containsDest = destination.stream().anyMatch(zoneId::equalsIgnoreCase);
					if (containsDest) {
						request.setFacility("MEL3");
						orderDetail.setInjectionState("MEL3");

					} else {
						iterator.set(orderDetail);
						continue;
					}
				}
			}
			else if ("TST1".equalsIgnoreCase(orderDetail.getServiceType())) {
				String zoneId = postCodeZoneMap
						.get(orderDetail.getConsigneeSuburb().trim().toUpperCase().concat(orderDetail.getConsigneePostcode().trim()));
				if (weight <= 0.5){
					List<String> bneDestination = Stream.of("Q0", "Q1", "Q5", "SC", "GC", "IP")
							.collect(Collectors.toList());
					boolean containsBneDest = bneDestination.stream().anyMatch(zoneId::equalsIgnoreCase);
					List<String> melDestination = Stream.of("V0", "V1", "V3", "BR", "GL")
							.collect(Collectors.toList());
					boolean containsMelDest = melDestination.stream().anyMatch(zoneId::equalsIgnoreCase);
					List<String> perthDestination = Stream.of("W0","W1")
							.collect(Collectors.toList());
					boolean containsPerthDest = perthDestination.stream().anyMatch(zoneId::equalsIgnoreCase);
					List<String> sydDestination = Stream.of("N4","NC","WG","CB","GF","N0","N1","N3")
							.collect(Collectors.toList());
					boolean containSydDest = sydDestination.stream().anyMatch(zoneId::equalsIgnoreCase);
					
					if (containsBneDest) {
						request.setFacility("BNE");
						orderDetail.setInjectionState("BNE");

					} else if(containsMelDest) {
						request.setFacility("MEL3");
						orderDetail.setInjectionState("MEL3");

					}
					/* else if(containsPerthDest) {
						 iterator.set(orderDetail);
						 continue;
					 }
					 else if(containSydDest){
						 request.setFacility("SYD2");
						 orderDetail.setInjectionState("SYD2");
					 }*/
					 else {
						 iterator.set(orderDetail);
						 continue;
					 }
				} else {
					/*List<String> sydDestination = Stream.of("N0","Q0","S0","V0","W0")
							.collect(Collectors.toList());
					boolean containSydDest = sydDestination.stream().anyMatch(zoneId::equalsIgnoreCase);
					
					 if(containSydDest){
						 request.setFacility("SYD2");
						 orderDetail.setInjectionState("SYD2");
					 }
					 else {*/
						 iterator.set(orderDetail);
						 continue;
					// }
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
	public void DeleteShipingResponse(List<String> referencenumber)
	{
	
		List<ETowerResponse> responseEntity = new ArrayList<ETowerResponse>();
		for(String s : referencenumber)
		{
						DeleteShippingResponse data = eTowerProxy.makeCallToDelete("SW10S" +
s);
			ETowerResponse errorResponse = new ETowerResponse();
			errorResponse.setAPIName("Delete Shipping Order");
			errorResponse.setStatus(data.getStatus());
			errorResponse.setOrderId(data.getOrderId());
			errorResponse.setReferenceNumber(s);
			//errorResponse.setTrackingNo(data.getTrackingNo());
			errorResponse.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
			responseEntity.add(errorResponse);
		}
		
		d2zDao.logEtowerResponse(responseEntity);
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

	private List<CreateShippingRequest> constructEtowerRequestWithAPIData(CreateConsignmentRequest data,Map<String, String> systemRefNbrMap) {
		List<com.d2z.d2zservice.model.etower.CreateShippingRequest> eTowerRequest = new ArrayList<com.d2z.d2zservice.model.etower.CreateShippingRequest>();
		Map<String, String> postCodeZoneMap = D2ZSingleton.getInstance().getPostCodeZoneMap();
		 
		List<SenderDataApi> updatedOrderDetail = new ArrayList<SenderDataApi>();
		for (SenderDataApi orderDetail : data.getConsignmentData()) {
			com.d2z.d2zservice.model.etower.CreateShippingRequest request = new com.d2z.d2zservice.model.etower.CreateShippingRequest();
			//Random rnd = new Random();
			int uniqueNumber = SingletonCounter.getInstance().getEtowerCount();
			request.setReferenceNo("SW10A" + uniqueNumber);
			systemRefNbrMap.put(request.getReferenceNo(), orderDetail.getReferenceNumber());
			//request.setReferenceNo(orderDetail.getReferenceNumber());
			request.setRecipientCompany(orderDetail.getConsigneeCompany());
			String recpName = orderDetail.getConsigneeName().length() > 34
					? orderDetail.getConsigneeName().substring(0, 34)
					: orderDetail.getConsigneeName();
					recpName = recpName.replaceAll("[^a-zA-Z0-9\\s+]", "");
				request.setRecipientName(recpName);
				String address = (orderDetail.getConsigneeAddr1()).replaceAll("[^a-zA-Z0-9\\s+]", "");
				address = address.length()>39 ? address.substring(0, 39):address ;
				request.setAddressLine1(address);
				String address2 = (orderDetail.getConsigneeAddr2()).replaceAll("[^a-zA-Z0-9\\s+]", "");
				address2 = address2.length()>60 ? address2.substring(0, 60):address2 ;
				request.setAddressLine2(address2);
				request.setDangerousGoods(orderDetail.isDangerousGoods());
				String description = (orderDetail.getProductDescription()).replaceAll("[^a-zA-Z0-9\\s+]", "");
				description = description.length()>50 ? description.substring(0, 50):description ;
				request.setDescription(description);
			request.setEmail(orderDetail.getConsigneeEmail());
			request.setCity(orderDetail.getConsigneeSuburb());
			request.setState(orderDetail.getConsigneeState());
			request.setPostcode(orderDetail.getConsigneePostcode());
			request.setShipperName(orderDetail.getShipperName());
			request.setShipperAddressLine1(orderDetail.getShipperAddr1());
			request.setShipperCity(orderDetail.getShipperCity());
			request.setShipperState(orderDetail.getShipperState());
			request.setShipperCountry(orderDetail.getShipperCountry());
			request.setReturnOption("Return");
			request.setVendorid(orderDetail.getVendorId());
			request.setAuthorityToleave(false);
			Double weight = Double.valueOf(orderDetail.getWeight());
			if ("1PM3E".equalsIgnoreCase(orderDetail.getServiceType())) {
				request.setFacility("MEL3");
				orderDetail.setInjectionType("MEL3");
				orderDetail.setCarrier("Express");
			} 
			/*else if ("1PS2".equalsIgnoreCase(orderDetail.getServiceType())) {
				request.setFacility("SYD2");
				orderDetail.setInjectionType("SYD2");
			} */else if ("1PS3".equalsIgnoreCase(orderDetail.getServiceType())) {
				String zoneId = postCodeZoneMap
						.get(orderDetail.getConsigneeSuburb().trim().toUpperCase().concat(orderDetail.getConsigneePostcode().trim()));
				if (weight > 3) {
					List<String> bneDestination = Stream.of("Q0", "Q1", "Q2", "Q3", "Q4", "Q5")
							.collect(Collectors.toList());
					boolean containsDest = bneDestination.stream().anyMatch(zoneId::equalsIgnoreCase);
					if (containsDest) {
						request.setFacility("BNE");
						orderDetail.setInjectionType("BNE");
					} else {
						//request.setFacility("SYD2");
						//orderDetail.setInjectionType("SYD2");
						
						updatedOrderDetail.add(orderDetail);
						continue;
					}
				} else {
					/*List<String> sydDestination = Stream.of("CB", "GF", "N0", "N1", "N2", "N3", "N4", "NC", "WG")
							.collect(Collectors.toList());
					boolean containsDest = sydDestination.stream().anyMatch(zoneId::equalsIgnoreCase);
					if (containsDest) {
						request.setFacility("SYD2");
						orderDetail.setInjectionType("SYD2");
					} else {*/
						updatedOrderDetail.add(orderDetail);
						continue;
					//}
				}
			}/*else if("1PS5".equalsIgnoreCase(orderDetail.getServiceType())) {
				String zoneId = postCodeZoneMap
						.get(orderDetail.getConsigneeSuburb().trim().toUpperCase().concat(orderDetail.getConsigneePostcode().trim()));
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
			else if("2PSP".equalsIgnoreCase(orderDetail.getServiceType())) {
				String zoneId = postCodeZoneMap
						.get(orderDetail.getConsigneeSuburb().trim().toUpperCase().concat(orderDetail.getConsigneePostcode().trim()));
				List<String> eTowerZoneID = Stream.of("N0", "Q0" , "S0", "V0")
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
			else if("1PS".equalsIgnoreCase(orderDetail.getServiceType())) {
				String zoneId = postCodeZoneMap
						.get(orderDetail.getConsigneeSuburb().trim().toUpperCase().concat(orderDetail.getConsigneePostcode().trim()));
				if (weight > 3) {
						request.setFacility("SYD2");
						orderDetail.setInjectionState("SYD2");
				} else {
					List<String> sydDestination = Stream.of("CB", "GF", "N0", "N1", "N2", "N3", "N4", "NC", "WG")
							.collect(Collectors.toList());
					boolean containsDest = sydDestination.stream().anyMatch(zoneId::equalsIgnoreCase);
					if (containsDest) {
						request.setFacility("SYD2");
						orderDetail.setInjectionState("SYD2");

					} else {
						updatedOrderDetail.add(orderDetail);
						continue;
					}
				}
			}*/
			else if("1PM5".equalsIgnoreCase(orderDetail.getServiceType())) {
				String zoneId = postCodeZoneMap
						.get(orderDetail.getConsigneeSuburb().trim().toUpperCase().concat(orderDetail.getConsigneePostcode().trim()));
				if(weight<=0.5) {
				List<String> destination = Stream.of("BR", "GL", "N0", "Q0", "S0", "V0", "V1", "V3", "W0")
						.collect(Collectors.toList());
				boolean containsDest = destination.stream().anyMatch(zoneId::equalsIgnoreCase);
				if (containsDest) {
					request.setFacility("MEL3");
					orderDetail.setInjectionState("MEL3");

				} else {
					updatedOrderDetail.add(orderDetail);
					continue;
				}
				}else {
					List<String> destination = Stream.of("N0", "Q0", "S0", "V0", "W0")
							.collect(Collectors.toList());
					boolean containsDest = destination.stream().anyMatch(zoneId::equalsIgnoreCase);
					if (containsDest) {
						request.setFacility("MEL3");
						orderDetail.setInjectionState("MEL3");

					} else {
						updatedOrderDetail.add(orderDetail);
						continue;
					}
				}
			}
			else if ("TST1".equalsIgnoreCase(orderDetail.getServiceType())) {
				String zoneId = postCodeZoneMap
						.get(orderDetail.getConsigneeSuburb().trim().toUpperCase().concat(orderDetail.getConsigneePostcode().trim()));
				if (weight <= 0.5){
					List<String> bneDestination = Stream.of("Q0", "Q1", "Q5", "SC", "GC", "IP")
							.collect(Collectors.toList());
					boolean containsBneDest = bneDestination.stream().anyMatch(zoneId::equalsIgnoreCase);
					List<String> melDestination = Stream.of("V0", "V1", "V3", "BR", "GL")
							.collect(Collectors.toList());
					boolean containsMelDest = melDestination.stream().anyMatch(zoneId::equalsIgnoreCase);
					List<String> perthDestination = Stream.of("W0","W1")
							.collect(Collectors.toList());
					boolean containsPerthDest = perthDestination.stream().anyMatch(zoneId::equalsIgnoreCase);
					List<String> sydDestination = Stream.of("N4","NC","WG","CB","GF","N0","N1","N3")
							.collect(Collectors.toList());
					boolean containSydDest = sydDestination.stream().anyMatch(zoneId::equalsIgnoreCase);
					
					if (containsBneDest) {
						request.setFacility("BNE");
						orderDetail.setInjectionState("BNE");

					} else if(containsMelDest) {
						request.setFacility("MEL3");
						orderDetail.setInjectionState("MEL3");

					}
					/* else if(containsPerthDest) {
						 updatedOrderDetail.add(orderDetail);
							continue;
					 }
					 else if(containSydDest){
						 request.setFacility("SYD2");
						 orderDetail.setInjectionState("SYD2");
					 }*/
					 else {
						 updatedOrderDetail.add(orderDetail);
							continue;
					 }
				} else {/*
					List<String> sydDestination = Stream.of("N0","Q0","S0","V0","W0")
							.collect(Collectors.toList());
					boolean containSydDest = sydDestination.stream().anyMatch(zoneId::equalsIgnoreCase);
					
					 if(containSydDest){
						 request.setFacility("SYD2");
						 orderDetail.setInjectionState("SYD2");
					 }
					 else {*/
						 updatedOrderDetail.add(orderDetail);
						 continue;
					// }
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
			Map<String, LabelData> barcodeMap, Map<String,String> systemRefNbrMap) {
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
					barcodeMap.put(systemRefNbrMap.get(data.getReferenceNo()), data);
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
				//Random rnd = new Random();
				int uniqueNumber = SingletonCounter.getInstance().getEtowerCount();
				request.setReferenceNo("SW10A" + uniqueNumber);
				request.setRecipientCompany(orderDetail.getConsigneeCompany());
				String recpName = orderDetail.getConsignee_name().length() > 34
						? orderDetail.getConsignee_name().substring(0, 34)
						: orderDetail.getConsignee_name();
						recpName = recpName.replaceAll("[^a-zA-Z0-9\\s+]", "");
				request.setRecipientName(recpName);
				String address = (orderDetail.getConsignee_addr1()).replaceAll("[^a-zA-Z0-9\\s+]", "");
				address = address.length()>39 ? address.substring(0, 39):address ;
				request.setAddressLine1(address);
				String address2 = (orderDetail.getConsignee_addr2()).replaceAll("[^a-zA-Z0-9\\s+]", "");
				address2 = address2.length()>60 ? address2.substring(0, 60):address2 ;
				request.setAddressLine2(address2);
				String description = (orderDetail.getProduct_Description()).replaceAll("[^a-zA-Z0-9\\s+]", "");
				description = description.length()>50 ? description.substring(0, 50):description ;
				request.setDescription(description);
				request.setDangerousGoods(true);
				request.setEmail(orderDetail.getConsignee_Email());
				request.setCity(orderDetail.getConsignee_Suburb());
				request.setState(orderDetail.getConsignee_State());
				request.setPostcode(orderDetail.getConsignee_Postcode());
				request.setShipperName(orderDetail.getShipper_Name());
				request.setShipperAddressLine1(orderDetail.getShipper_Addr1());
				request.setShipperCity(orderDetail.getShipper_City());
				request.setShipperState(orderDetail.getShipper_State());
				request.setShipperCountry(orderDetail.getShipper_Country());
				request.setReturnOption("Return");
				request.setAuthorityToleave(false);
				if (orderDetail.getCarrier().equalsIgnoreCase("Express")) {
					request.setServiceOption("Express-Post");
				} else {
					request.setServiceOption("E-Parcel");

				}
				request.setFacility(Facility.get(orderDetail.getMlid()).toString());
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
