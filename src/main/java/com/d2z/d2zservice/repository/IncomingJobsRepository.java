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
	
	@Query( nativeQuery = true, value="SELECT * FROM incomingjobs where broker in ('NEXB', 'VELB', 'RMFB', '5ULB','SSDB','PFLB') and Shipment is null")
	List<IncomingJobs> shipmentCharges();
	
	@Query(nativeQuery = true, value= "Select * from IncomingJobs where  mawb = :mawb")
	IncomingJobs fetchJobByMAWB(@Param("mawb") String mawb);
	
	@Modifying(flushAutomatically = true,clearAutomatically = true)
	@Transactional
	@Query(nativeQuery = true, value="Update IncomingJobs  set Shipment = 'Y' where mawb = :mawb")
	void approveShiment(@Param("mawb") String mawb);
	
	@Query(nativeQuery = true ,value="select\r\n" + 
			"Broker, sum(Total) as shipmentCharge\r\n" + 
			"FROM [D2Z].[dbo].[IncomingJobs]\r\n" + 
			"where\r\n" + 
			"  ATA between :fromDate and :toDate and total is not null and Broker not in ('5ULB')\r\n" + 
			"  group by Broker\r\n" + 
			"\r\n" + 
			"union\r\n" + 
			"\r\n" + 
			"select\r\n" + 
			"'5ULB Other' as Broker, sum(Total) as shipmentCharge\r\n" + 
			"FROM [D2Z].[dbo].[IncomingJobs]\r\n" + 
			"where\r\n" + 
			"  ATA between :fromDate and :toDate and total is not null and Broker = '5ULB' and Consignee not in ('APG')\r\n" + 
			"  group by Broker\r\n" + 
			"\r\n" + 
			"union\r\n" + 
			"select\r\n" + 
			"'5ULB HKG' as Broker, sum(Total) as shipmentCharge\r\n" + 
			"FROM [D2Z].[dbo].[IncomingJobs]\r\n" + 
			"where\r\n" + 
			"  ATA between :fromDate and :toDate and total is not null and Broker = '5ULB' and Consignee = 'APG'\r\n" + 
			"  group by Broker\r\n" + 
			"\r\n" + 
			"union\r\n" + 
			"\r\n" + 
			"select\r\n" + 
			"Consignee, sum(Total) as shipmentCharge\r\n" + 
			"FROM [D2Z].[dbo].[IncomingJobs]\r\n" + 
			"where\r\n" + 
			"  ATA between :fromDate and :toDate and total is not null\r\n" + 
			"  group by Consignee")
	List<String> getProfitShipmentDetails(@Param("fromDate") String fromDate, @Param("toDate") String toDate);

}
