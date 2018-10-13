package com.d2z.d2zservice.daoImpl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.d2z.d2zservice.dao.ID2ZSuperUserDao;
import com.d2z.d2zservice.entity.Trackandtrace;
import com.d2z.d2zservice.model.UploadTrackingFileData;
import com.d2z.d2zservice.repository.TrackAndTraceRepository;
@Repository
public class D2ZSuperUserDaoImpl implements ID2ZSuperUserDao{

	@Autowired
	TrackAndTraceRepository trackAndTraceRepository;
	
	@Override
	public List<Trackandtrace> uploadTrackingFile(List<UploadTrackingFileData> fileData) {
		List<Trackandtrace> trackingDetailsList = new ArrayList<Trackandtrace>();
		for(UploadTrackingFileData fileDataValue: fileData) {	
			Trackandtrace trackingDetails = new Trackandtrace();
			trackingDetails.setReference_number(fileDataValue.getReferenceNumber());
			trackingDetails.setArticleID(fileDataValue.getArticleID());
			trackingDetails.setTrackEventDetails(fileDataValue.getTrackEventDetails().toUpperCase());
			trackingDetails.setTrackEventDateOccured(fileDataValue.getTrackEventDateOccured());
			trackingDetails.setFileName(fileDataValue.getFileName());
			trackingDetails.setTimestamp(Timestamp.from(Instant.now()));
			trackingDetailsList.add(trackingDetails);
		}
		List<Trackandtrace> insertedData= (List<Trackandtrace>) trackAndTraceRepository.saveAll(trackingDetailsList);
		trackAndTraceRepository.updateTracking();
		return insertedData;
	}
	

}
