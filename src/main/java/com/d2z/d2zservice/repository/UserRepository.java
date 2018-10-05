package com.d2z.d2zservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.d2z.d2zservice.entity.User;

public interface UserRepository extends CrudRepository<User, Long>{ 

	/* @Query("SELECT t FROM User t where t.user_Name = :userName" ) 
	User fetchUserbyUserName(@Param("userName") String userName);
*/
	 @Query("Select t.user_Name from User t where t.user_IsDeleted = false") 
	List<String> fetchAllUserName();

	 @Query("SELECT t FROM User t where t.companyName = :companyName" ) 
	User fetchUserbyCompanyName(@Param("companyName") String companyName);
 
}
