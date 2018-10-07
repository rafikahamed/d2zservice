package com.d2z.d2zservice.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.d2z.d2zservice.dao.ID2ZBrokerDao;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.entity.User;
import com.d2z.d2zservice.model.DropDownModel;
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
	public User fetchUserDetails(String companyName) {
		User userDetails = d2zDao.fetchUserDetails(companyName);
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

}
