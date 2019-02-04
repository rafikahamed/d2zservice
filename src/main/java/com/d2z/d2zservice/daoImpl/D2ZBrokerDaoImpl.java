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
import com.d2z.d2zservice.model.BrokerRatesData;
import com.d2z.d2zservice.repository.SenderDataRepository;
import com.d2z.d2zservice.repository.UserRepository;

@Repository
public class D2ZBrokerDaoImpl implements ID2ZBrokerDao{
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	SenderDataRepository senderDataRepository;
	
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
	public List<String> getManifestList() {
		List<String> companyDetails = senderDataRepository.fetchManifestNumber();
		return companyDetails;
	}

	@Override
	public List<SenderdataMaster> consignmentDetails(String manifestNumber) {
		List<SenderdataMaster> consignmentData = senderDataRepository.fetchConsignmentByManifest(manifestNumber);
		return consignmentData;
	}

	@Override
	public List<String> fetchShipmentList() {
		List<String> shipmentDetails = senderDataRepository.fetchShipmentList();
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
	public List<SenderdataMaster> fetchShipmentData(String shipmentNumber) {
		return senderDataRepository.fetchShipmentData(shipmentNumber);
	}
	@Override
	public List<List<Consignments>> fetchConsignmentsByState(List<String> referenceNumbers){
		List<Consignments> consignments =  senderDataRepository.fetchConsignmentsForBagging(referenceNumbers);
		Map<String, List<Consignments>> grouped = new HashMap<String, List<Consignments>>();
		grouped = consignments.stream().collect(Collectors.groupingBy(Consignments::getStateCode));
		List<List<Consignments>> consignmentsByState = grouped.values().stream().collect(Collectors.toList());
		return consignmentsByState;
	}


}
