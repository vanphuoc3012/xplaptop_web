package com.xplaptop.address;

import com.xplaptop.common.entity.customer.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Integer> {

    Optional<Address> findByIdAndCustomer_Id(Integer id, Integer customerId);

    void deleteByIdAndCustomer_Id(Integer id, Integer customerId);

    List<Address> findAllByCustomer_Id(Integer customerId);

}
