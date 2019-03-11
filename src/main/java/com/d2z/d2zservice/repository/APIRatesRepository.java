package com.d2z.d2zservice.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.d2z.d2zservice.entity.APIRates;

public interface APIRatesRepository extends CrudRepository<APIRates, Long>{
	@Query("SELECT a.rate FROM APIRates a where a.userId=:userId and a.postCode = :postCode  and a.maxWeight = :maxWeight") 
			 String getRates(@Param("postCode") String postCode, @Param("maxWeight")  Double maxWeight, @Param("userId") Integer userId);

}