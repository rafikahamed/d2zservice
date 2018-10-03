package com.d2z.d2zservice.controller;

import java.util.List;
import javax.validation.Valid;
import javax.ws.rs.core.MediaType;
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
import org.springframework.web.bind.annotation.RestController;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.exception.ReferenceNumberNotUniqueException;
import com.d2z.d2zservice.model.DropDownModel;
import com.d2z.d2zservice.model.FileUploadData;
import com.d2z.d2zservice.model.SenderData;
import com.d2z.d2zservice.model.SenderDataResponse;
import com.d2z.d2zservice.model.TrackParcel;
import com.d2z.d2zservice.model.TrackingDetails;
import com.d2z.d2zservice.model.UserMessage;
import com.d2z.d2zservice.service.ID2ZService;

@RestController
@Validated
@RequestMapping(value = "/v1/d2z")
public class D2zController {
	
	Logger logger = LoggerFactory.getLogger(D2zController.class);
	
	@Autowired
    private  ID2ZService d2zService;
	
	@RequestMapping( method = RequestMethod.GET, path = "/login")
    public String login(@RequestParam("userName") String userName, @RequestParam("passWord") String passWord) {
		return null;
    }
	
	@RequestMapping( method = RequestMethod.POST, path = "/consignment-fileUpload", consumes=MediaType.APPLICATION_JSON)
    public UserMessage consignmentFileUpload(@RequestBody List<FileUploadData> fileData) {
		UserMessage successMsg = d2zService.exportParcel(fileData);
		return successMsg;
    }
	
	@RequestMapping( method = RequestMethod.GET, path = "/consignment-fileList")
    public List<DropDownModel> fileList() {
		List<DropDownModel> fileList = d2zService.fileList();
		return fileList;
    }
	
	@RequestMapping( method = RequestMethod.GET, path = "/consignment-fileData")
    public List<SenderdataMaster> consignmentFileData(@RequestParam("fileName") String fileName) {
		List<SenderdataMaster> fileListData = d2zService.consignmentFileData(fileName);
		return fileListData;
    }
	
	@RequestMapping( method = RequestMethod.GET, path = "/manifest-data")
    public List<SenderdataMaster> fetchManifestData(@RequestParam("fileName") String fileName) {
		List<SenderdataMaster> fileListData = d2zService.fetchManifestData(fileName);
		return fileListData;
    }
	
	@RequestMapping( method = RequestMethod.POST, path = "/consignment-delete")
    public UserMessage consignmentDelete(@RequestBody String refrenceNumlist) {
		UserMessage fileDeleteMsg = d2zService.consignmentDelete(refrenceNumlist);
		return fileDeleteMsg;
    }
	
	@RequestMapping( method = RequestMethod.GET, path = "/manifest-creation")
    public UserMessage manifestCreation(@RequestParam("manifestNumber") String manifestNumber, @RequestParam("refrenceNumber") String refrenceNumber) {
		UserMessage manifestUpdateMsg = d2zService.manifestCreation(manifestNumber, refrenceNumber);
		return manifestUpdateMsg;
    }
	
	@RequestMapping( method = RequestMethod.POST, path = "/generateLabel")
	public ResponseEntity<byte[]> generateLabel(@RequestBody List<SenderData> senderData) {
		byte[] bytes = d2zService.generateLabel(senderData);
		System.out.println("Byte Response ----->");
		System.out.println(bytes.toString());
	    return ResponseEntity
	      .ok()
	      // Specify content type as PDF
	      .header("Content-Type", "application/pdf; charset=UTF-8")
	      // Tell browser to display PDF if it can
	      .header("Content-Disposition", "inline; filename=\"Label.pdf\"")
	      .body(bytes);
    }
	
	@RequestMapping( method = RequestMethod.GET, path = "/tracking-list")
    public List<TrackingDetails> trackingDetails(@RequestParam("fileName") String fileName) {
		List<TrackingDetails> trackingDetails = d2zService.trackingDetails(fileName);
		return trackingDetails;
	}
	
	@RequestMapping( method = RequestMethod.POST, path = "/tracking-label")
    public ResponseEntity<byte[]> trackingLabel(@RequestBody String refBarNum) {
		byte[] bytes = d2zService.trackingLabel(refBarNum);
		System.out.println("Byte Response ----->");
		System.out.println(bytes.toString());
	    return ResponseEntity
	      .ok()
	      // Specify content type as PDF
	      .header("Content-Type", "application/pdf; charset=UTF-8")
	      // Tell browser to display PDF if it can
	      .header("Content-Disposition", "inline; filename=\"Label.pdf\"")
	      .body(bytes);
	}

	@RequestMapping( method = RequestMethod.GET, path = "/trackParcel/referenceNumber/{referenceNumbers}")
    public List<TrackParcel> trackParcel(@PathVariable List<String> referenceNumbers) {
		List<TrackParcel> trackParcelResponse = d2zService.trackParcelByRefNbr(referenceNumbers);
		return trackParcelResponse;
    }
	@RequestMapping( method = RequestMethod.GET, path = "/trackParcel/articleID/{articleIDs}")
    public List<TrackParcel> trackParcelByArticleID(@PathVariable List<String> articleIDs) {
		List<TrackParcel> trackParcelResponse = d2zService.trackParcelByArticleID(articleIDs);
		return trackParcelResponse;
    }
	
	@RequestMapping(method = RequestMethod.POST, path = "/consignments-create")
	 public List<SenderDataResponse> createConsignments( @RequestBody List<@Valid SenderData> orderDetailList) throws ReferenceNumberNotUniqueException {
		List<SenderDataResponse> senderDataResponse = d2zService.createConsignments(orderDetailList);
		return senderDataResponse;
    }	

}