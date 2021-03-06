package com.d2z.d2zservice.repository;

import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.d2z.d2zservice.entity.CSTickets;

public interface CSTicketsRepository extends CrudRepository<CSTickets, Long>{

	@Query( nativeQuery = true, value="SELECT NEXT VALUE FOR CSSeqnum")
	Integer fetchNextSeq();
	
	@Query( nativeQuery = true, value="SELECT * FROM CSTickets where "
			+ "enquiryOpenDate between :fromDate and :toDate and userId in (:userId) and status='open'") 
	List<CSTickets> fetchEnquiry(@Param("fromDate") String fromDate, @Param("toDate") String toDate,
									@Param("userId") Integer[] userIds);
	
	@Query( nativeQuery = true, value="SELECT * FROM CSTickets where userId in (:userId) and status='open' ") 
	List<CSTickets> fetchEnquiry(@Param("userId") Integer[] userIds);
	
	@Query( nativeQuery = true, value="SELECT * FROM CSTickets where userId in (:userId) and status = 'closed' and enquiryOpenDate >= getdate() -14") 
	List<CSTickets> fetchCompletedEnquiry(@Param("userId") Integer[] userIds);
	
	@Query( nativeQuery = true, value=" SELECT DISTINCT A.user_name, \r\n" + 
			"                B.ticketid, \r\n" + 
			"                A.user_id, \r\n" + 
			"                B.client_broker_id,\r\n" + 
			"                B.ArticleID,\r\n" + 
			"                B.TrackingEventDateOccured,\r\n" + 
			"				B.Consignee_name,\r\n" + 
			"                B.Status,\r\n" + 
			"                B.Comments,\r\n" + 
			"				B.D2ZComments,\r\n" + 
			"				B.Consignee_addr1,\r\n" + 
			"				B.Consignee_Suburb,\r\n" + 
			"				B.Consignee_State,\r\n" + 
			"				B.Consignee_Postcode,\r\n" + 
			"				B.Product_Description, B.TrackingEvent, B.EnquiryOpenDate, B.SendUpdate, B.fileName, B.ExpectedDeliveryDate, A.EmailAddress\r\n" + 
			"FROM   (\r\n" + 
			"	SELECT DISTINCT U.client_broker_id, \r\n" + 
			"                        S.ticketid ,\r\n" + 
			"						S.ArticleID,\r\n" + 
			"						S.TrackingEventDateOccured,\r\n" + 
			"						S.Consignee_name,\r\n" + 
			"						S.Status,\r\n" + 
			"						S.Comments,\r\n" + 
			"						S.D2ZComments,\r\n" + 
			"						S.Consignee_addr1,\r\n" + 
			"						S.Consignee_Suburb,\r\n" + 
			"						S.Consignee_State,\r\n" + 
			"						S.Consignee_Postcode,\r\n" + 
			"						S.Product_Description, S.TrackingEvent, S.EnquiryOpenDate, S.SendUpdate, S.fileName, S.ExpectedDeliveryDate \r\n" + 
			"        FROM CSTickets S \r\n" + 
			"               INNER JOIN users U \r\n" + 
			"                       ON U.role_id IN ( '3' ) \r\n" + 
			"                          AND S.userid = U.user_id \r\n" + 
			"                          AND S.status = 'Open' \r\n" + 
			"        UNION \r\n" + 
			"        SELECT DISTINCT U.user_id, \r\n" + 
			"                        S.ticketid,\r\n" + 
			"						S.ArticleID,\r\n" + 
			"						S.TrackingEventDateOccured,\r\n" + 
			"						S.Consignee_name,\r\n" + 
			"						S.Status,\r\n" + 
			"						S.Comments,\r\n" + 
			"						S.D2ZComments,\r\n" + 
			"						S.Consignee_addr1,\r\n" + 
			"						S.Consignee_Suburb,\r\n" + 
			"						S.Consignee_State,\r\n" + 
			"						S.Consignee_Postcode,\r\n" + 
			"						S.Product_Description, S.TrackingEvent, S.EnquiryOpenDate, S.SendUpdate, S.fileName, S.ExpectedDeliveryDate \r\n" + 
			"        FROM   CSTickets S \r\n" + 
			"               INNER JOIN users U \r\n" + 
			"                       ON U.role_id IN ( '2' ) \r\n" + 
			"                          AND S.userid = U.user_id \r\n" + 
			"                          AND S.status = 'Open') B \r\n" + 
			"       INNER JOIN users A \r\n" + 
			"               ON A.user_id = B.client_broker_id \r\n" + 
			"ORDER  BY B.EnquiryOpenDate  DESC ;")
	List<String> fetchOpenEnquiryDetails();
	
	
	@Modifying
	@Transactional
	@Query("update CSTickets c set c.d2zComments = :d2zComments, c.status = :status, c.sendUpdate = :sendUpdate where c.articleID = :articleID")
	int updateTicketInfo(@Param("d2zComments") String d2zComments, @Param("status") String status, 
				@Param("sendUpdate") String sendUpdate, @Param("articleID") String articleID);
	
	@Query(value="SELECT t FROM CSTickets t where status = 'closed' and enquiryOpenDate >= getdate() -14") 
	List<CSTickets> completedEnquiryDetails();

	@Query("SELECT distinct(c.referenceNumber) FROM CSTickets c") 
	List<String> fetchAllReferenceNumbers();
	
	@Query(value="SELECT t FROM CSTickets t where t.status = 'open'") 
	List<CSTickets> fetchCSTicketDetails();

	@Modifying
	@Transactional
	@Query("update CSTickets c set c.trackingEvent = :description, c.trackingStatus = :status, trackingEventDateOccured = :eventDate where c.articleID = :article_id")
	void updateAUCSTrackingDetails(@Param("article_id") String article_id, @Param("description") String description, @Param("status") String status, @Param("eventDate") Timestamp eventDate);
	
	@Modifying
	@Transactional
	@Query("update CSTickets c set c.trackingEvent = :status, c.trackingStatus = :status_code, trackingEventDateOccured = :eventDate where c.barcodelabelNumber = :barcodeLabel")
	void updatePFLCSTrackingDetails(@Param("barcodeLabel") String barcodeLabel, @Param("status") String status, @Param("status_code") String status_code, @Param("eventDate") Timestamp eventDate);
	
	@Modifying
	@Transactional
	@Query(nativeQuery = true, value="update CSTickets set TrackingEvent = :description, TrackingStatus = :status, TrackingEventDateOccured = :eventDate where substring(ArticleID,0,11) = :article_id")
	void updatePCACSTrackingDetails(@Param("article_id") String article_id, @Param("description") String description, @Param("status") String status, @Param("eventDate") Timestamp eventDate);
	
	
	@Query("SELECT distinct(c.proof) FROM CSTickets c where c.ticketID = :ticketId") 
	byte[] fetchPod(@Param("ticketId") String ticketId);
	
	@Modifying
	@Transactional
	@Query("update CSTickets t set t.comments = :comments, t.d2zComments = :d2zComments, t.sendUpdate = :sendUpdate, t.status = :status where t.ticketID = :ticketNumber")
	void enquiryUpdate(@Param("ticketNumber") String ticketNumber, @Param("comments") String comments, @Param("d2zComments") String d2zComments, 
			@Param("sendUpdate") String sendUpdate, @Param("status") String status);
	
	@Modifying
	@Transactional
	@Query("update CSTickets t set t.proof = :blob, t.fileName = :fileName where t.ticketID = :ticketNumber")
	void enquiryFileUpload(@Param("blob") byte[] blob, @Param("fileName") String fileName, @Param("ticketNumber") String ticketNumber);

	@Modifying
	@Transactional
	@Query("update CSTickets s set s.comments = :comments where s.ticketID = :ticketId")
	void enquiryUpdate(@Param("ticketId") String ticketId, @Param("comments") String comments);
	
	@Query("SELECT c FROM CSTickets c where c.ticketID= :ticketId")
	CSTickets fetchCSTicketDetails(@Param("ticketId") String ticketId);
	
  	@Query(nativeQuery = true, value="select\r\n" + 
			"distinct\r\n" + 
			"a.TicketID,\r\n" + 
			"a.ArticleID,\r\n" + 
			"a.ReferenceNumber,\r\n" + 
			"a.DeliveryEnquiry,\r\n" + 
			"a.pod,\r\n" + 
			"a.Comments,\r\n" + 
			"a.D2ZComments,\r\n" + 
			"a.Consignee_name,\r\n" + 
			"a.TrackingEvent,\r\n" + 
			"a.TrackingEventDateOccured,\r\n" + 
			"a.userId,\r\n" + 
			"a.Client_Broker_id,\r\n" + 
			"b.EmailAddress\r\n" + 
			"from\r\n" + 
			"[D2Z].[dbo].[csticketsbackup] a\r\n" + 
			"LEFT JOIN\r\n" + 
			"[D2Z].[dbo].[Users] b\r\n" + 
			"on a.Client_Broker_id = b.Client_Broker_id\r\n" + 
			"where\r\n" + 
			"a.Client_Broker_id is not null\r\n" + 
			"and a.status = 'open'") 
	List<Object[]> fetchOpenTicketDetails();
	
}
