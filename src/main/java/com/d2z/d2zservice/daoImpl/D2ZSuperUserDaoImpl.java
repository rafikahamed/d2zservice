package com.d2z.d2zservice.daoImpl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.d2z.d2zservice.dao.ID2ZSuperUserDao;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.entity.Trackandtrace;
import com.d2z.d2zservice.entity.User;
import com.d2z.d2zservice.model.ArrivalReportFileData;
import com.d2z.d2zservice.model.UploadTrackingFileData;
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

	@Override
	public List<Trackandtrace> uploadTrackingFile(List<UploadTrackingFileData> fileData) {
		List<Trackandtrace> trackingDetailsList = new ArrayList<Trackandtrace>();
		for(UploadTrackingFileData fileDataValue: fileData) {
			Trackandtrace trackingDetails = new Trackandtrace();
			trackingDetails.setReference_number(fileDataValue.getReferenceNumber());
			trackingDetails.setConnoteNo(fileDataValue.getConnoteNo());
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
			System.out.println(trackingDetails.getTrackEventDateOccured());
			trackingDetails.setFileName(fileDataValue.getFileName());
			trackingDetails.setTimestamp(Timestamp.from(Instant.now()).toString());
			trackingDetails.setIsDeleted("N");
			trackingDetailsList.add(trackingDetails);
		}
		List<Trackandtrace> insertedData= (List<Trackandtrace>) trackAndTraceRepository.saveAll(trackingDetailsList);
		trackAndTraceRepository.updateTracking();
		return insertedData;
	}

	@Override
	public List<Trackandtrace> uploadArrivalReport(List<ArrivalReportFileData> fileData){
		List<Trackandtrace> trackingDetailsList = new ArrayList<Trackandtrace>();
		for(ArrivalReportFileData fileDataValue: fileData) {	
			Trackandtrace trackingDetails = new Trackandtrace();
			trackingDetails.setConnoteNo(fileDataValue.getConnoteNo());
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
			trackingDetails.setTimestamp(Timestamp.from(Instant.now()).toString());
			trackingDetails.setIsDeleted("N");
			trackingDetailsList.add(trackingDetails);
		}
		List<Trackandtrace> insertedData= (List<Trackandtrace>) trackAndTraceRepository.saveAll(trackingDetailsList);
		trackAndTraceRepository.updateTracking();
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
				senderDataRepository.exportConsignments(Timestamp.valueOf(fromTime),Timestamp.valueOf(toTime));
		System.out.println(exportedConsignments.size());
		return exportedConsignments;
	}
	
	@Override
	public List<SenderdataMaster> exportShipment(String fromDate, String toDate) {
		String fromTime = fromDate.concat(" ").concat("00:00:00");
		String toTime = toDate.concat(" ").concat("23:59:59");
		List<SenderdataMaster> exportedShipment = senderDataRepository.exportShipment(Timestamp.valueOf(fromTime),Timestamp.valueOf(toTime));
		System.out.println(exportedShipment.size());
		return exportedShipment;
	}
	
}
