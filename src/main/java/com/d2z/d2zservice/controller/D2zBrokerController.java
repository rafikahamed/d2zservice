package com.d2z.d2zservice.controller;

import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import com.d2z.d2zservice.service.ShipmentAllocator;
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
import com.d2z.d2zservice.exception.ReferenceNumberNotUniqueException;
import com.d2z.d2zservice.model.BaggingRequest;
import com.d2z.d2zservice.model.BaggingResponse;
import com.d2z.d2zservice.model.DirectInjectionDetails;
import com.d2z.d2zservice.model.DropDownModel;
import com.d2z.d2zservice.model.ResponseMessage;
import com.d2z.d2zservice.model.ShipmentDetails;
import com.d2z.d2zservice.model.UserDetails;
import com.d2z.d2zservice.service.IBrokerD2ZService;
import com.d2z.d2zservice.service.ID2ZService;

@RestController
@RequestMapping(value = "/v1/d2z/broker-level")
public class D2zBrokerController {

	Logger logger = LoggerFactory.getLogger(D2zController.class);

	@Autowired
	private IBrokerD2ZService brokerD2zService;

	@Autowired
	private ID2ZService d2zService;

	@Autowired
    private ShipmentAllocator allocator;
	@RequestMapping(method = RequestMethod.GET, path = "/company-details")
	public List<DropDownModel> companyDetails(@RequestParam("brokerId") String brokerId) {
		return brokerD2zService.companyDetails(brokerId);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/user-details")
	public UserDetails fetchUserDetails(@RequestParam("companyName") String companyName,
			@RequestParam("roleId") String roleId) {
		return brokerD2zService.fetchUserDetails(companyName, roleId);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/manifestList")
	public List<DropDownModel> getManifestList(@RequestParam("userId") Integer userId) {
		return brokerD2zService.getManifestList(userId);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/shipmentList")
	public List<DropDownModel> fetchShipmentList(@RequestParam("userId") Integer userId) {
		return brokerD2zService.fetchShipmentList(userId);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/api-shipmentList")
	public List<DropDownModel> fetchApiShipmentList() {
		return brokerD2zService.fetchApiShipmentList();
	}

	@RequestMapping(method = RequestMethod.GET, path = "/consignment-details")
	public List<SenderdataMaster> consignmentDetails(@RequestParam("manifestNumber") String manifestNumber,
			@RequestParam("userId") Integer userId) {
		return brokerD2zService.consignmentDetails(manifestNumber, userId);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/direct-injection")
	public List<DirectInjectionDetails> directInjection(@RequestParam("companyName") String companyName) {
		List<DirectInjectionDetails> directInjection = brokerD2zService.directInjection(companyName);
		return directInjection;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/consignments/shipment")
	public List<SenderdataMaster> downloadShipmentData(@RequestParam("shipmentNumber") String shipmentNumber,
			@RequestParam("userId") Integer userId) {
		return brokerD2zService.downloadShipmentData(shipmentNumber, userId);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/consignments/shipmenttype")
	public List<SenderdataMaster> downloadShipmentDatabyType(@RequestParam("Data") List<String> Number,
			@RequestParam("userId") Integer userId,@RequestParam("Type") String Type) {
		List<SenderdataMaster> senderData = brokerD2zService.downloadShipmentDatabyType(Number, userId,Type);
		return senderData;
	}
	@RequestMapping(method = RequestMethod.POST, path = "/bag")
	public BaggingResponse getbagDetails(@RequestBody @Valid BaggingRequest request) {
		BaggingResponse response = brokerD2zService.getbagDetails(request);
		return response;
	}

	@RequestMapping(method = RequestMethod.PUT, path = "/consignments/shipment/{shipmentNumber}")
	public ResponseMessage allocateShipment(@RequestBody String referenceNumbers,@PathVariable String shipmentNumber)
			throws ReferenceNumberNotUniqueException {
		//return d2zService.allocateShipment(referenceNumbers.toString(), shipmentNumber);
		return    allocator.allocateShipment(Collections.singletonList(referenceNumbers),shipmentNumber,"referenceNumber");

	}
	
}
