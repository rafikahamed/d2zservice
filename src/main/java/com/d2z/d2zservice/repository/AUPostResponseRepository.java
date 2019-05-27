package com.d2z.d2zservice.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.d2z.d2zservice.entity.AUPostResponse;


public interface AUPostResponseRepository extends CrudRepository<AUPostResponse, Long>{

	 @Query(nativeQuery = true, value="SELECT * FROM AUPostResponse where Timestamp between :fromDate and :toDate") 
     List<AUPostResponse> fetchAUPostLogResponse(@Param("fromDate") String fromDate, @Param("toDate") String toDate);
	 
}
