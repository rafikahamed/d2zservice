package com.d2z.d2zservice.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.d2z.d2zservice.entity.LabelLogic;

@Repository
public interface LabelLogicRepository extends CrudRepository<LabelLogic, Long>{

	@Cacheable("labelName")
	@Query("select l.labelName from LabelLogic l where l.serviceType = :serviceType and l.carrier = :carrier")
	String findLabelName(String serviceType, @Param("carrier")String carrier);

	@Query("select l.trackingIdentifier from LabelLogic l where l.serviceType = :serviceType and l.carrier = :carrier")
	String fetchTrackingIdentifier(String serviceType, String carrier);

	@Cacheable("allocationIdentifier")
	@Query("select l.allocationIdentifier from LabelLogic l where l.serviceType = :serviceType and l.carrier = :carrier")
	String fetchAllocationIdentifier(String serviceType, String carrier);

}
