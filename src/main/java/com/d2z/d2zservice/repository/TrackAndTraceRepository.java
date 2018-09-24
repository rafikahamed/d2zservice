package com.d2z.d2zservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.d2z.d2zservice.entity.Trackandtrace;

public interface TrackAndTraceRepository extends CrudRepository<Trackandtrace, Long>{
	
	 @Query("SELECT t FROM Trackandtrace t where t.reference_number = :refNumber") 
	 List<Trackandtrace> fetchTrackEventByRefNbr(@Param("refNumber") String refNumber);

}
