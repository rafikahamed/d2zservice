package com.d2z.d2zservice.repository;

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

	 @Query("SELECT t FROM SenderdataMaster t where t.filename = :fileName") 
	 List<SenderdataMaster> fetchConsignmentData(@Param("fileName") String fileName);
	 
	 @Procedure(name = "consignee_delete")
	 void consigneeDelete(@Param("Reference_number") String Reference_number);
		 
	 @Query(nativeQuery = true, value="Select reference_number, consignee_name, substring(barcodelabelnumber,19,23) from senderdata_master t where filename=:fileName  and manifest_number is null") 
	 List<String> fetchTrackingDetails(@Param("fileName") String fileName);
	 
}
