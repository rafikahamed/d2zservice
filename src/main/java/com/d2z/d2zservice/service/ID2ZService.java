package com.d2z.d2zservice.service;

import java.util.List;

import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.model.DropDownModel;
import com.d2z.d2zservice.model.FileUploadData;
import com.d2z.d2zservice.model.SenderData;
import com.d2z.d2zservice.model.UserMessage;

public interface ID2ZService {
	
	public UserMessage exportParcel(List<FileUploadData> fileData);

	public List<DropDownModel> fileList();

	public List<SenderdataMaster> consignmentFileData(String fileName);

	public byte[] generateLabel(List<SenderData> senderData);
	
}
