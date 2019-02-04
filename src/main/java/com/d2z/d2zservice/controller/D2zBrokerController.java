package com.d2z.d2zservice.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.model.BaggingRequest;
import com.d2z.d2zservice.model.BaggingResponse;
import com.d2z.d2zservice.model.DirectInjectionDetails;
import com.d2z.d2zservice.model.DropDownModel;
import com.d2z.d2zservice.model.UserDetails;
import com.d2z.d2zservice.service.IBrokerD2ZService;

@RestController
@RequestMapping(value = "/v1/d2z/broker-level")
public class D2zBrokerController {
	
	Logger logger = LoggerFactory.getLogger(D2zController.class);
	
	@Autowired
    private  IBrokerD2ZService brokerD2zService;
	
	@RequestMapping( method = RequestMethod.GET, path = "/company-details")
    public List<DropDownModel> companyDetails(@RequestParam("brokerId") String brokerId) {
		return brokerD2zService.companyDetails(brokerId);
    }
	
	@RequestMapping( method = RequestMethod.GET, path = "/user-details")
    public UserDetails fetchUserDetails(@RequestParam("companyName") String companyName, @RequestParam("roleId") String roleId) {
		return brokerD2zService.fetchUserDetails(companyName,roleId);
    }
	
	@RequestMapping( method = RequestMethod.GET, path = "/manifestList")
    public List<DropDownModel> getManifestList() {
		return brokerD2zService.getManifestList();
    }
	
	@RequestMapping( method = RequestMethod.GET, path = "/shipmentList")
    public List<DropDownModel> fetchShipmentList() {
		return brokerD2zService.fetchShipmentList();
    }
	
	@RequestMapping( method = RequestMethod.GET, path = "/api-shipmentList")
    public List<DropDownModel> fetchApiShipmentList() {
		return brokerD2zService.fetchApiShipmentList();
    }
	
	@RequestMapping( method = RequestMethod.GET, path = "/consignment-details")
    public List<SenderdataMaster> consignmentDetails(@RequestParam("manifestNumber") String manifestNumber) {
		return brokerD2zService.consignmentDetails(manifestNumber);
    }
	
	@RequestMapping( method = RequestMethod.GET, path = "/direct-injection")
    public List<DirectInjectionDetails> directInjection(@RequestParam("companyName") String companyName) {
		List<DirectInjectionDetails> directInjection = brokerD2zService.directInjection(companyName);
    	return directInjection;
    }
	
	@RequestMapping( method = RequestMethod.GET, path = "/consignments/shipment")
    public List<SenderdataMaster> downloadShipmentData(@RequestParam("shipmentNumber") String shipmentNumber) {
		return brokerD2zService.downloadShipmentData(shipmentNumber);
    }
	
	@RequestMapping( method = RequestMethod.POST, path = "/bag")
    public BaggingResponse getbagDetails(@RequestBody BaggingRequest request) {
		BaggingResponse response = brokerD2zService.getbagDetails(request);
		return response;
    }
}
