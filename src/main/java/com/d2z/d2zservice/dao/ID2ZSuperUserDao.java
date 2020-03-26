package com.d2z.d2zservice.dao;

import java.util.List;
import java.util.Map;
import com.d2z.d2zservice.entity.AUPostResponse;
import com.d2z.d2zservice.entity.CSTickets;
import com.d2z.d2zservice.entity.ETowerResponse;
import com.d2z.d2zservice.entity.FFResponse;
import com.d2z.d2zservice.entity.IncomingJobs;
import com.d2z.d2zservice.entity.IncomingJobsLogic;
import com.d2z.d2zservice.entity.Mlid;
import com.d2z.d2zservice.entity.NonD2ZData;
import com.d2z.d2zservice.entity.Reconcile;
import com.d2z.d2zservice.entity.ReconcileND;
import com.d2z.d2zservice.entity.Returns;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.entity.Trackandtrace;
import com.d2z.d2zservice.entity.User;
import com.d2z.d2zservice.model.AUWeight;
import com.d2z.d2zservice.model.ApprovedInvoice;
import com.d2z.d2zservice.model.ArrivalReportFileData;
import com.d2z.d2zservice.model.BrokerRatesData;
import com.d2z.d2zservice.model.CreateJobRequest;
import com.d2z.d2zservice.model.D2ZRatesData;
import com.d2z.d2zservice.model.HeldParcel;
import com.d2z.d2zservice.model.IncomingJobResponse;
import com.d2z.d2zservice.model.OpenEnquiryResponse;
import com.d2z.d2zservice.model.PCATrackEventResponse;
import com.d2z.d2zservice.model.PFLTrackingResponseDetails;
import com.d2z.d2zservice.model.ParcelResponse;
import com.d2z.d2zservice.model.ProfitLossReport;
import com.d2z.d2zservice.model.ResponseMessage;
import com.d2z.d2zservice.model.ReturnsAction;
import com.d2z.d2zservice.model.ShipmentApproval;
import com.d2z.d2zservice.model.ShipmentCharges;
import com.d2z.d2zservice.model.SurplusData;
import com.d2z.d2zservice.model.UploadTrackingFileData;
import com.d2z.d2zservice.model.UserMessage;
import com.d2z.d2zservice.model.WeightUpload;
import com.d2z.d2zservice.model.Zone;
import com.d2z.d2zservice.model.ZoneRequest;
import com.d2z.d2zservice.model.auspost.TrackingResponse;
import com.d2z.d2zservice.model.etower.TrackingEventResponse;

public interface ID2ZSuperUserDao {

	List<Trackandtrace> uploadTrackingFile(List<UploadTrackingFileData> fileData);

	List<Trackandtrace> uploadArrivalReport(List<ArrivalReportFileData> fileData);

	List<String> brokerCompanyDetails();

	User fetchUserDetails(String companyName);

	List<String> exportDeteledConsignments(String fromDate, String toDate);

	List<Object> exportConsignments(String fromDate, String toDate);
	
	List<Object> exportConsignmentsfile(String Type, List<String> Data);

	List<Object> exportShipment(String fromDate, String toDate);
	
	List<Object> exportShipmentfile(String Type, List<String> Data);
	
	List<Object> exportNonShipment(String fromDate, String toDate);

	Map<String,String> fetchTrackingNumbersForETowerCall();
	
	List<String> fetchTrackingNumbersForPCACall();

	ResponseMessage insertTrackingDetails(TrackingEventResponse response, Map<String, String> map);

	public String uploadBrokerRates(List<BrokerRatesData> brokerRatesData);

	String uploadD2ZRates(List<D2ZRatesData> d2zRatesData);

	List<User> brokerList();

	List<String> fetchMlidList();

	List<String> brokerShipmentList(int user_Id);

	List<String> brokerShipment();

	List<Integer> fetchBrokerClientIds();

	List<String> brokerInvoiced();

	List<String> reconcileData(String articleNo, String referenceNumber);

	List<Reconcile> reconcileUpdate(List<Reconcile> reconcileCalculatedList);

	List<Reconcile> fetchReconcileData(List<String> reconcileReferenceNum);

	UserMessage approvedInvoice(ApprovedInvoice approvedInvoice);

	void reconcilerates(List<String> reconcileReferenceNum);

	List<String> fetchNotBilled();

	List<String> downloadInvoice(List<String> broker, List<String> airwayBill, String billed, String invoiced);

	UserMessage fetchNonD2zClient(List<NonD2ZData> nonD2zData);

	List<String> fetchAllArticleId();

	List<Reconcile> downloadReconcile();

	List<Reconcile> downloadReconcile(List<String> reconcileNumbers);

	List<String> fetchNonD2zBrokerUserName();

	NonD2ZData reconcileNonD2zData(String articleNo);

	List<ReconcileND> reconcileNonD2zUpdate(List<ReconcileND> reconcileNoND2zList);

	void reconcileratesND(List<String> reconcileArticleIdNum);

	List<ReconcileND> downloadNonD2zReconcile(List<String> nonD2zReconcileNumbers);

	List<NonD2ZData> brokerNonD2zShipment();

	List<String> downloadNonD2zInvoice(List<String> broker, List<String> airwayBill, String billed, String invoiced);

	UserMessage approveNdInvoiced(ApprovedInvoice approvedInvoice);

	List<NonD2ZData> brokerNdInvoiced();

	List<String> fetchNonD2zNotBilled();

	NonD2ZData reconcileNonD2zFreipostData(String referenceNumber);

	List<String> fetchAllReconcileReferenceNumbers();

	List<String> fetchAllReconcileArticleIdNumbers();

	List<String> fetchAllReconcileNonD2zReferenceNumbers();

	List<String> fetchAllReconcileNonD2zArticleIdNumbers();

	List<ETowerResponse> fetchEtowerLogResponse(String fromDate, String toDate);
	
	List<ETowerResponse> fetchEtowerLogResponseApi(List<String> api ,String fromDate, String toDate);


	List<AUPostResponse> fetchAUPosLogtResponse(String fromDate, String toDate);

	List<FFResponse> fetchFdmLogResponse(String fromDate, String toDate);

	List<FFResponse> fetchFreiPostResponseResponse(String fromDate, String toDate);

	List<String> trackingLabel(List<String> refBarNumArray);

	UserMessage deleteMlid(String service);
	
	List<Mlid> downloadMlid(String service);
	
	UserMessage addMlid(List<Object> MlidData);
 
	List<String> fetchMlidDeleteList();
 
	List<AUWeight> downloadAUweight(List<Object> ArticleID);

	String fetchUserById(int parseInt);

	List<OpenEnquiryResponse> fetchOpenEnquiryDetails();

	String updateEnquiryDetails(List<OpenEnquiryResponse> openEnquiryDetails);

	List<CSTickets> completedEnquiryDetails();
	
	List<IncomingJobsLogic> getBrokerMlidDetails();
	
	List<IncomingJobResponse> getJobList();
	
	List<IncomingJobResponse> getClosedJobList();

	String createEnquiry(List<CreateJobRequest> createJob);

	List<String> fetchClientDetails(String referenceNumber, String barcodeLabel, String articleId);

	String createReturns(List<Returns> returnsList);

	String updateJob(List<IncomingJobResponse> job);
	
	String deleteJob(List<IncomingJobResponse> job);

	List<String> fetchReturnsBroker();

	List<Returns> returnsOutstanding(String fromDate, String toDate, String brokerName);

	public List<String> fetchServiceTypeByUserName(String userName);

	List<CSTickets> fetchCSTickets();

	ResponseMessage updateAUCSTrackingDetails(TrackingResponse auTrackingDetails);

	ResponseMessage updateAUEtowerTrackingDetails(TrackingEventResponse response);

	String fetchBarcodeLabel(String articleID);

	ResponseMessage updatePFLTrackingDetails(List<PFLTrackingResponseDetails> pflTrackingDetails);
	
	String submitJob(List<IncomingJobResponse> job);

	List<Returns> returnsOutstanding(int roleId);

	UserMessage updateAction(List<ReturnsAction> returnsAction);

	UserMessage uploadweight(List<WeightUpload> weight);

	List<String> fetchRefnobyArticle(List<String> articleid);
	
	public List<SenderdataMaster> fetchConsignmentsByRefNbr(List<String> refNbrs);
	
	public List<SenderdataMaster> fetchDataBasedonSupplier(List<String> incomingRefNbr, String string);
	
	public List<String> fetchDataforPFLSubmitOrder(String[] refNbrs);
	
	public String allocateShipment(String referenceNumbers, String shipmentNumber);
	
	public List<String> fetchDataForEtowerForeCastCall(String[] refNbrs);

	String updateinvoicing(String toAllocate, String shipmentNumber);

	String createParcel(List<HeldParcel> createJob);

	List<ParcelResponse> getParcelList(String client);

	String updateParcel(List<ParcelResponse> parcel);

	List<ParcelResponse> getParcelReleaseList(String client);
	
	void updateReturnInvoice(Returns returnVal);

	List<ShipmentCharges> shipmentCharges();

	List<User> broker();

	Zone zoneReport(List<ZoneRequest> zoneRequest);

	UserMessage approveShiment(List<ShipmentApproval> shipmentApproval);

	void updateAirwayBill(String referenceNumbers, String shipmentNumber);

	String fetchServiceTypeByRefNbr(String string);

	String fetchServiceTypeByMlid(String string);

	List<ProfitLossReport> profitLossReport(String fromDate, String toDate);

	List<Returns> fetchAllReferenceNumber();

	ResponseMessage updatePCATrackingDetails(List<PCATrackEventResponse> responseList);

	List<String> fetchMlidsBasedOnSupplier(String string);

	IncomingJobs fetchJobsByMAWB(String mawb);

	List<SurplusData> fetchSurplusData(String mawb);

}

