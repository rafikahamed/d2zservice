package com.d2z.d2zservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.d2z.d2zservice.entity.Trackandtrace;

public interface TrackAndTraceRepository extends CrudRepository<Trackandtrace, Long>{
	
	 @Query(nativeQuery = true, value="SELECT * FROM Trackandtrace t where t.reference_number = :refNumber and t.isDeleted != 'Y' UNION SELECT * FROM Trackandtrace t where t.articleID = :refNumber and isDeleted != 'Y' order by t.trackEventDateOccured desc") 
	 List<Trackandtrace> fetchTrackEventByRefNbr(@Param("refNumber") String refNumber);

	// @Query(nativeQuery = true, value="SELECT reference_number,barcodelabelnumber,trackEventDateOccured,trackEventDetails FROM Trackandtrace where substring(barcodelabelnumber,19,23) = :articleID") 
	 //@Query("SELECT t FROM Trackandtrace t where SUBSTRING(t.barcodelabelNumber,19,40) = :articleID and isDeleted != 'Y' order by t.trackEventDateOccured desc")
	 @Query("SELECT t FROM Trackandtrace t where t.articleID = :articleID and isDeleted != 'Y' order by t.trackEventDateOccured desc")
	 List<Trackandtrace> fetchTrackEventByArticleID(String articleID);

	 @Procedure(name = "update-tracking")
	 void updateTracking();

	 @Procedure(name = "delete-duplicate")
	 void deleteDuplicates();
	 
	 @Query(nativeQuery = true,value="SELECT DISTINCT t.articleID FROM Trackandtrace t where t.fileName = 'eTowerAPI' and t.articleID NOT IN \n"+
	 "(SELECT DISTINCT t.articleID FROM Trackandtrace t where (t.trackEventDetails = 'DELIVERED' and t.fileName = 'eTowerAPI') OR\n"+
			 "(t.trackEventDetails = 'SHIPMENT ALLOCATED' AND  t.fileName = 'eTowerAPI' and t.trackEventDateOccured <= Dateadd(day,-21,Getdate())))")
	
	/* @Query(nativeQuery = true,value="SELECT DISTINCT t.articleID FROM Trackandtrace t where t.fileName = 'eTowerAPI' and t.articleID NOT IN \n"+
			 "(SELECT DISTINCT t.articleID FROM Trackandtrace t where t.trackEventDetails = 'DELIVERED' and t.fileName = 'eTowerAPI')")*/
	  List<String> fetchTrackingNumbersForETowerCall();

}
