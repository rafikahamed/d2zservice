package com.d2z.d2zservice.dao;

import java.util.List;

import com.d2z.d2zservice.entity.NonD2ZData;
import com.d2z.d2zservice.entity.Reconcile;
import com.d2z.d2zservice.entity.ReconcileND;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.entity.Trackandtrace;
import com.d2z.d2zservice.entity.User;
import com.d2z.d2zservice.model.ApprovedInvoice;
import com.d2z.d2zservice.model.ArrivalReportFileData;
import com.d2z.d2zservice.model.BrokerRatesData;
import com.d2z.d2zservice.model.D2ZRatesData;
import com.d2z.d2zservice.model.ResponseMessage;
import com.d2z.d2zservice.model.UploadTrackingFileData;
import com.d2z.d2zservice.model.UserMessage;
import com.d2z.d2zservice.model.etower.TrackingEventResponse;

public interface ID2ZSuperUserDao {

	List<Trackandtrace> uploadTrackingFile(List<UploadTrackingFileData> fileData);

	List<Trackandtrace> uploadArrivalReport(List<ArrivalReportFileData> fileData);

	List<String> brokerCompanyDetails();

	User fetchUserDetails(String companyName);

	List<String> exportDeteledConsignments(String fromDate, String toDate);

	List<SenderdataMaster> exportConsignments(String fromDate, String toDate);

	List<Object> exportShipment(String fromDate, String toDate);
	
	List<SenderdataMaster> exportNonShipment(String fromDate, String toDate);

	List<String> fetchTrackingNumbersForETowerCall();

	ResponseMessage insertTrackingDetails(TrackingEventResponse response);

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

	List<String> downloadInvoice(List<String> broker, List<String> airwayBill);

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

	List<String> downloadNonD2zInvoice(List<String> broker, List<String> airwayBill);

	UserMessage approveNdInvoiced(ApprovedInvoice approvedInvoice);

	List<NonD2ZData> brokerNdInvoiced();

	List<String> fetchNonD2zNotBilled();

}
