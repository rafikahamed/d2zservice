package com.d2z.d2zservice.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.d2z.d2zservice.entity.Mlid;

public interface MlidRepository extends CrudRepository<Mlid, Long>{
	
	@Query("SELECT distinct(serviceType) from Mlid order by serviceType asc") 
	List<String> getServiceTypeList();

	@Modifying
	@Transactional
	@Query("Update Mlid m set m.serviceType = :newservice where m.serviceType = :oldservice")
	int updateMlidList(@Param("newservice") String newservice,@Param("oldservice") String  oldservie);
	 
	@Query(nativeQuery = true, value="SELECT * FROM Mlid where serviceType = :service") 
	List<Mlid> downloadMlid(@Param("service") String service);
	 
}
