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
import com.d2z.d2zservice.model.PFLSubmitOrderData;

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

	@Query("SELECT t FROM SenderdataMaster t where t.filename = :fileName and t.isDeleted != 'Y' order by rowId asc") 
	List<SenderdataMaster> fetchConsignmentData(@Param("fileName") String fileName);
	 
	@Procedure(name = "consignee_delete")
	void consigneeDelete(@Param("Reference_number") String Reference_number);
		 
	@Query(nativeQuery = true, value="Select reference_number, consignee_name, barcodelabelnumber, carrier from senderdata_master where filename=:fileName and manifest_number is null and IsDeleted != 'Y'") 
	List<String> fetchTrackingDetails(@Param("fileName") String fileName);

	
	@Query(nativeQuery = true, value="Select distinct(reference_number) from senderdata_master t where t.isDeleted = 'N'") 
	List<String> fetchAllReferenceNumbers();
	 
	@Query(nativeQuery = true, value="Select reference_number, datamatrix, articleId, BarcodelabelNumber, Carrier,injectionState from senderdata_master t where sender_Files_ID=:senderFileID") 
	List<String> fetchBySenderFileId(@Param("senderFileID") String senderFileID);
	 
	@Query(nativeQuery = true, value="SELECT reference_number, consignee_name, consignee_addr1, consignee_Suburb, consignee_State, consignee_Postcode, consignee_Phone,\n" + 
		" weight, shipper_Name, shipper_Addr1, shipper_City, shipper_State, shipper_Country,\n" + 
		" shipper_Postcode, barcodelabelNumber, datamatrix, injectionState, sku, labelSenderName, deliveryInstructions, consigneeCompany, carrier, consignee_addr2,returnAddress1,returnAddress2,Product_Description,Servicetype,User_ID FROM senderdata_master\n" + 
		" WHERE reference_number IN (:refBarNum) and isDeleted != 'Y'" + 
		" UNION\n" + 
		" SELECT reference_number, consignee_name, consignee_addr1, consignee_Suburb, consignee_State, consignee_Postcode, consignee_Phone,\n" + 
		" weight, shipper_Name, shipper_Addr1, shipper_City, shipper_State, shipper_Country,\n" + 
		" shipper_Postcode, barcodelabelNumber, datamatrix, injectionState, sku, labelSenderName, deliveryInstructions,consigneeCompany, carrier,consignee_addr2,returnAddress1,returnAddress2,Product_Description,Servicetype,User_ID FROM senderdata_master\n" + 
	    " WHERE articleId IN (:refBarNum) and isDeleted != 'Y'") 

    List<String> fetchTrackingLabel(@Param("refBarNum") List<String> refBarNum);

	@Query(nativeQuery = true, value="SELECT reference_number, consignee_name, consignee_addr1, consignee_Suburb, consignee_State, consignee_Postcode, consignee_Phone,\n" + 
	 		" weight, shipper_Name, shipper_Addr1, shipper_City, shipper_State, shipper_Country,\n" + 
	 		" shipper_Postcode, barcodelabelNumber, datamatrix, injectionState, sku, labelSenderName, deliveryInstructions, consigneeCompany, carrier, consignee_addr2,returnAddress1,returnAddress2,Product_Description,Servicetype,User_ID FROM senderdata_master\n" + 
	 		" WHERE articleId IN (:refBarNum) and isDeleted != 'Y'") 
	List<String> fetchTrackingLabelByArticleId(@Param("refBarNum") List<String> refBarNum);
	 
	@Query(nativeQuery = true, value="SELECT reference_number, consignee_name, consignee_addr1, consignee_Suburb, consignee_State, consignee_Postcode, consignee_Phone,\n" + 
	 		" weight, shipper_Name, shipper_Addr1, shipper_City, shipper_State, shipper_Country,\n" + 
	 		" shipper_Postcode, barcodelabelNumber, datamatrix, injectionState, sku, labelSenderName, deliveryInstructions, consigneeCompany, carrier, consignee_addr2,returnAddress1,returnAddress2,Product_Description,Servicetype,User_ID FROM senderdata_master\n" + 
	 		" WHERE reference_number IN (:refBarNum) and isDeleted != 'Y'") 
	List<String> fetchTrackingLabelByReferenceNbr(@Param("refBarNum") List<String> refBarNum);
	 
//	@Procedure(name = "manifest_creation")
	@Modifying(flushAutomatically = true,clearAutomatically = true)
	@Transactional
	@Query("Update SenderdataMaster s set s.manifest_number = :ManifestNumber where s.reference_number IN (:Reference_number) and  isdeleted = 'N'")
		void manifestCreation(@Param("ManifestNumber") String ManifestNumber, @Param("Reference_number") String[] Reference_number);

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

	@Query("SELECT t FROM SenderdataMaster t where t.user_ID IN (:userId) and t.airwayBill = :shipmentNumber and t.isDeleted = 'N'") 
	List<SenderdataMaster> fetchShipmentData(@Param("shipmentNumber") String shipmentNumber,@Param("userId") List<Integer> userId);
	
	@Query("SELECT t FROM SenderdataMaster t where t.user_ID IN (:userId) and t.articleId IN (:articleid) and t.isDeleted = 'N'") 
	List<SenderdataMaster> fetchShipmentDatabyArticleId(@Param("articleid")List <String> shipmentNumber,@Param("userId") List<Integer> userId);
	
	@Query("SELECT t FROM SenderdataMaster t where t.user_ID IN (:userId) and t.reference_number IN (:referencenumber) and t.isDeleted = 'N'") 
	List<SenderdataMaster> fetchShipmentDatabyReference(@Param("referencenumber") List<String> shipmentNumber,@Param("userId") List<Integer> userId);
	
	@Query("SELECT t FROM SenderdataMaster t where t.user_ID IN (:userId) and t.barcodelabelNumber IN (:barcodelabelNumber) and t.isDeleted = 'N'") 
	List<SenderdataMaster> fetchShipmentDatabyBarcode(@Param("barcodelabelNumber") List<String> shipmentNumber,@Param("userId") List<Integer> userId);

	@Query(nativeQuery = true, value="SELECT DISTINCT t.manifest_number FROM senderdata_master t where t.user_ID IN (:userId) and t.AirwayBill is null and t.isDeleted = 'N'") 
	List<String> fetchManifestNumber(@Param("userId") List<Integer> userId);
	
	 @Query("SELECT t FROM SenderdataMaster t where t.user_ID IN (:userId) and t.manifest_number = :manifestNumber and t.airwayBill is null") 
	 List<SenderdataMaster> fetchConsignmentByManifest(@Param("manifestNumber") String manifestNumber, @Param("userId") List<Integer> userId);

	 @Query("SELECT t FROM SenderdataMaster t where t.reference_number in :referenceNumbers and (t.airwayBill is not null or t.isDeleted = 'Y')") 
	 List<SenderdataMaster> findRefNbrByShipmentNbr(@Param("referenceNumbers") String[] referenceNumbers);
	 
	 @Query("SELECT DISTINCT(t.airwayBill), t.timestamp FROM SenderdataMaster t where t.user_ID IN (:userId) and t.isDeleted = 'N' order by t.timestamp desc") 
	 List<String> fetchShipmentList(@Param("userId") List<Integer> userId);
	 
	 @Query("SELECT DISTINCT t.airwayBill FROM SenderdataMaster t where t.sender_Files_ID like '%D2ZAPI%' ") 
	 List<String> fetchApiShipmentList();
	 
	 @Query(nativeQuery = true, value="select reference_number, substring(barcodelabelnumber,19,23), consignee_name, consignee_Postcode, weight, Shipper_Name from senderdata_master \n" + 
	 		"where user_id in (select user_id from users where CompanyName=:companyName)\n" + 
	 		"and sender_files_id like '%API%' and airwaybill is null and isDeleted != 'Y'")
	 List<String> fetchDirectInjectionData(@Param("companyName") String companyName);

	/* @Query("SELECT t FROM SenderdataMaster t where t.isDeleted = 'Y' and t.timestamp between :fromTimestamp and :toTimestamp") 
	 List<SenderdataMaster> fetchDeletedConsignments(@Param("fromTimestamp") String fromTimestamp , @Param("toTimestamp") String toTimestamp);*/
	 
	 
	 @Query(nativeQuery = true, value = "SELECT A.Client_BrokerName, \r\n" + 
	 		"       S.reference_number, \r\n" + 
	 		"       S.articleid,\r\n" + 
	 		"	   S.Timestamp\r\n" + 
	 		"FROM   senderdata_master S \r\n" + 
	 		"               INNER JOIN users A \r\n" + 
	 		"                       ON S.isdeleted = 'Y' \r\n" + 
	 		"                          AND S.user_id = A.user_id \r\n" + 
	 		"                          AND s.timestamp BETWEEN \r\n" + 
	 		"                              :fromTimestamp AND :toTimestamp\r\n" + 
	 		"ORDER  BY A.Client_BrokerName\r\n"
	 		)
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
	 

	 //@Query("SELECT s FROM SenderdataMaster s JOIN s.trackAndTrace t where t.trackEventDetails = 'CONSIGNMENT CREATED' and t.isDeleted != 'Y' and t.trackEventDateOccured between :fromTime and :toTime") 
	 @Query(nativeQuery = true, value ="SELECT A.Client_BrokerName,   B.articleid,B.reference_number,  B.consignee_postcode,B.weight, B.servicetype, B.airwayBill ,   B.TrackEventDateOccured, \n"
	 		+ " B.value,	 B.consignee_name, B.consignee_addr1,B.consignee_addr2, B.consignee_suburb, B.consignee_state, B.product_description\n" + 
	 		"FROM   \n" + 
	 		"(SELECT S.reference_number, S.value, S.consignee_name, S.consignee_addr1, S.consignee_addr2, S.consignee_suburb, S.consignee_state, "
	 		+ "S.consignee_postcode, S.product_description, S.user_id, S.airwayBill, S.weight, S.servicetype, S.articleid, T.TrackEventDateOccured \n" + 
	 		"FROM  senderdata_master S INNER JOIN trackandtrace T ON S.reference_number = T.reference_number \n" + 
	 		"WHERE  T.trackeventdetails = 'CONSIGNMENT CREATED' AND T.isdeleted <> 'Y' AND (T.trackEventDateOccured between :fromTime and :toTime)) B \n" + 
	 		"INNER JOIN users A ON A.user_id = B.user_id AND A.role_id = '3'")
	 List<Object> exportConsignments(@Param("fromTime") String fromTime , @Param("toTime") String toTime);
	 
	 //@Query("SELECT s FROM SenderdataMaster s JOIN s.trackAndTrace t where t.trackEventDetails = 'CONSIGNMENT CREATED' and t.isDeleted != 'Y' and t.reference_number in (:Ref)") 
	 @Query(nativeQuery = true, value ="SELECT A.Client_BrokerName,   B.articleid,B.reference_number,  B.consignee_postcode,B.weight, B.servicetype, B.airwayBill ,   B.TrackEventDateOccured, \n"
		 		+ " B.value,	 B.consignee_name, B.consignee_addr1,B.consignee_addr2, B.consignee_suburb, B.consignee_state, B.product_description\n" + 
		 		"FROM   \n" + 
		 		"(SELECT S.reference_number, S.value, S.consignee_name, S.consignee_addr1, S.consignee_addr2, S.consignee_suburb, S.consignee_state, "
		 		+ "S.consignee_postcode, S.product_description, S.user_id, S.airwayBill, S.weight, S.servicetype, S.articleid, T.TrackEventDateOccured \n" + 
		 		"FROM  senderdata_master S INNER JOIN trackandtrace T ON S.reference_number = T.reference_number \n" + 
		 		"WHERE  T.trackeventdetails = 'CONSIGNMENT CREATED' AND T.isdeleted <> 'Y' AND T.reference_number in (:Ref)) B \n" + 
		 		"INNER JOIN users A ON A.user_id = B.user_id AND A.role_id = '3'")
		 List<Object> exportConsignmentsRef(@Param("Ref") List<String> Reference);
	 
	 //@Query("SELECT s FROM SenderdataMaster s JOIN s.trackAndTrace t where t.trackEventDetails = 'CONSIGNMENT CREATED' and t.isDeleted != 'Y' and t.articleID in (:Articleid)") 
	 @Query(nativeQuery = true, value ="SELECT A.Client_BrokerName,   B.articleid,B.reference_number,  B.consignee_postcode,B.weight, B.servicetype, B.airwayBill ,   B.TrackEventDateOccured, \n"
		 		+ " B.value,	 B.consignee_name, B.consignee_addr1,B.consignee_addr2, B.consignee_suburb, B.consignee_state, B.product_description\n" + 
		 		"FROM   \n" + 
		 		"(SELECT S.reference_number, S.value, S.consignee_name, S.consignee_addr1, S.consignee_addr2, S.consignee_suburb, S.consignee_state, "
		 		+ "S.consignee_postcode, S.product_description, S.user_id, S.airwayBill, S.weight, S.servicetype, S.articleid, T.TrackEventDateOccured \n" + 
		 		"FROM  senderdata_master S INNER JOIN trackandtrace T ON S.reference_number = T.reference_number \n" + 
		 		"WHERE  T.trackeventdetails = 'CONSIGNMENT CREATED' AND T.isdeleted <> 'Y' AND T.articleID in (:Articleid)) B \n" + 
		 		"INNER JOIN users A ON A.user_id = B.user_id AND A.role_id = '3'")
	 List<Object> exportConsignmentsArticleid(@Param("Articleid") List<String> Article);
	 
	// @Query("SELECT s FROM SenderdataMaster s JOIN s.trackAndTrace t where t.trackEventDetails = 'CONSIGNMENT CREATED' and t.isDeleted != 'Y' and t.barcodelabelNumber in (:Barcode)") 
	 @Query(nativeQuery = true, value ="SELECT A.Client_BrokerName,   B.articleid,B.reference_number,  B.consignee_postcode,B.weight, B.servicetype, B.airwayBill ,   B.TrackEventDateOccured, \n"
		 		+ " B.value,	 B.consignee_name, B.consignee_addr1,B.consignee_addr2, B.consignee_suburb, B.consignee_state, B.product_description\n" + 
		 		"FROM   \n" + 
		 		"(SELECT S.reference_number, S.value, S.consignee_name, S.consignee_addr1, S.consignee_addr2, S.consignee_suburb, S.consignee_state, "
		 		+ "S.consignee_postcode, S.product_description, S.user_id, S.airwayBill, S.weight, S.servicetype, S.articleid, T.TrackEventDateOccured \n" + 
		 		"FROM  senderdata_master S INNER JOIN trackandtrace T ON S.reference_number = T.reference_number \n" + 
		 		"WHERE  T.trackeventdetails = 'CONSIGNMENT CREATED' AND T.isdeleted <> 'Y' AND T.barcodelabelNumber in (:Barcode)) B \n" + 
		 		"INNER JOIN users A ON A.user_id = B.user_id AND A.role_id = '3'")
	 List<Object> exportConsignmentsBarcode(@Param("Barcode") List<String> Barcode);

	/* @Query(nativeQuery = true, value = "SELECT D.user_name, \r\n" + 
	 		"       C.reference_number,C.value,C.shipped_Quantity,\r\n" + 
	 		"C.consignee_name,C.consignee_addr1,C.consignee_Suburb,\r\n" + 
	 		"C.consignee_State,C.consignee_Postcode,C.consignee_Phone,\r\n" + 
	 		"C.product_Description,C.shipper_Country,C.weight,C.barcodelabelNumber,\r\n" + 
	 		"C.servicetype,C.currency,C.articleId\r\n" + 
	 		"FROM   (SELECT A.client_broker_id, \r\n" + 
	 		"               B.reference_number,B.value,B.shipped_Quantity,\r\n" + 
	 		"B.consignee_name,B.consignee_addr1,B.consignee_Suburb,\r\n" + 
	 		"B.consignee_State,B.consignee_Postcode,B.consignee_Phone,\r\n" + 
	 		"B.product_Description,B.shipper_Country,B.weight,B.barcodelabelNumber,\r\n" + 
	 		"B.servicetype,B.currency,B.articleId\r\n" + 
	 		"        FROM   (SELECT senderdata0_.reference_number,senderdata0_.value,\r\n" + 
	 		"senderdata0_.shipped_Quantity,senderdata0_.consignee_name,senderdata0_.consignee_addr1,\r\n" + 
	 		"senderdata0_.consignee_Suburb,senderdata0_.consignee_State,\r\n" + 
	 		"senderdata0_.consignee_Postcode,senderdata0_.consignee_Phone,\r\n" + 
	 		"senderdata0_.product_Description,senderdata0_.shipper_Country,senderdata0_.User_ID,\r\n" + 
	 		"senderdata0_.weight,senderdata0_.barcodelabelNumber,senderdata0_.servicetype,senderdata0_.currency,senderdata0_.articleId\r\n" + 
	 		"  FROM   dbo.senderdata_master senderdata0_ \r\n" + 
	 		"                       INNER JOIN trackandtrace trackandtr1_ \r\n" + 
	 		"                               ON senderdata0_.reference_number = \r\n" + 
	 		"                                  trackandtr1_.reference_number \r\n" + 
	 		"                WHERE  trackandtr1_.trackeventdetails = 'SHIPMENT ALLOCATED' \r\n" + 
	 		"                       AND trackandtr1_.isdeleted <> 'Y' \r\n" + 
	 		"                       AND ( trackandtr1_.reference_number in (:Ref)  \r\n" + 
	 		"                           ))B \r\n" + 
	 		"               INNER JOIN users A \r\n" + 
	 		"                       ON A.user_id = B.user_id \r\n" + 
	 		"                          AND A.role_id = '3') C \r\n" + 
	 		"       INNER JOIN users D \r\n" + 
	 		"               ON D.user_id = C.client_broker_id ") */
	 
	 @Query(nativeQuery = true, value ="SELECT A.Client_BrokerName, \r\n" + 
	 		"               B.reference_number, \r\n" + 
	 		"               B.value, \r\n" + 
	 		"               B.shipped_quantity, \r\n" + 
	 		"               B.consignee_name, \r\n" + 
	 		"               B.consignee_addr1, \r\n" +
	 		"				B.consignee_addr2, \r\n" +
	 		"               B.consignee_suburb, \r\n" + 
	 		"               B.consignee_state, \r\n" + 
	 		"               B.consignee_postcode, \r\n" + 
	 		"               B.consignee_phone, \r\n" + 
	 		"               B.product_description, \r\n" + 
	 		"               B.shipper_country, \r\n" + 
	 		"               B.weight, \r\n" + 
	 		"               B.barcodelabelnumber, \r\n" + 
	 		"               B.servicetype, \r\n" + 
	 		"               B.currency, \r\n" + 
	 		"               B.articleid,\r\n" + 
	 		"			   B.trackeventdateoccured,\r\n" + 
	 		"			   B.airwayBill\r\n" + 
	 		"        FROM   (SELECT senderdata0_.reference_number, \r\n" + 
	 		"                       senderdata0_.value, \r\n" + 
	 		"                       senderdata0_.shipped_quantity, \r\n" + 
	 		"                       senderdata0_.consignee_name, \r\n" + 
	 		"                       senderdata0_.consignee_addr1, \r\n" + 
	 		"						senderdata0_.consignee_addr2, \r\n" +
	 		"                       senderdata0_.consignee_suburb, \r\n" + 
	 		"                       senderdata0_.consignee_state, \r\n" + 
	 		"                       senderdata0_.consignee_postcode, \r\n" + 
	 		"                       senderdata0_.consignee_phone, \r\n" + 
	 		"                       senderdata0_.product_description, \r\n" + 
	 		"                       senderdata0_.shipper_country, \r\n" + 
	 		"                       senderdata0_.user_id, \r\n" + 
	 		"                       senderdata0_.weight, \r\n" + 
	 		"                       senderdata0_.barcodelabelnumber, \r\n" + 
	 		"                       senderdata0_.servicetype, \r\n" + 
	 		"                       senderdata0_.currency, \r\n" + 
	 		"                       senderdata0_.articleid,\r\n" + 
	 		"					   trackandtr1_.TrackEventDateOccured,\r\n" + 
	 		"					   senderdata0_.airwayBill\r\n" + 
	 		"                FROM   dbo.senderdata_master senderdata0_ \r\n" + 
	 		"                       INNER JOIN trackandtrace trackandtr1_ \r\n" + 
	 		"                               ON senderdata0_.reference_number = \r\n" + 
	 		"                                  trackandtr1_.reference_number \r\n" + 
	 		"                WHERE  trackandtr1_.trackeventdetails = 'SHIPMENT ALLOCATED' \r\n" + 
	 		"                       AND trackandtr1_.isdeleted <> 'Y' \r\n" + 
	 		"                       AND (trackandtr1_.reference_number in (:Ref)\r\n"+
	 		"                           ))B \r\n" + 
	 		"               INNER JOIN users A \r\n" + 
	 		"                       ON A.user_id = B.user_id \r\n" + 
	 		"                          AND A.role_id = '3'\r\n" ) 
	 	
	 List<Object> exportShipmentRef(@Param("Ref") List<String> Ref);
	 
	 
	 /*@Query(nativeQuery = true, value = "SELECT D.user_name, \r\n" + 
		 		"       C.reference_number,C.value,C.shipped_Quantity,\r\n" + 
		 		"C.consignee_name,C.consignee_addr1,C.consignee_Suburb,\r\n" + 
		 		"C.consignee_State,C.consignee_Postcode,C.consignee_Phone,\r\n" + 
		 		"C.product_Description,C.shipper_Country,C.weight,C.barcodelabelNumber,\r\n" + 
		 		"C.servicetype,C.currency,C.articleId\r\n" + 
		 		"FROM   (SELECT A.client_broker_id, \r\n" + 
		 		"               B.reference_number,B.value,B.shipped_Quantity,\r\n" + 
		 		"B.consignee_name,B.consignee_addr1,B.consignee_Suburb,\r\n" + 
		 		"B.consignee_State,B.consignee_Postcode,B.consignee_Phone,\r\n" + 
		 		"B.product_Description,B.shipper_Country,B.weight,B.barcodelabelNumber,\r\n" + 
		 		"B.servicetype,B.currency,B.articleId\r\n" + 
		 		"        FROM   (SELECT senderdata0_.reference_number,senderdata0_.value,\r\n" + 
		 		"senderdata0_.shipped_Quantity,senderdata0_.consignee_name,senderdata0_.consignee_addr1,\r\n" + 
		 		"senderdata0_.consignee_Suburb,senderdata0_.consignee_State,\r\n" + 
		 		"senderdata0_.consignee_Postcode,senderdata0_.consignee_Phone,\r\n" + 
		 		"senderdata0_.product_Description,senderdata0_.shipper_Country,senderdata0_.User_ID,\r\n" + 
		 		"senderdata0_.weight,senderdata0_.barcodelabelNumber,senderdata0_.servicetype,senderdata0_.currency,senderdata0_.articleId\r\n" + 
		 		"  FROM   dbo.senderdata_master senderdata0_ \r\n" + 
		 		"                       INNER JOIN trackandtrace trackandtr1_ \r\n" + 
		 		"                               ON senderdata0_.reference_number = \r\n" + 
		 		"                                  trackandtr1_.reference_number \r\n" + 
		 		"                WHERE  trackandtr1_.trackeventdetails = 'SHIPMENT ALLOCATED' \r\n" + 
		 		"                       AND trackandtr1_.isdeleted <> 'Y' \r\n" + 
		 		"                       AND ( trackandtr1_.articleid in (:Articleid)  \r\n" + 
		 		"                           ))B \r\n" + 
		 		"               INNER JOIN users A \r\n" + 
		 		"                       ON A.user_id = B.user_id \r\n" + 
		 		"                          AND A.role_id = '3') C \r\n" + 
		 		"       INNER JOIN users D \r\n" + 
		 		"               ON D.user_id = C.client_broker_id ") */
	 
	 @Query(nativeQuery = true, value ="SELECT A.Client_BrokerName, \r\n" + 
	 		"               B.reference_number, \r\n" + 
	 		"               B.value, \r\n" + 
	 		"               B.shipped_quantity, \r\n" + 
	 		"               B.consignee_name, \r\n" + 
	 		"               B.consignee_addr1, \r\n" +
	 		"				B.consignee_addr2, \r\n" +
	 		"               B.consignee_suburb, \r\n" + 
	 		"               B.consignee_state, \r\n" + 
	 		"               B.consignee_postcode, \r\n" + 
	 		"               B.consignee_phone, \r\n" + 
	 		"               B.product_description, \r\n" + 
	 		"               B.shipper_country, \r\n" + 
	 		"               B.weight, \r\n" + 
	 		"               B.barcodelabelnumber, \r\n" + 
	 		"               B.servicetype, \r\n" + 
	 		"               B.currency, \r\n" + 
	 		"               B.articleid,\r\n" + 
	 		"			   B.trackeventdateoccured,\r\n" + 
	 		"			   B.airwayBill\r\n" + 
	 		"        FROM   (SELECT senderdata0_.reference_number, \r\n" + 
	 		"                       senderdata0_.value, \r\n" + 
	 		"                       senderdata0_.shipped_quantity, \r\n" + 
	 		"                       senderdata0_.consignee_name, \r\n" + 
	 		"                       senderdata0_.consignee_addr1, \r\n" + 
	 		"						senderdata0_.consignee_addr2, \r\n" +
	 		"                       senderdata0_.consignee_suburb, \r\n" + 
	 		"                       senderdata0_.consignee_state, \r\n" + 
	 		"                       senderdata0_.consignee_postcode, \r\n" + 
	 		"                       senderdata0_.consignee_phone, \r\n" + 
	 		"                       senderdata0_.product_description, \r\n" + 
	 		"                       senderdata0_.shipper_country, \r\n" + 
	 		"                       senderdata0_.user_id, \r\n" + 
	 		"                       senderdata0_.weight, \r\n" + 
	 		"                       senderdata0_.barcodelabelnumber, \r\n" + 
	 		"                       senderdata0_.servicetype, \r\n" + 
	 		"                       senderdata0_.currency, \r\n" + 
	 		"                       senderdata0_.articleid,\r\n" + 
	 		"					   trackandtr1_.TrackEventDateOccured,\r\n" + 
	 		"					   senderdata0_.airwayBill\r\n" + 
	 		"                FROM   dbo.senderdata_master senderdata0_ \r\n" + 
	 		"                       INNER JOIN trackandtrace trackandtr1_ \r\n" + 
	 		"                               ON senderdata0_.reference_number = \r\n" + 
	 		"                                  trackandtr1_.reference_number \r\n" + 
	 		"                WHERE  trackandtr1_.trackeventdetails = 'SHIPMENT ALLOCATED' \r\n" + 
	 		"                       AND trackandtr1_.isdeleted <> 'Y' \r\n" + 
	 		"                       AND (trackandtr1_.articleid in (:Articleid)  \r\n"+
	 		"                           ))B \r\n" + 
	 		"               INNER JOIN users A \r\n" + 
	 		"                       ON A.user_id = B.user_id \r\n" + 
	 		"                          AND A.role_id = '3'\r\n" + 
	 		"")
		 List<Object> exportShipmentArticleid(@Param("Articleid") List<String> articleid);
	/* @Query(nativeQuery = true, value = "SELECT D.user_name, \r\n" + 
		 		"       C.reference_number,C.value,C.shipped_Quantity,\r\n" + 
		 		"C.consignee_name,C.consignee_addr1,C.consignee_Suburb,\r\n" + 
		 		"C.consignee_State,C.consignee_Postcode,C.consignee_Phone,\r\n" + 
		 		"C.product_Description,C.shipper_Country,C.weight,C.barcodelabelNumber,\r\n" + 
		 		"C.servicetype,C.currency,C.articleId\r\n" + 
		 		"FROM   (SELECT A.client_broker_id, \r\n" + 
		 		"               B.reference_number,B.value,B.shipped_Quantity,\r\n" + 
		 		"B.consignee_name,B.consignee_addr1,B.consignee_Suburb,\r\n" + 
		 		"B.consignee_State,B.consignee_Postcode,B.consignee_Phone,\r\n" + 
		 		"B.product_Description,B.shipper_Country,B.weight,B.barcodelabelNumber,\r\n" + 
		 		"B.servicetype,B.currency,B.articleId\r\n" + 
		 		"        FROM   (SELECT senderdata0_.reference_number,senderdata0_.value,\r\n" + 
		 		"senderdata0_.shipped_Quantity,senderdata0_.consignee_name,senderdata0_.consignee_addr1,\r\n" + 
		 		"senderdata0_.consignee_Suburb,senderdata0_.consignee_State,\r\n" + 
		 		"senderdata0_.consignee_Postcode,senderdata0_.consignee_Phone,\r\n" + 
		 		"senderdata0_.product_Description,senderdata0_.shipper_Country,senderdata0_.User_ID,\r\n" + 
		 		"senderdata0_.weight,senderdata0_.barcodelabelNumber,senderdata0_.servicetype,senderdata0_.currency,senderdata0_.articleId\r\n" + 
		 		"  FROM   dbo.senderdata_master senderdata0_ \r\n" + 
		 		"                       INNER JOIN trackandtrace trackandtr1_ \r\n" + 
		 		"                               ON senderdata0_.reference_number = \r\n" + 
		 		"                                  trackandtr1_.reference_number \r\n" + 
		 		"                WHERE  trackandtr1_.trackeventdetails = 'SHIPMENT ALLOCATED' \r\n" + 
		 		"                       AND trackandtr1_.isdeleted <> 'Y' \r\n" + 
		 		"                       AND ( trackandtr1_.barcodelabelnumber in (:Barcode)  \r\n" + 
		 		"                           ))B \r\n" + 
		 		"               INNER JOIN users A \r\n" + 
		 		"                       ON A.user_id = B.user_id \r\n" + 
		 		"                          AND A.role_id = '3') C \r\n" + 
		 		"       INNER JOIN users D \r\n" + 
		 		"               ON D.user_id = C.client_broker_id ") */
	 
	 @Query(nativeQuery = true, value ="SELECT A.Client_BrokerName, \r\n" + 
	 		"               B.reference_number, \r\n" + 
	 		"               B.value, \r\n" + 
	 		"               B.shipped_quantity, \r\n" + 
	 		"               B.consignee_name, \r\n" + 
	 		"               B.consignee_addr1, \r\n" +
	 		"				B.consignee_addr2, \r\n" +
	 		"               B.consignee_suburb, \r\n" + 
	 		"               B.consignee_state, \r\n" + 
	 		"               B.consignee_postcode, \r\n" + 
	 		"               B.consignee_phone, \r\n" + 
	 		"               B.product_description, \r\n" + 
	 		"               B.shipper_country, \r\n" + 
	 		"               B.weight, \r\n" + 
	 		"               B.barcodelabelnumber, \r\n" + 
	 		"               B.servicetype, \r\n" + 
	 		"               B.currency, \r\n" + 
	 		"               B.articleid,\r\n" + 
	 		"			   B.trackeventdateoccured,\r\n" + 
	 		"			   B.AirwayBill\r\n" + 
	 		"        FROM   (SELECT senderdata0_.reference_number, \r\n" + 
	 		"                       senderdata0_.value, \r\n" + 
	 		"                       senderdata0_.shipped_quantity, \r\n" + 
	 		"                       senderdata0_.consignee_name, \r\n" + 
	 		"                       senderdata0_.consignee_addr1, \r\n" + 
	 		"						senderdata0_.consignee_addr2, \r\n" +
	 		"                       senderdata0_.consignee_suburb, \r\n" + 
	 		"                       senderdata0_.consignee_state, \r\n" + 
	 		"                       senderdata0_.consignee_postcode, \r\n" + 
	 		"                       senderdata0_.consignee_phone, \r\n" + 
	 		"                       senderdata0_.product_description, \r\n" + 
	 		"                       senderdata0_.shipper_country, \r\n" + 
	 		"                       senderdata0_.user_id, \r\n" + 
	 		"                       senderdata0_.weight, \r\n" + 
	 		"                       senderdata0_.barcodelabelnumber, \r\n" + 
	 		"                       senderdata0_.servicetype, \r\n" + 
	 		"                       senderdata0_.currency, \r\n" + 
	 		"                       senderdata0_.articleid,\r\n" + 
	 		"					   trackandtr1_.TrackEventDateOccured,\r\n" + 
	 		"					   senderdata0_.AirwayBill\r\n" + 
	 		"                FROM   dbo.senderdata_master senderdata0_ \r\n" + 
	 		"                       INNER JOIN trackandtrace trackandtr1_ \r\n" + 
	 		"                               ON senderdata0_.reference_number = \r\n" + 
	 		"                                  trackandtr1_.reference_number \r\n" + 
	 		"                WHERE  trackandtr1_.trackeventdetails = 'SHIPMENT ALLOCATED' \r\n" + 
	 		"                       AND trackandtr1_.isdeleted <> 'Y' \r\n" + 
	 		"                       AND ( trackandtr1_.barcodelabelnumber in (:Barcode) \r\n"+
	 		"                           ))B \r\n" + 
	 		"               INNER JOIN users A \r\n" + 
	 		"                       ON A.user_id = B.user_id \r\n" + 
	 		"                          AND A.role_id = '3'\r\n" + 
	 		"")	
		 List<Object> exportShipmentBarcode(@Param("Barcode") List<String> data);
	/* @Query(nativeQuery = true, value = "SELECT D.user_name, \r\n" + 
		 		"       C.reference_number,C.value,C.shipped_Quantity,\r\n" + 
		 		"C.consignee_name,C.consignee_addr1,C.consignee_Suburb,\r\n" + 
		 		"C.consignee_State,C.consignee_Postcode,C.consignee_Phone,\r\n" + 
		 		"C.product_Description,C.shipper_Country,C.weight,C.barcodelabelNumber,\r\n" + 
		 		"C.servicetype,C.currency,C.articleId\r\n" + 
		 		"FROM   (SELECT A.client_broker_id, \r\n" + 
		 		"               B.reference_number,B.value,B.shipped_Quantity,\r\n" + 
		 		"B.consignee_name,B.consignee_addr1,B.consignee_Suburb,\r\n" + 
		 		"B.consignee_State,B.consignee_Postcode,B.consignee_Phone,\r\n" + 
		 		"B.product_Description,B.shipper_Country,B.weight,B.barcodelabelNumber,\r\n" + 
		 		"B.servicetype,B.currency,B.articleId\r\n" + 
		 		"        FROM   (SELECT senderdata0_.reference_number,senderdata0_.value,\r\n" + 
		 		"senderdata0_.shipped_Quantity,senderdata0_.consignee_name,senderdata0_.consignee_addr1,\r\n" + 
		 		"senderdata0_.consignee_Suburb,senderdata0_.consignee_State,\r\n" + 
		 		"senderdata0_.consignee_Postcode,senderdata0_.consignee_Phone,\r\n" + 
		 		"senderdata0_.product_Description,senderdata0_.shipper_Country,senderdata0_.User_ID,\r\n" + 
		 		"senderdata0_.weight,senderdata0_.barcodelabelNumber,senderdata0_.servicetype,senderdata0_.currency,senderdata0_.articleId\r\n" + 
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
		 		"               ON D.user_id = C.client_broker_id ") */
	 @Query(nativeQuery = true, value ="SELECT A.Client_BrokerName, \r\n" + 
	 		"               B.reference_number, \r\n" + 
	 		"               B.value, \r\n" + 
	 		"               B.shipped_quantity, \r\n" + 
	 		"               B.consignee_name, \r\n" + 
	 		"               B.consignee_addr1, \r\n" + 
	 		"				B.consignee_addr2, \r\n" +
	 		"               B.consignee_suburb, \r\n" + 
	 		"               B.consignee_state, \r\n" + 
	 		"               B.consignee_postcode, \r\n" + 
	 		"               B.consignee_phone, \r\n" + 
	 		"               B.product_description, \r\n" + 
	 		"               B.shipper_country, \r\n" + 
	 		"               B.weight, \r\n" + 
	 		"               B.barcodelabelnumber, \r\n" + 
	 		"               B.servicetype, \r\n" + 
	 		"               B.currency, \r\n" + 
	 		"               B.articleid,\r\n" + 
	 		"			   B.trackeventdateoccured,\r\n" + 
	 		"			   B.AirwayBill\r\n" + 
	 		"        FROM   (SELECT senderdata0_.reference_number, \r\n" + 
	 		"                       senderdata0_.value, \r\n" + 
	 		"                       senderdata0_.shipped_quantity, \r\n" + 
	 		"                       senderdata0_.consignee_name, \r\n" + 
	 		"                       senderdata0_.consignee_addr1, \r\n" + 
	 		"						senderdata0_.consignee_addr2, \r\n" +
	 		"                       senderdata0_.consignee_suburb, \r\n" + 
	 		"                       senderdata0_.consignee_state, \r\n" + 
	 		"                       senderdata0_.consignee_postcode, \r\n" + 
	 		"                       senderdata0_.consignee_phone, \r\n" + 
	 		"                       senderdata0_.product_description, \r\n" + 
	 		"                       senderdata0_.shipper_country, \r\n" + 
	 		"                       senderdata0_.user_id, \r\n" + 
	 		"                       senderdata0_.weight, \r\n" + 
	 		"                       senderdata0_.barcodelabelnumber, \r\n" + 
	 		"                       senderdata0_.servicetype, \r\n" + 
	 		"                       senderdata0_.currency, \r\n" + 
	 		"                       senderdata0_.articleid,\r\n" + 
	 		"					   trackandtr1_.TrackEventDateOccured,\r\n" + 
	 		"					   senderdata0_.AirwayBill\r\n" + 
	 		"                FROM   dbo.senderdata_master senderdata0_ \r\n" + 
	 		"                       INNER JOIN trackandtrace trackandtr1_ \r\n" + 
	 		"                               ON senderdata0_.reference_number = \r\n" + 
	 		"                                  trackandtr1_.reference_number \r\n" + 
	 		"                WHERE  trackandtr1_.trackeventdetails = 'SHIPMENT ALLOCATED' \r\n" + 
	 		"                       AND trackandtr1_.isdeleted <> 'Y' \r\n" + 
	 		"                       AND ( trackandtr1_.trackeventdateoccured BETWEEN \r\n" + 
	 		"                            :fromTime AND :toTime\r\n" + 
	 		"\r\n" + 
	 		"                           ))B \r\n" + 
	 		"               INNER JOIN users A \r\n" + 
	 		"                       ON A.user_id = B.user_id \r\n" + 
	 		"                          AND A.role_id = '3'\r\n" + 
	 		"")
		 		
		 List<Object> exportShipment(@Param("fromTime") String fromTime , @Param("toTime") String toTime);
	/* @Query("SELECT s FROM SenderdataMaster s JOIN s.trackAndTrace t where t.trackEventDetails = 'SHIPMENT ALLOCATED' and t.isDeleted != 'Y' and t.trackEventDateOccured between :fromTime and :toTime") 
	List<SenderdataMaster> exportShipment(@Param("fromTime") String fromTime , @Param("toTime") String toTime);*/
	/* @Query(nativeQuery = true, value = "SELECT B.user_name, \r\n" + 
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
	 		"                      ON C.client_broker_id = B.user_id ")*/
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 @Query(nativeQuery = true, value ="SELECT U.Client_BrokerName, senderdata0_.reference_number,  senderdata0_.value, senderdata0_.shipped_quantity, \r\n" + 
	 		"     senderdata0_.consignee_name, \r\n" + 
	 		"     senderdata0_.consignee_addr1, \r\n" + 
	 		"     senderdata0_.consignee_suburb, \r\n" + 
	 		"     senderdata0_.consignee_state, \r\n" + 
	 		"     senderdata0_.consignee_postcode, \r\n" + 
	 		"     senderdata0_.consignee_phone, \r\n" + 
	 		"     senderdata0_.product_description, \r\n" + 
	 		"     senderdata0_.shipper_country, \r\n" + 
	 		
	 		"     senderdata0_.weight, \r\n" + 
	 		"     senderdata0_.barcodelabelnumber, \r\n" + 
	 		"     senderdata0_.servicetype, \r\n" + 
	 		"     senderdata0_.currency,\r\n" + 
	 		"	   senderdata0_.Timestamp FROM   dbo.senderdata_master senderdata0_ \r\n" + 
	 		"     INNER JOIN users U \r\n" + 
	 		"             ON u.user_id = senderdata0_.user_id \r\n" + 
	 		"                AND senderdata0_.airwaybill IS NULL \r\n" + 
	 		"                AND senderdata0_.isdeleted = 'N' \r\n" + 
	 		"                AND senderdata0_.timestamp BETWEEN :fromTime AND :toTime")
   


	 
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


//	@Query(nativeQuery = true, value="SELECT DISTINCT A.user_name, B.airwaybill FROM (SELECT DISTINCT U.client_broker_id, S.airwaybill FROM Senderdata_Invoicing S INNER JOIN users U \n" + 
//			"ON S.airwaybill IS NOT NULL AND U.role_id = '3' AND S.user_id = U.user_id AND S.Invoiced = 'N') B INNER JOIN users A ON A.user_id = B.client_broker_id ORDER  BY A.user_name")
//	List<String> fetchSenderShipmenntData();
	 
	@Query(nativeQuery = true, value="SELECT DISTINCT U.Client_BrokerName, \r\n" + 
			"                        S.airwaybill \r\n" + 
			"        FROM   senderdata_invoicing S \r\n" + 
			"               INNER JOIN users U \r\n" + 
			"                       ON S.airwaybill IS NOT NULL \r\n" + 
			"                          AND U.role_id = '3' \r\n" + 
			"                          AND S.user_id = U.user_id \r\n" + 
			"                          AND S.invoiced = 'N'\r\n" + 
			"						  order by U.Client_BrokerName")
	List<String> fetchSenderShipmenntData();
	
//	@Query(nativeQuery = true, value="SELECT DISTINCT A.user_name, B.airwaybill FROM \n" + 
//			"(\n" + 
//			"SELECT DISTINCT U.client_broker_id, S.airwaybill FROM Senderdata_Invoicing S INNER JOIN users U \n" + 
//			"ON S.airwaybill IS NOT NULL AND U.role_id = '3' AND S.user_id = U.user_id AND S.Invoiced = 'Y' and S.Billed = 'N'\n" + 
//			") \n" + 
//			"B INNER JOIN users A ON A.user_id = B.client_broker_id ORDER  BY A.user_name;")
//	List<String> brokerInvoiced();
	
	@Query(nativeQuery = true, value="SELECT DISTINCT U.Client_BrokerName, \r\n" + 
			"                        S.airwaybill \r\n" + 
			"        FROM   senderdata_invoicing S \r\n" + 
			"               INNER JOIN users U \r\n" + 
			"                       ON S.airwaybill IS NOT NULL \r\n" + 
			"                          AND U.role_id = '3' \r\n" + 
			"                          AND S.user_id = U.user_id \r\n" + 
			"                          AND S.invoiced = 'Y' \r\n" + 
			"                          AND S.billed = 'N'\r\n" + 
			"Order by U.client_brokername\r\n" + 
			"")
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
	
	@Procedure(name = "reconcilerates")
	void reconcilerates(@Param("Reference_number") String Reference_number);
	
	/*@Query(nativeQuery = true, value="SELECT DISTINCT A.user_name, B.airwayBill, B.ArticleId, B.reference_number, B.D2ZRate,S.dateallocated 	 FROM\n" + 
			"( \n" + 
			"SELECT DISTINCT U.client_broker_id, S.airwaybill, S.ArticleId, S.reference_number, S.D2ZRate FROM Senderdata_Invoicing S INNER JOIN users U  \n" + 
			"ON s.reference_number not in ( select distinct reference_number from reconcile where Reference_number is not null ) AND U.role_id = '3' AND S.user_id = U.user_id \n" + 
			") \n" + 
			"B INNER JOIN users A ON A.user_id = B.client_broker_id ORDER  BY A.user_name")*/
	@Query(nativeQuery = true, value = "SELECT DISTINCT U.Client_BrokerName,S.airwaybill,S.articleid,S.reference_number,S.d2zrate,S.dateallocated,S.weight\n" + 
			"        FROM   senderdata_invoicing S INNER JOIN users U ON s.reference_number NOT IN \n" + 
			"             (SELECT DISTINCT reference_number FROM   reconcile WHERE reference_number IS NOT NULL) \n" + 
			"                          AND U.role_id = '3' AND S.user_id = U.user_id ORDER  BY U.Client_BrokerName")
	List<String> fetchNotBilled();
	
//	@Query(nativeQuery = true, value="SELECT DISTINCT S.articleid  AS TrackingNumber, S.reference_number AS reference, S.consignee_postcode AS postcode, S.weight AS Weight,\n" + 
//			"				  B.rate AS postage, S.fuelsurcharge AS Fuelsurcharge, S.brokerrate AS total, S.servicetype AS servicetype ,S.airwaybill AS ShipmentNumber FROM Senderdata_Invoicing S\n" + 
//			"				  INNER JOIN POSTCODEZONES P ON P.postcode = S.consignee_postcode AND P.suburb = S.consignee_suburb INNER JOIN BROKERRATES B\n" + 
//			"                ON S.airwaybill in (:airwayBill) AND B.brokerusername in (:broker) AND ( S.weight BETWEEN Cast(B.minweight AS DECIMAL(18, 4)) AND\n" + 
//			" 				  Cast( B.maxweight AS DECIMAL(18, 4)) ) AND S.servicetype = B.servicetype AND S.consignee_postcode = P.postcode\n" + 
//			"					AND B.zoneid = P.zone AND S.Billed = :billed AND S.Invoiced = :invoiced ")
//	List<String> downloadInvoice(@Param("broker") List<String> broker, @Param("airwayBill")  List<String> airwayBill, @Param("billed") String billed,
//									@Param("invoiced") String invoiced);

//	@Query(nativeQuery = true, value="SELECT \r\n" + 
//			"  DISTINCT S.articleid AS TrackingNumber, \r\n" + 
//			"  S.reference_number AS reference, \r\n" + 
//			"  S.consignee_postcode AS postcode, \r\n" + 
//			"  S.weight AS Weight, \r\n" + 
//			"  B.rate AS postage, \r\n" + 
//			"  S.fuelsurcharge AS Fuelsurcharge, \r\n" + 
//			"  S.brokerrate AS total, \r\n" + 
//			"  S.servicetype AS servicetype, \r\n" + 
//			"  S.airwaybill AS ShipmentNumber \r\n" + 
//			"FROM \r\n" + 
//			"  senderdata_invoicing S \r\n" + 
//			"  INNER JOIN fastwaypostcode P ON P.postcode = S.consignee_postcode \r\n" + 
//			"  AND P.suburb = S.consignee_suburb \r\n" + 
//			"  INNER JOIN brokerrates B ON S.airwaybill IN (:airwayBill) \r\n" + 
//			"  AND B.brokerusername IN (:broker) \r\n" + 
//			"  AND (\r\n" + 
//			"    S.weight BETWEEN Cast(\r\n" + 
//			"      B.minweight AS DECIMAL(18, 4)\r\n" + 
//			"    ) \r\n" + 
//			"    AND Cast(\r\n" + 
//			"      B.maxweight AS DECIMAL(18, 4)\r\n" + 
//			"    )\r\n" + 
//			"  ) \r\n" + 
//			"  AND S.servicetype = B.servicetype \r\n" + 
//			"  AND S.consignee_postcode = P.postcode \r\n" + 
//			"  AND B.zoneid = P.zoneid \r\n" + 
//			"  AND S.billed = :billed \r\n" + 
//			"  AND S.invoiced = :invoiced\r\n" + 
//			"UNION \r\n" + 
//			"SELECT \r\n" + 
//			"  DISTINCT S.articleid AS TrackingNumber, \r\n" + 
//			"  S.reference_number AS reference, \r\n" + 
//			"  S.consignee_postcode AS postcode, \r\n" + 
//			"  S.weight AS Weight, \r\n" + 
//			"  B.rate AS postage, \r\n" + 
//			"  S.fuelsurcharge AS Fuelsurcharge, \r\n" + 
//			"  S.brokerrate AS total, \r\n" + 
//			"  S.servicetype AS servicetype, \r\n" + 
//			"  S.airwaybill AS ShipmentNumber \r\n" + 
//			"FROM \r\n" + 
//			"  senderdata_invoicing S \r\n" + 
//			"  INNER JOIN postcodezones P ON P.postcode = S.consignee_postcode \r\n" + 
//			"  AND P.suburb = S.consignee_suburb \r\n" + 
//			"  INNER JOIN brokerrates B ON S.airwaybill IN (:airwayBill) \r\n" + 
//			"  AND B.brokerusername IN (:broker) \r\n" + 
//			"  AND (\r\n" + 
//			"    S.weight BETWEEN Cast(\r\n" + 
//			"      B.minweight AS DECIMAL(18, 4)\r\n" + 
//			"    ) \r\n" + 
//			"    AND Cast(\r\n" + 
//			"      B.maxweight AS DECIMAL(18, 4)\r\n" + 
//			"    )\r\n" + 
//			"  ) \r\n" + 
//			"  AND S.servicetype = B.servicetype \r\n" + 
//			"  AND S.consignee_postcode = P.postcode \r\n" + 
//			"  AND B.zoneid = P.zone \r\n" + 
//			"  AND S.billed = :billed\r\n" + 
//			"  AND S.invoiced = :invoiced\r\n" + 
//			"")
//	List<String> downloadInvoice(@Param("broker") List<String> broker, @Param("airwayBill")  List<String> airwayBill, @Param("billed") String billed,
//									@Param("invoiced") String invoiced);
	
	@Query("SELECT t.user_ID FROM SenderdataMaster t where  t.reference_number = :reference_number")
	 Integer fetchUserIdByReferenceNumber( String reference_number);


	@Query("SELECT t FROM SenderdataMaster t where t.articleId in (:refNbrs) and t.isDeleted = 'N'")
	List<SenderdataMaster> fetchDataForAusPost(List<String>  refNbrs);
	
	@Query("SELECT t.reference_number FROM SenderdataMaster t where t.articleId in (:article) ")
	List<String> fetchreferencenumberforArticleid(@Param("article") List<String>  article);
	 
	@Query("SELECT t.cubic_Weight FROM SenderdataMaster t where  t.articleId in (:articleID)")
	List<BigDecimal> fetchcubicweight(List<String> articleID);

	@Query(nativeQuery = true,value ="SELECT s.mlid,s.serviceType FROM senderdata_master s where ( s.carrier = 'FastwayM' or s.servicetype='1PS4' ) and s.reference_number in (:refNbrs)")
	List<Object[]> fetchDataforPFLSubmitOrder(String[] refNbrs);

	@Modifying(flushAutomatically = true,clearAutomatically = true)
	@Transactional
	@Query("Update SenderdataMaster s set s.airwayBill = :airwayBill, s.status = 'SHIPMENT ALLOCATED', \n"+
									  "s.timestamp = :timestamp where s.reference_number IN (:referenceNumbers) and  isdeleted = 'N'")
	void updateAirwayBill(@Param("referenceNumbers") String[] referenceNumbers, @Param("airwayBill") String shipmentNumber, @Param("timestamp") String timestamp);
	
	@Modifying(flushAutomatically = true,clearAutomatically = true)
	@Transactional
	@Query("Update SenderdataMaster s set s.airwayBill = :airwayBill, s.status = 'SHIPMENT ALLOCATED', \n"+
									  "s.timestamp = :timestamp, s.invoiced = 'M' where s.reference_number IN (:referenceNumbers) and  isdeleted = 'N'")
	void updateAirwayBillInvoiceMAWB(@Param("referenceNumbers") String[] referenceNumbers, @Param("airwayBill") String shipmentNumber, @Param("timestamp") String timestamp);
	
	
	@Modifying(flushAutomatically = true,clearAutomatically = true)
	@Transactional
	@Query("Update SenderdataMaster s set s.weight = :weight where s.articleId = :articleid")
	void updateweight(@Param("weight") Double weight, @Param("articleid") String articleid);
	
	@Query("SELECT s FROM SenderdataMaster s where s.articleId = :identifier")
	SenderdataMaster fetchDataArticleId(@Param("identifier") String identifier);
	
	@Query("SELECT s FROM SenderdataMaster s where s.reference_number = :identifier")
	SenderdataMaster fetchDataReferenceNum(@Param("identifier") String identifier);
	
	@Query("SELECT s.barcodelabelNumber FROM SenderdataMaster s where s.articleId = :articleID")
	String fetchBarcodeDetails(@Param("articleID") String articleID);

	@Query("SELECT s.servicetype FROM SenderdataMaster s where s.reference_number = :refNbr")
	String fetchServiceTypeByRefNbr(String refNbr);
	
	@Query("SELECT s.servicetype FROM SenderdataMaster s where s.reference_number = :identifier or s.articleId = :identifier")
	String fetchServiceType(String identifier);
	
	@Query("SELECT s.servicetype FROM SenderdataMaster s where s.mlid = :mlid")
	String fetchServiceTypeByMlid(String mlid);

	@Query(nativeQuery = true,value = "select s.articleId,s.consignee_name,s.consignee_addr1,s.consignee_addr2,s.consignee_suburb,"
			+ "s.consignee_state,s.consignee_postcode,i.ata,\n" + 
			"i.clearanceDate,injectionDate	from senderdata_master "
			+ "s FULL OUTER JOIN Incomingjobs i ON s.airwayBill = i.mawb "
			+ "where s.user_id in (189762,189765) and s.IsDeleted = 'N' and s.Status = 'SHIPMENT ALLOCATED' "
			+ "and DATEPART(m, s.Timestamp) = DATEPART(m, DATEADD(m, -1, getdate())) "
			+ "AND DATEPART(yy, s.Timestamp) = DATEPART(yy, DATEADD(m, -1, getdate()))")
	List<String> fetchPerformanceReportData();

	@Query(nativeQuery = true,value = "select s.articleId,s.serviceType	from senderdata_master s "
			+ "where s.user_id in (189762,189765) and s.IsDeleted = 'N' and s.Status = 'SHIPMENT ALLOCATED' "
			+ "and DATEPART(m, s.Timestamp) = DATEPART(m, DATEADD(m, -1, getdate())) "
			+ "AND DATEPART(yy, s.Timestamp) = DATEPART(yy, DATEADD(m, -1, getdate()))")
	List<Object[]> fetchArticleIdForPerformanceReport();

	@Query(nativeQuery = true,value = "select s.mlid,s.serviceType	from senderdata_master s "
			+ "where s.mlid in (select connoteNo from trackandtrace t where t.fileName = 'PFLSubmitOrder')")
	List<Object[]> fetchDataForPFLSubmitOrder();

	@Query("SELECT t.articleId FROM SenderdataMaster t where t.reference_number in (:refBarNum) ")
	List<String> fetchArticleIDforRefNbr(List<String> refBarNum);
	
	@Query(nativeQuery = true, value="SELECT mlid FROM senderdata_master\n" + 
			" WHERE reference_number IN (:refBarNum) and isDeleted != 'Y'" + 
			" UNION\n" + 
			" SELECT mlid FROM senderdata_master\n" + 
		    " WHERE articleId IN (:refBarNum) and isDeleted != 'Y'") 
	List<String> fetchMlid(List<String> refBarNum);

} 
