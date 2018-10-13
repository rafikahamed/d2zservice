package com.d2z.d2zservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.d2z.d2zservice.entity.User;

public interface UserRepository extends CrudRepository<User, Long>{
	
	@Query("SELECT t FROM User t where t.user_Name = :userName and user_Password = :passWord")  
	User fetchUserDetails(@Param("userName") String userName, @Param("passWord") String passWord);
	
	 @Query("SELECT u.companyName FROM User u where u.role_Id=3") 
	 List<String> fetchCompanyName();
	
	 @Query("Select t.user_Name from User t where t.user_IsDeleted = false") 
	 List<String> fetchAllUserName();

	 @Query("SELECT u FROM User u where u.companyName = :companyName and u.role_Id=3" ) 
	 User fetchUserbyCompanyName(@Param("companyName") String companyName);

	 @Query("Select user_Id from User t where t.user_Name = :userName and t.user_IsDeleted = false") 
	Integer fetchUserIdbyUserName(String userName);

}
