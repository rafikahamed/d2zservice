package com.d2z.d2zservice.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.d2z.d2zservice.entity.ReconcileND;

public interface ReconcileNDRepository extends CrudRepository<ReconcileND, Long>{
	
	@Query(nativeQuery = true, value="SELECT * FROM ReconcileND where articleId in (:nonD2zReconcileNumbers) union SELECT * FROM ReconcileND where reference_number in (:nonD2zReconcileNumbers)") 
	List<ReconcileND> downloadNonD2zReconcile(@Param("nonD2zReconcileNumbers") List<String> nonD2zReconcileNumbers);
	
	@Query(value="SELECT distinct(t.reference_number) from ReconcileND t where t.reference_number is not null") 
	List<String> fetchAllReconcileNonD2zReferenceNumbers();
	
	@Query(value="SELECT distinct(t.articleId) from ReconcileND t where t.articleId is not null") 
	List<String> fetchAllReconcileNonD2zArticleIdNumbers();
	
}