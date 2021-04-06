package com.d2z.d2zservice.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.d2z.d2zservice.dao.ID2ZDao;
import com.d2z.d2zservice.dao.ID2ZSuperUserDao;
import com.d2z.d2zservice.entity.NonD2ZData;
import com.d2z.d2zservice.entity.PostcodeZone;
import com.d2z.d2zservice.entity.Returns;
import com.d2z.d2zservice.exception.InvalidServiceTypeException;
import com.d2z.d2zservice.exception.InvalidSuburbPostcodeException;
import com.d2z.d2zservice.exception.ReferenceNumberNotUniqueException;
import com.d2z.d2zservice.model.CreateConsignmentRequest;
import com.d2z.d2zservice.model.PFLSenderDataFileRequest;
import com.d2z.d2zservice.model.PFLSenderDataRequest;
import com.d2z.d2zservice.model.ReconcileData;
import com.d2z.d2zservice.model.SenderData;
import com.d2z.d2zservice.model.SenderDataApi;
import com.d2z.d2zservice.util.ValidationUtils;
import com.d2z.d2zservice.model.ErrorDetails;
import com.d2z.d2zservice.model.HeldParcel;
import com.d2z.singleton.D2ZSingleton;

@Service
public class D2ZValidator {

	@Autowired
    private ID2ZDao d2zDao;
	
	@Autowired
    private ID2ZSuperUserDao d2zSuperUserDao;

	public void isPostCodeValid(List<SenderDataApi> senderData) {
		List<String> postCodeZoneList = D2ZSingleton.getInstance().getPostCodeZoneList();
		List<String> postCodeStateNameList = D2ZSingleton.getInstance().getPostCodeStateNameList();
		List<String> incorrectPostcode_Suburb = new ArrayList<String>();
		senderData.forEach(obj -> {
			String state = obj.getConsigneeState().trim().toUpperCase();
			String suburb = obj.getConsigneeSuburb().trim().toUpperCase();
			String postcode = obj.getConsigneePostcode().trim();
			
			String combination = state.concat(suburb).concat(postcode);
			if(!postCodeZoneList.contains(combination) && !postCodeStateNameList.contains(combination)) {
				incorrectPostcode_Suburb.add(obj.getReferenceNumber()+"-"+obj.getConsigneeState().trim().toUpperCase()+"-"+obj.getConsigneeSuburb().trim().toUpperCase()+"-"+obj.getConsigneePostcode().trim());
				
			}
		});
		if(!incorrectPostcode_Suburb.isEmpty()) {
			throw new InvalidSuburbPostcodeException("Invalid Combination of Consignee State, Postcode and Suburb",incorrectPostcode_Suburb);
		}
		
	}
	public void isPostCodeZone4Valid(List<SenderDataApi> senderData) {
		List<String> postCodeZoneList = D2ZSingleton.getInstance().getMasterPostCodeZone4List();
		List<String> incorrectPostcode_Suburb = new ArrayList<String>();
		senderData.forEach(obj -> {
			String state = obj.getConsigneeState().trim().toUpperCase();
			String suburb = obj.getConsigneeSuburb().trim().toUpperCase();
			String postcode = obj.getConsigneePostcode().trim();
			
			String combination = state.concat(suburb).concat(postcode);
			if(!postCodeZoneList.contains(combination)) {
				incorrectPostcode_Suburb.add(obj.getReferenceNumber()+"-"+obj.getConsigneeState().trim().toUpperCase()+"-"+obj.getConsigneeSuburb().trim().toUpperCase()+"-"+obj.getConsigneePostcode().trim());
				
			}
		});
		if(!incorrectPostcode_Suburb.isEmpty()) {
			throw new InvalidSuburbPostcodeException("Invalid Combination of Consignee State, Postcode and Suburb",incorrectPostcode_Suburb);
		}
		
	}
	public void isPostCodeZone4Valid(List<SenderDataApi> senderData,
			Map<String, List<ErrorDetails>> errorMap) {
		List<String> postCodeStateNameList = D2ZSingleton.getInstance().getMasterPostCodeZone4List();

		senderData.forEach(obj -> {
			if(null!=obj.getConsigneeState() && null!=obj.getConsigneeSuburb() && null!=obj.getConsigneePostcode()
					&& !obj.getConsigneeState().isEmpty() && !obj.getConsigneeSuburb().isEmpty() && !obj.getConsigneePostcode().isEmpty()) {

			String state = obj.getConsigneeState().trim().toUpperCase();
			String suburb = obj.getConsigneeSuburb().trim().toUpperCase();
			String postcode = obj.getConsigneePostcode().trim();
			String combination = state.concat(suburb).concat(postcode);
			if(!postCodeStateNameList.contains(combination)) {
		   				 ValidationUtils.populateErrorDetails(obj.getReferenceNumber(),obj.getConsigneeState().trim().toUpperCase()+"-"+obj.getConsigneeSuburb().trim().toUpperCase()+"-"+obj.getConsigneePostcode().trim(),
						 "Invalid combination of Consignee State or Postcode or Suburb",errorMap) ;
		
			}
			}
		});
		
	
		
	}
	public void isPostCodeValidUI(List<SenderData> senderData) {
		List<String> postCodeZoneList = D2ZSingleton.getInstance().getPostCodeZoneList();
		List<String> postCodeStateNameList = D2ZSingleton.getInstance().getPostCodeStateNameList();
		List<String> incorrectPostcode_Suburb = new ArrayList<String>();
		senderData.forEach(obj -> {
			String state = obj.getConsigneeState().trim().toUpperCase();
			String suburb = obj.getConsigneeSuburb().trim().toUpperCase();
			String postcode = obj.getConsigneePostcode().trim();
			String combination = state.concat(suburb).concat(postcode);
			if(!postCodeZoneList.contains(combination) && !postCodeStateNameList.contains(combination)) {
				incorrectPostcode_Suburb.add(obj.getReferenceNumber()+"-"+obj.getConsigneeState().trim().toUpperCase()+"-"+obj.getConsigneeSuburb().trim().toUpperCase()+"-"+obj.getConsigneePostcode().trim());
			}
		});
		if(!incorrectPostcode_Suburb.isEmpty()) {
			throw new InvalidSuburbPostcodeException("Invalid Combination of Consignee State, Postcode and Suburb",incorrectPostcode_Suburb);
		}
	}
	
	public void isReferenceNumberUnique(List<String> incomingRefNbr) throws ReferenceNumberNotUniqueException{
		List<String> referenceNumber_DB = d2zDao.fetchAllReferenceNumbers();
		referenceNumber_DB.addAll(incomingRefNbr);
		List<String> duplicateRefNbr = referenceNumber_DB.stream().collect(Collectors.groupingBy(Function.identity(),     
	              Collectors.counting()))                                             
	          .entrySet().stream()
	          .filter(e -> e.getValue() > 1)                                      
	          .map(e -> e.getKey())                                                  
	          .collect(Collectors.toList());
		
		if(!duplicateRefNbr.isEmpty()) {
			throw new ReferenceNumberNotUniqueException("Reference Number must be unique",duplicateRefNbr);
		}
	}
	
	public void isReferenceNumberUniqueUI(List<String> incomingRefNbr) throws ReferenceNumberNotUniqueException{
		List<String> referenceNumber_DB = d2zDao.fetchAllReferenceNumbers();
		referenceNumber_DB.addAll(incomingRefNbr);
		List<String> duplicateRefNbr = referenceNumber_DB.stream().collect(Collectors.groupingBy(Function.identity(),     
			           Collectors.counting()))                                             
			          .entrySet().stream()
			          .filter(e -> e.getValue() > 1)                                      
			          .map(e -> e.getKey())                                                  
			          .collect(Collectors.toList());
				if(!duplicateRefNbr.isEmpty()) {
					throw new ReferenceNumberNotUniqueException("Reference Number must be unique",duplicateRefNbr);
				}
	}
	
	public void isArticleIdUniqueUI(List<NonD2ZData> nonD2zData) throws ReferenceNumberNotUniqueException{
		List<String> articleNumber_DB = d2zSuperUserDao.fetchAllArticleId();
		List<String> articleNbr = nonD2zData.stream().map(obj -> {
			return obj.getArticleId(); })
				.collect(Collectors.toList());
		articleNumber_DB.addAll(articleNbr);
		List<String> duplicateArticleNbr = articleNumber_DB.stream().collect(Collectors.groupingBy(Function.identity(),     
	              Collectors.counting()))                                             
	          .entrySet().stream()
	          .filter(e -> e.getValue() > 1)                                      
	          .map(e -> e.getKey())                                                  
	          .collect(Collectors.toList());
		
		if(!duplicateArticleNbr.isEmpty()) {
			throw new ReferenceNumberNotUniqueException("Article Number must be unique",duplicateArticleNbr);
		}
	}

	public void isServiceValid(CreateConsignmentRequest orderDetail) {
		List<String> incorrectRefNbr = new ArrayList<String>();
		List<String> serviceType_DB = d2zDao.fetchServiceTypeByUserName(orderDetail.getUserName().trim());
		
		List<SenderDataApi> orderDetailList = orderDetail.getConsignmentData();
		for(SenderDataApi senderData : orderDetailList) {
			if(!serviceType_DB.contains(senderData.getServiceType().trim())) {
				incorrectRefNbr.add(senderData.getReferenceNumber()+"-"+senderData.getServiceType());
			}
		} 
		if(!incorrectRefNbr.isEmpty()) {
			throw new InvalidServiceTypeException("Invalid Service Type",incorrectRefNbr);
		}
	}
	
	public void isServiceValidUI(List<SenderData> orderDetail) {
		List<String> incorrectRefNbr = new ArrayList<String>();
		List<String> serviceType_DB = d2zDao.fetchServiceTypeByUserName(orderDetail.get(0).getUserName());
		for(SenderData senderData : orderDetail) {
			if(!serviceType_DB.contains(senderData.getServiceType())) {
				incorrectRefNbr.add(senderData.getReferenceNumber()+"-"+senderData.getServiceType());
			}
		} 
		if(!incorrectRefNbr.isEmpty()) {
			throw new InvalidServiceTypeException("Invalid Service Type",incorrectRefNbr);
		}
	}
	
	public void isParcelValid(List<HeldParcel> createJob) throws ReferenceNumberNotUniqueException {
		List<String> duplicateParcelDetails = new ArrayList<String>();
		List<String> parcelDetails = d2zSuperUserDao.fetchParcelDetails();
		createJob.forEach(obj -> {
			String hawb = obj.getHawb().trim().toUpperCase();
			String status = obj.getStat().trim().toUpperCase();
			String combination = hawb.concat(status);
			if(parcelDetails.contains(combination)) {
				duplicateParcelDetails.add(obj.getHawb());
			}
		});
		if(!duplicateParcelDetails.isEmpty()) {
			throw new ReferenceNumberNotUniqueException("Parcel Entry should be unique",duplicateParcelDetails);
		}
	}

	public void isReferenceNumberUniqueReconcile(List<ReconcileData> reconcileData, String client) throws ReferenceNumberNotUniqueException{
		List<String> referenceNumber_DB = null;
		if(client.equalsIgnoreCase("D2Z")) {
			referenceNumber_DB = d2zSuperUserDao.fetchAllReconcileReferenceNumbers();
		}else {
			referenceNumber_DB = d2zSuperUserDao.fetchAllReconcileNonD2zReferenceNumbers();
		}
		List<String> incomingRefNbr = reconcileData.stream().map(obj -> {
			return obj.getRefrenceNumber(); })
				.collect(Collectors.toList());
		referenceNumber_DB.addAll(incomingRefNbr);
		List<String> duplicateRefNbr = referenceNumber_DB.stream().collect(Collectors.groupingBy(Function.identity(),     
	              Collectors.counting()))                                             
	          .entrySet().stream()
	          .filter(e -> e.getValue() > 1)                                      
	          .map(e -> e.getKey())                                                  
	          .collect(Collectors.toList());
		
		if(!duplicateRefNbr.isEmpty()) {
			throw new ReferenceNumberNotUniqueException("Reference Number must be unique",duplicateRefNbr);
		}
	}

	public void isArticleIdUniqueReconcile(List<ReconcileData> reconcileData, String client) throws ReferenceNumberNotUniqueException {
		List<String> articleId_DB = null;
		if(client.equalsIgnoreCase("D2Z")) {
			articleId_DB = d2zSuperUserDao.fetchAllReconcileArticleIdNumbers();
		}else {
			articleId_DB = d2zSuperUserDao.fetchAllReconcileNonD2zArticleIdNumbers();
		}
		List<String> incomingRefNbr = reconcileData.stream().map(obj -> {
			return obj.getArticleNo(); })
				.collect(Collectors.toList());
		articleId_DB.addAll(incomingRefNbr);
		List<String> duplicateRefNbr = articleId_DB.stream().collect(Collectors.groupingBy(Function.identity(),     
	              Collectors.counting()))                                             
	          .entrySet().stream()
	          .filter(e -> e.getValue() > 1)                                      
	          .map(e -> e.getKey())                                                  
	          .collect(Collectors.toList());
		
		if(!duplicateRefNbr.isEmpty()) {
			throw new ReferenceNumberNotUniqueException("Article Id must be unique",duplicateRefNbr);
		}
	}

	public void isFWPostCodeValid(List<SenderDataApi> consignmentData) {
		List<String> postCodeFWZoneList = D2ZSingleton.getInstance().getFWPostCodeZoneList();
		List<String> postCodeFWStateNameList = D2ZSingleton.getInstance().getFWPostCodeStateNameList();
		List<String> incorrectFWPostcode_Suburb = new ArrayList<String>();
		consignmentData.forEach(obj -> {
			String state = obj.getConsigneeState().trim().toUpperCase();
			String suburb = obj.getConsigneeSuburb().trim().toUpperCase();
			String postcode = obj.getConsigneePostcode().trim();
			String combination = state.concat(suburb).concat(postcode);
			if(!postCodeFWZoneList.contains(combination) && !postCodeFWStateNameList.contains(combination)) {
				incorrectFWPostcode_Suburb.add(obj.getReferenceNumber()+"-"+obj.getConsigneeState().trim().toUpperCase()+"-"+obj.getConsigneeSuburb().trim().toUpperCase()+"-"+obj.getConsigneePostcode().trim());
			}
		});
		if(!incorrectFWPostcode_Suburb.isEmpty()) {
			throw new InvalidSuburbPostcodeException("Suburb is not in carrier serviced areas",incorrectFWPostcode_Suburb);
		}
	}
	
	public void isFWPostCodeUIValid(List<SenderData> consignmentData) {
		List<String> postCodeFWZoneList = D2ZSingleton.getInstance().getFWPostCodeZoneList();
		List<String> postCodeFWStateNameList = D2ZSingleton.getInstance().getFWPostCodeStateNameList();
		List<String> incorrectFWPostcode_Suburb = new ArrayList<String>();
		consignmentData.forEach(obj -> {
			String state = obj.getConsigneeState().trim().toUpperCase();
			String suburb = obj.getConsigneeSuburb().trim().toUpperCase();
			String postcode = obj.getConsigneePostcode().trim();
			String combination = state.concat(suburb).concat(postcode);
			if(!postCodeFWZoneList.contains(combination) && !postCodeFWStateNameList.contains(combination)) {
			
				incorrectFWPostcode_Suburb.add(obj.getReferenceNumber()+"-"+obj.getConsigneeState().trim().toUpperCase()+"-"+obj.getConsigneeSuburb().trim().toUpperCase()+"-"+obj.getConsigneePostcode().trim());
			}
		});
		if(!incorrectFWPostcode_Suburb.isEmpty()) {
			throw new InvalidSuburbPostcodeException("Suburb is not in carrier serviced areas",incorrectFWPostcode_Suburb);
		}
	}

	public PFLSenderDataRequest isFWSubPostCodeValid(CreateConsignmentRequest consignmentData) {
		List<SenderDataApi> pflSenderData = new ArrayList<SenderDataApi>();
		List<SenderDataApi> nonPflSenderData = new ArrayList<SenderDataApi>();
		PFLSenderDataRequest pflRequest = new PFLSenderDataRequest();
		List<String> postCodeFWSubList = D2ZSingleton.getInstance().getFWPostCodeZoneList();
		consignmentData.getConsignmentData().forEach(obj -> {
			if(postCodeFWSubList.contains(obj.getConsigneeState().trim().toUpperCase().concat(obj.getConsigneeSuburb().trim().toUpperCase()).concat(obj.getConsigneePostcode().trim()))) {
				pflSenderData.add(obj);
			}else {
				nonPflSenderData.add(obj);
			}
		});
		pflRequest.setPflSenderDataApi(pflSenderData);
		pflRequest.setNonPflSenderDataApi(nonPflSenderData);
		return pflRequest;
	}
	
	public PFLSenderDataFileRequest isFWSubPostCodeUIValid(List<SenderData> consignmentData) {
		List<SenderData> pflSenderData = new ArrayList<SenderData>();
		List<SenderData> nonPflSenderData = new ArrayList<SenderData>();
		PFLSenderDataFileRequest pflRequest = new PFLSenderDataFileRequest();
		List<String> postCodeFWSubList = D2ZSingleton.getInstance().getFWPostCodeZoneList();
		consignmentData.forEach(obj -> {
			if(postCodeFWSubList.contains(obj.getConsigneeState().trim().toUpperCase().concat(obj.getConsigneeSuburb().trim().toUpperCase()).concat(obj.getConsigneePostcode().trim()))) {
				pflSenderData.add(obj);
			}else {
				nonPflSenderData.add(obj);
			}
		});
		pflRequest.setPflSenderDataApi(pflSenderData);
		pflRequest.setNonPflSenderDataApi(nonPflSenderData);
		return pflRequest;
	}


	public void isReferenceNumberUnique(CreateConsignmentRequest orderDetail,
			Map<String, List<ErrorDetails>> errorMap) {
		List<String> incomingRefNbr = orderDetail.getConsignmentData().stream().map(obj -> {
			return obj.getReferenceNumber(); })
				.collect(Collectors.toList());
		List<String> referenceNumber_DB = d2zDao.fetchAllReferenceNumbers();
		referenceNumber_DB.addAll(incomingRefNbr);

		List<String> duplicateRefNbr = referenceNumber_DB.stream().collect(Collectors.groupingBy(Function.identity(),     
	              Collectors.counting()))                                             
	          .entrySet().stream()
	          .filter(e -> e.getValue() > 1)                                      
	          .map(e -> e.getKey())                                                  
	          .collect(Collectors.toList());
		duplicateRefNbr.forEach(obj -> {
			
				 ValidationUtils.populateErrorDetails(obj,obj,
						 "Reference Number must be unique",errorMap) ;
		
			});  
	}

	public void isServiceValid(CreateConsignmentRequest orderDetail,
			Map<String, List<ErrorDetails>> errorMap) {
		
		List<String> serviceType_DB = d2zDao.fetchServiceTypeByUserName(orderDetail.getUserName().trim());
		
		List<SenderDataApi> senderData = orderDetail.getConsignmentData();
		for(SenderDataApi data : senderData) {
			if(null!=data.getServiceType()) {
			if(!serviceType_DB.contains(data.getServiceType().trim())) {
				ValidationUtils.populateErrorDetails(data.getReferenceNumber(),data.getServiceType().trim(),
						 "Invalid Service Type",errorMap) ;
			}
		} 
		}
			
	}

	public void isPostCodeValid(List<SenderDataApi> senderData,
			Map<String, List<ErrorDetails>> errorMap) {
		List<String> postCodeStateNameList = D2ZSingleton.getInstance().getPostCodeStateNameList();
		List<String> postCodeZoneList = D2ZSingleton.getInstance().getPostCodeZoneList();

		senderData.forEach(obj -> {
			if(null!=obj.getConsigneeState() && null!=obj.getConsigneeSuburb() && null!=obj.getConsigneePostcode()
					&& !obj.getConsigneeState().isEmpty() && !obj.getConsigneeSuburb().isEmpty() && !obj.getConsigneePostcode().isEmpty()) {

			String state = obj.getConsigneeState().trim().toUpperCase();
			String suburb = obj.getConsigneeSuburb().trim().toUpperCase();
			String postcode = obj.getConsigneePostcode().trim();
			String combination = state.concat(suburb).concat(postcode);
			if(!postCodeZoneList.contains(combination) && !postCodeStateNameList.contains(combination)) {
		   				 ValidationUtils.populateErrorDetails(obj.getReferenceNumber(),obj.getConsigneeState().trim().toUpperCase()+"-"+obj.getConsigneeSuburb().trim().toUpperCase()+"-"+obj.getConsigneePostcode().trim(),
						 "Invalid combination of Consignee State or Postcode or Suburb",errorMap) ;
		
			}
			}
		});
		
	
		
	}

	public void isFWPostCodeValid(List<SenderDataApi> consignmentData, Map<String, List<ErrorDetails>> errorMap) {

		List<String> postCodeFWZoneList = D2ZSingleton.getInstance().getFWPostCodeZoneList();
		List<String> postCodeFWStateNameList = D2ZSingleton.getInstance().getFWPostCodeStateNameList();

		consignmentData.forEach(obj -> {
			if(null!=obj.getConsigneeState() && null!=obj.getConsigneeSuburb() && null!=obj.getConsigneePostcode()
					&& !obj.getConsigneeState().isEmpty() && !obj.getConsigneeSuburb().isEmpty() && !obj.getConsigneePostcode().isEmpty()) {
				String state = obj.getConsigneeState().trim().toUpperCase();
				String suburb = obj.getConsigneeSuburb().trim().toUpperCase();
				String postcode = obj.getConsigneePostcode().trim();
				String combination = state.concat(suburb).concat(postcode);
				if(!postCodeFWZoneList.contains(combination) && !postCodeFWStateNameList.contains(combination)) {
			   		 ValidationUtils.populateErrorDetails(obj.getReferenceNumber(),obj.getConsigneeState().trim().toUpperCase()+"-"+obj.getConsigneeSuburb().trim().toUpperCase()+"-"+obj.getConsigneePostcode().trim(),
						 "Suburb is not in carrier serviced areas",errorMap) ;
			}
			}
		});
	
	}

	public void isAddressValidUI(List<SenderData> senderData) throws ReferenceNumberNotUniqueException {
		List<String> invalidAddress = new ArrayList<String>();
		senderData.forEach(obj -> {
			if( (obj.getServiceType().startsWith("FW") || obj.getServiceType().startsWith("MC"))
					&& (obj.getConsigneeAddr1().toUpperCase().contains("PO BOX") || obj.getConsigneeAddr1().toUpperCase().contains("POBOX") 
							|| obj.getConsigneeAddr1().toUpperCase().contains("PARCEL COLLECT") || obj.getConsigneeAddr1().toUpperCase().contains("PARCEL LOCKER"))) {
				invalidAddress.add(obj.getReferenceNumber()+"-"+obj.getConsigneeAddr1());
			}
			if( (obj.getServiceType().startsWith("FW") || obj.getServiceType().startsWith("MC"))
					&& null != obj.getConsigneeAddr2() 
					&& (obj.getConsigneeAddr2().toUpperCase().contains("PO BOX") || obj.getConsigneeAddr1().toUpperCase().contains("POBOX") 
							|| obj.getConsigneeAddr2().toUpperCase().contains("PARCEL COLLECT") || obj.getConsigneeAddr1().toUpperCase().contains("PARCEL LOCKER"))) {
				invalidAddress.add(obj.getReferenceNumber()+"-"+obj.getConsigneeAddr2());
			}
			
		});
		if(!invalidAddress.isEmpty()) {
			throw new ReferenceNumberNotUniqueException("PO Box and Parcel collect not accepted on this service",invalidAddress);
		}
		
	}

	public void isSTPostCodeValid(List<SenderData> orderDetailList) {
		List<String> postCodeSTZoneList = D2ZSingleton.getInstance().getSTPostCodeZoneList();
		List<String> incorrectSTPostcode_Suburb = new ArrayList<String>();
		orderDetailList.forEach(obj -> {
			String state = obj.getConsigneeState().trim().toUpperCase();
			String suburb = obj.getConsigneeSuburb().trim().toUpperCase();
			String postcode = obj.getConsigneePostcode().trim();
			String combination = state.concat(suburb).concat(postcode);
			if(!postCodeSTZoneList.contains(combination)) {
				incorrectSTPostcode_Suburb.add(obj.getReferenceNumber()+"-"+obj.getConsigneeState().trim().toUpperCase()+"-"+obj.getConsigneeSuburb().trim().toUpperCase()+"-"+obj.getConsigneePostcode().trim());
			}
		});
		if(!incorrectSTPostcode_Suburb.isEmpty()) {
			throw new InvalidSuburbPostcodeException("Suburb is not in carrier serviced areas",incorrectSTPostcode_Suburb);
		}
	}

	public void isSTPostCodeValidAPI(List<SenderDataApi> orderDetailList) {
		List<String> postCodeSTZoneList = D2ZSingleton.getInstance().getSTPostCodeZoneList();
		List<String> incorrectSTPostcode_Suburb = new ArrayList<String>();
		orderDetailList.forEach(obj -> {
			String state = obj.getConsigneeState().trim().toUpperCase();
			String suburb = obj.getConsigneeSuburb().trim().toUpperCase();
			String postcode = obj.getConsigneePostcode().trim();
			String combination = state.concat(suburb).concat(postcode);
			if(!postCodeSTZoneList.contains(combination)) {
				incorrectSTPostcode_Suburb.add(obj.getReferenceNumber()+"-"+obj.getConsigneeState().trim().toUpperCase()+"-"+obj.getConsigneeSuburb().trim().toUpperCase()+"-"+obj.getConsigneePostcode().trim());
			}
		});
		if(!incorrectSTPostcode_Suburb.isEmpty()) {
			throw new InvalidSuburbPostcodeException("Suburb is not in carrier serviced areas",incorrectSTPostcode_Suburb);
		}
	}
	
	public void isEnquiryReferenceNumberUnique(List<Returns> returns) throws ReferenceNumberNotUniqueException{
	    List<Returns> returnObj = d2zSuperUserDao.fetchAllReferenceNumber();
	    
//    	List<String> uniqueReturns = returnObj.stream().map(daoObj -> {
//    		return (daoObj.getReferenceNumber()).concat(daoObj.getArticleId()).concat(daoObj.getBarcodelabelNumber());
//    	}).collect(Collectors.toList());
	    
    	List<String> uniqueReturnsRefNum = returnObj.stream().filter(daoObj -> daoObj.getReferenceNumber() != null).map(daoObj -> {
    			return (daoObj.getReferenceNumber().trim().toUpperCase());
    	}).collect(Collectors.toList());
    		
    	List<String> uniqueReturnsArticleId = returnObj.stream().filter(daoObj -> daoObj.getArticleId() != null).map(daoObj -> {
    			return (daoObj.getArticleId().trim().toUpperCase());
    	}).collect(Collectors.toList());
    		
		List<String> uniqueReturnsBarcode = returnObj.stream().filter(daoObj -> daoObj.getBarcodelabelNumber() != null).map(daoObj -> {
			return (daoObj.getBarcodelabelNumber().trim().toUpperCase());
		}).collect(Collectors.toList());
		
		List<String> uniqueValue = new ArrayList<String>();
		
		
		returns.forEach(obj -> {
			System.out.println(obj.getScanType());
			if(obj.getScanType().equalsIgnoreCase("Reference Number")) {
				String referenceNum = obj.getReferenceNumber() != null ? obj.getReferenceNumber().trim().toUpperCase() : obj.getReferenceNumber();
				if(null!=referenceNum && uniqueReturnsRefNum.contains(referenceNum)) {
					uniqueValue.add(obj.getReferenceNumber());
				}	
			}else if(obj.getScanType().equalsIgnoreCase("Article Id")) {
				String articleId = obj.getArticleId() != null ? obj.getArticleId().trim().toUpperCase() : obj.getArticleId();
				if(null!=articleId && uniqueReturnsArticleId.contains(articleId)) {
					uniqueValue.add(obj.getArticleId());
				}
			}else if(obj.getScanType().equalsIgnoreCase("Barcode Label")) {
				String barcodeLabel = obj.getBarcodelabelNumber() != null ? obj.getBarcodelabelNumber().trim().toUpperCase() : obj.getBarcodelabelNumber();
				if(null!=barcodeLabel && uniqueReturnsBarcode.contains(barcodeLabel)) {
					uniqueValue.add(obj.getBarcodelabelNumber().trim().toUpperCase());
			}
			}
		});
		
		if(!uniqueValue.isEmpty()) {
			throw new ReferenceNumberNotUniqueException("Return details already exist",uniqueValue);
		}
		
	}

	public void isSTPostCodeValidAPI(List<SenderDataApi> consignmentData, Map<String, List<ErrorDetails>> errorMap) {

		List<String> postCodeFWZoneList = D2ZSingleton.getInstance().getSTPostCodeZoneList();
		//List<String> postCodeFWStateNameList = D2ZSingleton.getInstance().getFWPostCodeStateNameList();

		consignmentData.forEach(obj -> {
			if(null!=obj.getConsigneeState() && null!=obj.getConsigneeSuburb() && null!=obj.getConsigneePostcode()
					&& !obj.getConsigneeState().isEmpty() && !obj.getConsigneeSuburb().isEmpty() && !obj.getConsigneePostcode().isEmpty()) {
				String state = obj.getConsigneeState().trim().toUpperCase();
				String suburb = obj.getConsigneeSuburb().trim().toUpperCase();
				String postcode = obj.getConsigneePostcode().trim();
				String combination = state.concat(suburb).concat(postcode);
				if(!postCodeFWZoneList.contains(combination)){ //&& !postCodeFWStateNameList.contains(combination)) {
			   		 ValidationUtils.populateErrorDetails(obj.getReferenceNumber(),obj.getConsigneeState().trim().toUpperCase()+"-"+obj.getConsigneeSuburb().trim().toUpperCase()+"-"+obj.getConsigneePostcode().trim(),
						 "Suburb is not in carrier serviced areas",errorMap) ;
			}
			}
		});
			
	}

	public void isNZPostCodeValid(List<SenderDataApi> senderData) {

		List<String> postCodeZoneList = D2ZSingleton.getInstance().getNZPostCodeZoneList();
		List<String> incorrectPostcode_Suburb = new ArrayList<String>();
		senderData.forEach(obj -> {
			String state = obj.getConsigneeState().trim().toUpperCase();
			String suburb = obj.getConsigneeSuburb().trim().toUpperCase();
			String postcode = obj.getConsigneePostcode().trim();
			
			String combination = state.concat(suburb.concat(postcode));
			System.out.println(postCodeZoneList);
			if(!postCodeZoneList.contains(combination)) {
				incorrectPostcode_Suburb.add(obj.getReferenceNumber()+"-"+obj.getConsigneeState().trim().toUpperCase()+"-"+obj.getConsigneeSuburb().trim().toUpperCase()+"-"+obj.getConsigneePostcode().trim());
				
			}
		});
		if(!incorrectPostcode_Suburb.isEmpty()) {
			throw new InvalidSuburbPostcodeException("Invalid Combination of Consignee Postcode and Suburb",incorrectPostcode_Suburb);
		}
		
			
	}

	public void isNZPostCodeValidUI(List<SenderData> senderData) {

		List<String> postCodeZoneList = D2ZSingleton.getInstance().getNZPostCodeZoneList();
		List<String> incorrectPostcode_Suburb = new ArrayList<String>();
		senderData.forEach(obj -> {
			String state = obj.getConsigneeState().trim().toUpperCase();
			String suburb = obj.getConsigneeSuburb().trim().toUpperCase();
			String postcode = obj.getConsigneePostcode().trim();
			String combination = state.concat(suburb.concat(postcode));
			if(!postCodeZoneList.contains(combination) ) {
				incorrectPostcode_Suburb.add(obj.getReferenceNumber()+"-"+obj.getConsigneeState().trim().toUpperCase()+"-"+obj.getConsigneeSuburb().trim().toUpperCase()+"-"+obj.getConsigneePostcode().trim());
			}
		});
		if(!incorrectPostcode_Suburb.isEmpty()) {
			throw new InvalidSuburbPostcodeException("Invalid Combination of Consignee State, Postcode and Suburb",incorrectPostcode_Suburb);
		}
			
	}

	public void isNZPostCodeValid(CreateConsignmentRequest orderDetail, Map<String, List<ErrorDetails>> errorMap) {

		List<SenderDataApi> senderData = orderDetail.getConsignmentData();
		List<String> postCodeZoneList = D2ZSingleton.getInstance().getNZPostCodeZoneList();

		senderData.forEach(obj -> {
			if(null!=obj.getConsigneeSuburb() && null!=obj.getConsigneePostcode()
					 && !obj.getConsigneeSuburb().isEmpty() && !obj.getConsigneePostcode().isEmpty()) {
				
			String state = obj.getConsigneeState().trim().toUpperCase();
			String suburb = obj.getConsigneeSuburb().trim().toUpperCase();
			String postcode = obj.getConsigneePostcode().trim();
			String combination = state.concat(suburb.concat(postcode));
			if(!postCodeZoneList.contains(combination)) {
		   				 ValidationUtils.populateErrorDetails(obj.getReferenceNumber(),obj.getConsigneeState().trim().toUpperCase()+"-"+obj.getConsigneeSuburb().trim().toUpperCase()+"-"+obj.getConsigneePostcode().trim(),
						 "Invalid combination of Consignee State, Postcode and Suburb",errorMap) ;
		
			}
			}
		});		
	}

	public void isPFLPostCodeValid(List<SenderDataApi> consignmentData) {

		List<String> postCodeZoneList = D2ZSingleton.getInstance().getPFLPostCodeZoneList();
		List<String> incorrectPostcode_Suburb = new ArrayList<String>();
		consignmentData.forEach(obj -> {
			String state = obj.getConsigneeState().trim().toUpperCase();
			String suburb = obj.getConsigneeSuburb().trim().toUpperCase();
			String postcode = obj.getConsigneePostcode().trim();
			
			String combination = state.concat(suburb.concat(postcode));
			if(!postCodeZoneList.contains(combination)) {
				incorrectPostcode_Suburb.add(obj.getReferenceNumber()+"-"+obj.getConsigneeState().trim().toUpperCase()+"-"+obj.getConsigneeSuburb().trim().toUpperCase()+"-"+obj.getConsigneePostcode().trim());
				
			}
		});
		if(!incorrectPostcode_Suburb.isEmpty()) {
			throw new InvalidSuburbPostcodeException("Invalid Combination of Consignee Postcode and Suburb",incorrectPostcode_Suburb);
		}
		
			
	}

	public void isPFLPostCodeValidUI(List<SenderData> orderDetailList) {


		List<String> postCodeZoneList = D2ZSingleton.getInstance().getPFLPostCodeZoneList();
		List<String> incorrectPostcode_Suburb = new ArrayList<String>();
		orderDetailList.forEach(obj -> {
			String state = obj.getConsigneeState().trim().toUpperCase();
			String suburb = obj.getConsigneeSuburb().trim().toUpperCase();
			String postcode = obj.getConsigneePostcode().trim();
			
			String combination = state.concat(suburb.concat(postcode));
			if(!postCodeZoneList.contains(combination)) {
				incorrectPostcode_Suburb.add(obj.getReferenceNumber()+"-"+obj.getConsigneeState().trim().toUpperCase()+"-"+obj.getConsigneeSuburb().trim().toUpperCase()+"-"+obj.getConsigneePostcode().trim());
				
			}
		});
		if(!incorrectPostcode_Suburb.isEmpty()) {
			throw new InvalidSuburbPostcodeException("Invalid Combination of Consignee Postcode and Suburb",incorrectPostcode_Suburb);
		}
		
			
	
		
	}

	public void isPFLPostCodeValid(CreateConsignmentRequest orderDetail, Map<String, List<ErrorDetails>> errorMap) {


		List<SenderDataApi> senderData = orderDetail.getConsignmentData();
		List<String> postCodeZoneList = D2ZSingleton.getInstance().getPFLPostCodeZoneList();

		senderData.forEach(obj -> {
			if(null!=obj.getConsigneeSuburb() && null!=obj.getConsigneePostcode()
					 && !obj.getConsigneeSuburb().isEmpty() && !obj.getConsigneePostcode().isEmpty()) {
				
			String state = obj.getConsigneeState().trim().toUpperCase();
			String suburb = obj.getConsigneeSuburb().trim().toUpperCase();
			String postcode = obj.getConsigneePostcode().trim();
			String combination = state.concat(suburb.concat(postcode));
			if(!postCodeZoneList.contains(combination)) {
		   				 ValidationUtils.populateErrorDetails(obj.getReferenceNumber(),obj.getConsigneeState().trim().toUpperCase()+"-"+obj.getConsigneeSuburb().trim().toUpperCase()+"-"+obj.getConsigneePostcode().trim(),
						 "Invalid combination of Consignee State, Postcode and Suburb",errorMap) ;
		
			}
			}
		});		
			
	}
	public void isPostCodeZone4ValidUI(List<SenderData> senderData) {

		List<String> postCodeZoneList = D2ZSingleton.getInstance().getMasterPostCodeZone4List();
		List<String> incorrectPostcode_Suburb = new ArrayList<String>();
		senderData.forEach(obj -> {
			String state = obj.getConsigneeState().trim().toUpperCase();
			String suburb = obj.getConsigneeSuburb().trim().toUpperCase();
			String postcode = obj.getConsigneePostcode().trim();
			
			String combination = state.concat(suburb).concat(postcode);
			if(!postCodeZoneList.contains(combination)) {
				incorrectPostcode_Suburb.add(obj.getReferenceNumber()+"-"+obj.getConsigneeState().trim().toUpperCase()+"-"+obj.getConsigneeSuburb().trim().toUpperCase()+"-"+obj.getConsigneePostcode().trim());
				
			}
		});
		if(!incorrectPostcode_Suburb.isEmpty()) {
			throw new InvalidSuburbPostcodeException("Invalid Combination of Consignee State, Postcode and Suburb",incorrectPostcode_Suburb);
		}
		
			
	}
	public void isTollPostCodeValid(List<SenderDataApi> senderData) {

		List<String> postCodeZoneList = D2ZSingleton.getInstance().getMasterTollPostCodeList();
		List<String> incorrectPostcode_Suburb = new ArrayList<String>();
		senderData.forEach(obj -> {
			String state = obj.getConsigneeState().trim().toUpperCase();
			String suburb = obj.getConsigneeSuburb().trim().toUpperCase();
			String postcode = obj.getConsigneePostcode().trim();
			
			String combination = state.concat(suburb).concat(postcode);
			if(!postCodeZoneList.contains(combination)) {
				incorrectPostcode_Suburb.add(obj.getReferenceNumber()+"-"+obj.getConsigneeState().trim().toUpperCase()+"-"+obj.getConsigneeSuburb().trim().toUpperCase()+"-"+obj.getConsigneePostcode().trim());
				
			}
		});
		if(!incorrectPostcode_Suburb.isEmpty()) {
			throw new InvalidSuburbPostcodeException("Suburb is not in carrier serviced areas",incorrectPostcode_Suburb);
		}
		
			
	}
	public void isTollPostCodeValidUI(List<SenderData> orderDetailList) {

		List<String> postCodeZoneList = D2ZSingleton.getInstance().getMasterTollPostCodeList();
		List<String> incorrectPostcode_Suburb = new ArrayList<String>();
		orderDetailList.forEach(obj -> {
			String state = obj.getConsigneeState().trim().toUpperCase();
			String suburb = obj.getConsigneeSuburb().trim().toUpperCase();
			String postcode = obj.getConsigneePostcode().trim();
			String combination = state.concat(suburb).concat(postcode);
			if(!postCodeZoneList.contains(combination)) {
				incorrectPostcode_Suburb.add(obj.getReferenceNumber()+"-"+obj.getConsigneeState().trim().toUpperCase()+"-"+obj.getConsigneeSuburb().trim().toUpperCase()+"-"+obj.getConsigneePostcode().trim());
			}
		});
		if(!incorrectPostcode_Suburb.isEmpty()) {
			throw new InvalidSuburbPostcodeException("Suburb is not in carrier serviced areas",incorrectPostcode_Suburb);
		}
			
	}
	public void isTollPostCodeValid(List<SenderDataApi> senderData, Map<String, List<ErrorDetails>> errorMap) {

		List<String> postCodeZoneList = D2ZSingleton.getInstance().getMasterTollPostCodeList();

		senderData.forEach(obj -> {
			if(null!=obj.getConsigneeState() && null!=obj.getConsigneeSuburb() && null!=obj.getConsigneePostcode()
					&& !obj.getConsigneeState().isEmpty() && !obj.getConsigneeSuburb().isEmpty() && !obj.getConsigneePostcode().isEmpty()) {

			String state = obj.getConsigneeState().trim().toUpperCase();
			String suburb = obj.getConsigneeSuburb().trim().toUpperCase();
			String postcode = obj.getConsigneePostcode().trim();
			String combination = state.concat(suburb).concat(postcode);
			if(!postCodeZoneList.contains(combination)) {
		   				 ValidationUtils.populateErrorDetails(obj.getReferenceNumber(),obj.getConsigneeState().trim().toUpperCase()+"-"+obj.getConsigneeSuburb().trim().toUpperCase()+"-"+obj.getConsigneePostcode().trim(),
						 "Suburb is not in carrier serviced areas",errorMap) ;
		
			}
			}
		});
		
	
		
			
	}
	public void isRC2PostCodeValid(List<SenderDataApi> senderData) {

		List<String> postCodeZoneList = D2ZSingleton.getInstance().getMasterRC2PostCodeList();
		List<String> incorrectPostcode_Suburb = new ArrayList<String>();
		senderData.forEach(obj -> {
			String state = obj.getConsigneeState().trim().toUpperCase();
			String suburb = obj.getConsigneeSuburb().trim().toUpperCase();
			String postcode = obj.getConsigneePostcode().trim();
			
			String combination = state.concat(suburb).concat(postcode);
			if(!postCodeZoneList.contains(combination)) {
				incorrectPostcode_Suburb.add(obj.getReferenceNumber()+"-"+obj.getConsigneeState().trim().toUpperCase()+"-"+obj.getConsigneeSuburb().trim().toUpperCase()+"-"+obj.getConsigneePostcode().trim());
				
			}
		});
		if(!incorrectPostcode_Suburb.isEmpty()) {
			throw new InvalidSuburbPostcodeException("Suburb is not in carrier serviced areas",incorrectPostcode_Suburb);
		}
		
			
	}
	public void isRC2PostCodeValid(List<SenderDataApi> senderData, Map<String, List<ErrorDetails>> errorMap) {

		List<String> postCodeStateNameList = D2ZSingleton.getInstance().getMasterRC2PostCodeList();

		senderData.forEach(obj -> {
			if(null!=obj.getConsigneeState() && null!=obj.getConsigneeSuburb() && null!=obj.getConsigneePostcode()
					&& !obj.getConsigneeState().isEmpty() && !obj.getConsigneeSuburb().isEmpty() && !obj.getConsigneePostcode().isEmpty()) {

			String state = obj.getConsigneeState().trim().toUpperCase();
			String suburb = obj.getConsigneeSuburb().trim().toUpperCase();
			String postcode = obj.getConsigneePostcode().trim();
			String combination = state.concat(suburb).concat(postcode);
			if(!postCodeStateNameList.contains(combination)) {
		   				 ValidationUtils.populateErrorDetails(obj.getReferenceNumber(),obj.getConsigneeState().trim().toUpperCase()+"-"+obj.getConsigneeSuburb().trim().toUpperCase()+"-"+obj.getConsigneePostcode().trim(),
						 "Suburb is not in carrier serviced areas",errorMap) ;
		
			}
			}
		});
		
	
		
			
	}

}
