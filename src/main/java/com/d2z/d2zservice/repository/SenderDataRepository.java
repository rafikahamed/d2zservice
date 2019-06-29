package com.d2z.d2zservice.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.d2z.d2zservice.entity.Consignments;
import com.d2z.d2zservice.entity.SenderdataMaster;

//This will be AUTO IMPLEMENTED by Spring into a Bean called FileDetailsRepository
//CRUD refers Create, Read, Update, Delete

public interface SenderDataRepository extends CrudRepository<SenderdataMaster, Long>{
	
	@Query("SELECT t FROM SenderdataMaster t") 
	List<SenderdataMaster> fetchAllData();
	 
	@Query( nativeQuery = true, value="SELECT NEXT VALUE FOR senderFileSeqNum")
	Integer fetchNextSeq();
	 
	@Procedure(name = "in_only_test")
	void inOnlyTest(@Param("Sender_file_id") String Sender_file_id);
	 
	@Query(value="SELECT DISTINCT(t.filename), t.timestamp FROM SenderdataMaster t where t.user_ID = :userId and t.isDeleted != 'Y' and t.manifest_number is null and t.sender_Files_ID like '%D2ZUI%' order by t.timestamp desc") 
	List<String> fetchFileName(@Param("userId") Integer userId);
	
	@Query(value="SELECT DISTINCT(t.filename), t.timestamp FROM SenderdataMaster t where t.user_ID = :userId and t.isDeleted != 'Y' and t.manifest_number is null and t.sender_Files_ID like '%D2ZUI%' order by t.timestamp desc") 
	List<String> fetchLabelFileName(@Param("userId") Integer userId);

	@Query("SELECT t FROM SenderdataMaster t where t.filename = :fileName and t.isDeleted != 'Y'") 
	List<SenderdataMaster> fetchConsignmentData(@Param("fileName") String fileName);
	 
	@Procedure(name = "consignee_delete")
	void consigneeDelete(@Param("Reference_number") String Reference_number);
		 
	@Query(nativeQuery = true, value="Select reference_number, consignee_name, barcodelabelnumber, carrier from senderdata_master where filename=:fileName and manifest_number is null and IsDeleted != 'Y'") 
	List<String> fetchTrackingDetails(@Param("fileName") String fileName);

	
	@Query(nativeQuery = true, value="Select reference_number from senderdata_master t where t.isDeleted = 'N'") 
	List<String> fetchAllReferenceNumbers();
	 
	@Query(nativeQuery = true, value="Select reference_number, datamatrix, articleId, BarcodelabelNumber, Carrier from senderdata_master t where sender_Files_ID=:senderFileID") 
	List<String> fetchBySenderFileId(@Param("senderFileID") String senderFileID);
	 
	@Query(nativeQuery = true, value="SELECT reference_number, consignee_name, consignee_addr1, consignee_Suburb, consignee_State, consignee_Postcode, consignee_Phone,\n" + 
	 		" weight, shipper_Name, shipper_Addr1, shipper_City, shipper_State, shipper_Country,\n" + 
	 		" shipper_Postcode, barcodelabelNumber, datamatrix, injectionState, sku, labelSenderName, deliveryInstructions, consigneeCompany, carrier, consignee_addr2,returnAddress1,returnAddress2,Product_Description,Servicetype FROM senderdata_master\n" + 
	 		" WHERE reference_number IN (:refBarNum) and isDeleted != 'Y'" + 
	 		" UNION\n" + 
	 		" SELECT reference_number, consignee_name, consignee_addr1, consignee_Suburb, consignee_State, consignee_Postcode, consignee_Phone,\n" + 
	 		" weight, shipper_Name, shipper_Addr1, shipper_City, shipper_State, shipper_Country,\n" + 
	 		" shipper_Postcode, barcodelabelNumber, datamatrix, injectionState, sku, labelSenderName, deliveryInstructions,consigneeCompany, carrier,consignee_addr2,returnAddress1,returnAddress2,Product_Description,Servicetype FROM senderdata_master\n" + 
	 		" WHERE articleId IN (:refBarNum) and isDeleted != 'Y'") 
	List<String> fetchTrackingLabel(@Param("refBarNum") List<String> refBarNum);
	 
	@Procedure(name = "manifest_creation")
	void manifestCreation(@Param("ManifestNumber") String ManifestNumber, @Param("Reference_number") String Reference_number);

	@Query("Select t from SenderdataMaster t where t.reference_number = :reference_number") 
	SenderdataMaster fetchByReferenceNumbers(@Param("reference_number") String referenceNumber);
	 
	@Procedure(name = "shipment_allocation")
	void allocateShipment(@Param("Reference_number") String Reference_number, @Param("Airwaybill") String Airwaybill);

	@Procedure(name = "update_cubicWeight")
	void updateCubicWeight();
	
	@Procedure(name = "rates")
	void updateRates();
	
	@Query("SELECT t FROM SenderdataMaster t where t.filename = :fileName and t.isDeleted != 'Y' and t.manifest_number is null") 
	List<SenderdataMaster> fetchManifestData(@Param("fileName") String fileName);

	@Query("SELECT t FROM SenderdataMaster t where t.user_ID IN (:userId) and t.airwayBill = :shipmentNumber and t.isDeleted != 'Y'") 
	List<SenderdataMaster> fetchShipmentData(@Param("shipmentNumber") String shipmentNumber,@Param("userId") List<Integer> userId);

	@Query(nativeQuery = true, value="SELECT DISTINCT t.manifest_number FROM senderdata_master t where t.user_ID IN (:userId) and t.AirwayBill is null and t.isDeleted = 'N'") 
	List<String> fetchManifestNumber(@Param("userId") List<Integer> userId);
	
	 @Query("SELECT t FROM SenderdataMaster t where t.user_ID IN (:userId) and t.manifest_number = :manifestNumber and t.airwayBill is null") 
	 List<SenderdataMaster> fetchConsignmentByManifest(@Param("manifestNumber") String manifestNumber, @Param("userId") List<Integer> userId);

	 @Query("SELECT t FROM SenderdataMaster t where t.reference_number in :referenceNumbers and (t.airwayBill is not null or t.isDeleted = 'Y')") 
	 List<SenderdataMaster> findRefNbrByShipmentNbr(@Param("referenceNumbers") String[] referenceNumbers);
	 
	 @Query("SELECT DISTINCT(t.airwayBill), t.timestamp FROM SenderdataMaster t where t.user_ID IN (:userId) and t.sender_Files_ID like '%D2ZUI%' order by t.timestamp desc") 
	 List<String> fetchShipmentList(@Param("userId") List<Integer> userId);
	 
	 @Query("SELECT DISTINCT t.airwayBill FROM SenderdataMaster t where t.sender_Files_ID like '%D2ZAPI%' ") 
	 List<String> fetchApiShipmentList();
	 
	 @Query(nativeQuery = true, value="select reference_number, substring(barcodelabelnumber,19,23), consignee_name, consignee_Postcode, weight, Shipper_Name from senderdata_master \n" + 
	 		"where user_id in (select user_id from users where CompanyName=:companyName)\n" + 
	 		"and sender_files_id like '%API%' and airwaybill is null and isDeleted != 'Y'")
	 List<String> fetchDirectInjectionData(@Param("companyName") String companyName);

	/* @Query("SELECT t FROM SenderdataMaster t where t.isDeleted = 'Y' and t.timestamp between :fromTimestamp and :toTimestamp") 
	 List<SenderdataMaster> fetchDeletedConsignments(@Param("fromTimestamp") String fromTimestamp , @Param("toTimestamp") String toTimestamp);*/
	 
	 
	 @Query(nativeQuery = true, value = "select A.user_name,B.reference_number,B.ArticleID \r\n" + 
	 		"	 from (SELECT U.Client_Broker_id,S.reference_number,S.ArticleID\r\n" + 
	 		"	 FROM SENDERDATA_MASTER S\r\n" + 
	 		"	 INNER JOIN Users U\r\n" + 
	 		"	 ON  S.isdeleted = 'Y' and U.role_id='3' and S.User_ID=U.User_Id \r\n" + 
	 		"	  AND s.timestamp BETWEEN :fromTimestamp AND :toTimestamp) B\r\n" + 
	 		"	 inner join Users A\r\n" + 
	 		"	 on A.User_Id=B.Client_Broker_id\r\n" + 
	 		"	 order by A.User_Name")
	 /*@Query(nativeQuery = true, value = "select D.user_name , C.reference_number,C.ArticleID from\r\n" + 
	 		"(select A.Client_Broker_id,B.Reference_number,B.ArticleID\r\n" + 
	 		"from (SELECT  senderdata0_.User_ID, senderdata0_.Reference_number,trackandtr1_ .ArticleID\r\n" + 
	 		"FROM   dbo.senderdata_master senderdata0_ \r\n" + 
	 		"       INNER JOIN trackandtrace trackandtr1_ \r\n" + 
	 		"               ON senderdata0_.reference_number = trackandtr1_.reference_number \r\n" + 
	 		"WHERE " + 
	 		"        trackandtr1_.isdeleted ='Y' \r\n" + 
	 		"       AND ( trackandtr1_.trackeventdateoccured BETWEEN :fromTimestamp AND :toTimestamp )\r\n" + 
	 		"	    )B\r\n" + 
	 		"inner join Users A\r\n" + 
	 		"on A.User_Id = B.User_ID\r\n" + 
	 		"and A.role_id='3') C\r\n" + 
	 		"inner join Users D\r\n" + 
	 		"on D.User_Id = C.Client_Broker_id")*/
	 		
	 List<String> fetchDeletedConsignments(@Param("fromTimestamp") String fromTimestamp , @Param("toTimestamp") String toTimestamp);
	 

	 @Query("SELECT s FROM SenderdataMaster s JOIN s.trackAndTrace t where t.trackEventDetails = 'CONSIGNMENT CREATED' and t.isDeleted != 'Y' and t.trackEventDateOccured between :fromTime and :toTime") 
	 List<SenderdataMaster> exportConsignments(@Param("fromTime") String fromTime , @Param("toTime") String toTime);

	 @Query(nativeQuery = true, value = "SELECT D.user_name, \r\n" + 
	 		"       C.reference_number,C.value,C.shipped_Quantity,\r\n" + 
	 		"C.consignee_name,C.consignee_addr1,C.consignee_Suburb,\r\n" + 
	 		"C.consignee_State,C.consignee_Postcode,C.consignee_Phone,\r\n" + 
	 		"C.product_Description,C.shipper_Country,C.weight,C.barcodelabelNumber,\r\n" + 
	 		"C.servicetype,C.currency\r\n" + 
	 		"FROM   (SELECT A.client_broker_id, \r\n" + 
	 		"               B.reference_number,B.value,B.shipped_Quantity,\r\n" + 
	 		"B.consignee_name,B.consignee_addr1,B.consignee_Suburb,\r\n" + 
	 		"B.consignee_State,B.consignee_Postcode,B.consignee_Phone,\r\n" + 
	 		"B.product_Description,B.shipper_Country,B.weight,B.barcodelabelNumber,\r\n" + 
	 		"B.servicetype,B.currency\r\n" + 
	 		"        FROM   (SELECT senderdata0_.reference_number,senderdata0_.value,\r\n" + 
	 		"senderdata0_.shipped_Quantity,senderdata0_.consignee_name,senderdata0_.consignee_addr1,\r\n" + 
	 		"senderdata0_.consignee_Suburb,senderdata0_.consignee_State,\r\n" + 
	 		"senderdata0_.consignee_Postcode,senderdata0_.consignee_Phone,\r\n" + 
	 		"senderdata0_.product_Description,senderdata0_.shipper_Country,senderdata0_.User_ID,\r\n" + 
	 		"senderdata0_.weight,senderdata0_.barcodelabelNumber,senderdata0_.servicetype,senderdata0_.currency\r\n" + 
	 		"  FROM   dbo.senderdata_master senderdata0_ \r\n" + 
	 		"                       INNER JOIN trackandtrace trackandtr1_ \r\n" + 
	 		"                               ON senderdata0_.reference_number = \r\n" + 
	 		"                                  trackandtr1_.reference_number \r\n" + 
	 		"                WHERE  trackandtr1_.trackeventdetails = 'SHIPMENT ALLOCATED' \r\n" + 
	 		"                       AND trackandtr1_.isdeleted <> 'Y' \r\n" + 
	 		"                       AND ( trackandtr1_.trackeventdateoccured BETWEEN :fromTime AND :toTime  \r\n" + 
	 		"                           ))B \r\n" + 
	 		"               INNER JOIN users A \r\n" + 
	 		"                       ON A.user_id = B.user_id \r\n" + 
	 		"                          AND A.role_id = '3') C \r\n" + 
	 		"       INNER JOIN users D \r\n" + 
	 		"               ON D.user_id = C.client_broker_id ") 
	 		
	 List<Object> exportShipment(@Param("fromTime") String fromTime , @Param("toTime") String toTime);
	/* @Query("SELECT s FROM SenderdataMaster s JOIN s.trackAndTrace t where t.trackEventDetails = 'SHIPMENT ALLOCATED' and t.isDeleted != 'Y' and t.trackEventDateOccured between :fromTime and :toTime") 
	List<SenderdataMaster> exportShipment(@Param("fromTime") String fromTime , @Param("toTime") String toTime);*/
	 @Query(nativeQuery = true, value = "SELECT B.user_name, \r\n" + 
	 		"       C.reference_number,C.value,C.shipped_Quantity,\r\n" + 
	 		"C.consignee_name,C.consignee_addr1,C.consignee_Suburb,\r\n" + 
	 		"C.consignee_State,C.consignee_Postcode,C.consignee_Phone,\r\n" + 
	 		"C.product_Description,C.shipper_Country,C.weight,C.barcodelabelNumber,\r\n" + 
	 		"C.servicetype,C.currency\r\n" + 
	 		"FROM   (SELECT U.client_broker_id, \r\n" + 
	 		"              senderdata0_.reference_number,senderdata0_.value,\r\n" + 
	 		"senderdata0_.shipped_Quantity,senderdata0_.consignee_name,senderdata0_.consignee_addr1,\r\n" + 
	 		"senderdata0_.consignee_Suburb,senderdata0_.consignee_State,\r\n" + 
	 		"senderdata0_.consignee_Postcode,senderdata0_.consignee_Phone,\r\n" + 
	 		"senderdata0_.product_Description,senderdata0_.shipper_Country,senderdata0_.User_ID,\r\n" + 
	 		"senderdata0_.weight,senderdata0_.barcodelabelNumber,senderdata0_.servicetype,senderdata0_.currency\r\n" + 
	 		"  FROM   dbo.senderdata_master senderdata0_ \r\n" + 
	 		"                       INNER JOIN users U   ON u.user_id = senderdata0_.user_id \r\n" + 
	 		"                AND senderdata0_.airwaybill IS NULL \r\n" + 
	 		"                          AND senderdata0_.isdeleted = 'N' \r\n" + 
	 		"                          AND senderdata0_.timestamp BETWEEN :fromTime AND :toTime \r\n" + 
	 		"                           )C \r\n" + 
	 		"               INNER JOIN users B \r\n" + 
	 		"                      ON C.client_broker_id = B.user_id ")
	 
	// @Query("SELECT s FROM SenderdataMaster s where s.airwayBill IS NULL and s.isDeleted = 'N'  and s.timestamp between :fromTime and :toTime") 
		
		 List<Object> exportNonShipment(@Param("fromTime") String fromTime , @Param("toTime") String toTime);
	 @Query("SELECT new com.d2z.d2zservice.entity.Consignments(t.reference_number,t.injectionState, t.weight) FROM SenderdataMaster t where t.reference_number in :referenceNumbers")
	 List<Consignments> fetchConsignmentsForBagging(List<String> referenceNumbers);

	 @Query("SELECT count(*) FROM SenderdataMaster t where t.user_ID = :userId and t.isDeleted = 'N'") 
	 String fecthConsignmentsCreated(@Param("userId") Integer userId);
	 
	 @Query("SELECT count(*) FROM SenderdataMaster t where t.user_ID = :userId and t.manifest_number is not null") 
	 String fetchConsignmentsManifested(@Param("userId") Integer userId);

	 @Query("SELECT count (distinct t.manifest_number) FROM SenderdataMaster t where t.user_ID = :userId and t.manifest_number is not null") 
	 String fetchConsignmentsManifests(@Param("userId") Integer userId);
	 
	 @Query("SELECT count(*) FROM SenderdataMaster t where t.user_ID = :userId and t.isDeleted = 'Y'") 
	 String fetchConsignmentsDeleted(@Param("userId") Integer userId);

	 @Query("SELECT count(*) FROM SenderdataMaster t where t.user_ID = :userId and t.isDeleted = 'N' and t.status = 'Delivered'") 
	 String fetchConsignmentDelivered(@Param("userId") Integer userId);
	 
	 @Query("SELECT t.reference_number FROM SenderdataMaster t where  t.user_ID = :userId")
	 List<String> fetchReferenceNumberByUserId(@Param("userId") Integer userId);
	 
	 @Procedure(name = "deleteConsignment")
	 void deleteConsignments(@Param("Reference_number") String Reference_number);
	 
	 @Query("SELECT s FROM SenderdataMaster s  where s.reference_number in (:referenceNumbers) ")
	 List<SenderdataMaster> fetchConsignmentsByRefNbr(@Param("referenceNumbers") List<String> referenceNumbers);
	 
	@Query("SELECT s FROM SenderdataMaster s JOIN s.consignmentCount c where s.reference_number in (:referenceNumbers) and s.mlid = c.mlid and c.supplier = :supplier")
	 List<SenderdataMaster> fetchDataBasedonSupplier(@Param("referenceNumbers") List<String> referenceNumbers,@Param("supplier") String supplier);

	 @Query("SELECT s.articleId FROM SenderdataMaster s JOIN s.consignmentCount c where s.reference_number in (:referenceNumbers) and s.mlid = c.mlid and c.supplier = 'eTower'")
	 List<String> fetchDataForEtowerForeCastCall(@Param("referenceNumbers") String[] referenceNumbers);
	 
	 @Query("SELECT distinct(s.airwayBill) FROM SenderdataMaster s where s.user_ID = :userId")
	 List<String> getBrokerShipmentList(@Param("userId") Integer userId);


	 @Query("SELECT s FROM SenderdataMaster s where s.reference_number in (:referenceNumbers) and (s.manifest_number is not null or s.airwayBill is not null)") 
	 List<SenderdataMaster> fetchConsignmentsManifestShippment(@Param("referenceNumbers") List<String> referenceNumbers);


	@Query(nativeQuery = true, value="SELECT DISTINCT A.user_name, B.airwaybill FROM (SELECT DISTINCT U.client_broker_id, S.airwaybill FROM Senderdata_Invoicing S INNER JOIN users U \n" + 
			"ON S.airwaybill IS NOT NULL AND U.role_id = '3' AND S.user_id = U.user_id AND S.Invoiced = 'N') B INNER JOIN users A ON A.user_id = B.client_broker_id ORDER  BY A.user_name")
	List<String> fetchSenderShipmenntData();

	
	@Query(nativeQuery = true, value="SELECT DISTINCT A.user_name, B.airwaybill FROM \n" + 
			"(\n" + 
			"SELECT DISTINCT U.client_broker_id, S.airwaybill FROM Senderdata_Invoicing S INNER JOIN users U \n" + 
			"ON S.airwaybill IS NOT NULL AND U.role_id = '3' AND S.user_id = U.user_id AND S.Invoiced = 'Y' and S.Billed = 'N'\n" + 
			") \n" + 
			"B INNER JOIN users A ON A.user_id = B.client_broker_id ORDER  BY A.user_name;")
	List<String> brokerInvoiced();
	
	@Query(nativeQuery = true, value="select D.user_name, C.airwayBill, C.articleId, C.reference_number, C.d2zRate, C.weight, C.brokerRate from\n" + 
			"(\n" + 
			"	SELECT B.client_broker_id, A.User_ID, A.airwayBill, A.articleId, A.reference_number, A.d2zRate, A.weight, A.brokerRate  FROM\n" + 
			"	(\n" + 
			"		select User_ID, airwayBill, articleId, reference_number, d2zRate, weight, brokerRate \n" + 
			"			from Senderdata_Invoicing where articleId = :articleNo or reference_number = :refrenceNumber\n" + 
			"	) A INNER JOIN users B ON A.user_id = B.user_id\n" + 
			") C INNER JOIN users D on C.client_broker_id = D.user_id \n" + 
			"")
	List<String> reconcileData(@Param("articleNo") String articleNo, @Param("refrenceNumber") String refrenceNumber);

//	@Procedure(name = "InvoiceUpdate")
//	void approvedInvoice(@Param("Indicator") String Indicator, @Param("Airwaybill") String Airwaybill);
	
	@Procedure(name = "reconcilerates")
	void reconcilerates(@Param("Reference_number") String Reference_number);
	
	@Query(nativeQuery = true, value="SELECT DISTINCT A.user_name, B.airwayBill, B.ArticleId, B.reference_number, B.D2ZRate FROM\n" + 
			"( \n" + 
			"SELECT DISTINCT U.client_broker_id, S.airwaybill, S.ArticleId, S.reference_number, S.D2ZRate FROM Senderdata_Invoicing S INNER JOIN users U  \n" + 
			"ON s.reference_number not in ( select distinct reference_number from reconcile where Reference_number is not null ) AND U.role_id = '3' AND S.user_id = U.user_id \n" + 
			") \n" + 
			"B INNER JOIN users A ON A.user_id = B.client_broker_id ORDER  BY A.user_name")
	List<String> fetchNotBilled();
	
	@Query(nativeQuery = true, value="SELECT DISTINCT S.articleid  AS TrackingNumber, S.reference_number AS reference, S.consignee_postcode AS postcode, S.weight AS Weight,\n" + 
			"				  B.rate AS postage, S.fuelsurcharge AS Fuelsurcharge, S.brokerrate AS total, S.servicetype AS servicetype ,S.airwaybill AS ShipmentNumber FROM Senderdata_Invoicing S\n" + 
			"				  INNER JOIN POSTCODEZONES P ON P.postcode = S.consignee_postcode AND P.suburb = S.consignee_suburb INNER JOIN BROKERRATES B\n" + 
			"                ON S.airwaybill in (:airwayBill) AND B.brokerusername in (:broker) AND ( S.weight BETWEEN Cast(B.minweight AS DECIMAL(18, 4)) AND\n" + 
			" 				  Cast( B.maxweight AS DECIMAL(18, 4)) ) AND S.servicetype = B.servicetype AND S.consignee_postcode = P.postcode\n" + 
			"					AND B.zoneid = P.zone AND S.Billed = :billed AND S.Invoiced = :invoiced ")
	List<String> downloadInvoice(@Param("broker") List<String> broker, @Param("airwayBill")  List<String> airwayBill, @Param("billed") String billed,
									@Param("invoiced") String invoiced);

	@Query("SELECT t.user_ID FROM SenderdataMaster t where  t.reference_number = :reference_number")
	 Integer fetchUserIdByReferenceNumber( String reference_number);


	@Query("SELECT t FROM SenderdataMaster t where t.reference_number in (:refNbrs) and mlid = '33PE9' and t.isDeleted = 'N'")
	List<SenderdataMaster> fetchDataForAusPost(List<String>  refNbrs);
	
	@Query("SELECT t.cubic_Weight FROM SenderdataMaster t where  t.articleId in (:articleID)")
	List<BigDecimal> fetchcubicweight(List<String> articleID);

	@Query("SELECT s.articleId FROM SenderdataMaster s where s.carrier = 'FastwayM' and s.reference_number in (:refNbrs)")
	List<String> fetchDataforPFLSubmitOrder(String[] refNbrs);

	@Modifying(flushAutomatically = true,clearAutomatically = true)
	@Transactional
	@Query("Update SenderdataMaster s set s.airwayBill = :airwayBill, s.status = 'SHIPMENT ALLOCATED', \n"+
									  "s.timestamp = :timestamp where s.reference_number IN (:referenceNumbers) and airwaybill is null and  isdeleted = 'N'")
	void updateAirwayBill(@Param("referenceNumbers") String[] referenceNumbers, @Param("airwayBill") String shipmentNumber, @Param("timestamp") String timestamp);

} 
