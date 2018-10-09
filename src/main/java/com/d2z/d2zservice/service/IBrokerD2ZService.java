package com.d2z.d2zservice.service;

import java.util.List;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.entity.User;
import com.d2z.d2zservice.model.DropDownModel;

public interface IBrokerD2ZService {

	public List<DropDownModel> companyDetails();

	public User fetchUserDetails(String companyName);

	public List<DropDownModel> getManifestList();

	public List<SenderdataMaster> consignmentDetails(String manifestNumber);

	public List<DropDownModel> fetchShipmentList();

}
