package com.d2z.d2zservice.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.d2z.d2zservice.entity.D2ZRates;

public interface D2ZRatesRepository  extends CrudRepository<D2ZRates, Long>{

	@Query("SELECT b FROM D2ZRates b where b.MLID = :MLID and b.zoneID = :zoneID and b.minWeight = :minWeight and b.maxWeight = :maxWeight") 
	D2ZRates findByCompositeKey(String MLID, String zoneID, Double minWeight, Double maxWeight);
}
