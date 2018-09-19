package com.d2z.d2zservice.dao;

import java.util.List;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.entity.Trackandtrace;
import com.d2z.d2zservice.model.FileUploadData;
import com.d2z.d2zservice.model.TrackingDetails;

public interface ID2ZDao {
	
	public List<FileUploadData> exportParcel(List<FileUploadData> fileData);

	public List<String> fileList();

	public List<SenderdataMaster> consignmentFileData(String fileName);

	public String consignmentDelete(String refrenceNumlist);

	public List<String> trackingDetails(String fileName);

	List<Trackandtrace> trackParcel(String refNbr);

}
