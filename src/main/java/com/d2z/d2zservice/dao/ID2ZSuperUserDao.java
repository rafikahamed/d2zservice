package com.d2z.d2zservice.dao;

import java.util.List;

import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.entity.Trackandtrace;
import com.d2z.d2zservice.entity.User;
import com.d2z.d2zservice.exception.InvalidDateException;
import com.d2z.d2zservice.model.ArrivalReportFileData;
import com.d2z.d2zservice.model.UploadTrackingFileData;

public interface ID2ZSuperUserDao {

	List<Trackandtrace> uploadTrackingFile(List<UploadTrackingFileData> fileData);

	List<Trackandtrace> uploadArrivalReport(List<ArrivalReportFileData> fileData);

	List<String> brokerCompanyDetails();

	User fetchUserDetails(String companyName);

	List<SenderdataMaster> exportDeteledConsignments(String fromDate, String toDate);

	List<SenderdataMaster> exportConsignments(String fromDate, String toDate);

	List<SenderdataMaster> exportShipment(String fromDate, String toDate);

}
