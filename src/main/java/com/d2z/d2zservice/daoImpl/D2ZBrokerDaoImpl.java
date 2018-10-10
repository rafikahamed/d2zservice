package com.d2z.d2zservice.daoImpl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.d2z.d2zservice.dao.ID2ZBrokerDao;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.entity.User;
import com.d2z.d2zservice.repository.SenderDataRepository;
import com.d2z.d2zservice.repository.UserRepository;

@Repository
public class D2ZBrokerDaoImpl implements ID2ZBrokerDao{
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	SenderDataRepository senderDataRepository;
	
	@Override
	public List<String> companyDetails() {
		List<String> companyDetails = userRepository.fetchCompanyName();
		return companyDetails;
	}

	@Override
	public User fetchUserDetails(String companyName) {
		User userDetails = userRepository.fetchUserbyCompanyName(companyName);
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
	public List<SenderdataMaster> directInjection(String companyName) {
		List<SenderdataMaster> senderData = senderDataRepository.fetchDirectInjectionData(companyName);
		return senderData;
	}

	@Override
	public List<String> fetchApiShipmentList() {
		List<String> shipmentDetails = senderDataRepository.fetchApiShipmentList();
		return shipmentDetails;
	}

}
