package com.d2z.d2zservice.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.d2z.d2zservice.entity.User;

public interface UserRepository extends CrudRepository<User, Long>{
	
	@Query("SELECT t FROM User t where t.username = :userName and password = :passWord")  
	User fetchUserDetails(@Param("userName") String userName, @Param("passWord") String passWord);
	
	 @Query("SELECT u.companyName FROM User u where u.role_Id=3 and u.clientBrokerId=:brokerId order by timestamp desc") 
	 List<String> fetchCompanyName(@Param("brokerId") String brokerId);
	
	 @Query("Select t.username from User t where t.user_IsDeleted = false") 
	 List<String> fetchAllUserName();

	 @Query("SELECT u FROM User u where u.companyName = :companyName and u.role_Id= :roleId") 
	 User fetchUserbyCompanyName(@Param("companyName") String companyName, @Param("roleId") int roleId);

	 @Query("Select user_Id from User t where t.username = :userName and t.user_IsDeleted = false") 
	 Integer fetchUserIdbyUserName(@Param("userName") String userName);
	
	 @Query("SELECT u.companyName FROM User u where u.role_Id=2") 
	 List<String> fetchBrokerCompanyName();
	 
	 @Query("SELECT u FROM User u where u.companyName = :companyName and u.role_Id=2") 
	 User fetchBrokerbyCompanyName(@Param("companyName") String companyName);
	 
	 @Query("SELECT DISTINCT u.user_Id FROM User u where u.clientBrokerId = :userId") 
	 List<Integer> getClientId(@Param("userId") String userId);
	 
	 User findByUsername(String username);

}
