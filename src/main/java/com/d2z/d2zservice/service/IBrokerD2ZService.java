package com.d2z.d2zservice.service;

import java.util.List;

import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.model.DirectInjectionDetails;
import com.d2z.d2zservice.model.DropDownModel;
import com.d2z.d2zservice.model.UserDetails;

public interface IBrokerD2ZService {

	public List<DropDownModel> companyDetails();

	public UserDetails fetchUserDetails(String companyName, String roleId);

	public List<DropDownModel> getManifestList();

	public List<SenderdataMaster> consignmentDetails(String manifestNumber);

	public List<DropDownModel> fetchShipmentList();

	public List<DirectInjectionDetails> directInjection(String companyName);

	public List<DropDownModel> fetchApiShipmentList();

	public List<SenderdataMaster> downloadShipmentData(String shipmentNumber);

}
