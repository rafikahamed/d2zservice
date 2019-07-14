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
	 
	 @Query(nativeQuery = true,value="SELECT DISTINCT t.articleID FROM Trackandtrace t where t.fileName = 'PCA' and t.articleID NOT IN \n"+
			 "(SELECT DISTINCT t.articleID FROM Trackandtrace t where (t.trackEventDetails = 'DELIVERED' and t.fileName = 'PCA') OR\n"+
					 "(t.trackEventDetails = 'SHIPMENT ALLOCATED' AND  t.fileName = 'PCA' and t.trackEventDateOccured <= Dateadd(day,-21,Getdate())))")
			
			/* @Query(nativeQuery = true,value="SELECT DISTINCT t.articleID FROM Trackandtrace t where t.fileName = 'eTowerAPI' and t.articleID NOT IN \n"+
					 "(SELECT DISTINCT t.articleID FROM Trackandtrace t where t.trackEventDetails = 'DELIVERED' and t.fileName = 'eTowerAPI')")*/
			  List<String> fetchTrackingNumbersForPCACall();
	 

	 @Query(nativeQuery = true,value="SELECT t.reference_number FROM Trackandtrace t where substring(t.articleID, 1, 5) = '33PE9' \n" + 
		 		"AND t.trackeventdetails = 'Shipment Allocated' \n" + 
		 		"AND t.trackeventdateoccured BETWEEN Dateadd(day, -2, Getdate()) AND Dateadd(day, -1, Getdate())")
		 List<String> fetchArticleIDForFDMCall();
	 
	
	 @Query(nativeQuery = true, value="SELECT top 2000 * FROM Trackandtrace t where t.trackEventCode = 'SA' AND t.fileName = 'AUPostCreate'")
	List<Trackandtrace> fetchArticleIDForAUPost();

    
//	 @Query(nativeQuery = true, value="SELECT ArticleId\r\n" + 
//	 		"FROM   trackandtrace t\r\n" + 
//	 		"WHERE  Substring(articleid, 1, 5) = '33PE9'\r\n" + 
//	 		"       AND trackeventdetails = 'Shipment Allocated'\r\n" + 
//	 		"       AND t.trackeventdateoccured BETWEEN Dateadd(day, -14, Getdate()) AND\r\n" + 
//	 		"       Getdate() order by TrackEventDateOccured asc")
//	 List<String> getArticleId();

	 
	// @Query(nativeQuery = true, value="SELECT distinct(ArticleId) FROM   trackandtrace;")
	/* @Query(nativeQuery = true, value="SELECT DISTINCT s.articleid\r\n" + 
	 		"\r\n" + 
	 		"FROM            senderdata_master S\r\n" + 
	 		"\r\n" + 
	 		"WHERE           s.user_id='52'\r\n" + 
	 		"\r\n" + 
	 		"AND             s.mlid='33PE9'\r\n" + 
	 		"\r\n" + 
	 		"AND             s.articleid IN\r\n" + 
	 		"\r\n" + 
	 		"            (\r\n" + 
	 		"\r\n" + 
	 		"            SELECT DISTINCT t.articleid\r\n" + 
	 		"\r\n" + 
	 		"            FROM            trackandtrace t\r\n" + 
	 		"\r\n" + 
	 		"            WHERE           t.trackeventdetails = 'SHIPMENT ALLOCATED'\r\n" + 
	 		"\r\n" + 
	 		"            AND             t.filename = 'AUPost' ) AND S.articleid NOT IN\r\n" + 
	 		"\r\n" + 
	 		"                (\r\n" + 
	 		"\r\n" + 
	 		"                SELECT DISTINCT t.articleid\r\n" + 
	 		"\r\n" + 
	 		"                FROM            trackandtrace t\r\n" + 
	 		"\r\n" + 
	 		"                WHERE           (\r\n" + 
	 		"\r\n" + 
	 		"                                                t.trackeventdetails = 'DELIVERED'\r\n" + 
	 		"\r\n" + 
	 		"                                AND             t.filename = 'AUPost')\r\n" + 
	 		"\r\n" + 
	 		"                OR              (\r\n" + 
	 		"\r\n" + 
	 		"                                                t.trackeventdetails = 'SHIPMENT ALLOCATED'\r\n" + 
	 		"\r\n" + 
	 		"                                AND             t.filename = 'AUPost'\r\n" + 
	 		"\r\n" + 
	 		"                                AND             t.trackeventdateoccured <= dateadd(day,-21,getdate())))\r\n" + 
	 		"\r\n" + 
	 		"")*/
	 
	 @Query(nativeQuery = true, value="SELECT DISTINCT t.articleid  from trackandtrace t where  t.user_id ='52' "
	 		+ " AND    t.trackeventdetails = 'SHIPMENT ALLOCATED' AND  t.filename = 'AUPost'"
	 		+ "AND t.trackeventdateoccured > dateadd(day,-21,getdate())")
	 
	 List<String> getArticleId();
	 
}
