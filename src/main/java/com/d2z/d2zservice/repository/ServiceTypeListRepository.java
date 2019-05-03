package com.d2z.d2zservice.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.d2z.d2zservice.entity.ServiceTypeList;

public interface ServiceTypeListRepository extends CrudRepository<ServiceTypeList, Long>{
	
	@Query("SELECT distinct(serviceType) from ServiceTypeList order by serviceType asc") 
	List<String> getServiceTypeList();

}
