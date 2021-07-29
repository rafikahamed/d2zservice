package com.d2z.d2zservice.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.d2z.d2zservice.entity.Veloce;

@Repository
public interface VeloceRepository extends CrudRepository<Veloce, Long>{
	
	@Query("select v from Veloce v where v.serviceType = :serviceType")
	Veloce find(String serviceType);

}
