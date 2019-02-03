package com.d2z.d2zservice.service;

import java.util.List;

import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.exception.ReferenceNumberNotUniqueException;
import com.d2z.d2zservice.model.ClientDashbaord;
import com.d2z.d2zservice.model.CreateConsignmentRequest;
import com.d2z.d2zservice.model.DropDownModel;
import com.d2z.d2zservice.model.Ebay_ShipmentDetails;
import com.d2z.d2zservice.model.EditConsignmentRequest;
import com.d2z.d2zservice.model.ParcelStatus;
import com.d2z.d2zservice.model.ResponseMessage;
import com.d2z.d2zservice.model.SenderData;
import com.d2z.d2zservice.model.SenderDataResponse;
import com.d2z.d2zservice.model.ShipmentDetails;
import com.d2z.d2zservice.model.TrackParcel;
import com.d2z.d2zservice.model.TrackingDetails;
import com.d2z.d2zservice.model.UserDetails;
import com.d2z.d2zservice.model.UserMessage;

public interface ID2ZService {
	
	public List<SenderDataResponse> exportParcel(List<SenderData> orderDetailList) throws ReferenceNumberNotUniqueException;

	public List<DropDownModel> fileList(Integer userId);
	
	public List<DropDownModel> labelFileList(Integer userId);

	public List<SenderdataMaster> consignmentFileData(String fileName);

	public byte[] generateLabel(List<SenderData> senderData);

	public UserMessage consignmentDelete(String refrenceNumlist);

	public List<TrackingDetails> trackingDetails(String fileName);

	public byte[] trackingLabel(String refBarNum);

	public UserMessage manifestCreation(String manifestNumber, String refrenceNumber);

	List<TrackParcel> trackParcelByRefNbr(List<String> referenceNumbers);

	public List<SenderDataResponse> createConsignments(CreateConsignmentRequest orderDetail) throws ReferenceNumberNotUniqueException;

	public List<TrackParcel> trackParcelByArticleID(List<String> articleIDs);

	public ResponseMessage editConsignments(List<EditConsignmentRequest> requestList);

	public ResponseMessage allocateShipment(String referenceNumbers, String shipmentNumber);

	public UserMessage addUser(UserDetails userDetails);

	public UserMessage updateUser(UserDetails userDetails);

	public UserMessage deleteUser(String companyName, String roleId);

	public List<SenderdataMaster> fetchManifestData(String fileName);

	public UserDetails login(String userName, String passWord);

	public List<ShipmentDetails> downloadShipmentData(String shipmentNumber);

	public List<ParcelStatus> getStatusByRefNbr(List<String> referenceNumbers);

	public List<ParcelStatus> getStatusByArticleID(List<String> articleIDs);

	public UserMessage uploadShipmentDetailsToEbay(Ebay_ShipmentDetails shipmentDetails);

	public ClientDashbaord clientDahbaord(Integer userId);

}
