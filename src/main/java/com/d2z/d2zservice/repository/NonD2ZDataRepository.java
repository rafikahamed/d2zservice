package com.d2z.d2zservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.d2z.d2zservice.entity.NonD2ZData;

public interface NonD2ZDataRepository extends CrudRepository<NonD2ZData, Long>{
	
	@Query(value="Select articleId from NonD2ZData") 
	List<String> fetchAllArticleId();
	
	@Procedure(name = "RatesND")
	void nonD2zRates(@Param("ArticleId") String ArticleId);
}
