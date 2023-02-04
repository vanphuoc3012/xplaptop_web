package com.xplaptop.security;

import com.xplaptop.common.entity.customer.Customer;
import com.xplaptop.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerUserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private CustomerRepository repo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = repo.findByEmail(email);

        if(customer == null) throw new UsernameNotFoundException("No customer found with email: " + email);

        return new CustomerUserDetailsImpl(customer);
    }
}
