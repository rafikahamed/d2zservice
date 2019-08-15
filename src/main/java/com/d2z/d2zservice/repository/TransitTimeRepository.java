package com.d2z.d2zservice.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.d2z.d2zservice.entity.TransitTime;

public interface TransitTimeRepository extends CrudRepository<TransitTime, Long>{
	
	 @Query("SELECT t FROM TransitTime t where t.postcode = :postcode") 
	 TransitTime fetchTransitTime (@Param("postcode") String postcode);

}
