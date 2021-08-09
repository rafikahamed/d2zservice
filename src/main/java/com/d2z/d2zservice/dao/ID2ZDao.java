package com.d2z.d2zservice.dao;

import java.util.List;
import java.util.Map;

import com.d2z.d2zservice.dto.ConsignmentDTO;
import com.d2z.d2zservice.entity.APIRates;
import com.d2z.d2zservice.entity.AUPostResponse;
import com.d2z.d2zservice.entity.CSTickets;
import com.d2z.d2zservice.entity.ETowerResponse;
import com.d2z.d2zservice.entity.FastwayPostcode;
import com.d2z.d2zservice.entity.MasterPostCodeZones;
import com.d2z.d2zservice.entity.NZPostcodes;
import com.d2z.d2zservice.entity.PFLPostcode;
import com.d2z.d2zservice.entity.PostcodeZone;
import com.d2z.d2zservice.entity.Returns;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.entity.StarTrackPostcode;
import com.d2z.d2zservice.entity.SupplierEntity;
import com.d2z.d2zservice.entity.SystemRefCount;
import com.d2z.d2zservice.entity.TrackEvents;
import com.d2z.d2zservice.entity.Trackandtrace;
import com.d2z.d2zservice.entity.User;
import com.d2z.d2zservice.entity.UserService;
import com.d2z.d2zservice.entity.Veloce;
import com.d2z.d2zservice.exception.ReferenceNumberNotUniqueException;
import com.d2z.d2zservice.model.ClientDashbaord;
import com.d2z.d2zservice.model.EditConsignmentRequest;
import com.d2z.d2zservice.model.EmailEnquiryDetails;
import com.d2z.d2zservice.model.EmailReturnDetails;
import com.d2z.d2zservice.model.Enquiry;
import com.d2z.d2zservice.model.EnquiryResponse;
import com.d2z.d2zservice.model.EnquiryUpdate;
import com.d2z.d2zservice.model.HeldParcelDetails;
import com.d2z.d2zservice.model.PFLSubmitOrderData;
import com.d2z.d2zservice.model.PerformanceReportTrackingData;
import com.d2z.d2zservice.model.ResponseMessage;
import com.d2z.d2zservice.model.ReturnsAction;
import com.d2z.d2zservice.model.SenderData;
import com.d2z.d2zservice.model.SenderDataApi;
import com.d2z.d2zservice.model.SuperUserEnquiry;
import com.d2z.d2zservice.model.TrackParcelResponse;
import com.d2z.d2zservice.model.TrackingEvents;
import com.d2z.d2zservice.model.UserDetails;
import com.d2z.d2zservice.model.UserMessage;
import com.d2z.d2zservice.model.auspost.TrackingResponse;
import com.d2z.d2zservice.model.etower.LabelData;
import com.d2z.d2zservice.model.etower.TrackingEventResponse;
import com.ebay.soap.eBLBaseComponents.CompleteSaleResponseType;

public interface ID2ZDao {
	
	public String exportParcel(List<SenderData> orderDetailList,Map<String, LabelData> barcodeMap);

	public List<String> fileList(Integer userId);
	
	public List<String> labelFileList(Integer userId);

	public List<SenderdataMaster> consignmentFileData(String fileName);

	public String consignmentDelete(String refrenceNumlist);

	public List<String> trackingDetails(String fileName);

	public List<String> trackingLabel(List<String> refBarNum, String identifier);

	public String manifestCreation(String manifestNumber, String[] refrenceNumber);

	List<Trackandtrace> trackParcel(String refNbr);

	public String createConsignments(List<SenderDataApi> orderDetailList,int userId, String userName,Map<String,LabelData> barcodeMap);
	
	public List<PostcodeZone> fetchAllPostCodeZone();
	
	public List<String> fetchAllReferenceNumbers();
	
	public List<String> fetchBySenderFileID(String senderFileID);

	public List<Trackandtrace> trackParcelByArticleID(String articleID);

	public ResponseMessage editConsignments(List<EditConsignmentRequest> requestList);

	public String allocateShipment(String referenceNumbers, String shipmentNumber);

	public User addUser(UserDetails userData);

	public List<UserService> addUserService(User user,List<String> serviceType);

	public User updateUser(User existingUser);

	public void updateUserService(User existingUser, UserDetails userDetails);

	public String deleteUser(String companyName, String roleId);

	public List<SenderdataMaster> fetchManifestData(String fileName);

	public User login(String userName, String passWord);

	public List<SenderdataMaster> fetchShipmentData(String shipmentNumber, List<Integer> clientIds);

	public List<String> fetchServiceTypeByUserName(String userName);

	public Trackandtrace getLatestStatusByReferenceNumber(String referenceNumber);

	public Trackandtrace getLatestStatusByArticleID(String articleID);

	public List<SenderdataMaster> findRefNbrByShipmentNbr(String[] referenceNumber);

	public void logEbayResponse(CompleteSaleResponseType response);

	public ClientDashbaord clientDahbaord(Integer userId);

	List<String> fetchReferenceNumberByUserId(Integer userId);

	public void deleteConsignment(String referenceNumbers);

	public List<String> fetchServiceType(Integer user_id);

	public List<APIRates> fetchAllAPIRates();

	public void logEtowerResponse(List<ETowerResponse> responseEntity);

	ResponseMessage insertTrackingDetails(TrackingEventResponse response);

	public List<SenderdataMaster> fetchConsignmentsManifestShippment(List<String> incomingRefNbr);

	public List<SenderdataMaster> fetchDataForAusPost(List<String> refNbrs);

	public int fetchUserIdByReferenceNumber(String string);

	List<String>  fetchArticleIDForFDMCall();

	public List<String>  fetchDataForAUPost();

	public ResponseMessage insertAUTrackingDetails(TrackingResponse auTrackingDetails, Map<String, String> map);
    
	public void logAUPostResponse(List<AUPostResponse> aupostresponse);

	public void updateCubicWeight();

	//public void makeFriePostUpdataManifestCall(String string);

	public void updateRates();

	public int fetchUserIdbyUserNameAndRole(String userName);
	
	public int fetchUserIdbyUserName(String userName);

	public List<SenderdataMaster> fetchDataBasedonSupplier(List<String> incomingRefNbr, String string);
	
	public List<SenderdataMaster> fetchDataBasedonrefnbr(List<String> incomingRefNbr);

	public List<String> fetchDataForEtowerForeCastCall(String[] refNbrs);

	public List<FastwayPostcode> fetchFWPostCodeZone();

	public List<PFLSubmitOrderData> fetchDataforPFLSubmitOrder(String[] refNbrs);

	public String fetchUserById(int userId);

	public List<String> getArticleIDForFreiPostTracking();

	public EnquiryResponse createEnquiry(Enquiry createEnquiry) throws ReferenceNumberNotUniqueException;

	public List<CSTickets> fetchEnquiry(String status, String fromDate, String toDate, String userId);

	public List<CSTickets> fetchCompletedEnquiry(String userId);

	public List<Integer> fetchUserId(String userId);
	
	public List<String> fetchReferencenumberByArticleid(List<String>ArticleID);
	
	public void logcurrencyRate();
	
	public Double getAudcurrency(String country);
	
	public List<SenderdataMaster> fetchConsignmentsByRefNbr(List<String> refNbrs);

	public List<Returns> returnsOutstanding(String fromDate, String toDate, String userId);

	public List<SenderdataMaster> fetchShipmentDatabyType(List<String> number, List<Integer> listOfClientId,
			String type);

	public UserMessage returnAction(List<ReturnsAction> returnsAction);

	public List<SystemRefCount> fetchAllSystemRefCount();

	public void updateSystemRefCount(Map<String, Integer> currentSysRefCount);

	public UserMessage enquiryFileUpload(byte[] blob, String fileName, String ticketNumber);

	public String fetchServiceTypeByRefNbr(String string);

	public List<String> fetchMlidsBasedOnSupplier(String supplier);

	public List<StarTrackPostcode> fetchSTPostCodeZone();

	public UserMessage enquiryFileUpload(List<SuperUserEnquiry> enquiry);

	public EnquiryResponse enquiryClientUpdate(EnquiryUpdate updateEnquiry);
	
	public UserMessage enquiryUpdate(String ticketNum, String cmts, String d2zCmts, String update, String sts);

	public CSTickets fetchCSTicketDetails(String ticketNumber);

	public List<String> fetchPerformanceReportData();

	public List<PerformanceReportTrackingData> fetchArticleIdForPerformanceReport(int day,int month);

	public List<EmailEnquiryDetails> fetchEmailEnquiryDetails();

	public List<User> fetchEmailDetails();

	public List<EmailReturnDetails> fetchReturnsDetails();

	public List<HeldParcelDetails> parcelEmail();

	public void updateForPFLSubmitOrder(List<String> fastwayOrderId,String status);

	public List<PFLSubmitOrderData> fetchDataForPFLSubmitOrder();

	public void updateForPFLSubmitOrderCompleted();

	public List<NZPostcodes> fetchAllNZPostCodeZone();

	public String fetchServiceType(String string);

	public List<String> fetchArticleID(List<String> refBarNum);

	public List<String> fetchMlid(List<String> refBarNum);

	public List<String> fetchPerformanceReportDataByArticleId(List<String> articleIds);

	public Map<String, TrackingEvents> fetchTrackingEvents(List<String> articleIds);

	//public void saveTrackingEvents(Map<String, TrackingEvents> trackingDataMap);

	public void saveTrackingEvents(String article_id, TrackingEvents events);

	public List<PFLPostcode> fetchAllPFLPostCodeZone();

	public List<String> fetchDataForFDMCall(String[] refNbrs);

	public List<MasterPostCodeZones> fetchAllMasterPostCodeZone();

	public ResponseMessage createTrackEvents(List<TrackParcelResponse> request);

	public List<TrackEvents> fetchEventsFromTrackEvents(List<String> d2zArticleIds);

	public List<String> fetchTrackingNumberFromEtowerResponse(List<String> artileIDList);

	public List<String> missingCreateShippingOrder();

	public List<String> missingForecast();

	public List<String> missingFdmArticleIds();

	public List<String> missingShipmentAllocation();

	public List<String> missingPFLIdsMonday();

	public List<String> missingPFLIds();

	public List<SenderdataMaster> fetchDataForVeloceCall(String[] refNbrs);

	public List<String> missingVeloceArticleIds();
	
	public List<SenderdataMaster> createConsignment(List<SenderdataMaster> senderDataMaster);
	
	public void generateBarcode(String fileSeqId);

	public void fetchAllMasterPostCodeZone(String serviceType, List<ConsignmentDTO> consignmentList);

	public boolean fetchAutoShipmentIndicator(int userId, String serviceType);

	public boolean fetchPostCodeValidationRequired(int userId, String serviceType);

	public SupplierEntity fetchSupplierData(int supplierAuthId);

	public int fetchNextFileSeqId();

	public String getPostCodeLogic(String serviceType);

	public String fetchLabelName(String servicetype, String carrier);

	List<SenderdataMaster> fetchLabelData(List<String> refBarNum);

	public String fetchFDMRoute(String state, String suburb, String postcode);

	public List<Trackandtrace> insertIntoTrackandTrace(List<Trackandtrace> list);

	public Map<String, List<String>> fetchLabelName(List<String> refBarNum,String identifier);
		
	public List<String> fetchTrackingNumberFromEtowerResponse(String articleID);

	List<Object[]> fetchServiceTypeCarrier(List<String> ids, String identifier);

	Map<String, List<String>> fetchtrackingIdentifier(List<String> refBarNum, String identifier);

	public Veloce findVeloceValues(String servicetype);

	List<Object[]> fetchAirwayBill(List<String> ids, String identifier);

	Map<String, List<SenderdataMaster>> fetchallocationIdentifier(List<SenderdataMaster> data);

	public List<SenderdataMaster> fetchConsignments(List<String> ids, String identifier);

	public int updateAirwayBill(List<String> validData, String shipmentNumber);

	List<SenderdataMaster> fetchByRefNbr(List<String> ids);

}
