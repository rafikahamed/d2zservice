package com.d2z.d2zservice.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.d2z.d2zservice.entity.CSTickets;

public interface CSTicketsRepository extends CrudRepository<CSTickets, Long>{

	@Query( nativeQuery = true, value="SELECT NEXT VALUE FOR CSSeqnum")
	Integer fetchNextSeq();

}
