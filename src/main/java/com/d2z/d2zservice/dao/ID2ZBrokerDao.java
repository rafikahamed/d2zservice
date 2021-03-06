package com.d2z.d2zservice.dao;

import java.util.List;

import com.d2z.d2zservice.entity.Consignments;

import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.entity.User;
import com.d2z.d2zservice.model.ShipmentDetails;

public interface ID2ZBrokerDao {
	
	public List<String> companyDetails(String brokerId);

	public User fetchUserDetails(String companyName, String roleId);

	public List<String> getManifestList(List<Integer> userId);

	public List<SenderdataMaster> consignmentDetails(String manifestNumber, List<Integer> userId);

	public List<String> fetchShipmentList(List<Integer> userId);

	public List<String> directInjection(String companyName);

	public List<String> fetchApiShipmentList();

	public List<SenderdataMaster> fetchShipmentData(String shipmentNumber, List<Integer> userId);

	List<Consignments> fetchConsignmentsByState(List<String> referenceNumbers);

	User login(String userName, String passWord);

	public List<Integer> getClientId(Integer userId);
	
	public List<String> fetchServiceTypeByUserName(String userName);

	public List<SenderdataMaster> fetchShipmentDatabyType(List<String> number, List<Integer> listOfClientId, String type);

}
