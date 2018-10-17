package com.d2z.d2zservice.dao;

import java.util.List;

import com.d2z.d2zservice.entity.Trackandtrace;
import com.d2z.d2zservice.exception.InvalidDateException;
import com.d2z.d2zservice.model.ArrivalReportFileData;
import com.d2z.d2zservice.model.UploadTrackingFileData;

public interface ID2ZSuperUserDao {

	List<Trackandtrace> uploadTrackingFile(List<UploadTrackingFileData> fileData) throws InvalidDateException;

	List<Trackandtrace> uploadArrivalReport(List<ArrivalReportFileData> fileData) throws InvalidDateException;

}
