package com.d2z.d2zservice.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.d2z.d2zservice.entity.InvoicingZones;

@Repository
public interface InvoicingZonesRepository extends CrudRepository<InvoicingZones, Long>{

	@Cacheable("fdmRoute")
	@Query("select i.fdmRoute from InvoicingZones i where i.state = :state and i.suburb = :suburb and i.postcode = :postcode")
	public String fdmRoute(String suburb,String state, String postcode);
}
