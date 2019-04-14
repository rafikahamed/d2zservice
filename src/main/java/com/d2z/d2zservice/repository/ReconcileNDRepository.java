package com.d2z.d2zservice.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.d2z.d2zservice.entity.ReconcileND;

public interface ReconcileNDRepository extends CrudRepository<ReconcileND, Long>{
	
	@Query("SELECT r FROM ReconcileND r where r.articleId in (:nonD2zReconcileNumbers)") 
	List<ReconcileND> downloadNonD2zReconcile(@Param("nonD2zReconcileNumbers") List<String> nonD2zReconcileNumbers);
	
}