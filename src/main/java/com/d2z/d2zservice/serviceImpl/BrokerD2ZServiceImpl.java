package com.d2z.d2zservice.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.d2z.d2zservice.dao.ID2ZBrokerDao;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.entity.User;
import com.d2z.d2zservice.entity.UserService;
import com.d2z.d2zservice.model.DropDownModel;
import com.d2z.d2zservice.model.ShipmentDetails;
import com.d2z.d2zservice.model.UserDetails;
import com.d2z.d2zservice.service.IBrokerD2ZService;

@Service
public class BrokerD2ZServiceImpl implements IBrokerD2ZService{
	
	@Autowired
    private ID2ZBrokerDao d2zDao;
	
	@Override
	public List<DropDownModel> companyDetails() {
		List<String> listOfCompany = d2zDao.companyDetails();
		List<DropDownModel> dropDownList= new ArrayList<DropDownModel>();
		for(String companyName:listOfCompany) {
			DropDownModel dropDownVaL = new DropDownModel();
			dropDownVaL.setName(companyName);
			dropDownVaL.setValue(companyName);
			dropDownList.add(dropDownVaL);
		}
		return dropDownList;
	}

	@Override
	public UserDetails fetchUserDetails(String companyName) {
		User user = d2zDao.fetchUserDetails(companyName);
		UserDetails userDetails = new UserDetails();
		userDetails.setAddress(user.getAddress());
		userDetails.setCompanyName(user.getCompanyName());
		userDetails.setContactName(user.getName());
		userDetails.setContactPhoneNumber(user.getPhoneNumber());
		userDetails.setCountry(user.getCountry());
		userDetails.setEmailAddress(user.getEmailAddress());
		userDetails.setPassword(user.getUser_Password());
		userDetails.setPostCode(user.getPostcode());
		userDetails.setState(user.getState());
		userDetails.setSuburb(user.getSuburb());
		userDetails.setUserName(user.getUser_Name());
		Set<UserService> userServiceList = user.getUserService();
		List<String> serviceType = userServiceList.stream().map(obj ->{
			return obj.getServiceType();}).collect(Collectors.toList());
		userDetails.setServiceType(serviceType);
		return userDetails;
	}

	@Override
	public List<DropDownModel> getManifestList() {
		List<String> listOfManifestId = d2zDao.getManifestList();
		List<DropDownModel> manifestDropDownList= new ArrayList<DropDownModel>();
		for(String manifestId:listOfManifestId) {
			if(manifestId != null) {
				DropDownModel dropDownVaL = new DropDownModel();
				dropDownVaL.setName(manifestId);
				dropDownVaL.setValue(manifestId);
				manifestDropDownList.add(dropDownVaL);
			}
		}
		return manifestDropDownList;
	}

	@Override
	public List<SenderdataMaster> consignmentDetails(String manifestNumber) {
		List<SenderdataMaster> consignmentDetails = d2zDao.consignmentDetails(manifestNumber);
		return consignmentDetails;
	}

	@Override
	public List<DropDownModel> fetchShipmentList() {
		List<String> listOfShipment = d2zDao.fetchShipmentList();
		List<DropDownModel> shipmentDropDownList= new ArrayList<DropDownModel>();
		for(String manifestId:listOfShipment) {
			if(manifestId != null) {
				DropDownModel dropDownVaL = new DropDownModel();
				dropDownVaL.setName(manifestId);
				dropDownVaL.setValue(manifestId);
				shipmentDropDownList.add(dropDownVaL);
			}
		}
		return shipmentDropDownList;
	}

	@Override
	public List<ShipmentDetails> directInjection(String companyName) {
		List<SenderdataMaster> senderDataList  = d2zDao.directInjection(companyName);
		List<ShipmentDetails> shipmentDetails = new ArrayList<ShipmentDetails>();
		for(SenderdataMaster senderData : senderDataList) {
			ShipmentDetails shipmentData = new ShipmentDetails();
			shipmentData.setReferenceNumber(senderData.getReference_number());
			shipmentData.setCon_no(senderData.getBarcodelabelNumber().substring(19,30));
			shipmentData.setConsigneeName(senderData.getConsignee_name());
			shipmentData.setConsigneeAddress(senderData.getConsignee_addr1());
			shipmentData.setWeight(senderData.getWeight());
			shipmentData.setConsigneePhone(senderData.getConsignee_Phone());
			shipmentData.setConsigneeSuburb(senderData.getConsignee_Suburb());
			shipmentData.setConsigneeState(senderData.getConsignee_State());
			shipmentData.setConsigneePostcode(senderData.getConsignee_Postcode());
			shipmentData.setDestination("AUSTRALIA");
			shipmentData.setQuantity(senderData.getShippedQuantity());
			shipmentData.setCommodity(senderData.getProduct_Description());
			shipmentData.setValue(senderData.getValue());
			shipmentData.setShipperName(senderData.getShipper_Name());
			shipmentData.setShipperAddress(senderData.getShipper_Addr1());
			shipmentData.setShipperCity(senderData.getShipper_City());
			shipmentData.setShipperState(senderData.getShipper_State());
			shipmentData.setShipperPostcode(senderData.getShipper_Postcode());
			shipmentData.setShipperCountry(senderData.getShipper_Country());
			shipmentData.setShipperContact(senderData.getAirwayBill());
			shipmentDetails.add(shipmentData);
		}
		return shipmentDetails;
	}

	@Override
	public List<DropDownModel> fetchApiShipmentList() {
		List<String> listOfApiShipment = d2zDao.fetchApiShipmentList();
		List<DropDownModel> apiShipmentDropDownList= new ArrayList<DropDownModel>();
		for(String shipmentId:listOfApiShipment) {
			if(shipmentId != null) {
				DropDownModel dropDownVaL = new DropDownModel();
				dropDownVaL.setName(shipmentId);
				dropDownVaL.setValue(shipmentId);
				apiShipmentDropDownList.add(dropDownVaL);
			}
		}
		return apiShipmentDropDownList;
	}
	

}
