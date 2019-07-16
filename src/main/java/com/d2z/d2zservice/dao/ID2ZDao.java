package com.d2z.d2zservice.dao;

import java.util.List;
import java.util.Map;
import com.d2z.d2zservice.entity.APIRates;
import com.d2z.d2zservice.entity.AUPostResponse;
import com.d2z.d2zservice.entity.CSTickets;
import com.d2z.d2zservice.entity.ETowerResponse;
import com.d2z.d2zservice.entity.FastwayPostcode;
import com.d2z.d2zservice.entity.PostcodeZone;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.entity.Trackandtrace;
import com.d2z.d2zservice.entity.User;
import com.d2z.d2zservice.entity.UserService;
import com.d2z.d2zservice.model.ClientDashbaord;
import com.d2z.d2zservice.model.CreateEnquiryRequest;
import com.d2z.d2zservice.model.EditConsignmentRequest;
import com.d2z.d2zservice.model.ResponseMessage;
import com.d2z.d2zservice.model.SenderData;
import com.d2z.d2zservice.model.SenderDataApi;
import com.d2z.d2zservice.model.UserDetails;
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

	public List<String> trackingLabel(List<String> refBarNum);

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

	public ResponseMessage insertAUTrackingDetails(TrackingResponse auTrackingDetails);
    
	public void logAUPostResponse(List<AUPostResponse> aupostresponse);

	public void updateCubicWeight();

	//public void makeFriePostUpdataManifestCall(String string);

	public void updateRates();

	public int fetchUserIdbyUserName(String userName);

	public List<SenderdataMaster> fetchDataBasedonSupplier(List<String> incomingRefNbr, String string);

	public List<String> fetchDataForEtowerForeCastCall(String[] refNbrs);

	public List<FastwayPostcode> fetchFWPostCodeZone();

	public List<String> fetchDataforPFLSubmitOrder(String[] refNbrs);

	public String fetchUserById(int userId);


	public List<String> getArticleIDForFreiPostTracking();

	public String createEnquiry(List<CreateEnquiryRequest> createEnquiry);

}
