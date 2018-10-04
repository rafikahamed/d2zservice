package com.d2z.d2zservice.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.d2z.d2zservice.entity.User;

public interface UserRepository extends CrudRepository<User, Long>{
	
	@Query("SELECT t FROM User t where t.user_Name = :userName and user_Password = :passWord")  
	User fetchUserDetails(@Param("userName") String userName, @Param("passWord") String passWord);
	
}
