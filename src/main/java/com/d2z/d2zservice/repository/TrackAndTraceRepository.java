package com.d2z.d2zservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.d2z.d2zservice.entity.Trackandtrace;

public interface TrackAndTraceRepository extends CrudRepository<Trackandtrace, Long>{
	
	 @Query("SELECT t FROM Trackandtrace t where t.reference_number = :refNumber and isDeleted != 'Y' order by trackEventDateOccured desc") 
	 List<Trackandtrace> fetchTrackEventByRefNbr(@Param("refNumber") String refNumber);

	// @Query(nativeQuery = true, value="SELECT reference_number,barcodelabelnumber,trackEventDateOccured,trackEventDetails FROM Trackandtrace where substring(barcodelabelnumber,19,23) = :articleID") 
	 @Query("SELECT t FROM Trackandtrace t where SUBSTRING(barcodelabelNumber,19,40) = :articleID and isDeleted != 'Y' order by trackEventDateOccured desc")
	 List<Trackandtrace> fetchTrackEventByArticleID(String articleID);

	 @Procedure(name = "update-tracking")
	 void updateTracking();

	 @Query(nativeQuery = true,value="SELECT DISTINCT barcodelabelNumber FROM Trackandtrace where fileName = 'eTowerAPI' and barcodelabelNumber NOT IN \n"+
	 "(SELECT DISTINCT barcodelabelNumber FROM Trackandtrace where trackEventDetails = 'DELIVERED' and fileName = 'eTowerAPI')")
	List<String> fetchTrackingNumbersForETowerCall();

}
