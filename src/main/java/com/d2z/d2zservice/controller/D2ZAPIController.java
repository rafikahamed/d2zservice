package com.d2z.d2zservice.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.d2z.d2zservice.exception.ReferenceNumberNotUniqueException;
import com.d2z.d2zservice.model.APIRatesRequest;
import com.d2z.d2zservice.model.CreateConsignmentRequest;
import com.d2z.d2zservice.model.DeleteConsignmentRequest;
import com.d2z.d2zservice.model.EditConsignmentRequest;
import com.d2z.d2zservice.model.ParcelStatus;
import com.d2z.d2zservice.model.PostCodeWeight;
import com.d2z.d2zservice.model.ResponseMessage;
import com.d2z.d2zservice.model.SenderDataResponse;
import com.d2z.d2zservice.model.UserMessage;
import com.d2z.d2zservice.service.ID2ZService;

@RestController
@Validated
@RequestMapping(value = "/v1/d2z/api")
public class D2ZAPIController {
Logger logger = LoggerFactory.getLogger(D2zController.class);
	
	@Autowired
    private  ID2ZService d2zService;
	
	@RequestMapping( method = RequestMethod.GET, path = "/trackParcel/referenceNumber/{referenceNumbers}")
    public List<ParcelStatus> trackParcel(@PathVariable List<String> referenceNumbers) {
		List<ParcelStatus> trackParcelResponse = d2zService.getStatusByRefNbr(referenceNumbers);
		return trackParcelResponse;
    }
	
	@RequestMapping( method = RequestMethod.GET, path = "/trackParcel/articleID/{articleIDs}")
    public List<ParcelStatus> trackParcelByArticleID(@PathVariable List<String> articleIDs) {
		List<ParcelStatus> trackParcelResponse = d2zService.getStatusByArticleID(articleIDs);
		return trackParcelResponse;
    }
	
	@RequestMapping(method = RequestMethod.POST, path = "/consignments-create")
	 public List<SenderDataResponse> createConsignments(@Valid @RequestBody CreateConsignmentRequest orderDetail) throws ReferenceNumberNotUniqueException {
		List<SenderDataResponse> senderDataResponse = d2zService.createConsignments(orderDetail);
		return senderDataResponse;
    }	
	
	@RequestMapping(method = RequestMethod.PUT, path = "/consignments")
	 public ResponseMessage editConsignments(@RequestBody List<@Valid EditConsignmentRequest> requestList) {
		return d2zService.editConsignments(requestList);
	}	
	
	@RequestMapping(method = RequestMethod.PUT, path = "/consignments/{referenceNumbers}/shipment/{shipmentNumber}")
	 public ResponseMessage allocateShipment(@PathVariable String referenceNumbers,@PathVariable String shipmentNumber) {
		return  d2zService.allocateShipment(referenceNumbers,shipmentNumber);
	}	
	
	@RequestMapping( method = RequestMethod.DELETE, path = "/consignments")
    public UserMessage deleteConsignments(@Valid @RequestBody DeleteConsignmentRequest request) throws ReferenceNumberNotUniqueException {
		return d2zService.deleteConsignments(request);
    }
	
	@RequestMapping( method = RequestMethod.POST, path = "/rates",  produces = "application/json")
	@ResponseBody
    public List<PostCodeWeight> getRates(@Valid @RequestBody APIRatesRequest request) throws ReferenceNumberNotUniqueException {
		return d2zService.getRates(request);
    }
	
	@RequestMapping( method = RequestMethod.POST, path = "/tracking-label")
    public ResponseEntity<byte[]> trackingLabel(@RequestBody String refBarNum) {
    	List<String> refBarNumArray =
    			  Stream.of(refBarNum.split(","))
    			  .collect(Collectors.toList());
		byte[] bytes = d2zService.trackingLabel(refBarNumArray);
	    return ResponseEntity
	      .ok()
	      // Specify content type as PDF
	      .header("Content-Type", "application/pdf; charset=UTF-8")
	      // Tell browser to display PDF if it can
	      .header("Content-Disposition", "inline; filename=\"Label.pdf\"")
	      .body(bytes);
	}

}
