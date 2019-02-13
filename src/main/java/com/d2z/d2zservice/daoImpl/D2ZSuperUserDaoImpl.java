package com.d2z.d2zservice.daoImpl;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.d2z.d2zservice.dao.ID2ZSuperUserDao;
import com.d2z.d2zservice.entity.BrokerRates;
import com.d2z.d2zservice.entity.D2ZRates;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.entity.Trackandtrace;
import com.d2z.d2zservice.entity.User;
import com.d2z.d2zservice.model.ArrivalReportFileData;
import com.d2z.d2zservice.model.BrokerRatesData;
import com.d2z.d2zservice.model.D2ZRatesData;
import com.d2z.d2zservice.model.ETowerTrackingDetails;
import com.d2z.d2zservice.model.ResponseMessage;
import com.d2z.d2zservice.model.UploadTrackingFileData;
import com.d2z.d2zservice.model.ZoneDetails;
import com.d2z.d2zservice.model.ZoneRates;
import com.d2z.d2zservice.repository.BrokerRatesRepository;
import com.d2z.d2zservice.repository.D2ZRatesRepository;
import com.d2z.d2zservice.repository.SenderDataRepository;
import com.d2z.d2zservice.repository.TrackAndTraceRepository;
import com.d2z.d2zservice.repository.UserRepository;

@Repository
public class D2ZSuperUserDaoImpl implements ID2ZSuperUserDao{

	@Autowired
	TrackAndTraceRepository trackAndTraceRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	SenderDataRepository senderDataRepository;
	
	@Autowired
	BrokerRatesRepository brokerRatesRepository;

	@Autowired
	D2ZRatesRepository d2zRatesRepository;
	
	@Override
	public List<Trackandtrace> uploadTrackingFile(List<UploadTrackingFileData> fileData) {
		List<Trackandtrace> trackingDetailsList = new ArrayList<Trackandtrace>();
		for(UploadTrackingFileData fileDataValue: fileData) {
			Trackandtrace trackingDetails = new Trackandtrace();
			trackingDetails.setReference_number(fileDataValue.getReferenceNumber());
			trackingDetails.setArticleID(fileDataValue.getConnoteNo());
			trackingDetails.setTrackEventDetails(fileDataValue.getTrackEventDetails().toUpperCase());
			/*System.out.println(fileDataValue.getTrackEventDateOccured());
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		    Date parsedDate;
		    Timestamp timestamp = null;
			try {
				parsedDate = dateFormat.parse(fileDataValue.getTrackEventDateOccured());
				timestamp = new java.sql.Timestamp(parsedDate.getTime());
			} catch (ParseException e) {
				throw new InvalidDateException("Invalid Date");
			}*/
			//trackingDetails.setTrackEventDateOccured(Timestamp.valueOf(fileDataValue.getTrackEventDateOccured()));
			trackingDetails.setTrackEventDateOccured(fileDataValue.getTrackEventDateOccured());
			trackingDetails.setFileName(fileDataValue.getFileName());
			trackingDetails.setTimestamp(Timestamp.valueOf(LocalDateTime.now()).toString());
			trackingDetails.setIsDeleted("N");
			//trackAndTraceRepository.save(trackingDetails);
			trackingDetailsList.add(trackingDetails);
		}
		List<Trackandtrace> insertedData=  (List<Trackandtrace>) trackAndTraceRepository.saveAll(trackingDetailsList);
		trackAndTraceRepository.updateTracking();
		trackAndTraceRepository.deleteDuplicates();
		return insertedData;
	}

	@Override
	public List<Trackandtrace> uploadArrivalReport(List<ArrivalReportFileData> fileData){
		List<Trackandtrace> trackingDetailsList = new ArrayList<Trackandtrace>();
		for(ArrivalReportFileData fileDataValue: fileData) {	
			Trackandtrace trackingDetails = new Trackandtrace();
			trackingDetails.setReference_number(fileDataValue.getReferenceNumber());
			trackingDetails.setArticleID(fileDataValue.getConnoteNo());
			String trackEvent = "Shortage";
			if("CLEAR".equalsIgnoreCase(fileDataValue.getStatus())) {
				trackEvent = "Received and Clear";
			}else if("HELD".equalsIgnoreCase(fileDataValue.getStatus())){
				trackEvent = "Received and Held";
			}
			trackingDetails.setTrackEventDetails(trackEvent.toUpperCase());
			/*System.out.println(fileDataValue.getScannedDateTime());
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		    Date parsedDate;
		    Timestamp timestamp = null;
			try {
				parsedDate = dateFormat.parse(fileDataValue.getScannedDateTime());
				timestamp = new java.sql.Timestamp(parsedDate.getTime());

			} catch (ParseException e) {
				throw new InvalidDateException("Invalid Date");
			}*/
			//trackingDetails.setTrackEventDateOccured(Timestamp.valueOf(fileDataValue.getScannedDateTime()));
			trackingDetails.setTrackEventDateOccured(fileDataValue.getScannedDateTime());
			System.out.println(trackingDetails.getTrackEventDateOccured());
			trackingDetails.setFileName(fileDataValue.getFileName());
			trackingDetails.setTimestamp(Timestamp.valueOf(LocalDateTime.now()).toString());
//			System.out.println("Instant : "+Timestamp.from(Instant.now()).toString());
			trackingDetails.setIsDeleted("N");
			trackingDetailsList.add(trackingDetails);
		}
		List<Trackandtrace> insertedData= (List<Trackandtrace>) trackAndTraceRepository.saveAll(trackingDetailsList);
		trackAndTraceRepository.updateTracking();
		trackAndTraceRepository.deleteDuplicates();
		return insertedData;	
	}

	@Override
	public List<String> brokerCompanyDetails() {
		List<String> companyDetails = userRepository.fetchBrokerCompanyName();
		return companyDetails;
	}

	@Override
	public User fetchUserDetails(String companyName) {
		User userDetails = userRepository.fetchBrokerbyCompanyName(companyName);
		return userDetails;
	}

	@Override
	public List<SenderdataMaster> exportDeteledConsignments(String fromDate, String toDate) {
		String fromTime = fromDate.concat(" ").concat("00:00:00");
		String toTime = toDate.concat(" ").concat("23:59:59");
		List<SenderdataMaster> deletedConsignments = senderDataRepository.fetchDeletedConsignments(fromTime,toTime);
		System.out.println(deletedConsignments.size());
		
		return deletedConsignments;
	}
	
	@Override
	public List<SenderdataMaster> exportConsignments(String fromDate, String toDate) {
		String fromTime = fromDate.concat(" ").concat("00:00:00");
		String toTime = toDate.concat(" ").concat("23:59:59");
		List<SenderdataMaster> exportedConsignments = 
				senderDataRepository.exportConsignments(fromTime,toTime);
		System.out.println(exportedConsignments.size());
		return exportedConsignments;
	}
	
	@Override
	public List<SenderdataMaster> exportShipment(String fromDate, String toDate) {
		String fromTime = fromDate.concat(" ").concat("00:00:00");
		String toTime = toDate.concat(" ").concat("23:59:59");
		List<SenderdataMaster> exportedShipment = senderDataRepository.exportShipment(fromTime,toTime);
		System.out.println(exportedShipment.size());
		return exportedShipment;
	}

	@Override
	public ResponseMessage insertTrackingDetails(List<List<ETowerTrackingDetails>> response) {
		List<Trackandtrace> trackAndTraceList = new ArrayList<Trackandtrace>();
		ResponseMessage responseMsg =  new ResponseMessage();
		if(response.isEmpty()) {
			responseMsg.setResponseMessage("No Data from ETower");
		}
		else {
			int coun =0 ;
		for(List<ETowerTrackingDetails> etowerResponse : response) {
			System.out.println(coun++);
			for(ETowerTrackingDetails trackingDetails : etowerResponse) {
				Trackandtrace trackandTrace = new Trackandtrace();
				trackandTrace.setArticleID(trackingDetails.getTrackingNo());
				trackandTrace.setFileName("eTowerAPI");
				//Date date = Date.from(Instant.ofEpochSecond(trackingDetails.getEventTime()));
				//Date date = new Date((long)trackingDetails.getEventTime());
				
				//DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
              //  String trackEventOccurred = dateFormat.format(date);
                //System.out.println("DAte: "+date);
                //System.out.println("String: "+trackEventOccurred);
                trackandTrace.setTrackEventDateOccured(trackingDetails.getTimestamp());
				trackandTrace.setTrackEventCode(trackingDetails.getEventCode());
			/*	if(trackingDetails.getActivity()!=null) {
					if(trackingDetails.getActivity().contains("Attempted Delivery") || trackingDetails.getActivity().contains("Unable to complete delivery") ) {
						trackandTrace.setTrackEventDetails("ATTEMPTED DELIVERY");
					}else if(trackingDetails.getActivity().contains("Collection")) {
						trackandTrace.setTrackEventDetails("AWAITING COLLECTION");
					}else if(trackingDetails.getActivity().contains("Delivered") || trackingDetails.getActivity().contains("Receiver requested Safe Drop") ) {
						trackandTrace.setTrackEventDetails("DELIVERED");
					}else if(trackingDetails.getActivity().contains("Accepted by Driver") || trackingDetails.getActivity().contains("With Australia Post for delivery today") 
							|| trackingDetails.getActivity().contains("In Transit")) {
						trackandTrace.setTrackEventDetails("IN TRANSIT");
					}else if(trackingDetails.getActivity().contains("facility") || trackingDetails.getActivity().contains("Transferred") ) {
						trackandTrace.setTrackEventDetails("ITEM PROCESSED BY FACILITY");
					}else if(trackingDetails.getActivity().contains("Shipping information") || trackingDetails.getActivity().contains("Order Processed") 
							|| trackingDetails.getActivity().contains("Order Accepted") || trackingDetails.getActivity().contains("Lodged")
							|| trackingDetails.getActivity().contains("Manifested")) {
						trackandTrace.setTrackEventDetails("SHIPPING INFORMATION RECEIVED");
					}else {
						trackandTrace.setTrackEventDetails(trackingDetails.getActivity());
					}
				}		*/
				trackandTrace.setTrackEventDetails(trackingDetails.getActivity());
				trackandTrace.setCourierEvents(trackingDetails.getActivity());
				trackandTrace.setTimestamp(Timestamp.valueOf(LocalDateTime.now()).toString());
				trackandTrace.setReference_number(trackingDetails.getTrackingNo());
				trackandTrace.setLocation(trackingDetails.getLocation());
				trackandTrace.setIsDeleted("N");
				if("ARRIVED AT DESTINATION AIRPORT".equalsIgnoreCase(trackandTrace.getTrackEventDetails()) ||
						("COLLECTED FROM AIRPORT TERMINAL".equalsIgnoreCase(trackandTrace.getTrackEventDetails())) ||
							("PREPARING TO DISPATCH".equalsIgnoreCase(trackandTrace.getTrackEventDetails())))
					{
					trackandTrace.setIsDeleted("Y");
					}
				trackAndTraceList.add(trackandTrace);
			}
			
		
		}
		trackAndTraceRepository.saveAll(trackAndTraceList);
		trackAndTraceRepository.updateTracking();
		trackAndTraceRepository.deleteDuplicates();
		responseMsg.setResponseMessage("Data uploaded successfully from ETower");
		}
		return responseMsg;
	}

	@Override
	public List<String> fetchTrackingNumbersForETowerCall() {
		List<String> dbResult  =  trackAndTraceRepository.fetchTrackingNumbersForETowerCall();
		return dbResult;
		
	}


	@Override
	public String uploadBrokerRates(List<BrokerRatesData> brokerRatesData) {
		String message = "";
		List<BrokerRates> brokerRatesList = new ArrayList<BrokerRates>();
		for(BrokerRatesData brokerRateData : brokerRatesData) {
			
			for(ZoneDetails zoneData : brokerRateData.getZone()) {
				
				
				for(ZoneRates zoneRates : zoneData.getRates()) {
					BrokerRates brokerRates_DB = brokerRatesRepository.findByCompositeKey(brokerRateData.getBrokerUserName(), brokerRateData.getInjectionType(), 
							brokerRateData.getServiceType(), zoneData.getZoneID(), zoneRates.getMinWeight(), zoneRates.getMaxWeight());
					if(null != brokerRates_DB) {
						brokerRates_DB.setBackupInd("Y");
						brokerRatesList.add(brokerRates_DB);
					}
					BrokerRates brokerRates = new  BrokerRates();
					brokerRates.setBrokerUserName(brokerRateData.getBrokerUserName());
					brokerRates.setInjectionType(brokerRateData.getInjectionType());
					brokerRates.setServiceType(brokerRateData.getServiceType());
					brokerRates.setGST(brokerRateData.getGST());
					brokerRates.setZoneID(zoneData.getZoneID());
					brokerRates.setRate(zoneRates.getRate());
					brokerRates.setMaxWeight(zoneRates.getMaxWeight());
					brokerRates.setMinWeight(zoneRates.getMinWeight());
					brokerRates.setBackupInd("N");
					brokerRates.setTimestamp(Timestamp.valueOf(LocalDateTime.now()).toString());
					brokerRatesList.add(brokerRates);
					
				}
			}
			
		}
		List<BrokerRates> insertedData = (List<BrokerRates>) brokerRatesRepository.saveAll(brokerRatesList);
		if(null!= insertedData && insertedData.size() > 0) {
			message = "Data uploaded Successfully";
		}
		else {
			message = "Failed to upload Data";
		}
		return message;
	}

	@Override
	public String uploadD2ZRates(List<D2ZRatesData> d2zRatesData) {
		String message = "";
		List<D2ZRates> d2zRatesList = new ArrayList<D2ZRates>();
		for(D2ZRatesData d2zRateData : d2zRatesData) {
			
			for(ZoneDetails zoneData : d2zRateData.getZone()) {
				
				
				for(ZoneRates zoneRates : zoneData.getRates()) {
					D2ZRates d2zRates_DB = d2zRatesRepository.findByCompositeKey(d2zRateData.getMLID(), zoneData.getZoneID(), zoneRates.getMinWeight(), zoneRates.getMaxWeight());
					if(null != d2zRates_DB) {
						d2zRates_DB.setBackupInd("Y");
						d2zRatesList.add(d2zRates_DB);
					}
					D2ZRates d2zRates = new  D2ZRates();
					d2zRates.setMLID(d2zRateData.getMLID());
					d2zRates.setGST(d2zRateData.getGST());
					d2zRates.setZoneID(zoneData.getZoneID());
					d2zRates.setRate(zoneRates.getRate());
					d2zRates.setMaxWeight(zoneRates.getMaxWeight());
					d2zRates.setMinWeight(zoneRates.getMinWeight());
					d2zRates.setBackupInd("N");
					d2zRates.setTimestamp(Timestamp.valueOf(LocalDateTime.now()).toString());
					d2zRatesList.add(d2zRates);
					
				}
			}
			
		}
		List<D2ZRates> insertedData = (List<D2ZRates>) d2zRatesRepository.saveAll(d2zRatesList);
		if(null!= insertedData && insertedData.size() > 0) {
			message = "Data uploaded Successfully";
		}
		else {
			message = "Failed to upload Data";
		}
		return message;
	}

}
