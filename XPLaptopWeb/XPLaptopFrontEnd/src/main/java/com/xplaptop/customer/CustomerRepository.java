package com.xplaptop.customer;

import com.xplaptop.common.entity.AuthenticationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.xplaptop.common.entity.customer.Customer;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{
	
	Customer findByEmail(String email);
	
	Customer findByVerificationCode(String code);
	
	@Modifying
	@Query("UPDATE Customer c SET c.enabled = true, c.verificationCode = null WHERE c.id = ?1")
	void enable(Integer customerId);

	boolean existsByEmail(String email);

	@Query("UPDATE Customer c SET c.authenticationType = ?2 WHERE c.id = ?1")
	@Modifying
	void updateAuthenticationType(Integer customerId, AuthenticationType authenticationType);

	Optional<Customer> findByResetPasswordToken(String token);
}
