package com.d2z.d2zservice.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.d2z.d2zservice.entity.APIRates;
import com.d2z.d2zservice.entity.BrokerRates;

public interface APIRatesRepository extends CrudRepository<APIRates, Long>{
	@Query("SELECT a.rate FROM APIRates a where a.userId=:userId and a.postCode = :postCode  and a.maxWeight = :maxWeight") 
			 String getRates(String postCode, Double maxWeight, Integer userId);

}