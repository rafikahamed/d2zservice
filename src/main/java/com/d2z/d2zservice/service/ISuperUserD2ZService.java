package com.d2z.d2zservice.service;

import java.util.List;

import com.d2z.d2zservice.exception.InvalidDateException;
import com.d2z.d2zservice.model.ArrivalReportFileData;
import com.d2z.d2zservice.model.DropDownModel;
import com.d2z.d2zservice.model.UploadTrackingFileData;
import com.d2z.d2zservice.model.UserDetails;
import com.d2z.d2zservice.model.UserMessage;

public interface ISuperUserD2ZService{

	public UserMessage uploadTrackingFile(List<UploadTrackingFileData> fileData) throws InvalidDateException;

	public UserMessage uploadArrivalReport(List<ArrivalReportFileData> fileData) throws InvalidDateException;

	public List<DropDownModel> brokerCompanyDetails();

	public UserDetails fetchUserDetails(String companyName);

}
