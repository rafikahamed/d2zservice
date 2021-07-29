package com.d2z.d2zservice.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.d2z.d2zservice.entity.LabelLogic;

@Repository
public interface LabelLogicRepository extends CrudRepository<LabelLogic, Long>{

	@Query("select l.labelName from LabelLogic l where l.serviceType = :serviceType and l.carrier = :carrier")
	String findLabelName(String serviceType, String carrier);

	@Query("select l.trackingIdentifier from LabelLogic l where l.serviceType = :serviceType and l.carrier = :carrier")
	String fetchTrackingIdentifier(String serviceType, String carrier);

}
