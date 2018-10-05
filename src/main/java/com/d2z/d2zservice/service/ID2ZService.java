package com.d2z.d2zservice.service;

import java.util.List;

import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.entity.User;
import com.d2z.d2zservice.exception.ReferenceNumberNotUniqueException;
import com.d2z.d2zservice.model.DropDownModel;
import com.d2z.d2zservice.model.EditConsignmentRequest;
import com.d2z.d2zservice.model.FileUploadData;
import com.d2z.d2zservice.model.SenderData;
import com.d2z.d2zservice.model.SenderDataResponse;
import com.d2z.d2zservice.model.TrackParcel;
import com.d2z.d2zservice.model.TrackingDetails;
import com.d2z.d2zservice.model.UserDetails;
import com.d2z.d2zservice.model.UserMessage;

public interface ID2ZService {
	
	public UserMessage exportParcel(List<FileUploadData> fileData);

	public List<DropDownModel> fileList();

	public List<SenderdataMaster> consignmentFileData(String fileName);

	public byte[] generateLabel(List<SenderData> senderData);

	public UserMessage consignmentDelete(String refrenceNumlist);

	public List<TrackingDetails> trackingDetails(String fileName);

	public byte[] trackingLabel(String refBarNum);

	public UserMessage manifestCreation(String manifestNumber, String refrenceNumber);

	List<TrackParcel> trackParcelByRefNbr(List<String> referenceNumbers);

	public List<SenderDataResponse> createConsignments(List<SenderData> orderDetailList) throws ReferenceNumberNotUniqueException;

	public List<TrackParcel> trackParcelByArticleID(List<String> articleIDs);

	public UserMessage editConsignments(List<EditConsignmentRequest> requestList);

	public UserMessage allocateShipment(String referenceNumbers, String shipmentNumber);

	public UserMessage addUser(UserDetails userDetails);

	public UserMessage updateUser(UserDetails userDetails);

	public UserMessage deleteUser(String companyName);

	public List<SenderdataMaster> fetchManifestData(String fileName);

	public User login(String userName, String passWord);

}
