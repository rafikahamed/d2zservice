package com.d2z.d2zservice.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.d2z.d2zservice.entity.ETowerResponse;

public interface ETowerResponseRepository extends CrudRepository<ETowerResponse, Long>{

     @Query(nativeQuery = true, value="SELECT * FROM ETowerResponse where Timestamp between :fromDate and :toDate") 
     List<ETowerResponse> fetchEtowerLogResponse(@Param("fromDate") String fromDate, @Param("toDate") String toDate);
     
     @Query(nativeQuery = true, value="SELECT * FROM ETowerResponse where APIName IN (:api) and Timestamp between :fromDate and :toDate") 
     List<ETowerResponse> fetchEtowerLogResponseApi(@Param("fromDate") String fromDate, @Param("toDate") String toDate,@Param("api") List<String> apiname);

     @Query("SELECT e.trackingNo FROM ETowerResponse e where orderId in (:artileIDList)")
	List<String> fetchTrackingNumberFromEtowerResponse(List<String> artileIDList);
     
 
}

