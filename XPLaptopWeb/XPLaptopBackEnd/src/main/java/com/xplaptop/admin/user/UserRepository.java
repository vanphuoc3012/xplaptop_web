package com.xplaptop.admin.user;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.xplaptop.common.entity.user.User;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Integer>{
	
	@Query("SELECT u FROM User u WHERE u.email = :email")
	User getUserByEmail(@Param("email") String email);
	
	Integer countById(Integer id);
	
	@Query("UPDATE User u SET u.enabled = ?2 WHERE u.id = ?1")
	@Modifying
	void updateEnableStatus(Integer id, Boolean enable);
	
	@Query("SELECT u FROM User u WHERE CONCAT(u.email,' ', u.id,' ', u.lastName,' ', u.firstName) LIKE %?1%")
	Page<User> findAll(String keyword, Pageable pageable);
	
	Optional<User> findByEmail(String email);
}
