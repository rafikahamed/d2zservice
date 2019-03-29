package com.d2z.d2zservice.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.d2z.d2zservice.entity.Mlid;

public interface MlidRepository extends CrudRepository<Mlid, Long>{
	
	@Query("SELECT distinct(serviceType) from Mlid order by serviceType asc") 
	List<String> getServiceTypeList();

}
