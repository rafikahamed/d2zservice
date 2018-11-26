package com.d2z.d2zservice.repository;

import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

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
	 
	@Query(value="SELECT DISTINCT t.filename FROM SenderdataMaster t") 
	List<String> fetchFileName();

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
	 		" weight, shipper_Name, shipper_Addr1, shipper_Addr2, shipper_City, shipper_State, shipper_Country,\n" + 
	 		" shipper_Postcode, barcodelabelNumber, datamatrix, injectionState FROM senderdata_master\n" + 
	 		" WHERE reference_number=:refBarNum and isDeleted != 'Y'" + 
	 		" UNION\n" + 
	 		" SELECT reference_number, consignee_name, consignee_addr1, consignee_Suburb, consignee_State, consignee_Postcode, consignee_Phone,\n" + 
	 		" weight, shipper_Name, shipper_Addr1, shipper_Addr2, shipper_City, shipper_State, shipper_Country,\n" + 
	 		" shipper_Postcode, barcodelabelNumber, datamatrix, injectionState FROM senderdata_master\n" + 
	 		" WHERE BarcodelabelNumber like '%'+:refBarNum+'%' and isDeleted != 'Y'") 
	String fetchTrackingLabel(@Param("refBarNum") String refBarNum);
	 
	@Procedure(name = "manifest_creation")
	void manifestCreation(@Param("ManifestNumber") String ManifestNumber, @Param("Reference_number") String Reference_number);

	@Query("Select t from SenderdataMaster t where t.reference_number = :reference_number") 
	SenderdataMaster fetchByReferenceNumbers(@Param("reference_number") String referenceNumber);
	 
	@Procedure(name = "shipment_allocation")
	void allocateShipment(@Param("Reference_number") String Reference_number, @Param("Airwaybill") String Airwaybill);

	@Query("SELECT t FROM SenderdataMaster t where t.filename = :fileName and t.isDeleted != 'Y' and t.manifest_number is null") 
	List<SenderdataMaster> fetchManifestData(@Param("fileName") String fileName);

	@Query("SELECT t FROM SenderdataMaster t where t.airwayBill = :shipmentNumber and t.isDeleted != 'Y'") 
	List<SenderdataMaster> fetchShipmentData(@Param("shipmentNumber") String shipmentNumber);

	@Query("SELECT DISTINCT t.manifest_number FROM SenderdataMaster t where t.airwayBill is null") 
	List<String> fetchManifestNumber();
	
	 @Query("SELECT t FROM SenderdataMaster t where t.manifest_number = :manifestNumber and t.airwayBill is null") 
	 List<SenderdataMaster> fetchConsignmentByManifest(@Param("manifestNumber") String manifestNumber);

	 @Query("SELECT reference_number FROM SenderdataMaster t where t.reference_number in :referenceNumbers and t.airwayBill is not null") 
	 List<String> findRefNbrByShipmentNbr(@Param("referenceNumbers") String[] referenceNumbers);
	 
	 @Query("SELECT DISTINCT t.airwayBill FROM SenderdataMaster t where t.sender_Files_ID like '%D2ZUI%' ") 
	 List<String> fetchShipmentList();
	 
	 @Query("SELECT DISTINCT t.airwayBill FROM SenderdataMaster t where t.sender_Files_ID like '%D2ZAPI%' ") 
	 List<String> fetchApiShipmentList();
	 
	 @Query(nativeQuery = true, value="select reference_number, substring(barcodelabelnumber,19,23), consignee_name, consignee_Postcode, weight, Shipper_Name from senderdata_master \n" + 
	 		"where user_id in (select user_id from users where CompanyName=:companyName)\n" + 
	 		"and sender_files_id like '%API%' and airwaybill is null")
	 List<String> fetchDirectInjectionData(@Param("companyName") String companyName);

	 @Query("SELECT t FROM SenderdataMaster t where t.isDeleted = 'Y' and t.timestamp between :fromTimestamp and :toTimestamp") 
	List<SenderdataMaster> fetchDeletedConsignments(@Param("fromTimestamp") String fromTimestamp , @Param("toTimestamp") String toTimestamp);

	 @Query("SELECT s FROM SenderdataMaster s JOIN s.trackAndTrace t where t.trackEventDetails = 'CONSIGNMENT CREATED' and t.isDeleted != 'Y' and t.trackEventDateOccured between :fromTime and :toTime") 
	List<SenderdataMaster> exportConsignments(@Param("fromTime") String fromTime , @Param("toTime") String toTime);

	 @Query("SELECT s FROM SenderdataMaster s JOIN s.trackAndTrace t where t.trackEventDetails = 'SHIPMENT ALLOCATED' and t.isDeleted != 'Y' and t.trackEventDateOccured between :fromTime and :toTime") 
	List<SenderdataMaster> exportShipment(@Param("fromTime") String fromTime , @Param("toTime") String toTime);
} 
