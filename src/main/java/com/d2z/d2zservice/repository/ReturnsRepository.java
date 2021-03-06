package com.d2z.d2zservice.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.d2z.d2zservice.entity.Returns;

public interface ReturnsRepository extends CrudRepository<Returns, Long>{
	
	@Query( nativeQuery = true, value="select a.User_Name AS ClientName, a.Client_BrokerName, b.Consignee_name, a.Role_Id, a.User_Id, a.Client_Broker_id, "
			+ "b.carrier, b.Reference_number, b.BarcodelabelNumber, b.ArticleId, b.AirwayBill \r\n" + 
			"FROM Users a\r\n" + 
			"	  INNER JOIN\r\n" + 
			"	  (\r\n" + 
			"	  select User_ID, Consignee_name, carrier, Reference_number, BarcodelabelNumber, ArticleId, AirwayBill FROM SENDERDATA_MASTER where \r\n" + 
			"	 Reference_number = :referenceNumber or \r\n" + 
			"  BarcodelabelNumber like %:barcodeLabel% or ArticleId = :articleId or datamatrix = :barcodeLabel or BarcodelabelNumber like %:dataMatrix% ) b \r\n" + 
			"  ON A.User_ID = b.User_ID \r\n" + 
			"  and A.Role_Id = 3")
	List<String> fetchClientDetails(@Param("referenceNumber") String referenceNumber, @Param("barcodeLabel") String barcodeLabel, 
			@Param("articleId") String articleId, @Param("dataMatrix") String dataMatrix);
	
	@Query( nativeQuery = true, value="SELECT * FROM Returns where "
			+ "returnsCreatedDate between :fromDate and :toDate and User_Id in (:userId) and action is null") 
	List<Returns> fetchOutstandingDetails(@Param("fromDate") String fromDate, @Param("toDate") String toDate, @Param("userId") Integer[] userId);
	
	@Query("SELECT distinct(r.brokerName) FROM Returns r") 
	List<String> fetchReturnsBroker();

	@Query( nativeQuery = true, value="SELECT * FROM Returns where brokerName = :brokerName and "
			+ "returnsCreatedDate between :fromDate and :toDate") 
	List<Returns> returnsOutstandingDetails(@Param("fromDate") String fromDate, @Param("toDate") String toDate, @Param("brokerName") String brokerName);
	
	@Query( nativeQuery = true, value="SELECT * FROM Returns where brokerName = :brokerName and action is null") 
	List<Returns> returnsOutstandingDetailsBroker(@Param("brokerName") String brokerName);

	@Query( nativeQuery = true, value="SELECT * FROM Returns where User_Id in (:userId) and action is null") 
	List<Returns> fetchOutstandingCompleteDetails(@Param("userId") Integer[] userId);
	
	@Modifying
	@Transactional
	@Query("update Returns r set r.action = :action, r.resendRefNumber = :resendRefNumber where r.articleId = :articleId")
	void updateReturnAction(@Param("action") String action, @Param("resendRefNumber") String resendRefNumber, 
			@Param("articleId") String articleId);

	@Query( nativeQuery = true, value="SELECT * FROM Returns where action is not null and status is null") 
	List<Returns> returnsOutstanding();

	@Modifying
	@Transactional
	@Query("update Returns r set r.status = 'closed' where r.articleId = :articleId")
	void updateReturnStatus(@Param("articleId") String articleId);

	@Query("Select distinct(referenceNumber) from Returns") 
	List<String> fetchAllReferenceNumbers();
	
	@Procedure(name = "updatereturnsClientIdDetails")
	void updateReturnsClientDetails();

	@Query( nativeQuery = true, value="SELECT * FROM Returns where action is not null and status is null and carrier != 'FastwayM'")
	List<Returns> returnsOutstandingExceptFastway();
	
	@Query( nativeQuery = true, value="select\r\n" + 
			"distinct\r\n" + 
			"a.ArticleId,\r\n" + 
			"a.ReferenceNumber,\r\n" + 
			"a.ConsigneeName,\r\n" + 
			"a.ClientName,\r\n" + 
			"a.ReturnReason,\r\n" + 
			"a.airwaybill,\r\n" + 
			"a.ReturnsCreatedDate,\r\n" + 
			"a.User_Id,\r\n" + 
			"a.Client_Broker_id,\r\n" + 
			"b.EmailAddress\r\n" + 
			"from\r\n" + 
			"[D2Z].[dbo].[Returns] a\r\n" + 
			"LEFT JOIN\r\n" + 
			"[D2Z].[dbo].[Users] b\r\n" + 
			"on a.Client_Broker_id = b.Client_Broker_id\r\n" + 
			"where\r\n" + 
			"a.Client_Broker_id is not null\r\n" + 
			"and action is null")
	List<Object[]> fetchReturnsDetails();
	
}
