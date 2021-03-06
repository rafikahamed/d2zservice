package com.d2z.d2zservice.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.d2z.d2zservice.entity.Parcels;

public interface ParcelRepository extends CrudRepository<Parcels,Long>{

	@Query("SELECT p FROM Parcels p where p.output = 'C' and p.client = :client and p.status not in ('SHORTAGE','DAMAGED','OTHER','CLEAR') ") 
	List<Parcels> fetchheldparcel(String client);
	
	@Query("SELECT p FROM Parcels p where  p.output = 'C' and p.status = 'CLEAR' and p.client = :client") 
	List<Parcels>fetchreleaseparcel(String client);

	@Query("SELECT p FROM Parcels p where p.hawb = :hawb and p.status not in ('SHORTAGE','DAMAGED','OTHER','CLEAR')")
	Parcels findByHAWB(String hawb);

	@Query("SELECT distinct p.hawb,p.status,p.note FROM Parcels p where p.mawb = :mawb")
	List<Object[]> fetchSurplusData(String mawb);
	
	@Query(nativeQuery = true, value="SELECT \r\n" + 
			"  DISTINCT\r\n" + 
			"  MAWB, HAWB, NOTE, Client, POD, status, Email\r\n" + 
			"  FROM [D2Z].[dbo].[Parcels] A\r\n" + 
			"  INNER JOIN\r\n" + 
			"  [D2Z].[dbo].[IncomingJobsLogic] B\r\n" + 
			"  ON A.CLIENT = B.broker\r\n" + 
			"  WHERE \r\n" + 
			"  status like '%HELD'")
	List<Object[]> fetchParcelDetails();
	
	@Query("SELECT p FROM Parcels p")
	List<Parcels> fetchParcel();
	
}

