package com.d2z.d2zservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.d2z.d2zservice.entity.Parcels;

public interface ParcelRepository extends CrudRepository<Parcels,Long>{

	@Query("SELECT p FROM Parcels p where p.output = 'C' and p.client = :client and p.status like '%HELD' ") 
	List<Parcels> fetchheldparcel(String client);
	
	@Query("SELECT p FROM Parcels p where  p.output = 'C' and p.status = 'CLEAR' and p.client = :client") 
	List<Parcels>fetchreleaseparcel(String client);


	@Query("SELECT p FROM Parcels p where p.hawb = :hawb ")
	Parcels findByHAWB(String hawb);

	@Query("SELECT p.hawb,p.status FROM Parcels p where p.mawb = :mawb and p.note = 'SURPLUS/SHORTAGE'")
	List<Object[]> fetchSurplusData(String mawb);
}

