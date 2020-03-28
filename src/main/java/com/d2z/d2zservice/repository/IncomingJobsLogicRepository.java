package com.d2z.d2zservice.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.d2z.d2zservice.entity.IncomingJobsLogic;

public interface IncomingJobsLogicRepository extends CrudRepository<IncomingJobsLogic,Long>{
	
	@Query("SELECT distinct(Broker) from IncomingJobsLogic order by broker asc") 
	List<String> getIncomeBrokerList();

	@Query("Select distinct(email) from IncomingJobsLogic where broker = :broker" )
	String fetchEmailForBroker(String broker);

//	@Query("SELECT distinct(broker) from IncomingJobsLogic order by broker asc") 
//	List<String> getBrokerList();

}
