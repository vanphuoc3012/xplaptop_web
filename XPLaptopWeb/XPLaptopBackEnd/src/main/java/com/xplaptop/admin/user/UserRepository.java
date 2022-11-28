package com.xplaptop.admin.user;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.xplaptop.common.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer>{
	
	@Query("SELECT u FROM User u WHERE u.email = :email")
	User getUserByEmail(@Param("email") String email);
	
	Integer countById(Integer id);
	
	@Query("UPDATE User u SET u.enabled = ?2 WHERE u.id = ?1")
	@Modifying
	void updateEnableStatus(Integer id, Boolean enable);
}
