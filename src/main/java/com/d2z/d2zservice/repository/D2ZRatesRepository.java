package com.d2z.d2zservice.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.d2z.d2zservice.entity.D2ZRates;

public interface D2ZRatesRepository  extends CrudRepository<D2ZRates, Long>{

	@Query("SELECT b FROM D2ZRates b where b.serviceType = :serviceType and b.zoneID = :zoneID and b.minWeight = :minWeight and b.maxWeight = :maxWeight") 
	D2ZRates findByCompositeKey(@Param("serviceType") String serviceType, @Param("zoneID") String zoneID, @Param("minWeight") Double minWeight, @Param("maxWeight") Double maxWeight);
}
