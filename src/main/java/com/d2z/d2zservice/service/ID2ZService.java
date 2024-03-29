package com.d2z.d2zservice.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.validation.Valid;
import com.d2z.d2zservice.entity.CSTickets;
import com.d2z.d2zservice.entity.Returns;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.exception.FailureResponseException;
import com.d2z.d2zservice.exception.PCAlabelException;
import com.d2z.d2zservice.exception.ReferenceNumberNotUniqueException;
import com.d2z.d2zservice.model.APIRatesRequest;
import com.d2z.d2zservice.model.ClientDashbaord;
import com.d2z.d2zservice.model.CreateConsignmentRequest;
import com.d2z.d2zservice.model.DeleteConsignmentRequest;
import com.d2z.d2zservice.model.DropDownModel;
import com.d2z.d2zservice.model.Ebay_ShipmentDetails;
import com.d2z.d2zservice.model.EditConsignmentRequest;
import com.d2z.d2zservice.model.Enquiry;
import com.d2z.d2zservice.model.EnquiryResponse;
import com.d2z.d2zservice.model.EnquiryUpdate;
import com.d2z.d2zservice.model.ParcelStatus;
import com.d2z.d2zservice.model.PostCodeWeight;
import com.d2z.d2zservice.model.ResponseMessage;
import com.d2z.d2zservice.model.ReturnsAction;
import com.d2z.d2zservice.model.SenderData;
import com.d2z.d2zservice.model.SenderDataApi;
import com.d2z.d2zservice.model.SenderDataResponse;
import com.d2z.d2zservice.model.ShipmentDetails;
import com.d2z.d2zservice.model.ShippingQuoteRequest;
import com.d2z.d2zservice.model.SuperUserEnquiry;
import com.d2z.d2zservice.model.TrackParcel;
import com.d2z.d2zservice.model.TrackParcelResponse;
import com.d2z.d2zservice.model.TrackingDetails;
import com.d2z.d2zservice.model.UserDetails;
import com.d2z.d2zservice.model.UserMessage;

public interface ID2ZService {
	
	public List<SenderDataResponse> exportParcel(List<SenderData> orderDetailList, List<String> autoShipRefNbrs) 
			throws ReferenceNumberNotUniqueException, FailureResponseException;

	public List<DropDownModel> fileList(Integer userId);
	
	public List<DropDownModel> labelFileList(Integer userId);

	public List<SenderdataMaster> consignmentFileData(String fileName);

	public byte[] generateLabel(List<SenderData> senderData);

	public UserMessage consignmentDelete(String refrenceNumlist);

	public List<TrackingDetails> trackingDetails(String fileName);

	public byte[] trackingLabel(List<String> refBarNum, String identifier) throws PCAlabelException;

	public UserMessage manifestCreation(String manifestNumber, String refrenceNumber);

	List<TrackParcel> trackParcelByRefNbr(List<String> referenceNumbers);

	public List<SenderDataResponse> createConsignments(CreateConsignmentRequest orderDetail, List<String> autoShipRefNbrs) 
			throws ReferenceNumberNotUniqueException, FailureResponseException;

	public List<TrackParcel> trackParcelByArticleID(List<String> articleIDs);

	public ResponseMessage editConsignments(List<EditConsignmentRequest> requestList);

	public ResponseMessage allocateShipment(String referenceNumbers, String shipmentNumber) throws ReferenceNumberNotUniqueException;
	
	public ResponseMessage allocateShipmentArticleid(String referenceNumbers,String shipmentNumber) throws ReferenceNumberNotUniqueException;
	public UserMessage addUser(UserDetails userDetails);

	public UserMessage updateUser(UserDetails userDetails);
	
	public UserMessage addUserService(String userName ,String ServiceType );

	public UserMessage deleteUser(String companyName, String roleId);

	public List<SenderdataMaster> fetchManifestData(String fileName);

	public UserDetails login(String userName, String passWord);

	public List<ShipmentDetails> downloadShipmentData(String shipmentNumber, Integer userId);

	public List<ParcelStatus> getStatusByRefNbr(List<String> referenceNumbers);

	public List<ParcelStatus> getStatusByArticleID(List<String> articleIDs);

	public UserMessage uploadShipmentDetailsToEbay(Ebay_ShipmentDetails shipmentDetails);

	public ClientDashbaord clientDahbaord(Integer userId);

	public UserMessage deleteConsignments(@Valid DeleteConsignmentRequest request) throws ReferenceNumberNotUniqueException;

	public List<PostCodeWeight> getRates(@Valid APIRatesRequest request);

	public UserMessage contactUs(String email, String message, String name, String subject);

	public void triggerFreipost(String referenceNumbers);

	public void triggerFDM(List<String> refnbrs);

	public ResponseMessage auTrackingEvent();
	
	public void makeCalltoAusPost();

	public void updateRates();

	public void makeCallToEtowerBasedonSupplierUI(List<String> refNbr);

	public void freipostTrackingEvent();

	public EnquiryResponse createEnquiry(Enquiry createEnquiry) throws ReferenceNumberNotUniqueException;

	public List<CSTickets> fetchEnquiry(String status, String fromDate, String toDate, String userId);

	public List<CSTickets> fetchCompletedEnquiry(String userId);

	public List<Integer> fetchUserId(String userId);

	public void makeCalltoAusPost(List<String> referenceNumbers);
	
	public void currencyRate();

	void updateCubicWeight();

	public List<Returns> returnsOutstanding(String fromDate, String toDate, String userId);

	public List<ShipmentDetails> downloadShipmentDatabyType(List<String> number, Integer userId, String type);

	public List<ShipmentDetails> downloadShipmentDataTemplate(String shipmentNumber, Integer userId);

	public List<ShipmentDetails> downloadShipmentDataTemplatebyType(List<String> number, Integer userId, String type);

	public UserMessage returnAction(List<ReturnsAction> returnsAction);
	
	public List<TrackParcelResponse> trackParcels(List<String> articleIds) throws InterruptedException, ExecutionException;

	public UserMessage enquiryFileUpload(byte[] blob, String filename, String ticketNumber);

	public UserMessage enquiryFileUpload(List<SuperUserEnquiry> enquiry);

	public EnquiryResponse enquiryClientUpdate(EnquiryUpdate updateEnquiry);
	
	public UserMessage enquiryUpdate(SuperUserEnquiry updatedData);

	public UserMessage generatePerformanceReport();

	public UserMessage enquiryEmail();

	public UserMessage returnsEmail();

	public UserMessage parcelEmail();

	public void pflSubmitOrder();

	public UserMessage shippingQuote(ShippingQuoteRequest shippingQuoteRequest);

	UserMessage generateDataForPerformanceReport(int date,int month);

	public void triggerFDMLabel();

	public ResponseMessage createTrackEvents(List<TrackParcelResponse> request);

	public void triggerTransVirtual();

	public void etowerDelete(List<String> referenceNumbers);

	public void eTowerMonitoring(Map<String,List<String>> monitoringMap);

	public void fdmMonitoring(Map<String,List<String>> monitoringMap);

	public void monitorAutoShipment(Map<String,List<String>> monitoringMap);

	public void pflMonitoring(Map<String,List<String>> monitoringMap);

	public void generateMonitoringReport(Map<String, List<String>> monitoringMap);

	public void makeVeloceCall(List<SenderDataApi> consignmentData);

	public void veloceMonitoring(Map<String, List<String>> monitoringMap);

	public void makeEtowerForecastCall(List<String> incomingRefNbr);

	void triggerpflSubmitOrder(List<String> orderIds, String key, String token);

	public void sendDataToTrackingDB(List<String> incomingRefNbr);


}
