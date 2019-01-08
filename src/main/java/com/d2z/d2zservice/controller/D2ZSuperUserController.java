package com.d2z.d2zservice.controller;

import java.util.List;
import javax.ws.rs.core.MediaType;
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
import com.d2z.d2zservice.model.ArrivalReportFileData;
import com.d2z.d2zservice.model.BrokerRatesData;
import com.d2z.d2zservice.model.DropDownModel;
import com.d2z.d2zservice.model.ResponseMessage;
import com.d2z.d2zservice.model.UploadTrackingFileData;
import com.d2z.d2zservice.model.UserDetails;
import com.d2z.d2zservice.model.UserMessage;
import com.d2z.d2zservice.service.ISuperUserD2ZService;

@RestController
@RequestMapping(value = "/v1/d2z/superUser-level")
public class D2ZSuperUserController {

Logger logger = LoggerFactory.getLogger(D2zController.class);
	
	@Autowired
    private  ISuperUserD2ZService superUserD2zService;
	
	@RequestMapping( method = RequestMethod.POST, path = "/track-fileUpload", consumes=MediaType.APPLICATION_JSON)
    public UserMessage uploadTrackingFile(@RequestBody List<UploadTrackingFileData> fileData) {
		UserMessage successMsg = superUserD2zService.uploadTrackingFile(fileData);
		return successMsg;
    }
	
	@RequestMapping( method = RequestMethod.POST, path = "/track-arrivalReportUpload", consumes=MediaType.APPLICATION_JSON)
    public UserMessage uploadArrivalReport(@RequestBody List<ArrivalReportFileData> fileData) {
		UserMessage successMsg = superUserD2zService.uploadArrivalReport(fileData);
		return successMsg;
    }
	
	@RequestMapping( method = RequestMethod.GET, path = "/broker-company-details")
    public List<DropDownModel> brokerCompanyDetails() {
		return superUserD2zService.brokerCompanyDetails();
    }
	
	@RequestMapping( method = RequestMethod.GET, path = "/broker-details")
    public UserDetails fetchUserDetails(@RequestParam("companyName") String companyName) {
		return superUserD2zService.fetchUserDetails(companyName);
    }

	@RequestMapping( method = RequestMethod.GET, path = "/export/delete")
	 public List<SenderdataMaster> exportDeteledConsignments(@RequestParam("fromDate") String fromDate,@RequestParam("toDate") String toDate) {
		return superUserD2zService.exportDeteledConsignments(fromDate, toDate);
    }
	
	@RequestMapping( method = RequestMethod.GET, path = "/export/consignment")
	 public List<SenderdataMaster> exportConsignmentData(@RequestParam("fromDate") String fromDate,@RequestParam("toDate") String toDate) {
		return superUserD2zService.exportConsignmentData(fromDate, toDate);
   }
	
	@RequestMapping( method = RequestMethod.GET, path = "/export/shipment")
	 public List<SenderdataMaster> exportShipmentData(@RequestParam("fromDate") String fromDate,@RequestParam("toDate") String toDate) {
		return superUserD2zService.exportShipmentData(fromDate, toDate);
   }
	@RequestMapping( method = RequestMethod.GET, path = "/track/etower/{trackingNumbers}")
    public ResponseMessage trackingCode(@PathVariable List<String> trackingNumbers) {
		return superUserD2zService.trackingEvent(trackingNumbers);
		
    }
	@RequestMapping( method = RequestMethod.POST, path = "/brokerRates", consumes=MediaType.APPLICATION_JSON)
    public UserMessage uploadBrokerRates(@RequestBody List<BrokerRatesData> brokerRatesData) {
		UserMessage successMsg = superUserD2zService.uploadBrokerRates(brokerRatesData);
		return successMsg;
    }
}
