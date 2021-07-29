package com.d2z.d2zservice.service;

import java.util.List;
import java.util.Map;

import com.d2z.d2zservice.entity.Mlid;
import com.d2z.d2zservice.entity.NonD2ZData;
import com.d2z.d2zservice.entity.Reconcile;
import com.d2z.d2zservice.entity.ReconcileND;
import com.d2z.d2zservice.entity.Returns;
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
import com.d2z.d2zservice.model.ExportConsignment;
import com.d2z.d2zservice.model.ExportDelete;
import com.d2z.d2zservice.model.ExportShipment;
import com.d2z.d2zservice.model.HeldParcel;
import com.d2z.d2zservice.model.IncomingJobResponse;
import com.d2z.d2zservice.model.InvoiceShipment;
import com.d2z.d2zservice.model.InvoicingZonesModel;
import com.d2z.d2zservice.model.ManualInvoiceData;
import com.d2z.d2zservice.model.MasterPostCodeModel;
import com.d2z.d2zservice.model.NotBilled;
import com.d2z.d2zservice.model.OpenEnquiryResponse;
import com.d2z.d2zservice.model.ParcelResponse;
import com.d2z.d2zservice.model.PendingTrackingDetails;
import com.d2z.d2zservice.model.ProfitLossReport;
import com.d2z.d2zservice.model.ReconcileData;
import com.d2z.d2zservice.model.ResponseMessage;
import com.d2z.d2zservice.model.ReturnsAction;
import com.d2z.d2zservice.model.ReturnsClientResponse;
import com.d2z.d2zservice.model.ShipmentApproval;
import com.d2z.d2zservice.model.ShipmentCharges;
import com.d2z.d2zservice.model.UploadTrackingFileData;
import com.d2z.d2zservice.model.UserDetails;
import com.d2z.d2zservice.model.UserMessage;
import com.d2z.d2zservice.model.WeightUpload;
import com.d2z.d2zservice.model.Zone;
import com.d2z.d2zservice.model.ZoneRequest;

public interface ISuperUserD2ZService{

	public UserMessage uploadTrackingFile(List<UploadTrackingFileData> fileData);

	public UserMessage uploadArrivalReport(List<ArrivalReportFileData> fileData);

	public List<DropDownModel> brokerCompanyDetails();

	public UserDetails fetchUserDetails(String companyName);

	public List<ExportDelete> exportDeteledConsignments(String fromDate, String toDate);

	List<ExportConsignment> exportConsignmentData(String fromDate, String toDate);
	
	List<ExportConsignment> exportConsignmentDatafile(String type, List<String> Data);

	List<ExportShipment> exportShipmentData(String fromDate, String toDate);
	
	List<ExportShipment> exportShipmentDatafile(String type, List<String> Data);
	
	List<ExportShipment> exportNonShipmentData(String fromDate, String toDate);

	//public ResponseMessage trackingEvent(List<String> trackingNumbers);

	public UserMessage uploadBrokerRates(List<BrokerRatesData> brokerRatesData);

	public UserMessage uploadD2ZRates(List<D2ZRatesData> d2zRatesData);

	public List<BrokerList> brokerList();

	public List<DropDownModel> fetchMlidList();

	public List<BrokerShipmentList> brokerShipmentList();

	public List<InvoiceShipment> brokerShipment();

	public List<InvoiceShipment> brokerInvoiced();

	public UserMessage fetchReconcile(List<ReconcileData> reconcileData) throws ReferenceNumberNotUniqueException;

	public UserMessage approvedInvoice(ApprovedInvoice approvedInvoice);

	public List<NotBilled> fetchNotBilled();

	public List<DownloadInvice> downloadInvoice(List<String> broker, List<String> airwayBill, String billed, String invoiced);

	public UserMessage fetchNonD2zClient(List<NonD2ZData> nonD2zData) throws ReferenceNumberNotUniqueException;

	public List<Reconcile> downloadReconcile(List<String> reconcileNumbers);

	public List<DropDownModel> fetchNonD2zBrokerUserName();

	public UserMessage uploadReconcileNonD2z(List<ReconcileData> reconcileData) throws ReferenceNumberNotUniqueException;

	public List<ReconcileND> downloadNonD2zReconcile(List<String> nonD2zReconcileNumbers);

	public List<InvoiceShipment> brokerNonD2zShipment();

	public List<DownloadInvice> downloadNonD2zInvoice(List<String> broker,List<String> airwayBill,String billed,String invoiced);

	public UserMessage approveNdInvoiced(ApprovedInvoice approvedInvoice);

	public List<InvoiceShipment> brokerNdInvoiced();

	public List<NotBilled> fetchNonD2zNotBilled();
	
	public void scheduledTrackingEvent();
	
	public void scheduledPCATrackingEvent();

	public List<?> fetchApiLogs(String client, String fromDate, String toDate);

	public byte[] trackingLabel(List<String> refBarNumArray, String identifier);

	public UserMessage deleteMLID(String service);
	
	public List<Mlid> downloadMlid(String service);
	
	public UserMessage uploadMlid(List<Object> MlidData);
	
	public List<DropDownModel> fetchMlidDeleteList();
	
	public List<AUWeight> downloadAUWeight(List<Object> AUWeight);

	public List<OpenEnquiryResponse> fetchOpenEnquiryDetails();

	public UserMessage updateEnquiryDetails(List<OpenEnquiryResponse> openEnquiryDetails);

	public List<OpenEnquiryResponse> completedEnquiryDetails();

	public List<AddShipmentModel> incomingjobList();
	
	public UserMessage createJob(List<CreateJobRequest> createJob);
	
	public List<IncomingJobResponse> getJobList();
	
	
	public List<IncomingJobResponse> getcloseJobList();

	public ReturnsClientResponse fetchClientDetails(String referenceNumber, String barcodeLabel, String articleId);

	public UserMessage createReturns(List<Returns> returns) throws ReferenceNumberNotUniqueException;

	public UserMessage updateJob(List<IncomingJobResponse> job);
	
	public UserMessage deleteJob(List<IncomingJobResponse> job);

	public List<DropDownModel> fetchReturnsBroker();

	public List<Returns> returnsOutstanding(String fromDate, String toDate, String brokerName);

	public void triggerSC();

	public UserMessage submitJob(List<IncomingJobResponse> job);
	
	ResponseMessage trackingEvent(List<String> trackingNbrs, Map<String, String> map);

	public List<Returns> returnsOutstanding(int roleId);

	public UserMessage updateAction(List<ReturnsAction> returnsAction);

	public UserMessage uploadWeight(List<WeightUpload> weight);

	public ResponseMessage allocateShipment(String articleid, String shipmentNumber);

	public UserMessage createParcel(List<HeldParcel> createJob) throws ReferenceNumberNotUniqueException;

	public List<ParcelResponse> getParcelList(String client);
	
	public UserMessage updateParcel(List<ParcelResponse> parcel);

	public List<ParcelResponse> getParcelreleaseList(String client);

	public List<ShipmentCharges> shipmentCharges();

	public List<User> broker();

	public Zone zoneReport(List<ZoneRequest> zoneRequest);

	public UserMessage approveShiment(List<ShipmentApproval> shipmentApproval);

	public List<ProfitLossReport> profitLossReport(String fromDate, String toDate);

	UserMessage generateShipmentReport(List<IncomingJobResponse> jobs);

	public UserMessage uploadManualInvoice(List<ManualInvoiceData> fileData);

	public List<String> downloadFDMArticleIds();

	public List<PendingTrackingDetails> downloadPendingTracking();

	public UserMessage uploadMasterPostcode(List<MasterPostCodeModel> fileData);

	public UserMessage uploadInvoicingZones(List<InvoicingZonesModel> fileData);


}
