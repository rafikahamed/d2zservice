package com.d2z.d2zservice.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.d2z.d2zservice.entity.SystemRefCount;

public interface SystemRefCountRepository extends CrudRepository<SystemRefCount, Long>{


}
