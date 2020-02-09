package com.d2z.d2zservice.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.d2z.d2zservice.entity.IncomingJobs;

public interface IncomingJobsRepository extends CrudRepository<IncomingJobs,Long>{

	@Query( nativeQuery = true, value="SELECT * FROM incomingjobs where  ISSubmitted = 'Y' and ISDeleted = 'N'") 
	List<IncomingJobs> fetchJobs();
	
	@Query( nativeQuery = true, value="SELECT * FROM incomingjobs where   ISSubmitted IS NULL and ISDeleted = 'N' Order by ETA asc") 
	List<IncomingJobs> fetchincomingJobs();
	
	@Query( nativeQuery = true, value="SELECT * FROM incomingjobs where broker in ('NEXB', 'VELB', 'RMFB', '5ULB') and Shipment is null")
	List<IncomingJobs> shipmentCharges();
	
	@Modifying(flushAutomatically = true,clearAutomatically = true)
	@Transactional
	@Query(nativeQuery = true, value="Update IncomingJobs  set Shipment = 'Y' where mawb = :mawb")
	void approveShiment(@Param("mawb") String mawb);

}
