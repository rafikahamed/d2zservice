package com.d2z.d2zservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.d2z.d2zservice.entity.PostcodeZone;

public interface PostcodeZoneRepository extends CrudRepository<PostcodeZone, Long>{
	
	 @Query("SELECT p FROM PostcodeZone p ORDER BY p.suburb ASC") 
	 List<PostcodeZone> fetchAllData();

}
