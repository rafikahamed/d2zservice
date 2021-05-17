package com.d2z.d2zservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.d2z.d2zservice.entity.TrackEvents;

public interface TrackEventsRepository extends CrudRepository<TrackEvents, Long>{

	@Query(nativeQuery=true, value= "SELECT top 1 * FROM TrackEvents t where t.articleID = :articleID and isDeleted != 'Y' order by t.trackEventDateOccured desc")
	TrackEvents fetchLatestStatus(String articleID);

	@Query("Select t FROM TrackEvents t where t.articleID in (:articleIds)")
	List<TrackEvents> fetchEventsFromTrackEvents(List<String> articleIds);
	
	@Query(nativeQuery = true,value="SELECT distinct articleid \r\n"
			+ "FROM   trackevents\r\n"
			+ "WHERE  reference_number LIKE 'NEX%'\r\n"
			+ "AND articleid NOT IN (SELECT DISTINCT articleid\r\n"
			+ "FROM   trackevents\r\n"
			+ "WHERE  trackeventcode IN ( 'DEL', 'OBD' ))\r\n"
			+ "AND trackeventdateoccured < Dateadd(day, -2, Getdate())")
	List<String> fetchPendingArticleIDs();
}
