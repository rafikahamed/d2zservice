package com.d2z.d2zservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.d2z.d2zservice.entity.UserService;

public interface UserServiceRepository extends CrudRepository<UserService, Long>{

	 @Query("Select t from UserService t where t.companyName = :companyName and t.serviceType = :serviceType") 
	 UserService fetchbyCompanyNameAndServiceType(@Param("companyName") String companyName, @Param("serviceType") String serviceType);

	@Query(nativeQuery = true, value="Select serviceType from UserService t where t.user_Name = :userName and t.service_isDeleted = 'false'") 
	List<String> fetchAllServiceTypeByUserName(@Param("userName") String userName);

	@Query("Select t from UserService t where t.companyName = :companyName") 
	List<UserService> fetchbyCompanyName(String companyName);
	
	@Query("Select t.serviceType from UserService t where t.userId = :user_Id and t.service_isDeleted = 0") 
	List<String> fetchUserServiceById(@Param("user_Id") Integer user_Id);
	
}
