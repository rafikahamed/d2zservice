package com.d2z.d2zservice.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.d2z.d2zservice.dao.ID2ZDao;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.exception.FailureResponseException;
import com.d2z.d2zservice.exception.InvalidUserException;
import com.d2z.d2zservice.exception.MaxSizeCountException;
import com.d2z.d2zservice.model.CreateConsignmentRequest;
import com.d2z.d2zservice.model.ErrorDetails;
import com.d2z.d2zservice.model.MCSSenderDataRequest;
import com.d2z.d2zservice.model.PFLSenderDataRequest;
import com.d2z.d2zservice.model.PflCreateShippingOrderInfo;
import com.d2z.d2zservice.model.PflCreateShippingRequest;
import com.d2z.d2zservice.model.SenderDataApi;
import com.d2z.d2zservice.model.SenderDataResponse;
import com.d2z.d2zservice.repository.UserRepository;
import com.d2z.d2zservice.repository.UserServiceRepository;
import com.d2z.d2zservice.service.ID2ZAPIService;
import com.d2z.d2zservice.util.ValidationUtils;
import com.d2z.d2zservice.validation.D2ZValidator;
import com.d2z.d2zservice.wrapper.ETowerWrapper;
import com.d2z.d2zservice.wrapper.PCAWrapper;
import com.d2z.d2zservice.wrapper.PFLWrapper;
import com.d2z.singleton.D2ZSingleton;
import com.d2z.singleton.SingletonCounter;

@Service
public class D2ZAPIServiceImpl implements ID2ZAPIService{

	@Autowired
	private ID2ZDao d2zDao;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserServiceRepository userServiceRepository;
	
	@Autowired
	private D2ZValidator d2zValidator;

	@Autowired
	ETowerWrapper eTowerWrapper; 
	
	@Autowired
	PFLWrapper pflWrapper;
	
	@Autowired
	PCAWrapper pcaWrapper;

	
	@Override
	public void createConsignments(CreateConsignmentRequest orderDetail,  List<SenderDataResponse> senderDataResponseList ,
			Map<String, List<ErrorDetails>> errorMap,List<String> autoShipRefNbrs) throws FailureResponseException {
		
		Integer userId = userRepository.fetchUserIdbyUserName(orderDetail.getUserName());
		if (userId == null) {
			throw new InvalidUserException("User does not exist", orderDetail.getUserName());
		}
		if (orderDetail.getConsignmentData().size() > 300) {
			throw new MaxSizeCountException("We are allowing max 300 records, Your Request contains - "
					+ orderDetail.getConsignmentData().size() + " Records");
		
		}
		boolean isPostcodeValidationReq = true;
		if(("N").equals(userRepository.fetchPostcodeValidationIndicator(orderDetail.getUserName()))) {
			isPostcodeValidationReq = false;
		}
		d2zValidator.isReferenceNumberUnique(orderDetail,errorMap);
		d2zValidator.isServiceValid(orderDetail,errorMap);
		/*
		 * if(isPostcodeValidationReq) {
		 * d2zValidator.isPostCodeValid(orderDetail,errorMap); }
		 */
		ValidationUtils.removeInvalidconsignments(orderDetail,errorMap);
		boolean autoShipment = false;
		String barcodeLabelNumber =null;
		String datamatrix=null;
		String serviceType = null;
		if(orderDetail.getConsignmentData().size()>0) {	
			barcodeLabelNumber = orderDetail.getConsignmentData().get(0).getBarcodeLabelNumber();
			datamatrix= orderDetail.getConsignmentData().get(0).getDatamatrix();
		    serviceType = orderDetail.getConsignmentData().get(0).getServiceType();

		}else {
			return;
		}
		if(null==barcodeLabelNumber || barcodeLabelNumber.trim().isEmpty() || null==datamatrix || datamatrix.trim().isEmpty()) {
			if (serviceType.startsWith("MCS")) {

				MCSSenderDataRequest request = constructMCSRequest(orderDetail);
				d2zValidator.isPostCodeZone4Valid(request.getEparcelSenderData(),errorMap);
				ValidationUtils.removeInvalidconsignments(request,errorMap);

				if(request.getPflSenderData().size()>0) {
					try {
					makeCreateShippingOrderPFLCall(request.getPflSenderData(), senderDataResponseList,orderDetail.getUserName(), "MC1");
					}
					catch(Exception e) {
						List<String> refNbrList = request.getPflSenderData().stream().map(obj->{
							return obj.getReferenceNumber();
						}).collect(Collectors.toList());
						for(String referenceNumber : refNbrList)
						ValidationUtils.populateErrorDetails(referenceNumber, "", "Shipment Error. Please contact us", errorMap);
					}
				}
				if(request.getFastwaySenderData().size()>0) {
					try {
						makeCreateShippingOrderPFLCall(request.getFastwaySenderData(), senderDataResponseList,orderDetail.getUserName(), "FW");	
					}
					catch(Exception e) {
						List<String> refNbrList = request.getFastwaySenderData().stream().map(obj->{
							return obj.getReferenceNumber();
						}).collect(Collectors.toList());
						for(String referenceNumber : refNbrList)
						ValidationUtils.populateErrorDetails(referenceNumber, "", "Shipment Error. Please contact us", errorMap);
					}
				}
				if(request.getEparcelSenderData().size()>0) {
					try {
					eTowerWrapper.makeCreateShippingOrderEtowerCallForAPIData(orderDetail,senderDataResponseList);
					}
					catch(Exception e) {
						List<String> refNbrList = request.getFastwaySenderData().stream().map(obj->{
							return obj.getReferenceNumber();
						}).collect(Collectors.toList());
						for(String referenceNumber : refNbrList)
						ValidationUtils.populateErrorDetails(referenceNumber, "", "Shipment Error. Please contact us", errorMap);
					}

				/*	String senderFileID = d2zDao.createConsignments(request.getEparcelSenderData(), userId,
							orderDetail.getUserName(), null);
					List<String> insertedOrder = d2zDao.fetchBySenderFileID(senderFileID);
					SenderDataResponse senderDataResponse = null;
					Iterator itr = insertedOrder.iterator();
					while (itr.hasNext()) {

						Object[] obj = (Object[]) itr.next();
						senderDataResponse = new SenderDataResponse();
						senderDataResponse.setReferenceNumber(obj[0].toString());
						String barcode = obj[1].toString();

					senderDataResponse.setBarcodeLabelNumber("]d2".concat(barcode.replaceAll("\\[|\\]", "")));
						senderDataResponse.setCarrier(obj[4].toString());
						senderDataResponse.setInjectionPort(obj[5] != null ? obj[5].toString() : "");
						senderDataResponseList.add(senderDataResponse);
					
					}*/
				}
				/*
									 * PFLSenderDataRequest request = constructMCSRequest(orderDetail);
									 * d2zValidator.isPostCodeValid(request.getNonPflSenderDataApi(),errorMap);
									 * ValidationUtils.removeInvalidconsignments(request,errorMap);
									 * d2zValidator.isPostCodeValid(request.getEtowerSenderData(),errorMap);
									 * ValidationUtils.removeInvalidEtowerconsignments(request,errorMap);
									 * if(request.getEtowerSenderData().size()>0) { CreateConsignmentRequest
									 * requestDetail = new CreateConsignmentRequest();
									 * requestDetail.setConsignmentData(request.getEtowerSenderData());
									 * requestDetail.setUserName(orderDetail.getUserName());
									 * eTowerWrapper.makeCreateShippingOrderEtowerCallForAPIData(requestDetail,
									 * senderDataResponseList); } if(request.getPflSenderDataApi().size()>0) {
									 * makeCreateShippingOrderPFLCall(request.getPflSenderDataApi(),
									 * senderDataResponseList,orderDetail.getUserName(), serviceType); }
									 * 
									 * if(request.getNonPflSenderDataApi().size()>0) { String senderFileID =
									 * d2zDao.createConsignments(request.getNonPflSenderDataApi(), userId,
									 * orderDetail.getUserName(), null); List<String> insertedOrder =
									 * d2zDao.fetchBySenderFileID(senderFileID);
									 * 
									 * Iterator itr = insertedOrder.iterator(); while (itr.hasNext()) {
									 * 
									 * Object[] obj = (Object[]) itr.next(); SenderDataResponse senderDataResponse =
									 * new SenderDataResponse();
									 * senderDataResponse.setReferenceNumber(obj[0].toString()); String barcode =
									 * obj[1].toString();
									 * 
									 * senderDataResponse.setBarcodeLabelNumber("]d2".concat(barcode.replaceAll(
									 * "\\[|\\]", ""))); senderDataResponse.setCarrier(obj[4].toString());
									 * senderDataResponse.setInjectionPort(obj[5] != null ? obj[5].toString() : "");
									 * senderDataResponseList.add(senderDataResponse); } }
									 */
				return;
			}
			if("MC1".equalsIgnoreCase(serviceType)) {
				if (isPostcodeValidationReq) {
					d2zValidator.isPFLPostCodeValid(orderDetail,errorMap);
				}
		    	ValidationUtils.removeInvalidconsignments(orderDetail,errorMap);
				if(orderDetail.getConsignmentData().size()>0) {
				makeCreateShippingOrderPFLCall(orderDetail.getConsignmentData(),senderDataResponseList,orderDetail.getUserName(), serviceType);
				}
				return;
			}
			if( "TL1".equalsIgnoreCase(serviceType)) {
		    	if(isPostcodeValidationReq) {
		    		
		    			d2zValidator.isTollPostCodeValid(orderDetail.getConsignmentData(),errorMap);
		    		}
		    				
		    	ValidationUtils.removeInvalidconsignments(orderDetail,errorMap);
		    	if(orderDetail.getConsignmentData().size()>0) {
				eTowerWrapper.makeCreateShippingOrderEtowerCallForAPIData(orderDetail,senderDataResponseList);
		    	}
				return;
			}
	    if( "1PM3E".equalsIgnoreCase(serviceType) 
				|| "1PS3".equalsIgnoreCase(serviceType) 
				|| "1PM5".equalsIgnoreCase(serviceType) || "TST1".equalsIgnoreCase(serviceType) || "NZ".equalsIgnoreCase(serviceType)) {
	    	if(isPostcodeValidationReq) {
	    		if("NZ".equalsIgnoreCase(serviceType)) {
	    			d2zValidator.isNZPostCodeValid(orderDetail,errorMap);
	    		}else {
	    			d2zValidator.isPostCodeValid(orderDetail.getConsignmentData(),errorMap);
	    		}
	    		}			
	    	ValidationUtils.removeInvalidconsignments(orderDetail,errorMap);
	    	if(orderDetail.getConsignmentData().size()>0) {
			eTowerWrapper.makeCreateShippingOrderEtowerCallForAPIData(orderDetail,senderDataResponseList);
	    	}
			return;
			
		}else if ("FWM".equalsIgnoreCase(serviceType) || "FW".equalsIgnoreCase(serviceType) || "FW3".equalsIgnoreCase(serviceType) || "1PS4".equalsIgnoreCase(serviceType)) {
			if(isPostcodeValidationReq) {
				if("1PS4".equalsIgnoreCase(serviceType)) {
						d2zValidator.isPostCodeValid(orderDetail.getConsignmentData(),errorMap);
						
				}else {
					d2zValidator.isFWPostCodeValid(orderDetail.getConsignmentData(),errorMap);				}
			}
			ValidationUtils.removeInvalidconsignments(orderDetail,errorMap);
			if(orderDetail.getConsignmentData().size()>0) {
			makeCreateShippingOrderPFLCall(orderDetail.getConsignmentData(),senderDataResponseList,orderDetail.getUserName(), serviceType);
			}
			return;// senderDataResponseList;
		}else if("FWS".equalsIgnoreCase(serviceType)) {
			if(isPostcodeValidationReq) {
			d2zValidator.isFWPostCodeValid(orderDetail.getConsignmentData(),errorMap);
			}
			ValidationUtils.removeInvalidconsignments(orderDetail,errorMap);
			if(orderDetail.getConsignmentData().size()>0) {
			pcaWrapper.makeCreateShippingOrderPFACall(orderDetail.getConsignmentData(),senderDataResponseList,orderDetail.getUserName(),serviceType);
			}
			return;// senderDataResponseList;
		}
		else if("STS".equalsIgnoreCase(serviceType)) {
			if(isPostcodeValidationReq) {
				d2zValidator.isSTPostCodeValidAPI(orderDetail.getConsignmentData(),errorMap);
			}
			ValidationUtils.removeInvalidconsignments(orderDetail,errorMap);
			if(orderDetail.getConsignmentData().size()>0) {
			pcaWrapper.makeCreateShippingOrderPFACall(orderDetail.getConsignmentData(),senderDataResponseList,orderDetail.getUserName(),"STS-Sub");
			}
			return; //senderDataResponseList;
		}
		/*else if("MCM".equalsIgnoreCase(serviceType) || "MCM1".equalsIgnoreCase(serviceType) || "MCM2".equalsIgnoreCase(serviceType) 
					|| "MCM3".equalsIgnoreCase(serviceType) || "MCS".equalsIgnoreCase(serviceType) || "STS".equalsIgnoreCase(serviceType)){
			//ValidationUtils.removeInvalidconsignments(orderDetail,errorMap);
			if(orderDetail.getConsignmentData().size()>0) {
			PFLSenderDataRequest consignmentData = d2zValidator.isFWSubPostCodeValid(orderDetail);
			if(consignmentData.getPflSenderDataApi().size() > 0) {
				if("MCS".equalsIgnoreCase(serviceType) || "STS".equalsIgnoreCase(serviceType)) 	
					pcaWrapper.makeCreateShippingOrderPFACall(consignmentData.getPflSenderDataApi(),senderDataResponseList,orderDetail.getUserName(),serviceType);
				else
					makeCreateShippingOrderPFLCall(consignmentData.getPflSenderDataApi(),senderDataResponseList,orderDetail.getUserName(), serviceType);
			}
			
			if(consignmentData.getNonPflSenderDataApi().size() > 0 && "STS".equalsIgnoreCase(serviceType)) {
				pcaWrapper.makeCreateShippingOrderPFACall(consignmentData.getNonPflSenderDataApi(),senderDataResponseList,orderDetail.getUserName(),"STS-Sub");
			}
			
			if(consignmentData.getNonPflSenderDataApi().size() > 0 && !"STS".equalsIgnoreCase(serviceType)) {
				if(isPostcodeValidationReq) {
		    		d2zValidator.isPostCodeValid(orderDetail,errorMap);
		    		}	
				ValidationUtils.removeInvalidconsignments(orderDetail,errorMap);
				if(orderDetail.getConsignmentData().size()>0) {
				String senderFileID = d2zDao.createConsignments(consignmentData.getNonPflSenderDataApi(), userId, orderDetail.getUserName(), null);
				List<String> insertedOrder = d2zDao.fetchBySenderFileID(senderFileID);
				Iterator itr = insertedOrder.iterator();
				while (itr.hasNext()) {
					Object[] obj = (Object[]) itr.next();
					SenderDataResponse senderDataResponse = new SenderDataResponse();
					senderDataResponse.setReferenceNumber(obj[0].toString());
					//senderDataResponse.setBarcodeLabelNumber(obj[3] != null ? obj[3].toString() : "");
					senderDataResponse.setCarrier(obj[4].toString());
					senderDataResponse.setInjectionPort(obj[5] != null ? obj[5].toString() : "");
					if("MCS".equalsIgnoreCase(serviceType))
					{
				String barcode = obj[1].toString();
				senderDataResponse.setBarcodeLabelNumber("]d2".concat(barcode.replaceAll("\\[|\\]", "")));
				
				if(senderDataResponse.getInjectionPort().equals("SYD") ||senderDataResponse.getInjectionPort().equals("MEL")||senderDataResponse.getInjectionPort().equals("BNE")||senderDataResponse.getInjectionPort().equals("ADL") ||senderDataResponse.getInjectionPort().equals("PER"))
				{
					senderDataResponse.setSortcode(senderDataResponse.getInjectionPort());
				}
				else
				{
					senderDataResponse.setSortcode("OTH");
				}
					}
			else
			{
			senderDataResponse.setBarcodeLabelNumber(obj[3] != null ? obj[3].toString() : "");
			}
					senderDataResponseList.add(senderDataResponse);
				}
				}
			}
			}
			return;
		}*/}else {
			autoShipment = ("Y").equals(userServiceRepository.fetchAutoShipmentIndicator(userId,serviceType));
			System.out.println(userId+" : "+autoShipment);
			
		}
		System.out.println(isPostcodeValidationReq);
		if("RC1".equalsIgnoreCase(serviceType)) {
			isPostcodeValidationReq = false;
		}
		else if("RC2".equalsIgnoreCase(serviceType)) {
			d2zValidator.isRC2PostCodeValid(orderDetail.getConsignmentData(),errorMap);
		}
		if(isPostcodeValidationReq) {
    		d2zValidator.isPostCodeValid(orderDetail.getConsignmentData(),errorMap);
    		}	
		ValidationUtils.removeInvalidconsignments(orderDetail,errorMap);
		if(orderDetail.getConsignmentData().size()>0) {
		String senderFileID = d2zDao.createConsignments(orderDetail.getConsignmentData(), userId, orderDetail.getUserName(), null);
		List<String> insertedOrder = d2zDao.fetchBySenderFileID(senderFileID);
		Iterator itr = insertedOrder.iterator();
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
			SenderDataResponse senderDataResponse = new SenderDataResponse();
			senderDataResponse.setReferenceNumber(obj[0].toString());
			String barcode = obj[1].toString();
			senderDataResponse.setBarcodeLabelNumber("]d2".concat(barcode.replaceAll("\\[|\\]", "")));
			senderDataResponse.setCarrier(obj[4].toString());
			senderDataResponse.setInjectionPort(obj[5] != null ? obj[5].toString() : "");
			senderDataResponseList.add(senderDataResponse);
			if (autoShipment)
				autoShipRefNbrs.add(senderDataResponse.getReferenceNumber());
		}
		}
		return; //senderDataResponseList;
	}
	

	private MCSSenderDataRequest constructMCSRequest(CreateConsignmentRequest orderDetail) {


		List<SenderDataApi> orderDetailList = orderDetail.getConsignmentData();
		MCSSenderDataRequest request = new MCSSenderDataRequest();
		List<SenderDataApi> pflSenderData = new ArrayList<SenderDataApi>();
		List<SenderDataApi> fastwaySenderData = new ArrayList<SenderDataApi>();
		List<SenderDataApi> etowerSenderData = new ArrayList<SenderDataApi>();

		List<String> pflPostcodeList = D2ZSingleton.getInstance().getMasterPflPostCodeZoneList();
		List<String> fastwayPostcodeList = D2ZSingleton.getInstance().getMasterFWPostCodeZoneList();
		List<String> postcodeZone3List = D2ZSingleton.getInstance().getMasterPostCodeZone3List();

		orderDetailList.forEach(obj -> {
			String requestData = obj.getConsigneeState().trim().toUpperCase()
					.concat(obj.getConsigneeSuburb().trim().toUpperCase()).concat(obj.getConsigneePostcode().trim());
			if(pflPostcodeList.contains(requestData)) {
				pflSenderData.add(obj);
			}else if(fastwayPostcodeList.contains(requestData)) {
				fastwaySenderData.add(obj);
			}else if(postcodeZone3List.contains(requestData)) {
				pflSenderData.add(obj);
			}else {
				etowerSenderData.add(obj);
			}
		});
		request.setPflSenderData(pflSenderData);
		request.setFastwaySenderData(fastwaySenderData);
		request.setEparcelSenderData(etowerSenderData);
		return request;
	
	}


	/*
	 * private PFLSenderDataRequest constructMCSRequest(CreateConsignmentRequest
	 * orderDetail) {
	 * 
	 * PFLSenderDataRequest request = new PFLSenderDataRequest();
	 * List<SenderDataApi> pflSenderData = new ArrayList<SenderDataApi>();
	 * List<SenderDataApi> nonPflSenderData = new ArrayList<SenderDataApi>();
	 * List<SenderDataApi> etowerData = new ArrayList<SenderDataApi>();
	 * 
	 * List<String> postCodeFWSubList =
	 * D2ZSingleton.getInstance().getFWPostCodeZoneList(); Map<String, String>
	 * postCodeZoneMap = D2ZSingleton.getInstance().getPostCodeZoneMap();
	 * orderDetail.getConsignmentData().forEach(obj -> { String suburbState =
	 * obj.getConsigneeSuburb().trim().toUpperCase().concat(obj.getConsigneePostcode
	 * ().trim()); if("N0".equalsIgnoreCase(postCodeZoneMap.get(suburbState))) {
	 * etowerData.add(obj); } else
	 * if("V0".equalsIgnoreCase(postCodeZoneMap.get(suburbState))) {
	 * nonPflSenderData.add(obj); } else
	 * if(postCodeFWSubList.contains(obj.getConsigneeState().trim().toUpperCase().
	 * concat(obj.getConsigneeSuburb().trim().toUpperCase()).concat(obj.
	 * getConsigneePostcode().trim()))) { pflSenderData.add(obj); }else {
	 * etowerData.add(obj); } }); request.setPflSenderDataApi(pflSenderData);
	 * request.setNonPflSenderDataApi(nonPflSenderData);
	 * request.setEtowerSenderData(etowerData); return request;
	 * 
	 * }
	 */


	private void makeCreateShippingOrderPFLCall (List<SenderDataApi> data,
			List<SenderDataResponse> senderDataResponseList, String userName, String serviceType) throws FailureResponseException {
		PflCreateShippingRequest pflRequest = new PflCreateShippingRequest();
		Map<String, String> systemRefNbrMap = new HashMap<String, String>();

		List<PflCreateShippingOrderInfo> pflOrderInfoRequest = new ArrayList<PflCreateShippingOrderInfo>();
		for (SenderDataApi orderDetail : data) {
			PflCreateShippingOrderInfo request = new PflCreateShippingOrderInfo();
			 //Random rnd = new Random();
			 int uniqueNumber = SingletonCounter.getInstance().getPFLCount();
    		 String sysRefNbr = "RTFGA"+uniqueNumber;
    		 request.setCustom_ref(sysRefNbr);
    		 
			systemRefNbrMap.put(request.getCustom_ref(), orderDetail.getReferenceNumber());

			//request.setCustom_ref(orderDetail.getReferenceNumber());
			request.setRecipientCompany(orderDetail.getConsigneeCompany());
			String recpName = orderDetail.getConsigneeName().length() > 34
					? orderDetail.getConsigneeName().substring(0, 34)
					: orderDetail.getConsigneeName();
			request.setRecipientName(recpName);
			request.setAddressLine1(orderDetail.getConsigneeAddr1());
			request.setAddressLine2(orderDetail.getConsigneeAddr2());
			request.setEmail(orderDetail.getConsigneeEmail());
			request.setPhone(orderDetail.getConsigneePhone());
			request.setCity(orderDetail.getConsigneeSuburb());
			request.setState(orderDetail.getConsigneeState());
			request.setPostcode(orderDetail.getConsigneePostcode());
			request.setCountry("AU");
			request.setWeight(Double.valueOf(orderDetail.getWeight()));
			if("FW3".equalsIgnoreCase(serviceType)) {
				request.setDelivery_instruction("300LBX");
			}
			pflOrderInfoRequest.add(request);
		}
		pflRequest.setOrderinfo(pflOrderInfoRequest);
		pflWrapper.createShippingOrderPFL(data, pflRequest, userName, senderDataResponseList, serviceType,systemRefNbrMap);
	}

	@Override
	public void makeCallToEtowerBasedonSupplierUI(List<String> incomingRefNbr) {
		
		List<SenderdataMaster> eTowerOrders = d2zDao.fetchDataBasedonSupplier(incomingRefNbr,"eTower");
		eTowerWrapper.makeCalltoEtower(eTowerOrders);
	}
}
