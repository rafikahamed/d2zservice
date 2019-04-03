package com.d2z.d2zservice.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
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
		 
	@Query(nativeQuery = true, value="Select reference_number, consignee_name, substring(barcodelabelnumber,19,23) from senderdata_master where filename=:fileName and manifest_number is null and IsDeleted != 'Y'") 
	List<String> fetchTrackingDetails(@Param("fileName") String fileName);

	@Query(nativeQuery = true, value="Select reference_number from senderdata_master t and t.isDeleted = 'N'") 
	List<String> fetchAllReferenceNumbers();
	 
	@Query(nativeQuery = true, value="Select reference_number, datamatrix from senderdata_master t where sender_Files_ID=:senderFileID") 
	List<String> fetchBySenderFileId(@Param("senderFileID") String senderFileID);
	 
	@Query(nativeQuery = true, value="SELECT reference_number, consignee_name, consignee_addr1, consignee_Suburb, consignee_State, consignee_Postcode, consignee_Phone,\n" + 
	 		" weight, shipper_Name, shipper_Addr1, shipper_City, shipper_State, shipper_Country,\n" + 
	 		" shipper_Postcode, barcodelabelNumber, datamatrix, injectionState, sku, labelSenderName, deliveryInstructions, consigneeCompany, carrier, consignee_addr2 FROM senderdata_master\n" + 
	 		" WHERE reference_number IN (:refBarNum) and isDeleted != 'Y'" + 
	 		" UNION\n" + 
	 		" SELECT reference_number, consignee_name, consignee_addr1, consignee_Suburb, consignee_State, consignee_Postcode, consignee_Phone,\n" + 
	 		" weight, shipper_Name, shipper_Addr1, shipper_City, shipper_State, shipper_Country,\n" + 
	 		" shipper_Postcode, barcodelabelNumber, datamatrix, injectionState, sku, labelSenderName, deliveryInstructions,consigneeCompany, carrier,consignee_addr2 FROM senderdata_master\n" + 
	 		" WHERE articleId IN (:refBarNum) and isDeleted != 'Y'") 
	List<String> fetchTrackingLabel(@Param("refBarNum") List<String> refBarNum);
	 
	@Procedure(name = "manifest_creation")
	void manifestCreation(@Param("ManifestNumber") String ManifestNumber, @Param("Reference_number") String Reference_number);

	@Query("Select t from SenderdataMaster t where t.reference_number = :reference_number") 
	SenderdataMaster fetchByReferenceNumbers(@Param("reference_number") String referenceNumber);
	 
	@Procedure(name = "shipment_allocation")
	void allocateShipment(@Param("Reference_number") String Reference_number, @Param("Airwaybill") String Airwaybill);

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

	 @Query("SELECT t FROM SenderdataMaster t where t.isDeleted = 'Y' and t.timestamp between :fromTimestamp and :toTimestamp") 
	 List<SenderdataMaster> fetchDeletedConsignments(@Param("fromTimestamp") String fromTimestamp , @Param("toTimestamp") String toTimestamp);

	 @Query("SELECT s FROM SenderdataMaster s JOIN s.trackAndTrace t where t.trackEventDetails = 'CONSIGNMENT CREATED' and t.isDeleted != 'Y' and t.trackEventDateOccured between :fromTime and :toTime") 
	 List<SenderdataMaster> exportConsignments(@Param("fromTime") String fromTime , @Param("toTime") String toTime);

	 @Query("SELECT s FROM SenderdataMaster s JOIN s.trackAndTrace t where t.trackEventDetails = 'SHIPMENT ALLOCATED' and t.isDeleted != 'Y' and t.trackEventDateOccured between :fromTime and :toTime") 
	List<SenderdataMaster> exportShipment(@Param("fromTime") String fromTime , @Param("toTime") String toTime);

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
	 
	@Query("SELECT s FROM SenderdataMaster s JOIN s.consignmentCount c where s.reference_number in (:referenceNumbers) and s.mlid = c.mlid and c.supplier = 'eTower'")
	 List<SenderdataMaster> fetchDataForEtowerCall(@Param("referenceNumbers") List<String> referenceNumbers);

	 @Query("SELECT s.articleId FROM SenderdataMaster s JOIN s.consignmentCount c where s.reference_number in (:referenceNumbers) and s.mlid = c.mlid and c.supplier = 'eTower'")
	 List<String> fetchDataForEtowerForeCastCall(@Param("referenceNumbers") String[] referenceNumbers);
	 
	 @Query("SELECT distinct(s.airwayBill) FROM SenderdataMaster s where s.user_ID = :userId")
	 List<String> getBrokerShipmentList(@Param("userId") Integer userId);


	 @Query("SELECT s FROM SenderdataMaster s where s.reference_number in (:referenceNumbers) and (s.manifest_number is not null or s.airwayBill is not null)") 
	 List<SenderdataMaster> fetchConsignmentsManifestShippment(@Param("referenceNumbers") List<String> referenceNumbers);


	@Query(nativeQuery = true, value="SELECT DISTINCT A.user_name, B.airwaybill FROM (SELECT DISTINCT U.client_broker_id, S.airwaybill FROM senderdata_master S INNER JOIN users U \n" + 
			"ON S.airwaybill IS NOT NULL AND U.role_id = '3' AND S.user_id = U.user_id AND S.Invoiced IS NULL) B INNER JOIN users A ON A.user_id = B.client_broker_id ORDER  BY A.user_name")
	List<String> fetchSenderShipmenntData();

	
	@Query(nativeQuery = true, value="SELECT DISTINCT A.user_name, B.airwaybill FROM \n" + 
			"(\n" + 
			"SELECT DISTINCT U.client_broker_id, S.airwaybill FROM senderdata_master S INNER JOIN users U \n" + 
			"ON S.airwaybill IS NOT NULL AND U.role_id = '3' AND S.user_id = U.user_id AND S.Invoiced = 'Y' and S.Billed is null \n" + 
			") \n" + 
			"B INNER JOIN users A ON A.user_id = B.client_broker_id ORDER  BY A.user_name;")
	List<String> brokerInvoiced();
	
	@Query(nativeQuery = true, value="select D.user_name, C.airwayBill, C.articleId, C.reference_number, C.d2zRate, C.weight, C.brokerRate from\n" + 
			"(\n" + 
			"	SELECT B.client_broker_id, A.User_ID, A.airwayBill, A.articleId, A.reference_number, A.d2zRate, A.weight, A.brokerRate  FROM\n" + 
			"	(\n" + 
			"		select User_ID, airwayBill, articleId, reference_number, d2zRate, weight, brokerRate \n" + 
			"			from SENDERDATA_MASTER where articleId = :articleNo or reference_number = :refrenceNumber\n" + 
			"	) A INNER JOIN users B ON A.user_id = B.user_id\n" + 
			") C INNER JOIN users D on C.client_broker_id = D.user_id \n" + 
			"")
	List<String> reconcileData(@Param("articleNo") String articleNo, @Param("refrenceNumber") String refrenceNumber);

	@Procedure(name = "InvoiceUpdate")
	void approvedInvoice(@Param("Indicator") String Indicator, @Param("Airwaybill") String Airwaybill);
	
	@Procedure(name = "reconcilerates")
	void reconcilerates(@Param("Reference_number") String Reference_number);
	
	@Query(nativeQuery = true, value="SELECT DISTINCT A.user_name, B.airwayBill, B.ArticleId, B.reference_number, B.D2ZRate FROM\n" + 
			"( \n" + 
			"SELECT DISTINCT U.client_broker_id, S.airwaybill, S.ArticleId, S.reference_number, S.D2ZRate FROM senderdata_master S INNER JOIN users U  \n" + 
			"ON s.reference_number not in ( select distinct reference_number from reconcile ) AND U.role_id = '3' AND S.user_id = U.user_id \n" + 
			") \n" + 
			"B INNER JOIN users A ON A.user_id = B.client_broker_id ORDER  BY A.user_name")
	List<String> fetchNotBilled();
	
	@Query(nativeQuery = true, value="SELECT DISTINCT S.articleid  AS TrackingNumber, S.reference_number AS reference, S.consignee_postcode AS postcode, S.weight AS Weight,\n" + 
			"				  B.rate AS postage, B.fuelsurcharge AS Fuelsurcharge, S.brokerrate AS total FROM SENDERDATA_MASTER S\n" + 
			"				  INNER JOIN POSTCODEZONES P ON P.postcode = S.consignee_postcode AND P.suburb = S.consignee_suburb INNER JOIN BROKERRATES B\n" + 
			"                ON S.airwaybill in (:airwayBill) AND B.brokerusername in (:broker) AND ( S.weight BETWEEN Cast(B.minweight AS DECIMAL(18, 4)) AND\n" + 
			" 				  Cast( B.maxweight AS DECIMAL(18, 4)) ) AND S.servicetype = B.servicetype AND S.consignee_postcode = P.postcode\n" + 
			"					AND B.zoneid = P.zone")
	List<String> downloadInvoice(@Param("broker") List<String> broker, @Param("airwayBill")  List<String> airwayBill);

	@Query("SELECT t.user_ID FROM SenderdataMaster t where  t.reference_number = :reference_number")
	 Integer fetchUserIdByReferenceNumber( String reference_number);
	 


} 
