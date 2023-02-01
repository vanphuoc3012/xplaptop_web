package com.xplaptop.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.xplaptop.common.entity.customer.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{
	
	Customer findByEmail(String email);
	
	Customer findByVerificationCode(String code);
	
	@Modifying
	@Query("UPDATE Customer c SET c.enabled = true, c.verificationCode = null WHERE c.id = ?1")
	void enable(Integer customerId);

	boolean existsByEmail(String email);
}
