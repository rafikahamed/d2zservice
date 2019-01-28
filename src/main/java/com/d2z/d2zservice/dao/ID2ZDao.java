package com.d2z.d2zservice.dao;

import java.util.List;
import com.d2z.d2zservice.entity.PostcodeZone;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.entity.Trackandtrace;
import com.d2z.d2zservice.entity.User;
import com.d2z.d2zservice.entity.UserService;
import com.d2z.d2zservice.model.EditConsignmentRequest;
import com.d2z.d2zservice.model.ResponseMessage;
import com.d2z.d2zservice.model.SenderData;
import com.d2z.d2zservice.model.UserDetails;

public interface ID2ZDao {
	
	public String exportParcel(List<SenderData> orderDetailList);

	public List<String> fileList(Integer userId);
	
	public List<String> labelFileList(Integer userId);

	public List<SenderdataMaster> consignmentFileData(String fileName);

	public String consignmentDelete(String refrenceNumlist);

	public List<String> trackingDetails(String fileName);

	public String trackingLabel(String refBarNum);

	public String manifestCreation(String manifestNumber, String refrenceNumber);

	List<Trackandtrace> trackParcel(String refNbr);

	public String createConsignments(List<SenderData> orderDetailList,int userId);
	
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

	public List<SenderdataMaster> fetchShipmentData(String shipmentNumber);

	public List<String> fetchServiceTypeByUserName(String userName);

	public Trackandtrace getLatestStatusByReferenceNumber(String referenceNumber);

	public Trackandtrace getLatestStatusByArticleID(String articleID);

	public List<String> findRefNbrByShipmentNbr(String[] referenceNumber);

}
