package com.d2z.d2zservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.d2z.d2zservice.entity.TrackingEvent;

public interface TrackingEventRepository extends CrudRepository<TrackingEvent,Long>{

	//@Query("select t from TrackingEvent t where t.orderId in (:articleIds)")
	@Query("select t from TrackingEvent t")
	List<TrackingEvent> findbyArticleIds(List<String> articleIds);

}
