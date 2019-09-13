package com.d2z.d2zservice.daoImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.d2z.d2zservice.dao.ID2ZBrokerDao;
import com.d2z.d2zservice.entity.Consignments;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.entity.User;
import com.d2z.d2zservice.repository.SenderDataRepository;
import com.d2z.d2zservice.repository.UserRepository;
import com.d2z.d2zservice.repository.UserServiceRepository;

@Repository
public class D2ZBrokerDaoImpl implements ID2ZBrokerDao{
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	SenderDataRepository senderDataRepository;
	
	@Autowired
	UserServiceRepository userServiceRepository ;
	
	
	@Override
	public List<String> companyDetails(String brokerId) {
		List<String> companyDetails = userRepository.fetchCompanyName(brokerId);
		return companyDetails;
	}

	@Override
	public User fetchUserDetails(String companyName, String roleId) {
		User userDetails = userRepository.fetchUserbyCompanyName(companyName,Integer.parseInt(roleId));
		return userDetails;
	}

	@Override
	public List<String> getManifestList(List<Integer> userId) {
		List<String> companyDetails = senderDataRepository.fetchManifestNumber(userId);
		return companyDetails;
	}

	@Override
	public List<SenderdataMaster> consignmentDetails(String manifestNumber, List<Integer> userId) {
		List<SenderdataMaster> consignmentData = senderDataRepository.fetchConsignmentByManifest(manifestNumber, userId);
		return consignmentData;
	}

	@Override
	public List<String> fetchShipmentList(List<Integer> userId) {
		List<String> shipmentDetails = senderDataRepository.fetchShipmentList(userId);
		return shipmentDetails;
	}

	@Override
	public List<String> directInjection(String companyName) {
		List<String> senderData = senderDataRepository.fetchDirectInjectionData(companyName);
		return senderData;
	}

	@Override
	public List<String> fetchApiShipmentList() {
		List<String> shipmentDetails = senderDataRepository.fetchApiShipmentList();
		return shipmentDetails;
	}

	@Override
	public List<SenderdataMaster> fetchShipmentData(String shipmentNumber, List<Integer> userId) {
		return senderDataRepository.fetchShipmentData(shipmentNumber, userId);
	}
	
	@Override
	public List<Consignments> fetchConsignmentsByState(List<String> referenceNumbers){
		List<Consignments> consignments =  senderDataRepository.fetchConsignmentsForBagging(referenceNumbers);
		return consignments;
	}

	@Override
	public User login(String userName, String passWord) {
		User userDaetils = userRepository.fetchUserDetails(userName, passWord);
		return userDaetils;
	}


	@Override
	public List<Integer> getClientId(Integer userId) {
		List<Integer> listOfClientId = userRepository.getClientId(userId.toString());
		return listOfClientId;
	}

	@Override
	public List<String> fetchServiceTypeByUserName(String userName) {
		List<String> serviceTypeList = userServiceRepository.fetchAllServiceTypeByUserName(userName);
		return serviceTypeList;
	}

}
