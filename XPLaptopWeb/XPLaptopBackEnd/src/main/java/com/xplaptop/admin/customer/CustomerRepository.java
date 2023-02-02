package com.xplaptop.admin.customer;


import com.xplaptop.common.entity.customer.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Page<Customer> findAll(Pageable pageable);

    @Query("SELECT c FROM Customer c WHERE CONCAT(c.firstName,' ', c.lastName,' ', c.email,' ', c.addressLine1" +
            ", ' ', c.addressLine2, ' ', c.city, ' ', c.state, ' ', c.country, ' ', c.postalCode) LIKE %?1%")
    Page<Customer> findAll(String keyword, Pageable pageable);

    Customer findByEmail(String email);
}
