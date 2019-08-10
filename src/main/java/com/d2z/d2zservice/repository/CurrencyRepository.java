package com.d2z.d2zservice.repository;
import com.d2z.d2zservice.entity.Currency;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
public interface CurrencyRepository extends CrudRepository<Currency, Long> {

	 @Query("SELECT t.lastUpdated FROM Currency t ORDER BY (t.lastUpdated) ASC") 
	 List<String> latestCurrencyTimeStamp();
	 
	 @Query("SELECT t.audCurrencyRate FROM Currency t where t.currencyCode = :country")
	 Double getaud(@Param("country")String country);
}
