package com.d2z.d2zservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.d2z.d2zservice.entity.IncomingJobs;
import com.d2z.d2zservice.entity.Parcels;

public interface ParcelRepository extends CrudRepository<Parcels,Long>{

	@Query("SELECT p FROM Parcels p where p.output = 'C' and p.client = :client and p.status != 'CLEAR' ") 
	List<Parcels> fetchheldparcel(String client);
	
	@Query("SELECT p FROM Parcels p where  p.output = 'C' and p.status = 'CLEAR' and p.client = :client") 
	List<Parcels>fetchreleaseparcel(String client);

	@Query("SELECT p FROM Parcels p where p.mawb = :mawb ") 
	Parcels findByMAWB(String mawb);
}

