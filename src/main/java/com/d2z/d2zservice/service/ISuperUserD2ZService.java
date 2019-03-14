package com.d2z.d2zservice.service;

import java.util.List;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.model.ArrivalReportFileData;
import com.d2z.d2zservice.model.BrokerList;
import com.d2z.d2zservice.model.BrokerRatesData;
import com.d2z.d2zservice.model.BrokerShipmentList;
import com.d2z.d2zservice.model.D2ZRatesData;
import com.d2z.d2zservice.model.DropDownModel;
import com.d2z.d2zservice.model.ResponseMessage;
import com.d2z.d2zservice.model.UploadTrackingFileData;
import com.d2z.d2zservice.model.UserDetails;
import com.d2z.d2zservice.model.UserMessage;

public interface ISuperUserD2ZService{

	public UserMessage uploadTrackingFile(List<UploadTrackingFileData> fileData);

	public UserMessage uploadArrivalReport(List<ArrivalReportFileData> fileData);

	public List<DropDownModel> brokerCompanyDetails();

	public UserDetails fetchUserDetails(String companyName);

	public List<SenderdataMaster> exportDeteledConsignments(String fromDate, String toDate);

	List<SenderdataMaster> exportConsignmentData(String fromDate, String toDate);

	List<SenderdataMaster> exportShipmentData(String fromDate, String toDate);

	public ResponseMessage trackingEvent(List<String> trackingNumbers);

	public UserMessage uploadBrokerRates(List<BrokerRatesData> brokerRatesData);

	public UserMessage uploadD2ZRates(List<D2ZRatesData> d2zRatesData);

	public List<BrokerList> brokerList();

	public List<DropDownModel> fetchMlidList();

	public List<BrokerShipmentList> brokerShipmentList();

	public List<SenderdataMaster> brokerShipment();

}
