package com.d2z.d2zservice.repository;

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
	
	@Query( nativeQuery = true, value="SELECT * FROM CSTickets where status = :status and "
			+ "trackingEventDateOccured between :fromDate and :toDate and userId in (:userId)") 
	List<CSTickets> fetchEnquiry(@Param("status") String status, @Param("fromDate") String fromDate, @Param("toDate") String toDate,
									@Param("userId") Integer[] userIds);
	
	@Query( nativeQuery = true, value="SELECT * FROM CSTickets where userId in (:userId) and status = 'closed' and trackingEventDateOccured >= getdate() -14") 
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
			"				B.Attachment,\r\n" + 
			"				B.Consignee_addr1,\r\n" + 
			"				B.Consignee_Suburb,\r\n" + 
			"				B.Consignee_State,\r\n" + 
			"				B.Consignee_Postcode,\r\n" + 
			"				B.Product_Description\r\n" + 
			"FROM   (\r\n" + 
			"	SELECT DISTINCT U.client_broker_id, \r\n" + 
			"                        S.ticketid ,\r\n" + 
			"						S.ArticleID,\r\n" + 
			"						S.TrackingEventDateOccured,\r\n" + 
			"						S.Consignee_name,\r\n" + 
			"						S.Status,\r\n" + 
			"						S.Comments,\r\n" + 
			"						S.Attachment,\r\n" + 
			"						S.Consignee_addr1,\r\n" + 
			"						S.Consignee_Suburb,\r\n" + 
			"						S.Consignee_State,\r\n" + 
			"						S.Consignee_Postcode,\r\n" + 
			"						S.Product_Description\r\n" + 
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
			"						S.Attachment,\r\n" + 
			"						S.Consignee_addr1,\r\n" + 
			"						S.Consignee_Suburb,\r\n" + 
			"						S.Consignee_State,\r\n" + 
			"						S.Consignee_Postcode,\r\n" + 
			"						S.Product_Description\r\n" + 
			"        FROM   CSTickets S \r\n" + 
			"               INNER JOIN users U \r\n" + 
			"                       ON U.role_id IN ( '2' ) \r\n" + 
			"                          AND S.userid = U.user_id \r\n" + 
			"                          AND S.status = 'Open') B \r\n" + 
			"       INNER JOIN users A \r\n" + 
			"               ON A.user_id = B.client_broker_id \r\n" + 
			"ORDER  BY A.user_name ;")
	List<String> fetchOpenEnquiryDetails();
	
	
	@Modifying
	@Transactional
	@Query("update CSTickets c set c.d2zComments = :d2zComments, c.status = :status, c.sendUpdate = :sendUpdate where c.articleID = :articleID")
	int updateTicketInfo(@Param("d2zComments") String d2zComments, @Param("status") String status, 
				@Param("sendUpdate") String sendUpdate, @Param("articleID") String articleID);
	
	@Query(value="SELECT t FROM CSTickets t where status = 'closed' and trackingEventDateOccured >= getdate() -14") 
	List<CSTickets> completedEnquiryDetails();

}
