package com.d2z.d2zservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.d2z.d2zservice.entity.User;

public interface UserRepository extends CrudRepository<User, Long>{
	
	 @Query("SELECT t FROM User t where t.username = :userName and password_value = :passWord")  
	 User fetchUserDetails(@Param("userName") String userName, @Param("passWord") String passWord);
	
	 @Query("SELECT u.companyName FROM User u where u.role_Id=3 and u.clientBrokerId=:brokerId order by timestamp desc") 
	 List<String> fetchCompanyName(@Param("brokerId") String brokerId);
	
	 @Query("Select t.username from User t where t.user_IsDeleted = false") 
	 List<String> fetchAllUserName();

	 @Query("SELECT u FROM User u where u.companyName = :companyName and u.role_Id= :roleId") 
	 User fetchUserbyCompanyName(@Param("companyName") String companyName, @Param("roleId") int roleId);

	 @Query("Select t.user_Id from User t where t.username = :userName") 
	 Integer fetchUserIdbyUserName(@Param("userName") String userName);
	 
	 @Query("Select t.clientBrokerId from User t where t.username = :userName") 
	 Integer fetchClientBrokerIdbyUserName(@Param("userName") String userName);
	
	 @Query("SELECT u.companyName FROM User u where u.role_Id=2") 
	 List<String> fetchBrokerCompanyName();
	 
	 @Query("SELECT u FROM User u where u.companyName = :companyName and u.role_Id=2") 
	 @EntityGraph(attributePaths = "userService")
	 User fetchBrokerbyCompanyName(@Param("companyName") String companyName);
	 
	 @Query("SELECT DISTINCT u.user_Id FROM User u where u.clientBrokerId = :userId") 
	 List<Integer> getClientId(@Param("userId") String userId);
	 
	 User findByUsername(String username);

	 @Query("SELECT t FROM User t where t.role_Id = 2 and t.user_IsDeleted=0")  
	 @EntityGraph(attributePaths = "userService")
	 List<User> fetchBrokerList();
	 
	 @Query(nativeQuery = true, value="select user_ID from dbo.Users where client_Broker_id in (select user_ID from dbo.users where role_Id=2)")  
	 List<Integer> fetchBrokerClientIds();

	 @Query ("Select u.autoShipment from User u where u.user_Id = :user_Id")
	 String fetchAutoShipmentIndicator(Integer user_Id);

	@Query ("Select u.username from User u where u.typeOfClient = 'NonD2Z'")
	List<String> fetchNonD2zBrokerUserName();

	@Query ("Select u.username from User u where u.user_Id = :user_Id")
	String fetchUserById(Integer user_Id);
	 
	@Query("SELECT u FROM User u where u.role_Id = 2") 
	@EntityGraph(attributePaths = "userService")
	List<User> broker();

	@Query ("Select u.postcodeValidate from User u where u.username = :userName")
	Object fetchPostcodeValidationIndicator(String  userName);
	
	@Query(nativeQuery = true, value=" select * from dbo.Users where Client_Broker_id in (\n" + 
			"   SELECT distinct(Client_Broker_id) FROM [D2Z].[dbo].[csticketsbackup] where \n" + 
			"   status='open' and Client_Broker_id is not null ) and role_id=2") 
	@EntityGraph(attributePaths = "userService")
	List<User> fetchEmailDetails();

}
