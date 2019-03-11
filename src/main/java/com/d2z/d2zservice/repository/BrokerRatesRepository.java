package com.d2z.d2zservice.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.d2z.d2zservice.entity.BrokerRates;

public interface BrokerRatesRepository extends CrudRepository<BrokerRates, Long>{

	@Query("SELECT b FROM BrokerRates b where b.brokerUserName = :brokerUserName and b.injectionType = :injectionType and b.serviceType = :serviceType \n" +
	"and b.zoneID = :zoneID and b.minWeight = :minWeight and b.maxWeight = :maxWeight") 
	 BrokerRates findByCompositeKey(@Param("brokerUserName")  String brokerUserName, @Param("injectionType") String injectionType, 
			 @Param("serviceType") String serviceType, @Param("zoneID") String zoneID, @Param("minWeight") Double minWeight, @Param("maxWeight") Double maxWeight);
}
