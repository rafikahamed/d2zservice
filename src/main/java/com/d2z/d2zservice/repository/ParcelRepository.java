package com.d2z.d2zservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.d2z.d2zservice.entity.IncomingJobs;
import com.d2z.d2zservice.entity.Parcels;

public interface ParcelRepository extends CrudRepository<Parcels,Long>{

	@Query( nativeQuery = true, value="SELECT * FROM parcels where   output = 'C'") 
	List<Parcels> fetchheldparcel();
	
	@Query( nativeQuery = true, value="SELECT * FROM parcels where   output = 'C' and status = 'CLEAR'") 
	List<Parcels>fetchreleaseparcel();
}

