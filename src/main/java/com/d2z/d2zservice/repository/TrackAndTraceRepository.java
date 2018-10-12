package com.d2z.d2zservice.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.d2z.d2zservice.entity.Trackandtrace;

public interface TrackAndTraceRepository extends CrudRepository<Trackandtrace, Long>{
	
	 @Query("SELECT t FROM Trackandtrace t where t.reference_number = :refNumber and isDeleted != 'Y'") 
	 List<Trackandtrace> fetchTrackEventByRefNbr(@Param("refNumber") String refNumber);

	// @Query(nativeQuery = true, value="SELECT reference_number,barcodelabelnumber,trackEventDateOccured,trackEventDetails FROM Trackandtrace where substring(barcodelabelnumber,19,23) = :articleID") 
	 @Query("SELECT t FROM Trackandtrace t where SUBSTRING(barcodelabelNumber,19,40) = :articleID and isDeleted != 'Y'")
	 List<Trackandtrace> fetchTrackEventByArticleID(String articleID);

	 @Procedure(name = "UpdateTracking")
	void updateTracking();

	 @Query(value="SELECT 1 * FROM Trackandtrace  where reference_number = :referenceNumber and isDeleted != 'Y' order by trackEventDateOccured desc LIMIT 1", nativeQuery=true) 
	 Trackandtrace getLatestStatusByReferenceNumber(@Param("referenceNumber") String referenceNumber);

	 @Query(value="SELECT 1  * FROM Trackandtrace where SUBSTRING(barcodelabelNumber,19,40) = :articleID and isDeleted != 'Y' order by trackEventDateOccured desc LIMIT 1", nativeQuery=true)
	Trackandtrace getLatestStatusByArticleID(String articleID);

}
