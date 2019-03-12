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

	@Query(nativeQuery = true, value="Select reference_number from senderdata_master t") 
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

	 @Query("SELECT reference_number FROM SenderdataMaster t where t.reference_number in :referenceNumbers and t.airwayBill is not null") 
	 List<String> findRefNbrByShipmentNbr(@Param("referenceNumbers") String[] referenceNumbers);
	 
	 @Query("SELECT DISTINCT t.airwayBill FROM SenderdataMaster t where t.user_ID IN (:userId) and t.sender_Files_ID like '%D2ZUI%' ") 
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
	 List<String> fetchDataForEtowerForeCastCall(@Param("referenceNumbers") String referenceNumbers);
	 
	 @Query("SELECT distinct(s.airwayBill) FROM SenderdataMaster s where s.user_ID = :userId")
	 List<String> getBrokerShipmentList(@Param("userId") Integer userId);

} 
