package com.d2z.d2zservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.d2z.d2zservice.entity.SupplierEntity;

@Repository
public interface SupplierRepository  extends CrudRepository<SupplierEntity, Integer>{

}
