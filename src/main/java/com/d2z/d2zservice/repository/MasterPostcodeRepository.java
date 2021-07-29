package com.d2z.d2zservice.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.d2z.d2zservice.entity.MasterPostcode;

public interface MasterPostcodeRepository extends CrudRepository<MasterPostcode, Long>{
	
	@Query(nativeQuery = true,value="select * from MasterPostcode where (state = :state OR stateName =:state) and suburb = :suburb and postcode = :postcode")
	MasterPostcode fetchAllMasterPostCodeZone(String state, String suburb, String postcode);

	@Query(nativeQuery = true,value="select m.fdmRoute from MasterPostcode m where (m.state = :state OR m.stateName = :state) and m.suburb = :suburb and m.postcode = :postcode")
	String fetchFDMRoute(String state, String suburb, String postcode);

}
