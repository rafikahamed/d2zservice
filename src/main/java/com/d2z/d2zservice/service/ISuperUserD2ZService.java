package com.d2z.d2zservice.service;

import java.util.List;

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
import com.d2z.d2zservice.model.InvoiceShipment;
import com.d2z.d2zservice.model.NotBilled;
import com.d2z.d2zservice.model.ReconcileData;
import com.d2z.d2zservice.model.ResponseMessage;
import com.d2z.d2zservice.model.UploadTrackingFileData;
import com.d2z.d2zservice.model.UserDetails;
import com.d2z.d2zservice.model.UserMessage;
import com.d2z.d2zservice.model.ExportDelete;
import com.d2z.d2zservice.model.ExportShipment;

public interface ISuperUserD2ZService{

	public UserMessage uploadTrackingFile(List<UploadTrackingFileData> fileData);

	public UserMessage uploadArrivalReport(List<ArrivalReportFileData> fileData);

	public List<DropDownModel> brokerCompanyDetails();

	public UserDetails fetchUserDetails(String companyName);

	public List<ExportDelete> exportDeteledConsignments(String fromDate, String toDate);

	List<SenderdataMaster> exportConsignmentData(String fromDate, String toDate);

	List<ExportShipment> exportShipmentData(String fromDate, String toDate);
	
	List<ExportShipment> exportNonShipmentData(String fromDate, String toDate);

	public ResponseMessage trackingEvent(List<String> trackingNumbers);

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

}
