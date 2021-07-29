package com.d2z.d2zservice.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.d2z.d2zservice.entity.PostcodeLogic;


@Repository
public interface PostcodeLogicRepository  extends CrudRepository<PostcodeLogic, Long>{

	@Query("select p.postcodeLogic from PostcodeLogic p where p.serviceType = :serviceType")
	String findByServiceType(String serviceType);

}
