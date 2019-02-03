package com.d2z.d2zservice.dao;

import java.util.List;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.entity.User;

public interface ID2ZBrokerDao {
	
	public List<String> companyDetails(String brokerId);

	public User fetchUserDetails(String companyName, String roleId);

	public List<String> getManifestList();

	public List<SenderdataMaster> consignmentDetails(String manifestNumber);

	public List<String> fetchShipmentList();

	public List<String> directInjection(String companyName);

	public List<String> fetchApiShipmentList();

	public List<SenderdataMaster> fetchShipmentData(String shipmentNumber);


}
