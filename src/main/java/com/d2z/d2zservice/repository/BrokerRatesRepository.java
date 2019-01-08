package com.d2z.d2zservice.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.d2z.d2zservice.entity.BrokerRates;

public interface BrokerRatesRepository extends CrudRepository<BrokerRates, Long>{

	@Query("SELECT b FROM BrokerRates b where b.brokerUserName = :brokerUserName and b.injectionType = :injectionType and b.serviceType = :serviceType \n" +
	"and b.zoneID = :zoneID and b.minWeight = :minWeight and b.maxWeight = :maxWeight") 
	 BrokerRates findByCompositeKey(String brokerUserName, String injectionType, String serviceType, String zoneID, Double minWeight, Double maxWeight);
}
