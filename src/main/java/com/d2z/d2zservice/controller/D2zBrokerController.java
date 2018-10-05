package com.d2z.d2zservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.d2z.d2zservice.entity.User;
import com.d2z.d2zservice.service.IBrokerD2ZService;
import com.d2z.d2zservice.service.ID2ZService;

@RestController
@Validated
@RequestMapping(value = "/v1/d2z/broker-level")
public class D2zBrokerController {
	
Logger logger = LoggerFactory.getLogger(D2zController.class);
	
	@Autowired
    private  IBrokerD2ZService brokerD2zService;
	
	@RequestMapping( method = RequestMethod.GET, path = "/compant-details")
    public User companyDetails() {
		User userDetails = brokerD2zService.companyDetails();
		return userDetails;
    }

}
