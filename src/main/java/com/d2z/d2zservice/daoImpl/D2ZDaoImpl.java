package com.d2z.d2zservice.daoImpl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.d2z.d2zservice.dao.ID2ZDao;
import com.d2z.d2zservice.entity.APIRates;
import com.d2z.d2zservice.entity.ETowerResponse;
import com.d2z.d2zservice.entity.EbayResponse;
import com.d2z.d2zservice.entity.PostcodeZone;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.entity.Trackandtrace;
import com.d2z.d2zservice.entity.User;
import com.d2z.d2zservice.entity.UserService;
import com.d2z.d2zservice.model.ClientDashbaord;
import com.d2z.d2zservice.model.EditConsignmentRequest;
import com.d2z.d2zservice.model.ResponseMessage;
import com.d2z.d2zservice.model.SenderData;
import com.d2z.d2zservice.model.SenderDataApi;
import com.d2z.d2zservice.model.UserDetails;
import com.d2z.d2zservice.model.etower.CreateShippingRequest;
import com.d2z.d2zservice.model.etower.CreateShippingResponse;
import com.d2z.d2zservice.model.etower.ETowerTrackingDetails;
import com.d2z.d2zservice.model.etower.EtowerErrorResponse;
import com.d2z.d2zservice.model.etower.ResponseData;
import com.d2z.d2zservice.model.etower.TrackEventResponseData;
import com.d2z.d2zservice.model.etower.TrackingEventResponse;
import com.d2z.d2zservice.proxy.ETowerProxy;
import com.d2z.d2zservice.repository.APIRatesRepository;
import com.d2z.d2zservice.repository.ETowerResponseRepository;
import com.d2z.d2zservice.repository.EbayResponseRepository;
import com.d2z.d2zservice.repository.PostcodeZoneRepository;
import com.d2z.d2zservice.repository.SenderDataRepository;
import com.d2z.d2zservice.repository.TrackAndTraceRepository;
import com.d2z.d2zservice.repository.UserRepository;
import com.d2z.d2zservice.repository.UserServiceRepository;
import com.d2z.d2zservice.util.D2ZCommonUtil;
import com.d2z.d2zservice.wrapper.ETowerWrapper;
import com.d2z.singleton.D2ZSingleton;
import com.ebay.soap.eBLBaseComponents.CompleteSaleResponseType;

@Repository
public class D2ZDaoImpl implements ID2ZDao{
	
	@Autowired
	SenderDataRepository senderDataRepository;

	@Autowired
	TrackAndTraceRepository trackAndTraceRepository;
	
	@Autowired
	PostcodeZoneRepository postcodeZoneRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserServiceRepository userServiceRepository;
	
	@Autowired
	EbayResponseRepository ebayResponseRepository;
	
	@Autowired
	ETowerResponseRepository eTowerResponseRepository;
	
	@Autowired
	APIRatesRepository apiRatesRepository;
	
	@Autowired
	private ETowerWrapper etowerWrapper;
	
	@Autowired
	private ETowerProxy eTowerProxy;
	@Override
	public String exportParcel(List<SenderData> orderDetailList) {
		Map<String,String> postCodeStateMap = D2ZSingleton.getInstance().getPostCodeStateMap();
		List<String> incomingRefNbr = new ArrayList<String>();
		User userInfo = userRepository.findByUsername(orderDetailList.get(0).getUserName());
		List<SenderdataMaster> senderDataList = new ArrayList<SenderdataMaster>();
		String fileSeqId = "D2ZUI"+senderDataRepository.fetchNextSeq().toString();
		for(SenderData senderDataValue: orderDetailList) {
			incomingRefNbr.add(senderDataValue.getReferenceNumber());
			SenderdataMaster senderDataObj = new SenderdataMaster();
			senderDataObj.setSender_Files_ID(fileSeqId);
			senderDataObj.setReference_number(senderDataValue.getReferenceNumber());
			senderDataObj.setConsigneeCompany(senderDataValue.getConsigneeCompany());
			senderDataObj.setConsignee_name(senderDataValue.getConsigneeName());
			senderDataObj.setConsignee_addr1(senderDataValue.getConsigneeAddr1());
			senderDataObj.setConsignee_addr2(senderDataValue.getConsigneeAddr2());
			senderDataObj.setConsignee_Suburb(senderDataValue.getConsigneeSuburb().trim());
			senderDataObj.setConsignee_State(postCodeStateMap.get(senderDataValue.getConsigneePostcode().trim()));
			senderDataObj.setConsignee_Postcode(senderDataValue.getConsigneePostcode().trim());
			senderDataObj.setConsignee_Phone(senderDataValue.getConsigneePhone());
			senderDataObj.setProduct_Description(senderDataValue.getProductDescription());
			senderDataObj.setValue(senderDataValue.getValue());
			senderDataObj.setCurrency(senderDataValue.getCurrency());
			senderDataObj.setShippedQuantity(senderDataValue.getShippedQuantity());
			senderDataObj.setWeight(Double.parseDouble(senderDataValue.getWeight()));
			senderDataObj.setDimensions_Length(senderDataValue.getDimensionsLength());
			senderDataObj.setDimensions_Width(senderDataValue.getDimensionsWidth());
			senderDataObj.setDimensions_Height(senderDataValue.getDimensionsHeight());
			senderDataObj.setServicetype(senderDataValue.getServiceType());
			senderDataObj.setDeliverytype(senderDataValue.getDeliverytype());
			String shipperName = (senderDataValue.getShipperName() != null && !senderDataValue.getShipperName().isEmpty()) ? senderDataValue.getShipperName() : userInfo.getCompanyName();
			senderDataObj.setShipper_Name(shipperName);
			String shipperAddress = (senderDataValue.getShipperAddr1() != null && !senderDataValue.getShipperAddr1().isEmpty()) ? senderDataValue.getShipperAddr1() : userInfo.getAddress();
			senderDataObj.setShipper_Addr1(shipperAddress);
			String shipperCity = (senderDataValue.getShipperCity() != null && !senderDataValue.getShipperCity().isEmpty()) ? senderDataValue.getShipperCity() : userInfo.getSuburb();
			senderDataObj.setShipper_City(shipperCity);
			String shipperState = (senderDataValue.getShipperState() != null && !senderDataValue.getShipperState().isEmpty()) ? senderDataValue.getShipperState() : userInfo.getState();
			senderDataObj.setShipper_State(shipperState);
			String shipperPostcode = (senderDataValue.getShipperPostcode() != null && !senderDataValue.getShipperPostcode().isEmpty()) ? senderDataValue.getShipperPostcode() : userInfo.getPostcode();
			senderDataObj.setShipper_Postcode(shipperPostcode);
			String shipperCountry = (senderDataValue.getShipperCountry() != null && !senderDataValue.getShipperCountry().isEmpty()) ? senderDataValue.getShipperCountry() : userInfo.getCountry();
			senderDataObj.setShipper_Country(shipperCountry);
			senderDataObj.setFilename(senderDataValue.getFileName());
			senderDataObj.setInnerItem(1);
			senderDataObj.setInjectionType(senderDataValue.getInjectionType());
			senderDataObj.setBagId(senderDataValue.getBagId());
			senderDataObj.setUser_ID(senderDataValue.getUserID());
			senderDataObj.setSku(senderDataValue.getSku());
			senderDataObj.setLabelSenderName(senderDataValue.getLabelSenderName());
			senderDataObj.setDeliveryInstructions(senderDataValue.getDeliveryInstructions());
			senderDataObj.setCarrier(senderDataValue.getCarrier());
			senderDataObj.setConsignee_addr2(senderDataValue.getConsigneeAddr2());
			senderDataObj.setConsignee_Email(senderDataValue.getConsigneeEmail());
			senderDataList.add(senderDataObj);
		}
		senderDataRepository.saveAll(senderDataList);
		senderDataRepository.inOnlyTest(fileSeqId);
		Runnable r = new Runnable( ) {			
	        public void run() {
	        	 makeCalltoEtower(incomingRefNbr);
	        }
	     };
	    new Thread(r).start();
		return fileSeqId;
	}

	public void makeCalltoEtower(List<String> incomingRefNbr) {
		System.out.println("Background Thread created.....");
		List<SenderdataMaster> eTowerOrders = senderDataRepository.fetchDataForEtowerCall(incomingRefNbr);
		System.out.println(eTowerOrders.size());
		if(!eTowerOrders.isEmpty()) {
			List<CreateShippingRequest> eTowerRequest = new ArrayList<CreateShippingRequest>();

			for(SenderdataMaster orderDetail : eTowerOrders) {
				CreateShippingRequest request = new CreateShippingRequest();
				System.out.println(orderDetail.getArticleId());
				request.setTrackingNo(orderDetail.getArticleId());
				request.setReferenceNo("SW10"+orderDetail.getReference_number());
				request.setRecipientCompany(orderDetail.getConsigneeCompany());
				String recpName = orderDetail.getConsignee_name().length() >34 ? orderDetail.getConsignee_name().substring(0, 34) : orderDetail.getConsignee_name(); 
				request.setRecipientName(recpName);
				request.setAddressLine1(orderDetail.getConsignee_addr1());
				request.setAddressLine2(orderDetail.getConsignee_addr2());
				request.setEmail(orderDetail.getConsignee_Email());
				request.setCity(orderDetail.getConsignee_Suburb());
				request.setState(orderDetail.getConsignee_State());
				request.setPostcode(orderDetail.getConsignee_Postcode());
				if(orderDetail.getCarrier().equalsIgnoreCase("Express")){
				request.setServiceOption("Express-Post");
				}
				else {
					request.setServiceOption("E-Parcel");

				}
				request.setFacility(orderDetail.getInjectionState());
				request.setWeight(Double.valueOf(orderDetail.getWeight()));
				request.setInvoiceValue(orderDetail.getValue());
				request.getOrderItems().get(0).setUnitValue(orderDetail.getValue());
				eTowerRequest.add(request);
			}
			CreateShippingResponse response = eTowerProxy.makeCallForCreateShippingOrder(eTowerRequest);
			//CreateShippingResponse response = eTowerProxy.makeStubForCreateShippingOrder();
			 List<ETowerResponse> responseEntity = new ArrayList<ETowerResponse>();
			 if(response!=null) {
	     			List<ResponseData> responseData = response.getData();
	     			if(responseData== null && null!=response.getErrors()) {
	     				 for(EtowerErrorResponse error : response.getErrors()) {
	 	     				ETowerResponse errorResponse  = new ETowerResponse();
	     				 	errorResponse.setAPIName("Create Shipping Order");
		     			 	errorResponse.setStatus(response.getStatus());
	     				 	errorResponse.setErrorCode(error.getCode());
	     				 	errorResponse.setErrorMessage(error.getMessage());
	     				 	responseEntity.add(errorResponse);
	     				}
	     			}
	     			
	     			for(ResponseData data : responseData) {
	     				List<EtowerErrorResponse> errors = data.getErrors();
	     				if(null == errors) {
	     				ETowerResponse errorResponse  = new ETowerResponse();
  				 	errorResponse.setAPIName("Create Shipping Order");
	     			 	errorResponse.setStatus(data.getStatus());
	     			 	errorResponse.setOrderId(data.getOrderId());
	     		 		errorResponse.setReferenceNumber(data.getReferenceNo());
	     		 		errorResponse.setTrackingNo(data.getTrackingNo());
	     		 		errorResponse.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
	     		 		responseEntity.add(errorResponse);
	     				}
	     				else {
	     				 for(EtowerErrorResponse error : errors) {
	     					ETowerResponse errorResponse  = new ETowerResponse();
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
	     				logEtowerResponse(responseEntity);
	        
		}
	}
	@Override
	public List<String> fileList(Integer userId) {
		List<String> listOfFileNames= senderDataRepository.fetchFileName(userId);
		return listOfFileNames;
	}
	
	@Override
	public List<String> labelFileList(Integer userId) {
		List<String> listOfFileNames= senderDataRepository.fetchLabelFileName(userId);
		return listOfFileNames;
	}

	@Override
	public List<SenderdataMaster> consignmentFileData(String fileName) {
		List<SenderdataMaster> listOfFileNames= senderDataRepository.fetchConsignmentData(fileName);
		return listOfFileNames;
	}
	
	@Override
	public List<SenderdataMaster> fetchManifestData(String fileName) {
		List<SenderdataMaster> allConsignmentData= senderDataRepository.fetchManifestData(fileName);
		return allConsignmentData;
	}

	@Override
	public String consignmentDelete(String refrenceNumlist) {
		//Calling Delete Store Procedure
		senderDataRepository.consigneeDelete(refrenceNumlist);
		return "Selected Consignments Deleted Successfully";
	}

	@Override
	public List<String> trackingDetails(String fileName) {
		List<String> trackingDetails= senderDataRepository.fetchTrackingDetails(fileName);
		return trackingDetails;
	}

	@Override
	public List<String> trackingLabel(List<String> refBarNum) {
		//String trackingDetails= senderDataRepository.fetchTrackingLabel(refBarNum);
		List<String> trackingDetails= senderDataRepository.fetchTrackingLabel(refBarNum);
		System.out.println(trackingDetails.size());
		return trackingDetails;
	}

	@Override
	public String manifestCreation(String manifestNumber, String refrenceNumber) {
		//Calling Delete Store Procedure
		senderDataRepository.manifestCreation(manifestNumber, refrenceNumber);
		String [] refNbrs = refrenceNumber.split(",");
		int userId = senderDataRepository.fetchUserIdByReferenceNumber(refNbrs[0]);
		String autoShipment = userRepository.fetchAutoShipmentIndicator(userId);
		if("Y".equalsIgnoreCase(autoShipment)) {
			allocateShipment(refrenceNumber, manifestNumber.concat("AutoShip"));
		}
		return "Manifest Updated Successfully";
	}

	public List<Trackandtrace> trackParcel(String refNbr) {
		List<Trackandtrace> trackAndTrace = trackAndTraceRepository.fetchTrackEventByRefNbr(refNbr);
		return trackAndTrace;
	}


	@Override
	public String createConsignments(List<SenderDataApi> orderDetailList, int userId, String userName) {
		Map<String,String> postCodeStateMap = D2ZSingleton.getInstance().getPostCodeStateMap();
		List<SenderdataMaster> senderDataList = new ArrayList<SenderdataMaster>();
		User userInfo = userRepository.findByUsername(userName);
		String fileSeqId = "D2ZAPI"+senderDataRepository.fetchNextSeq();
		for(SenderDataApi senderDataValue: orderDetailList) {
			SenderdataMaster senderDataObj = new SenderdataMaster();
			senderDataObj.setUser_ID(userId);
			senderDataObj.setSender_Files_ID(fileSeqId);
			senderDataObj.setReference_number(senderDataValue.getReferenceNumber());
			senderDataObj.setConsigneeCompany(senderDataValue.getConsigneeCompany());
			senderDataObj.setConsignee_name(senderDataValue.getConsigneeName());
			senderDataObj.setConsignee_addr1(senderDataValue.getConsigneeAddr1());
			senderDataObj.setConsignee_addr2(senderDataValue.getConsigneeAddr2());
			senderDataObj.setConsignee_Suburb(senderDataValue.getConsigneeSuburb());
			senderDataObj.setConsignee_State(postCodeStateMap.get(senderDataValue.getConsigneePostcode()));
			senderDataObj.setConsignee_Postcode(senderDataValue.getConsigneePostcode());
			senderDataObj.setConsignee_Phone(senderDataValue.getConsigneePhone());
			senderDataObj.setProduct_Description(senderDataValue.getProductDescription());
			senderDataObj.setValue(senderDataValue.getValue());
			senderDataObj.setCurrency(senderDataValue.getCurrency());
			senderDataObj.setShippedQuantity(senderDataValue.getShippedQuantity());
			senderDataObj.setWeight(Double.valueOf(senderDataValue.getWeight()));
			senderDataObj.setDimensions_Length(senderDataValue.getDimensionsLength());
			senderDataObj.setDimensions_Width(senderDataValue.getDimensionsWidth());
			senderDataObj.setDimensions_Height(senderDataValue.getDimensionsHeight());
			senderDataObj.setServicetype(senderDataValue.getServiceType());
			senderDataObj.setDeliverytype(senderDataValue.getDeliverytype());
			String shipperName = (senderDataValue.getShipperName() != null && !senderDataValue.getShipperName().isEmpty()) ? senderDataValue.getShipperName() : userInfo.getCompanyName();
			senderDataObj.setShipper_Name(shipperName);
			String shipperAddress = (senderDataValue.getShipperAddr1() != null && !senderDataValue.getShipperAddr1().isEmpty()) ? senderDataValue.getShipperAddr1() : userInfo.getAddress();
			senderDataObj.setShipper_Addr1(shipperAddress);
			String shipperCity = (senderDataValue.getShipperCity() != null && !senderDataValue.getShipperCity().isEmpty()) ? senderDataValue.getShipperCity() : userInfo.getSuburb();
			senderDataObj.setShipper_City(shipperCity);
			String shipperState = (senderDataValue.getShipperState() != null && !senderDataValue.getShipperState().isEmpty()) ? senderDataValue.getShipperState() : userInfo.getState();
			senderDataObj.setShipper_State(shipperState);
			String shipperPostcode = (senderDataValue.getShipperPostcode() != null && !senderDataValue.getShipperPostcode().isEmpty()) ? senderDataValue.getShipperPostcode() : userInfo.getPostcode();
			senderDataObj.setShipper_Postcode(shipperPostcode);
			String shipperCountry = (senderDataValue.getShipperCountry() != null && !senderDataValue.getShipperCountry().isEmpty()) ? senderDataValue.getShipperCountry() : userInfo.getCountry();
			senderDataObj.setShipper_Country(shipperCountry);
			senderDataObj.setFilename("D2ZAPI"+D2ZCommonUtil.getCurrentTimestamp());
			//senderDataObj.setFilename(senderDataValue.getFileName());
			senderDataObj.setSku(senderDataValue.getSku());
			senderDataObj.setLabelSenderName(senderDataValue.getLabelSenderName());
			senderDataObj.setDeliveryInstructions(senderDataValue.getDeliveryInstructions());
			senderDataObj.setCarrier("eParcel");
			senderDataObj.setConsignee_Email(senderDataValue.getConsigneeEmail());
			senderDataList.add(senderDataObj);
		}
		List<SenderdataMaster> insertedOrder = (List<SenderdataMaster>) senderDataRepository.saveAll(senderDataList);
		senderDataRepository.inOnlyTest(fileSeqId);
		return fileSeqId;
	}
	
    public List<PostcodeZone> fetchAllPostCodeZone(){
    	List<PostcodeZone> postCodeZoneList= (List<PostcodeZone>) postcodeZoneRepository.findAll();
    	System.out.println(postCodeZoneList.size());
    	return postCodeZoneList;
    }
    
    public List<String> fetchAllReferenceNumbers(){
    	List<String> referenceNumber_DB= senderDataRepository.fetchAllReferenceNumbers();
    	return referenceNumber_DB;
    }

	@Override
	public List<String> fetchBySenderFileID(String senderFileID) {
		List<String> senderDataMaster = senderDataRepository.fetchBySenderFileId(senderFileID);
		return senderDataMaster;
	}

	@Override
	public List<Trackandtrace> trackParcelByArticleID(String articleID) {
		List<Trackandtrace> trackAndTrace = trackAndTraceRepository.fetchTrackEventByArticleID(articleID);
		return trackAndTrace;
	}

	@Override

public ResponseMessage editConsignments(List<EditConsignmentRequest> requestList) {
		/*requestList.forEach(obj->{
			senderDataRepository.editConsignments(obj.getReferenceNumber(), obj.getWeight());
		});*/
		List<String> incorrectRefNbrs = new ArrayList<String>();
		int updatedRows=0;
		//Timestamp start = Timestamp.from(Instant.now());
		List<SenderdataMaster> senderDataList = new ArrayList<SenderdataMaster>();
		for(EditConsignmentRequest obj : requestList) {
			SenderdataMaster senderData = senderDataRepository.fetchByReferenceNumbers(obj.getReferenceNumber());
			if(senderData!=null) {
			updatedRows++;
			senderData.setWeight(obj.getWeight());
			senderDataList.add(senderData);
		}
			else {
				incorrectRefNbrs.add(obj.getReferenceNumber());
			}
		}
		senderDataRepository.saveAll(senderDataList);
		/*Timestamp end = Timestamp.from(Instant.now());
		long callDuration = end.getTime() - start.getTime();
		System.out.println("Call Duration : "+callDuration);*/
		ResponseMessage responseMsg = new ResponseMessage();
		if(updatedRows==0) {
			responseMsg.setResponseMessage("Update failed");
		}else if(updatedRows == requestList.size()) {
			responseMsg.setResponseMessage("Weight updated successfully");
		}
		else {
			responseMsg.setResponseMessage("Partially updated");
		}
		responseMsg.setMessageDetail(incorrectRefNbrs);
		return responseMsg;
	}

	@Override
	public String allocateShipment(String referenceNumbers, String shipmentNumber) {
		senderDataRepository.allocateShipment(referenceNumbers, shipmentNumber);
		Runnable r = new Runnable( ) {			
	        public void run() {
	        	String[] refNbrArray = referenceNumbers.split(",");
	        	List<String> articleIDS = senderDataRepository.fetchDataForEtowerForeCastCall(refNbrArray);
	       	
	        	 if(!articleIDS.isEmpty()) {
	 	        	List<List<String>> trackingNbrList = ListUtils.partition(articleIDS, 300);
	 	        	for(List<String> trackingNumbers : trackingNbrList) {
	        		 CreateShippingResponse response = eTowerProxy.makeCallForForeCast(trackingNumbers);
	        	 	List<ETowerResponse> responseEntity = new ArrayList<ETowerResponse>();

	     		if(response!=null) {
	     			List<ResponseData> responseData = response.getData();
	     			if(responseData== null && null!=response.getErrors()) {
	     				 for(EtowerErrorResponse error : response.getErrors()) {
	 	     				ETowerResponse errorResponse  = new ETowerResponse();
	     				 	errorResponse.setAPIName("Forecast");
		     			 	errorResponse.setStatus(response.getStatus());
	     				 	errorResponse.setErrorCode(error.getCode());
	     				 	errorResponse.setErrorMessage(error.getMessage());
	     				 	responseEntity.add(errorResponse);
	     				}
	     			}
	     			
	     			for(ResponseData data : responseData) {
	     				List<EtowerErrorResponse> errors = data.getErrors();
	     				if(null == errors) {
	     				ETowerResponse errorResponse  = new ETowerResponse();
     				 	errorResponse.setAPIName("Forecast");
	     			 	errorResponse.setStatus(data.getStatus());
	     			 	errorResponse.setOrderId(data.getOrderId());
	     		 		errorResponse.setReferenceNumber(data.getReferenceNo());
	     		 		errorResponse.setTrackingNo(data.getTrackingNo());
	     		 		errorResponse.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
	     		 		responseEntity.add(errorResponse);
	     				}
	     				else {
	     				 for(EtowerErrorResponse error : errors) {
	     					ETowerResponse errorResponse  = new ETowerResponse();
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
	     				logEtowerResponse(responseEntity);
	     	
	     		TrackingEventResponse trackEventresponse = eTowerProxy.makeCallForTrackingEvents(trackingNumbers);	
	     		insertTrackingDetails(trackEventresponse);
	        	 }
	        	 }
	       }
	    };
	    new Thread(r).start();
		
		return "Shipment allocation Successful";
	}

	@Override
	public User addUser(UserDetails userData) {
		User userObj =new User();
		userObj.setCompanyName(userData.getCompanyName());
		userObj.setAddress(userData.getAddress());
		userObj.setSuburb(userData.getSuburb());
		userObj.setState(userData.getState());
		userObj.setPostcode(userData.getPostCode());
		userObj.setCountry(userData.getCountry());
		userObj.setEmail(userData.getEmailAddress());
		userObj.setUsername(userData.getUserName());
		userObj.setPassword(D2ZCommonUtil.hashPassword(userData.getPassword()));
		userObj.setRole_Id(userData.getRole_Id());
		userObj.setName(userData.getContactName());
		userObj.setPhoneNumber(userData.getContactPhoneNumber());
		userObj.setUser_IsDeleted(false);
		userObj.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
		userObj.setModifiedTimestamp(Timestamp.valueOf(LocalDateTime.now()));
		userObj.setClientBrokerId(userData.getClientBroker());
		userObj.setEBayToken(userData.geteBayToken());
		userObj.setPassword_value(userData.getPassword());
		User savedUser = userRepository.save(userObj);
		return savedUser;
	}

	@Override
	public List<UserService> addUserService(User user,List<String> serviceTypeList) {
		List<UserService> userServiceList = new ArrayList<UserService>();
		for(String serviceType : serviceTypeList) {
		UserService userService = new UserService();
		userService.setUserId(user.getUser_Id());
		userService.setCompanyName(user.getCompanyName());
		userService.setUser_Name(user.getUsername());
		userService.setServiceType(serviceType);
		if(serviceType.equalsIgnoreCase("UnTracked")) {
			userService.setInjectionType("Origin Injection");
		}else {
			userService.setInjectionType("Direct Injection");
		}
		userService.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
		userService.setModifiedTimestamp(Timestamp.valueOf(LocalDateTime.now()));
		userServiceList.add(userService);
		}
		List<UserService> savedUserService = (List<UserService>) userServiceRepository.saveAll(userServiceList);
		return savedUserService;
	}


	@Override
	public User updateUser(User existingUser) {
		User updateduser = userRepository.save(existingUser);
		return updateduser;
	}

	@Override
	public void updateUserService(User existingUser, UserDetails userDetails) {
		List<UserService> userServiceList = new ArrayList<UserService>();
		if(!userDetails.getServiceType().isEmpty()) {
			for(String serviceType : userDetails.getServiceType() ) {
				UserService userService  = userServiceRepository.fetchbyCompanyNameAndServiceType(existingUser.getCompanyName(), serviceType,userDetails.getUserName());
				if(userService == null) {
					UserService newUserService = new UserService();
					newUserService.setUserId(existingUser.getUser_Id());
					newUserService.setCompanyName(existingUser.getCompanyName());
					newUserService.setUser_Name(existingUser.getUsername());
					newUserService.setServiceType(serviceType);
					newUserService.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
					newUserService.setModifiedTimestamp(Timestamp.valueOf(LocalDateTime.now()));
					userServiceList.add(newUserService);			
					}
				else {
					if(userService.isService_isDeleted()) {
						userService.setService_isDeleted(false);
						userService.setModifiedTimestamp(Timestamp.valueOf(LocalDateTime.now()));
						userServiceList.add(userService);
					}
				}
			}
		}
		if(!userDetails.getDeletedServiceTypes().isEmpty()) {
			for(String serviceType : userDetails.getDeletedServiceTypes() ) {
				UserService userService  = userServiceRepository.fetchbyCompanyNameAndServiceType(existingUser.getCompanyName(), serviceType,userDetails.getUserName());
				if(userService!=null) {
					userService.setService_isDeleted(true);
					userService.setModifiedTimestamp(Timestamp.valueOf(LocalDateTime.now()));
					userServiceList.add(userService);
				}
				
		}
		}
		userServiceRepository.saveAll(userServiceList);

	}

	/*private void deleteUserService(User existingUser, List<String> deletedServiceTypes) {
		List<UserService> userServiceList = new ArrayList<UserService>();
		if(!deletedServiceTypes.isEmpty()) {
		for(String serviceType : deletedServiceTypes ) {
			UserService userService  = userServiceRepository.fetchbyCompanyNameAndServiceType(existingUser.getCompanyName(), serviceType);
			if(userService!=null) {
				userService.setService_isDeleted(true);
				userService.setModifiedTimestamp(Timestamp.from(Instant.now()));
				userServiceList.add(userService);
			}
			
	}
		userServiceRepository.saveAll(userServiceList);
	}
	}*/

	@Override
	public String deleteUser(String companyName, String roleId) {
		User existingUser = userRepository.fetchUserbyCompanyName(companyName, Integer.parseInt(roleId));
		if(existingUser==null) {
			return "Company Name does not exist";
		}
		else {
			existingUser.setUser_IsDeleted(true);
			existingUser.setModifiedTimestamp(Timestamp.valueOf(LocalDateTime.now()));
			userRepository.save(existingUser);
			List<UserService> userService_DB  = userServiceRepository.fetchbyCompanyName(companyName);
			List<UserService> userServiceList = new ArrayList<UserService>();
			for(UserService userService: userService_DB) {
				userService.setService_isDeleted(true);
				userService.setModifiedTimestamp(Timestamp.valueOf(LocalDateTime.now()));
				userServiceList.add(userService);
			}
			userServiceRepository.saveAll(userServiceList);
		}
		return "User deleted successfully";
	}

	public User login(String userName, String passWord) {
		User userDaetils = userRepository.fetchUserDetails(userName, passWord);
		return userDaetils;
	}

	@Override
	public List<SenderdataMaster> fetchShipmentData(String shipmentNumber, List<Integer> clientIds) {
		List<SenderdataMaster> senderData = senderDataRepository.fetchShipmentData(shipmentNumber, clientIds);
		return senderData;
	}

	@Override
	public List<String> fetchServiceTypeByUserName(String userName) {
		List<String> serviceTypeList = userServiceRepository.fetchAllServiceTypeByUserName(userName);
		return serviceTypeList;
	}

	@Override
	public Trackandtrace getLatestStatusByReferenceNumber(String referenceNumber) {
		List<Trackandtrace> trackAndTraceList =  trackAndTraceRepository.fetchTrackEventByRefNbr(referenceNumber);
		Trackandtrace trackandTrace = null;
		if(!trackAndTraceList.isEmpty()) {
			trackandTrace = trackAndTraceList.get(0);
		}
		return trackandTrace;
	}
	
	@Override
	public List<String> fetchReferenceNumberByUserId(Integer userId) {
		List<String> referenceNumbers_DB = senderDataRepository.fetchReferenceNumberByUserId(userId);
		return referenceNumbers_DB;
	}

	@Override
	public Trackandtrace getLatestStatusByArticleID(String articleID) {
		List<Trackandtrace> trackAndTraceList =  trackAndTraceRepository.fetchTrackEventByArticleID(articleID);
		Trackandtrace trackandTrace = null;
		if(!trackAndTraceList.isEmpty()) {
			trackandTrace = trackAndTraceList.get(0);
		}
		return trackandTrace;
	}

	@Override
	public List<SenderdataMaster> findRefNbrByShipmentNbr(String[] referenceNumbers) {
		return senderDataRepository.findRefNbrByShipmentNbr(referenceNumbers);
	}

	@Override
	public void logEbayResponse(CompleteSaleResponseType response) {
				EbayResponse resp = new EbayResponse();
				resp.setAck(response.getAck().toString());
				if(null!= response.getErrors() && response.getErrors().length>0) {
				resp.setShortMessage(response.getErrors(0).getShortMessage());
				resp.setLongMessage(response.getErrors(0).getLongMessage());
				}
				ebayResponseRepository.save(resp);
	}
	
	public ClientDashbaord clientDahbaord(Integer userId) {
		ClientDashbaord clientDashbaord = new ClientDashbaord();
		clientDashbaord.setConsignmentsCreated(senderDataRepository.fecthConsignmentsCreated(userId));
		clientDashbaord.setConsignmentsManifested(senderDataRepository.fetchConsignmentsManifested(userId));
		clientDashbaord.setConsignmentsManifests(senderDataRepository.fetchConsignmentsManifests(userId));
		clientDashbaord.setConsignmentsDeleted(senderDataRepository.fetchConsignmentsDeleted(userId));
		clientDashbaord.setConsignmentDelivered(senderDataRepository.fetchConsignmentDelivered(userId));
		return clientDashbaord;
	}

	@Override
	public void deleteConsignment(String referenceNumbers) {
		senderDataRepository.deleteConsignments(referenceNumbers);
	}

	@Override
	public List<String> fetchServiceType(Integer user_id) {
		List<String> userServiceType = userServiceRepository.fetchUserServiceById(user_id);
		return userServiceType;
	}

	@Override
	public List<APIRates> fetchAllAPIRates() {
		List<APIRates> apiRates= (List<APIRates>) apiRatesRepository.findAll();
    	System.out.println(apiRates.size());
    	return apiRates;
	}

	@Override
	public void logEtowerResponse(List<ETowerResponse> responseEntity) {
		eTowerResponseRepository.saveAll(responseEntity);
		
	}
	@Override
	public ResponseMessage insertTrackingDetails(TrackingEventResponse trackEventresponse) {
		List<Trackandtrace> trackAndTraceList = new ArrayList<Trackandtrace>();
		List<TrackEventResponseData> responseData = trackEventresponse.getData();
		ResponseMessage responseMsg =  new ResponseMessage();

		if(responseData!=null && responseData.isEmpty()) {
			responseMsg.setResponseMessage("No Data from ETower");
		}
		else {
		
		for(TrackEventResponseData data : responseData ) {
		if(data!=null &&  data.getEvents()!=null) {
			
			for(ETowerTrackingDetails trackingDetails : data.getEvents()) {
				Trackandtrace trackandTrace = new Trackandtrace();
				trackandTrace.setArticleID(trackingDetails.getTrackingNo());
				trackandTrace.setFileName("eTowerAPI");
		
                trackandTrace.setTrackEventDateOccured(trackingDetails.getEventTime());
				trackandTrace.setTrackEventCode(trackingDetails.getEventCode());
			
				trackandTrace.setTrackEventDetails(trackingDetails.getActivity());
				trackandTrace.setCourierEvents(trackingDetails.getActivity());
				trackandTrace.setTimestamp(Timestamp.valueOf(LocalDateTime.now()).toString());
				trackandTrace.setLocation(trackingDetails.getLocation());
				trackandTrace.setIsDeleted("N");
				if("ARRIVED AT DESTINATION AIRPORT".equalsIgnoreCase(trackandTrace.getTrackEventDetails()) ||
						("COLLECTED FROM AIRPORT TERMINAL".equalsIgnoreCase(trackandTrace.getTrackEventDetails())) ||
							("PREPARING TO DISPATCH".equalsIgnoreCase(trackandTrace.getTrackEventDetails())))
					{
					trackandTrace.setIsDeleted("Y");
					}
				trackAndTraceList.add(trackandTrace);
			}
			
		}
		}
		trackAndTraceRepository.saveAll(trackAndTraceList);
		trackAndTraceRepository.updateTracking();
		trackAndTraceRepository.deleteDuplicates();
		responseMsg.setResponseMessage("Data uploaded successfully from ETower");
		}
		return responseMsg;
	}
	public List<SenderdataMaster> fetchConsignmentsManifestShippment(List<String> incomingRefNbr){
		return senderDataRepository.fetchConsignmentsManifestShippment(incomingRefNbr);
	}

}
