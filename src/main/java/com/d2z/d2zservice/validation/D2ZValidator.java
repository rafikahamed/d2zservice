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
import com.d2z.d2zservice.exception.InvalidServiceTypeException;
import com.d2z.d2zservice.exception.InvalidSuburbPostcodeException;
import com.d2z.d2zservice.exception.ReferenceNumberNotUniqueException;
import com.d2z.d2zservice.model.CreateConsignmentRequest;
import com.d2z.d2zservice.model.PFLSenderDataFileRequest;
import com.d2z.d2zservice.model.PFLSenderDataRequest;
import com.d2z.d2zservice.model.ReconcileData;
import com.d2z.d2zservice.model.SenderData;
import com.d2z.d2zservice.model.SenderDataApi;
import com.d2z.singleton.D2ZSingleton;

@Service
public class D2ZValidator {

	@Autowired
    private ID2ZDao d2zDao;
	
	@Autowired
    private ID2ZSuperUserDao d2zSuperUserDao;

	public void isPostCodeValid(List<SenderDataApi> senderData) {
		List<String> postCodeZoneList = D2ZSingleton.getInstance().getPostCodeZoneList();

		List<String> incorrectPostcode_Suburb = new ArrayList<String>();
		senderData.forEach(obj -> {
			if(!postCodeZoneList.contains(obj.getConsigneeState().trim().toUpperCase().concat(obj.getConsigneeSuburb().trim().toUpperCase()).concat(obj.getConsigneePostcode().trim()))) {
				incorrectPostcode_Suburb.add(obj.getReferenceNumber()+"-"+obj.getConsigneeState().trim().toUpperCase()+"-"+obj.getConsigneeSuburb().trim().toUpperCase()+"-"+obj.getConsigneePostcode().trim());
				
			}
		});
		if(!incorrectPostcode_Suburb.isEmpty()) {
			throw new InvalidSuburbPostcodeException("Invalid Combination of Consignee State, Postcode and Suburb",incorrectPostcode_Suburb);
		}
		
	}
	
	public void isPostCodeValidUI(List<SenderData> senderData) {
		List<String> postCodeZoneList = D2ZSingleton.getInstance().getPostCodeZoneList();
		List<String> incorrectPostcode_Suburb = new ArrayList<String>();
		senderData.forEach(obj -> {
			if(!postCodeZoneList.contains(obj.getConsigneeState().trim().toUpperCase().concat(obj.getConsigneeSuburb().trim().toUpperCase()).concat(obj.getConsigneePostcode().trim()))) {
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
		List<String> incorrectFWPostcode_Suburb = new ArrayList<String>();
		consignmentData.forEach(obj -> {
			if(!postCodeFWZoneList.contains(obj.getConsigneeSuburb().trim().toUpperCase().concat(obj.getConsigneePostcode().trim()))) {
				incorrectFWPostcode_Suburb.add(obj.getReferenceNumber()+"-"+obj.getConsigneeSuburb().trim().toUpperCase()+"-"+obj.getConsigneePostcode().trim());
			}
		});
		if(!incorrectFWPostcode_Suburb.isEmpty()) {
			throw new InvalidSuburbPostcodeException("Postcode or Suburb is not matched for this services",incorrectFWPostcode_Suburb);
		}
	}
	
	public void isFWPostCodeUIValid(List<SenderData> consignmentData) {
		List<String> postCodeFWZoneList = D2ZSingleton.getInstance().getFWPostCodeZoneList();
		List<String> incorrectFWPostcode_Suburb = new ArrayList<String>();
		consignmentData.forEach(obj -> {
			if(!postCodeFWZoneList.contains(obj.getConsigneeSuburb().trim().toUpperCase().concat(obj.getConsigneePostcode().trim()))) {
				incorrectFWPostcode_Suburb.add(obj.getReferenceNumber()+"-"+obj.getConsigneeSuburb().trim().toUpperCase()+"-"+obj.getConsigneePostcode().trim());
			}
		});
		if(!incorrectFWPostcode_Suburb.isEmpty()) {
			throw new InvalidSuburbPostcodeException("Postcode or Suburb is not matched for this services",incorrectFWPostcode_Suburb);
		}
	}

	public PFLSenderDataRequest isFWSubPostCodeValid(CreateConsignmentRequest consignmentData) {
		List<SenderDataApi> pflSenderData = new ArrayList<SenderDataApi>();
		List<SenderDataApi> nonPflSenderData = new ArrayList<SenderDataApi>();
		PFLSenderDataRequest pflRequest = new PFLSenderDataRequest();
		List<String> postCodeFWSubList = D2ZSingleton.getInstance().getFWPostCodeZoneList();
		consignmentData.getConsignmentData().forEach(obj -> {
			if(postCodeFWSubList.contains(obj.getConsigneeSuburb().trim().toUpperCase().concat(obj.getConsigneePostcode().trim()))) {
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
			if(postCodeFWSubList.contains(obj.getConsigneeSuburb().trim().toUpperCase().concat(obj.getConsigneePostcode().trim()))) {
				pflSenderData.add(obj);
			}else {
				nonPflSenderData.add(obj);
			}
		});
		pflRequest.setPflSenderDataApi(pflSenderData);
		pflRequest.setNonPflSenderDataApi(nonPflSenderData);
		return pflRequest;
	}

}
