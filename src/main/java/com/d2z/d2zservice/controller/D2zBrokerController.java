package com.d2z.d2zservice.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.entity.User;
import com.d2z.d2zservice.model.DropDownModel;
import com.d2z.d2zservice.service.IBrokerD2ZService;

@RestController
@RequestMapping(value = "/v1/d2z/broker-level")
public class D2zBrokerController {
	
	Logger logger = LoggerFactory.getLogger(D2zController.class);
	
	@Autowired
    private  IBrokerD2ZService brokerD2zService;
	
	@RequestMapping( method = RequestMethod.GET, path = "/company-details")
    public List<DropDownModel> companyDetails() {
		return brokerD2zService.companyDetails();
    }
	
	@RequestMapping( method = RequestMethod.GET, path = "/user-details")
    public User fetchUserDetails(@RequestParam("companyName") String companyName) {
		return brokerD2zService.fetchUserDetails(companyName);
    }
	
	@RequestMapping( method = RequestMethod.GET, path = "/manifestList")
    public List<DropDownModel> getManifestList() {
		return brokerD2zService.getManifestList();
    }
	
	@RequestMapping( method = RequestMethod.GET, path = "/consignment-details")
    public List<SenderdataMaster> consignmentDetails(@RequestParam("manifestNumber") String manifestNumber) {
		return brokerD2zService.consignmentDetails(manifestNumber);
    }

}
