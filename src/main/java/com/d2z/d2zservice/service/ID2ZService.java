package com.d2z.d2zservice.service;

import java.util.List;

import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.model.DropDownModel;
import com.d2z.d2zservice.model.FileUploadData;
import com.d2z.d2zservice.model.SenderData;
import com.d2z.d2zservice.model.TrackParcel;
import com.d2z.d2zservice.model.TrackingDetails;
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

	List<TrackParcel> trackParcel(List<String> referenceNumbers);
	
}
