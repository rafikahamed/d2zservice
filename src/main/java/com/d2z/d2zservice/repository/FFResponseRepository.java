package com.d2z.d2zservice.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.d2z.d2zservice.entity.FFResponse;


public interface FFResponseRepository extends CrudRepository<FFResponse,Long>{
	
	  @Query(nativeQuery = true, value="select * from FFResponse f where f.message_no = :message")
	  List<FFResponse> findByMessageNoIs(@Param("message") String message);
	  
	  @Query(nativeQuery = true, value="SELECT * FROM FFResponse where Timestamp between :fromDate and :toDate") 
	  List<FFResponse> fetchFFLogResponse(@Param("fromDate") String fromDate, @Param("toDate") String toDate);

}
