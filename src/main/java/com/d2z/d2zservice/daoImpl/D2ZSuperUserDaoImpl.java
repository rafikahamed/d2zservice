package com.d2z.d2zservice.daoImpl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.d2z.d2zservice.dao.ID2ZSuperUserDao;
import com.d2z.d2zservice.entity.Trackandtrace;
import com.d2z.d2zservice.exception.InvalidDateException;
import com.d2z.d2zservice.model.ArrivalReportFileData;
import com.d2z.d2zservice.model.UploadTrackingFileData;
import com.d2z.d2zservice.repository.TrackAndTraceRepository;
@Repository
public class D2ZSuperUserDaoImpl implements ID2ZSuperUserDao{

	@Autowired
	TrackAndTraceRepository trackAndTraceRepository;
	
	@Override
	public List<Trackandtrace> uploadTrackingFile(List<UploadTrackingFileData> fileData) throws InvalidDateException {
		List<Trackandtrace> trackingDetailsList = new ArrayList<Trackandtrace>();
		for(UploadTrackingFileData fileDataValue: fileData) {	
			Trackandtrace trackingDetails = new Trackandtrace();
			trackingDetails.setReference_number(fileDataValue.getReferenceNumber());
			trackingDetails.setConnoteNo(fileDataValue.getConnoteNo());
			trackingDetails.setTrackEventDetails(fileDataValue.getTrackEventDetails().toUpperCase());
			System.out.println(fileDataValue.getTrackEventDateOccured());
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		    Date parsedDate;
		    Timestamp timestamp = null;
			try {
				parsedDate = dateFormat.parse(fileDataValue.getTrackEventDateOccured());
				timestamp = new java.sql.Timestamp(parsedDate.getTime());

			} catch (ParseException e) {
				throw new InvalidDateException("Invalid Date");
			}
			trackingDetails.setTrackEventDateOccured(timestamp);
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
	public List<Trackandtrace> uploadArrivalReport(List<ArrivalReportFileData> fileData) throws InvalidDateException {
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
			System.out.println(fileDataValue.getScannedDateTime());
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		    Date parsedDate;
		    Timestamp timestamp = null;
			try {
				parsedDate = dateFormat.parse(fileDataValue.getScannedDateTime());
				timestamp = new java.sql.Timestamp(parsedDate.getTime());

			} catch (ParseException e) {
				throw new InvalidDateException("Invalid Date");
			}
			trackingDetails.setTrackEventDateOccured(timestamp);
			System.out.println(trackingDetails.getTrackEventDateOccured());
			trackingDetails.setFileName(fileDataValue.getFileName());
			trackingDetails.setTimestamp(Timestamp.from(Instant.now()).toString());
			trackingDetails.setIsDeleted("N");
			trackingDetailsList.add(trackingDetails);
		}
		List<Trackandtrace> insertedData= (List<Trackandtrace>) trackAndTraceRepository.saveAll(trackingDetailsList);
		trackAndTraceRepository.updateTracking();
		return insertedData;	}
	

}
