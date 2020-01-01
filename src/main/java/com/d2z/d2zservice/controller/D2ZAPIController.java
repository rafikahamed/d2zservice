package com.d2z.d2zservice.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.d2z.d2zservice.exception.EtowerFailureResponseException;
import com.d2z.d2zservice.exception.PCAlabelException;
import com.d2z.d2zservice.exception.ReferenceNumberNotUniqueException;
import com.d2z.d2zservice.model.APIRatesRequest;
import com.d2z.d2zservice.model.CreateConsignmentRequest;
import com.d2z.d2zservice.model.DeleteConsignmentRequest;
import com.d2z.d2zservice.model.EditConsignmentRequest;
import com.d2z.d2zservice.model.Enquiry;
import com.d2z.d2zservice.model.EnquiryResponse;
import com.d2z.d2zservice.model.ParcelStatus;
import com.d2z.d2zservice.model.PostCodeWeight;
import com.d2z.d2zservice.model.ResponseMessage;
import com.d2z.d2zservice.model.SenderDataResponse;
import com.d2z.d2zservice.model.TrackParcelResponse;
import com.d2z.d2zservice.model.UserMessage;
import com.d2z.d2zservice.repository.CSTicketsRepository;
import com.d2z.d2zservice.repository.UserRepository;
import com.d2z.d2zservice.service.ID2ZService;

@RestController
@Validated
@RequestMapping(value = "/v1/d2z/api")
public class D2ZAPIController {
Logger logger = LoggerFactory.getLogger(D2ZAPIController.class);
	
	@Autowired
    private  ID2ZService d2zService;
	
	@Autowired
	private CSTicketsRepository csTicketsRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping( method = RequestMethod.GET, path = "/trackParcels")
    public List<TrackParcelResponse> trackParcels(@RequestBody List<String> articleIds) {
		List<TrackParcelResponse> trackParcelResponse = new ArrayList<TrackParcelResponse>();
		try{
			trackParcelResponse = d2zService.trackParcels(articleIds);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return trackParcelResponse;
    }
	
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
	 public List<SenderDataResponse> createConsignments(@Valid @RequestBody CreateConsignmentRequest orderDetail) throws ReferenceNumberNotUniqueException, EtowerFailureResponseException {
		List<String> autoShipRefNbrs = new ArrayList<String>();
		List<SenderDataResponse> senderDataResponse = d2zService.createConsignments(orderDetail,autoShipRefNbrs);
		 Runnable r = new Runnable( ) {			
		        public void run() {
		        	
		        	List<String> incomingRefNbr = orderDetail.getConsignmentData().stream().map(obj -> {
		    			return obj.getReferenceNumber(); })
		    				.collect(Collectors.toList());
		        	d2zService.makeCallToEtowerBasedonSupplierUI(incomingRefNbr);
		        	
		        	if(null!=autoShipRefNbrs && !autoShipRefNbrs.isEmpty()) {
		    			System.out.println("Auto-Shipment Allocation");
		    			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		    			String shipmentNumber = orderDetail.getUserName()+simpleDateFormat.format(new Date());
		    			try {
							d2zService.allocateShipment(String.join(",", autoShipRefNbrs), shipmentNumber);
						} catch (ReferenceNumberNotUniqueException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}        
		            	
		    		}
		    		
		        }
		     };
		    new Thread(r).start();
		return senderDataResponse;
    }	
	
	@RequestMapping(method = RequestMethod.PUT, path = "/consignments")
	 public ResponseMessage editConsignments(@RequestBody List<@Valid EditConsignmentRequest> requestList) {
		return d2zService.editConsignments(requestList);
	}	
	
	@RequestMapping(method = RequestMethod.PUT, path = "/consignments/{referenceNumbers}/shipment/{shipmentNumber}")
	 public ResponseMessage allocateShipment(@PathVariable String referenceNumbers,@PathVariable String shipmentNumber) throws ReferenceNumberNotUniqueException {
		return  d2zService.allocateShipment(referenceNumbers,shipmentNumber);
	}
	@RequestMapping(method = RequestMethod.PUT, path = "/consignments/shipment/{shipmentNumber}")
	 public ResponseMessage shipmentAllocation(@RequestBody String referenceNumbers,@PathVariable String shipmentNumber) throws ReferenceNumberNotUniqueException {
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
    public ResponseEntity<byte[]> trackingLabel(@RequestBody String refBarNum) throws PCAlabelException {
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
    
    @RequestMapping(method = RequestMethod.PUT, path = "/consignments/shipmentarticleid/{shipmentNumber}")
	public ResponseMessage allocateShipmentbyArticleID(@RequestBody String articleid,@PathVariable String shipmentNumber)
			throws ReferenceNumberNotUniqueException {
		return d2zService.allocateShipmentArticleid(articleid.toString(), shipmentNumber);
	}

	@RequestMapping(method = RequestMethod.POST, path = "/create-enquiry")
	public EnquiryResponse createEnquiry(@RequestBody Enquiry createEnquiry) throws ReferenceNumberNotUniqueException {
		Integer userId = userRepository.fetchUserIdbyUserName(createEnquiry.getUserName());
		EnquiryResponse enquiryInfo;
		if(userId !=null) {
			enquiryInfo = d2zService.createEnquiry(createEnquiry);
		}else {
			enquiryInfo = new EnquiryResponse();
			enquiryInfo.setMessage("UserName is Not Avilale in the system");
		}
		return enquiryInfo;
	}
	
	@RequestMapping(value = "/generatePod", method = RequestMethod.POST)
	public ResponseEntity<byte[]> fetchPod (@RequestParam("ticketId") String ticketId) throws Exception {
		byte[] poddata = csTicketsRepository.fetchPod(ticketId);
		return ResponseEntity.ok()
				.header("Content-Type", "application/pdf; charset=UTF-8")
				.header("Content-Disposition", "inline; filename=\"Label.pdf\"").body(poddata);
	}
	
}
