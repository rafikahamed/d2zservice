package com.d2z.d2zservice.controller;

import java.sql.Blob;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.sql.rowset.serial.SerialBlob;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.d2z.d2zservice.entity.Mlid;
import com.d2z.d2zservice.entity.NonD2ZData;
import com.d2z.d2zservice.entity.Reconcile;
import com.d2z.d2zservice.entity.ReconcileND;
import com.d2z.d2zservice.entity.Returns;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.entity.User;
import com.d2z.d2zservice.exception.ReferenceNumberNotUniqueException;
import com.d2z.d2zservice.model.AUWeight;
import com.d2z.d2zservice.model.AddShipmentModel;
import com.d2z.d2zservice.model.ApprovedInvoice;
import com.d2z.d2zservice.model.ArrivalReportFileData;
import com.d2z.d2zservice.model.BrokerList;
import com.d2z.d2zservice.model.BrokerRatesData;
import com.d2z.d2zservice.model.BrokerShipmentList;
import com.d2z.d2zservice.model.CreateJobRequest;
import com.d2z.d2zservice.model.D2ZRatesData;
import com.d2z.d2zservice.model.DownloadInvice;
import com.d2z.d2zservice.model.DropDownModel;
import com.d2z.d2zservice.model.Enquiry;
import com.d2z.d2zservice.model.EnquiryResponse;
import com.d2z.d2zservice.model.ExportConsignment;
import com.d2z.d2zservice.model.InvoiceShipment;
import com.d2z.d2zservice.model.NotBilled;
import com.d2z.d2zservice.model.OpenEnquiryResponse;
import com.d2z.d2zservice.model.ParcelResponse;
import com.d2z.d2zservice.model.ProfitLossReport;
import com.d2z.d2zservice.model.ReconcileData;
import com.d2z.d2zservice.model.ResponseMessage;
import com.d2z.d2zservice.model.ReturnsAction;
import com.d2z.d2zservice.model.ReturnsClientResponse;
import com.d2z.d2zservice.model.ShipmentApproval;
import com.d2z.d2zservice.model.ShipmentCharges;
import com.d2z.d2zservice.model.SuperUserEnquiry;
import com.d2z.d2zservice.model.UploadTrackingFileData;
import com.d2z.d2zservice.model.UserDetails;
import com.d2z.d2zservice.model.UserMessage;
import com.d2z.d2zservice.model.WeightUpload;
import com.d2z.d2zservice.model.Zone;
import com.d2z.d2zservice.model.ZoneRequest;
import com.d2z.d2zservice.model.ExportDelete;
import com.d2z.d2zservice.model.ExportShipment;
import com.d2z.d2zservice.model.HeldParcel;
import com.d2z.d2zservice.model.IncomingJobResponse;
import com.d2z.d2zservice.service.ID2ZService;
import com.d2z.d2zservice.service.ISuperUserD2ZService;

@RestController
@RequestMapping(value = "/v1/d2z/superUser-level")
public class D2ZSuperUserController {

	Logger logger = LoggerFactory.getLogger(D2zController.class);
	
	@Autowired
    private  ISuperUserD2ZService superUserD2zService;
	
	@Autowired
	private ID2ZService d2zService;

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

	/*@RequestMapping( method = RequestMethod.GET, path = "/export/delete")
	 public List<SenderdataMaster> exportDeteledConsignments(@RequestParam("fromDate") String fromDate,@RequestParam("toDate") String toDate) {
		return superUserD2zService.exportDeteledConsignments(fromDate, toDate);
    }*/
	@RequestMapping( method = RequestMethod.GET, path = "/export/delete")
	public List<ExportDelete> exportDeteledConsignments(@RequestParam("fromDate") String fromDate,@RequestParam("toDate") String toDate) {
		return superUserD2zService.exportDeteledConsignments(fromDate, toDate);
    }
	
	@RequestMapping( method = RequestMethod.GET, path = "/export/consignment")
	 public List<ExportConsignment> exportConsignmentData(@RequestParam("fromDate") String fromDate,@RequestParam("toDate") String toDate) {
		return superUserD2zService.exportConsignmentData(fromDate, toDate);
   }
	@RequestMapping( method = RequestMethod.GET, path = "/export/consignmentfile")
	 public List<ExportConsignment> exportConsignmentDataFile(@RequestParam("type") String type,@RequestParam("Data") List<String> Data) {
		System.out.println("in type:"+type);
		
		
		return superUserD2zService.exportConsignmentDatafile(type, Data);
		//return superUserD2zService.exportConsignmentData(fromDate, toDate);
  }
	
	@RequestMapping( method = RequestMethod.GET, path = "/export/shipment")
	 public List<ExportShipment> exportShipmentData(@RequestParam("fromDate") String fromDate,@RequestParam("toDate") String toDate) {
		return superUserD2zService.exportShipmentData(fromDate, toDate);
	}
	
	@RequestMapping( method = RequestMethod.GET, path = "/export/shipmentfile")
	 public List<ExportShipment> exportShipmentDataFile(@RequestParam("type") String type,@RequestParam("Data") List<String> Data) {
		return superUserD2zService.exportShipmentDatafile(type, Data);
	}
	@RequestMapping( method = RequestMethod.GET, path = "/export/nonshipment")
	 public List<ExportShipment> exportNonShipmentData(@RequestParam("fromDate") String fromDate,@RequestParam("toDate") String toDate) {
		return superUserD2zService.exportNonShipmentData(fromDate, toDate);
	}
	@RequestMapping( method = RequestMethod.GET, path = "/track/etower/{trackingNumbers}")
    public ResponseMessage trackingCode(@PathVariable List<String> trackingNumbers) {
		return null;//superUserD2zService.trackingEvent(trackingNumbers);
		
    }
	@RequestMapping( method = RequestMethod.POST, path = "/brokerRates", consumes=MediaType.APPLICATION_JSON)
    public UserMessage uploadBrokerRates(@RequestBody List<BrokerRatesData> brokerRatesData) {
		UserMessage successMsg = superUserD2zService.uploadBrokerRates(brokerRatesData);
		return successMsg;
    }
	@RequestMapping( method = RequestMethod.POST, path = "/d2zRates", consumes=MediaType.APPLICATION_JSON)
    public UserMessage uploadD2ZRates(@RequestBody List<D2ZRatesData> d2zRatesData) {
		UserMessage successMsg = superUserD2zService.uploadD2ZRates(d2zRatesData);
		return successMsg;
    }
	
	@RequestMapping( method = RequestMethod.GET, path = "/brokerList",  produces = "application/json")
    public List<BrokerList> brokerList() {
		List<BrokerList> brokerList = superUserD2zService.brokerList();
		return brokerList;
    }
	

	@RequestMapping( method = RequestMethod.GET, path = "/incomingList",  produces = "application/json")
    public List<AddShipmentModel> incomingjobList() {
		List<AddShipmentModel> jobList = superUserD2zService.incomingjobList();
		return jobList;
    }
	
	@RequestMapping( method = RequestMethod.GET, path = "/broker-shipmentList")
    public List<BrokerShipmentList> brokerShipmentList() {
		List<BrokerShipmentList> brokerList = superUserD2zService.brokerShipmentList();
		return brokerList;
    }
	
	@RequestMapping( method = RequestMethod.GET, path = "/broker-shipment")
    public List<InvoiceShipment> brokerShipment() {
		List<InvoiceShipment> brokerList = superUserD2zService.brokerShipment();
		return brokerList;
    }
	
	@RequestMapping( method = RequestMethod.GET, path = "/broker-nonD2z-shipment")
    public List<InvoiceShipment> brokerNonD2zShipment() {
		List<InvoiceShipment> ndBrokerList = superUserD2zService.brokerNonD2zShipment();
		return ndBrokerList;
    }
	
	@RequestMapping( method = RequestMethod.GET, path = "/broker-Invoiced")
    public List<InvoiceShipment> brokerInvoiced() {
		List<InvoiceShipment> brokerList = superUserD2zService.brokerInvoiced();
		return brokerList;
    }
	
	@RequestMapping( method = RequestMethod.GET, path = "/broker-nonD2z-Invoiced")
    public List<InvoiceShipment> brokerNdInvoiced() {
		List<InvoiceShipment> ndBrokerList = superUserD2zService.brokerNdInvoiced();
		return ndBrokerList;
    }
	
	@RequestMapping( method = RequestMethod.PUT, path = "/approve-Invoice")
    public UserMessage approveInvoiced(@RequestBody ApprovedInvoice approvedInvoice) {
		UserMessage approvedMsg = superUserD2zService.approvedInvoice(approvedInvoice);
		return approvedMsg;
    }
	
	@RequestMapping( method = RequestMethod.PUT, path = "/approve-NonD2z-Invoice")
    public UserMessage approveNdInvoiced(@RequestBody ApprovedInvoice approvedInvoice) {
		UserMessage approvedMsg = superUserD2zService.approveNdInvoiced(approvedInvoice);
		return approvedMsg;
    }
	
	@RequestMapping( method = RequestMethod.GET, path = "/mlidList")
    public List<DropDownModel> fetchMlidList() {
		return superUserD2zService.fetchMlidList();
    }
	
	@RequestMapping( method = RequestMethod.POST, path = "/reconcileInfo")
    public UserMessage fetchReconcile(@RequestBody List<ReconcileData> reconcileData) throws ReferenceNumberNotUniqueException {
		return superUserD2zService.fetchReconcile(reconcileData);
    }
	
	@RequestMapping( method = RequestMethod.POST, path = "/reconcileInfo-NonD2z")
    public UserMessage uploadReconcileNonD2z(@RequestBody List<ReconcileData> reconcileData) throws ReferenceNumberNotUniqueException {
		return superUserD2zService.uploadReconcileNonD2z(reconcileData);
    }
	
	@RequestMapping( method = RequestMethod.POST, path = "/download-reconcile")
    public List<Reconcile> downloadReconcile(@RequestBody List<String> reconcileNumbers) {
		return superUserD2zService.downloadReconcile(reconcileNumbers);
    }
	
	@RequestMapping( method = RequestMethod.POST, path = "/download-non-d2z-reconcile")
    public List<ReconcileND> downloadNonD2zReconcile(@RequestBody List<String> nonD2zReconcileNumbers) {
		return superUserD2zService.downloadNonD2zReconcile(nonD2zReconcileNumbers);
    }
	
	@RequestMapping( method = RequestMethod.GET, path = "/not-billed")
    public List<NotBilled> fetchNotBilled() {
		return superUserD2zService.fetchNotBilled();
    }
	
	@RequestMapping( method = RequestMethod.GET, path = "/nd-Not-billed")
    public List<NotBilled> fetchNonD2zNotBilled() {
		return superUserD2zService.fetchNonD2zNotBilled();
    }
	
	@RequestMapping( method = RequestMethod.GET, path = "/download-Invoice")
    public List<DownloadInvice> downloadInvoice(@RequestParam("broker") List<String> broker, @RequestParam("airwayBill") List<String> airwayBill,
    		@RequestParam("billed") String billed, @RequestParam("invoiced") String invoiced) {
		return superUserD2zService.downloadInvoice(broker, airwayBill, billed, invoiced);
    }
	
	@RequestMapping( method = RequestMethod.GET, path = "/shipment-Charge")
    public List<ShipmentCharges> shipmentCharges() {
		return superUserD2zService.shipmentCharges();
    }
	
	@RequestMapping( method = RequestMethod.PUT, path = "/approve-shipment")
    public UserMessage approveShiment(@RequestBody List<ShipmentApproval> shipmentApproval) {
		UserMessage approvedMsg = superUserD2zService.approveShiment(shipmentApproval);
		return approvedMsg;
    }
	
	@RequestMapping( method = RequestMethod.GET, path = "/download-nonD2z-Invoice")
    public List<DownloadInvice> downloadNonD2zInvoice(@RequestParam("broker") List<String> broker, @RequestParam("airwayBill") List<String> airwayBill,
    		@RequestParam("billed") String billed, @RequestParam("invoiced") String invoiced) {
		return superUserD2zService.downloadNonD2zInvoice(broker, airwayBill, billed, invoiced);
    }
	
	@RequestMapping( method = RequestMethod.POST, path = "/Non-D2Z-Client")
    public UserMessage fetchNonD2zClient(@RequestBody List<NonD2ZData> nonD2zData) throws ReferenceNumberNotUniqueException{
		return superUserD2zService.fetchNonD2zClient(nonD2zData);
    }
	
	@RequestMapping( method = RequestMethod.GET, path = "/Non-D2z-Broker")
    public List<DropDownModel> fetchNonD2zBrokerUserName(){
		return superUserD2zService.fetchNonD2zBrokerUserName();
    }
	
	@RequestMapping( method = RequestMethod.GET, path = "/d2z-apiLogs")
    public List<?> fetchApiLogs(@RequestParam("client") String client, @RequestParam("fromDate") String fromDate,
    		@RequestParam("toDate") String toDate){
		return superUserD2zService.fetchApiLogs(client, fromDate, toDate);
    }
	
	@RequestMapping(method = RequestMethod.POST, path = "/tracking-label")
	public ResponseEntity<byte[]> trackingLabel(@RequestBody String refBarNum) {
		System.out.println("Incoming refBarNum :: " + refBarNum);
		List<String> refBarNumArray = Stream.of(refBarNum.split(",")).collect(Collectors.toList());
		byte[] bytes = superUserD2zService.trackingLabel(refBarNumArray);
		return ResponseEntity.ok()
				// Specify content type as PDF
				.header("Content-Type", "application/pdf; charset=UTF-8")
				// Tell browser to display PDF if it can
				.header("Content-Disposition", "inline; filename=\"Label.pdf\"").body(bytes);
	}
	
	@RequestMapping( method = RequestMethod.GET, path = "/deleteMLID")
    public UserMessage deleteMLID(@RequestParam("service") String service) {
		UserMessage successMsg = superUserD2zService.deleteMLID(service);
		return successMsg;
    }
	

	@RequestMapping( method = RequestMethod.GET, path = "/downloadMLID")
    public List<Mlid> downloadMLID(@RequestParam("service") String service) {
		
		return superUserD2zService.downloadMlid(service);
    }
	
	@RequestMapping( method = RequestMethod.POST, path = "/addMLID", consumes=MediaType.APPLICATION_JSON)
    public UserMessage addMLID(@RequestBody List<Object> MlidData) {
		UserMessage successMsg = superUserD2zService.uploadMlid(MlidData);
		return successMsg;
    }

	@RequestMapping( method = RequestMethod.GET, path = "/mliddeleteList")
    public List<DropDownModel> fetchMlidDeleteList() {
		return superUserD2zService.fetchMlidDeleteList();
    }
	
	@RequestMapping( method = RequestMethod.POST, path = "/downloadAUweight", consumes=MediaType.APPLICATION_JSON)
    public List<AUWeight> downloadAU(@RequestBody List<Object> ArticleID) {
		return superUserD2zService.downloadAUWeight(ArticleID);
    }
	
	@RequestMapping( method = RequestMethod.GET, path = "/open-enquiry")
    public List<OpenEnquiryResponse> fetchOpenEnquiryDetails() {
		return superUserD2zService.fetchOpenEnquiryDetails();
    }
	
	@RequestMapping( method = RequestMethod.PUT, path = "/Update-enquiry", consumes=MediaType.APPLICATION_JSON)
    public UserMessage updateEnquiryDetails(@RequestBody List<OpenEnquiryResponse> openEnquiryDetails) {
		return superUserD2zService.updateEnquiryDetails(openEnquiryDetails);
    }
	
	@RequestMapping( method = RequestMethod.GET, path = "/completed-enquiry")
    public List<OpenEnquiryResponse> completedEnquiryDetails() {
		return superUserD2zService.completedEnquiryDetails();
    }
	

	@RequestMapping(method = RequestMethod.POST, path = "/create-job")
	public UserMessage createJob(@RequestBody List<CreateJobRequest> createJob) {
		UserMessage jobInfo = superUserD2zService.createJob(createJob);
		return jobInfo;
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "/held-parcel")
	public UserMessage createParcel(@RequestBody List<HeldParcel> createJob) {
		UserMessage jobInfo = superUserD2zService.createParcel(createJob);
		return jobInfo;
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "/update-job")
	public UserMessage updateJob(@RequestBody List<IncomingJobResponse> Job) {
		UserMessage jobInfo = superUserD2zService.updateJob(Job);
		return jobInfo;
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "/update-parcel")
	public UserMessage updateParcel(@RequestBody List<ParcelResponse> Job) {
		UserMessage jobInfo = superUserD2zService.updateParcel(Job);
		return jobInfo;
	}
	@RequestMapping(method = RequestMethod.POST, path = "/delete-job")
	public UserMessage deleteJob(@RequestBody List<IncomingJobResponse> Job) {
		UserMessage jobInfo = superUserD2zService.deleteJob(Job);
		return jobInfo;
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "/submit-job")
	public UserMessage submitJob(@RequestBody List<IncomingJobResponse> Job) {
		UserMessage jobInfo = superUserD2zService.submitJob(Job);
		return jobInfo;
	}
	@RequestMapping(method = RequestMethod.GET, path = "/incoming-job-list")
	public List<IncomingJobResponse> createJobList() {
		List<IncomingJobResponse> jobInfo = superUserD2zService.getJobList();
		return jobInfo;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/incoming-parcel-list/{client}")
	public List<ParcelResponse> createParcelList(@PathVariable String client) {
		List<ParcelResponse> jobInfo = superUserD2zService.getParcelList(client);
		return jobInfo;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/incoming-parcel-releaselist/{client}")
	public List<ParcelResponse> createParcelreleaseList(@PathVariable String client) {
		List<ParcelResponse> jobInfo = superUserD2zService.getParcelreleaseList(client);
		return jobInfo;
	}
	@RequestMapping(method = RequestMethod.GET, path = "/closing-job-list")
	public List<IncomingJobResponse> closeJobList() {
		List<IncomingJobResponse> jobInfo = superUserD2zService.getcloseJobList();
		return jobInfo;
	}
	
	@RequestMapping( method = RequestMethod.GET, path = "/clientDetails")
    public ReturnsClientResponse fetchClientDetails(@RequestParam("referenceNumber") String referenceNumber,
    	@RequestParam("barcodeLabel") String barcodeLabel, @RequestParam("articleId") String articleId) {
		return superUserD2zService.fetchClientDetails(referenceNumber,barcodeLabel,articleId);
    }
	
	@RequestMapping( method = RequestMethod.POST, path = "/create-returns")
    public UserMessage createReturns(@RequestBody List<Returns> returns) {
		return superUserD2zService.createReturns(returns);
    }
	
	@RequestMapping( method = RequestMethod.GET, path = "/returns-broker")
    public List<DropDownModel> fetchReturnsBroker() {
		return superUserD2zService.fetchReturnsBroker();
    }
	
	@RequestMapping(method = RequestMethod.GET, path = "/outStanding-returns")
	public List<Returns> returnsOutstanding(@RequestParam("fromDate") String fromDate, @RequestParam("toDate") String toDate, @RequestParam("brokerName") String brokerName) {
		return superUserD2zService.returnsOutstanding(fromDate, toDate, brokerName);
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/action-returns")
	public List<Returns> returnsActionDetails() {
		return superUserD2zService.returnsOutstanding();
	}
	
	@RequestMapping(method = RequestMethod.PUT, path = "/action")
	public UserMessage updateAction(@RequestBody List<ReturnsAction> returnsAction) {
		return superUserD2zService.updateAction(returnsAction);
	}
	
	
	@RequestMapping(method = RequestMethod.POST, path = "/weight",consumes=MediaType.APPLICATION_JSON)
	public UserMessage updateWeight(@RequestBody List<WeightUpload> weight) {
		return superUserD2zService.uploadWeight(weight);
	}
	
	@RequestMapping(method = RequestMethod.PUT, path = "/allocate-shipment/{shipmentNumber}")
	 public ResponseMessage allocateShipment(@RequestBody String articleid, @PathVariable String shipmentNumber)  {
		return  superUserD2zService.allocateShipment(articleid,shipmentNumber);
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/tracking-status")
	public UserMessage updateTrackingStatus() {
		superUserD2zService.triggerSC();
		UserMessage userMsg = new UserMessage();
		userMsg.setMessage("Tracking Status Updated Successfully");
		return userMsg;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/broker")
	public List<User> broker() {
		return superUserD2zService.broker();
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "/zone-report")
	public Zone zoneReport(@RequestBody List<ZoneRequest> zoneRequest) {
		return superUserD2zService.zoneReport(zoneRequest);
	}
	
	@RequestMapping(value = "/enquiryPodFile/{ticketNumber}/{comments}/{d2zComments}/{sendUpdate}/{status}", method = RequestMethod.POST)
	public UserMessage enquiryFileUpload(@RequestParam("file") MultipartFile file, 
			@PathVariable String ticketNumber, @PathVariable String comments,
			@PathVariable String d2zComments, @PathVariable String sendUpdate, @PathVariable String status) throws Exception {
		Blob blob = null;
		byte[] myArray = file.getBytes();
        blob = new SerialBlob(myArray);
        String ticketNum = ticketNumber != null ? ticketNumber : "";
        String cmts = comments != null ? comments : "";
        String d2zCmts = d2zComments != null ? d2zComments : "";
        String update = sendUpdate != null ? sendUpdate : "";
        String sts = status != null ? status : "";
        UserMessage successMsg = d2zService.enquiryFileUpload(blob, ticketNum,cmts,d2zCmts,update,sts,file.getOriginalFilename());
	    return successMsg;
	}  
	
	@RequestMapping(value = "/enquiryPod/{ticketNumber}/{comments}/{d2zComments}/{sendUpdate}/{status}", method = RequestMethod.POST)
	public UserMessage enquiryFileUpload(
			@PathVariable String ticketNumber, @PathVariable String comments,
			@PathVariable String d2zComments, @PathVariable String sendUpdate, @PathVariable String status) throws Exception {
		 	String ticketNum = ticketNumber != null ? ticketNumber : "";
	        String cmts = comments != null ? comments : "";
	        String d2zCmts = d2zComments != null ? d2zComments : "";
	        String update = sendUpdate != null ? sendUpdate : "";
	        String sts = status != null ? status : "";
        UserMessage successMsg = d2zService.enquiryFileUpload(null, ticketNum,cmts,d2zCmts,update,sts,null);
	    return successMsg;
	}  
	
	@RequestMapping(method = RequestMethod.PUT, path = "/enquiry/update")
	public UserMessage enquiryFileUpload(@RequestBody List<SuperUserEnquiry> enquiry) throws Exception {
        UserMessage successMsg = d2zService.enquiryFileUpload(enquiry);
	    return successMsg;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/profit-loss")
	public List<ProfitLossReport> profitLossReport(
			@RequestParam("fromDate") String fromDate, @RequestParam("toDate") String toDate) {
		return superUserD2zService.profitLossReport(fromDate,toDate);
	}
	
}
