package com.d2z.d2zservice.controller;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.d2z.d2zservice.entity.NonD2ZData;
import com.d2z.d2zservice.entity.Reconcile;
import com.d2z.d2zservice.entity.ReconcileND;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.exception.ReferenceNumberNotUniqueException;
import com.d2z.d2zservice.model.ApprovedInvoice;
import com.d2z.d2zservice.model.ArrivalReportFileData;
import com.d2z.d2zservice.model.BrokerList;
import com.d2z.d2zservice.model.BrokerRatesData;
import com.d2z.d2zservice.model.BrokerShipmentList;
import com.d2z.d2zservice.model.D2ZRatesData;
import com.d2z.d2zservice.model.DownloadInvice;
import com.d2z.d2zservice.model.DropDownModel;
import com.d2z.d2zservice.model.ExportDelete;
import com.d2z.d2zservice.model.ExportShipment;
import com.d2z.d2zservice.model.InvoiceShipment;
import com.d2z.d2zservice.model.NotBilled;
import com.d2z.d2zservice.model.ReconcileData;
import com.d2z.d2zservice.model.ResponseMessage;
import com.d2z.d2zservice.model.UploadTrackingFileData;
import com.d2z.d2zservice.model.UserDetails;
import com.d2z.d2zservice.model.UserMessage;
import com.d2z.d2zservice.service.ISuperUserD2ZService;

@RestController
@Validated
@RequestMapping(value = "/v1/speedcouriers/api")
public class SPCourierAPIController {
	
	Logger logger = LoggerFactory.getLogger(SPCourierAPIController.class);
	
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

	/*@RequestMapping( method = RequestMethod.GET, path = "/export/delete")
	 public List<SenderdataMaster> exportDeteledConsignments(@RequestParam("fromDate") String fromDate,@RequestParam("toDate") String toDate) {
		return superUserD2zService.exportDeteledConsignments(fromDate, toDate);
    }*/
	@RequestMapping( method = RequestMethod.GET, path = "/export/delete")
	public List<ExportDelete> exportDeteledConsignments(@RequestParam("fromDate") String fromDate,@RequestParam("toDate") String toDate) {
		return superUserD2zService.exportDeteledConsignments(fromDate, toDate);
    }
	
	@RequestMapping( method = RequestMethod.GET, path = "/export/consignment")
	 public List<SenderdataMaster> exportConsignmentData(@RequestParam("fromDate") String fromDate,@RequestParam("toDate") String toDate) {
		return superUserD2zService.exportConsignmentData(fromDate, toDate);
   }
	
	@RequestMapping( method = RequestMethod.GET, path = "/export/shipment")
	 public List<ExportShipment> exportShipmentData(@RequestParam("fromDate") String fromDate,@RequestParam("toDate") String toDate) {
		return superUserD2zService.exportShipmentData(fromDate, toDate);
	}
	
	@RequestMapping( method = RequestMethod.GET, path = "/export/nonshipment")
	 public List<ExportShipment> exportNonShipmentData(@RequestParam("fromDate") String fromDate,@RequestParam("toDate") String toDate) {
		return superUserD2zService.exportNonShipmentData(fromDate, toDate);
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
	


}
