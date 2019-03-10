package com.d2z.d2zservice.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.d2z.d2zservice.entity.ConsignmentCount;

public interface ConsigneeCountRepository extends CrudRepository<ConsignmentCount, Long>{
	
	@Query("SELECT distinct(mlid) FROM ConsignmentCount") 
	List<String> getMlidList();

}