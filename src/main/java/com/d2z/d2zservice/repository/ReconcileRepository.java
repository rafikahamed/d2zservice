package com.d2z.d2zservice.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.d2z.d2zservice.entity.Reconcile;

public interface ReconcileRepository extends CrudRepository<Reconcile, Long>{
	
	@Query("SELECT c FROM Reconcile c where c.reference_number in (:reconcileReferenceNum)") 
	List<Reconcile> fetchReconcileData(@Param("reconcileReferenceNum") List<String> reconcileReferenceNum);

	@Query(nativeQuery = true, value="SELECT * FROM Reconcile where articleId in (:reconcileReferenceNum) union SELECT * FROM Reconcile  where reference_number in (:reconcileReferenceNum)") 
	List<Reconcile> downloadReconcile(@Param("reconcileReferenceNum") List<String> reconcileReferenceNum);
	
}
