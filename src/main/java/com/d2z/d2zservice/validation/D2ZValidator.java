package com.d2z.d2zservice.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.d2z.d2zservice.dao.ID2ZDao;
import com.d2z.d2zservice.exception.InvalidServiceTypeException;
import com.d2z.d2zservice.exception.InvalidSuburbPostcodeException;
import com.d2z.d2zservice.exception.ReferenceNumberNotUniqueException;
import com.d2z.d2zservice.model.CreateConsignmentRequest;
import com.d2z.d2zservice.model.SenderData;
import com.d2z.singleton.D2ZSingleton;

@Service
public class D2ZValidator {

	@Autowired
    private ID2ZDao d2zDao;

	public void isPostCodeValid(List<SenderData> senderData) {
		
		List<String> postCodeZoneList = D2ZSingleton.getInstance().getPostCodeZoneList();
		System.out.println(postCodeZoneList.toString());
		//postCodeZoneList.forEach(System.out::println);
		
		//System.out.println("Incoming suburb & postcode");
		
		/*senderData.stream().map(obj -> {
			return obj.getConsigneeSuburb().toUpperCase().concat(obj.getConsigneePostcode()); })
				.collect(Collectors.toList()).forEach(System.out::println);*/

		/*if(!postCodeZoneList.containsAll(senderData.stream().map(obj -> {
			return obj.getConsigneeSuburb().toUpperCase().concat(obj.getConsigneePostcode()); })
				.collect(Collectors.toList()))) {
			throw new InvalidSuburbPostcodeException("Invalid Consignee Postcode or Consignee Suburb");
		}*/
		
		List<String> incorrectPostcode_Suburb = new ArrayList<String>();
		senderData.forEach(obj -> {
			if(!postCodeZoneList.contains(obj.getConsigneeSuburb().toUpperCase().concat(obj.getConsigneePostcode()))) {
				incorrectPostcode_Suburb.add(obj.getReferenceNumber());
			}
		});
		if(!incorrectPostcode_Suburb.isEmpty()) {
			throw new InvalidSuburbPostcodeException("Invalid Consignee Postcode or Consignee Suburb",incorrectPostcode_Suburb);

		}
		
	}
	
	public void isReferenceNumberUnique(List<SenderData> senderData) throws ReferenceNumberNotUniqueException{
		
		List<String> referenceNumber_DB = d2zDao.fetchAllReferenceNumbers();
		
		List<String> incomingRefNbr = senderData.stream().map(obj -> {
			return obj.getReferenceNumber(); })
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

	public void isServiceValid(CreateConsignmentRequest orderDetail) {
		List<String> incorrectRefNbr = new ArrayList<String>();
		List<String> serviceType_DB = d2zDao.fetchServiceTypeByUserName(orderDetail.getUserName());
		List<SenderData> orderDetailList = orderDetail.getConsignmentData();
		for(SenderData senderData : orderDetailList) {
			if(!serviceType_DB.contains(senderData.getServiceType())) {
				incorrectRefNbr.add(senderData.getReferenceNumber());
			}
		} 
		if(!incorrectRefNbr.isEmpty()) {
			throw new InvalidServiceTypeException("Invalid Service Type",incorrectRefNbr);
		}
	}

}
