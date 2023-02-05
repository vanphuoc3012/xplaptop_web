package com.xplaptop.customer;

import com.xplaptop.common.entity.country.Country;
import com.xplaptop.common.entity.customer.Customer;
import com.xplaptop.setting.CountryRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class CustomerService {
	@Autowired
	private CustomerRepository customerRepo;
	
	@Autowired
	private CountryRepository countryRepo;

	@Autowired
	PasswordEncoder passwordEncoder;


	public List<Country> listAllCountries() {
		return countryRepo.findAllByOrderByNameAsc();
	}

	public Customer findCustomerByEmail(String email) {
		Customer customer = customerRepo.findByEmail(email);
		if(customer == null) throw new CustomerNotFoundException("Can't find any customer with email: "+email);
		return customer;
	}

	public boolean isEmailAlreadyUsed(String email) {
		return customerRepo.existsByEmail(email);

	}

	public void registerCustomer(Customer customer) {
		encodePassword(customer);
		customer.setEnabled(false);
		customer.setCreatedTime(new Date());

		String randomCode = RandomString.make(64);
		customer.setVerificationCode(randomCode);
		customerRepo.save(customer);
	}

	private void encodePassword(Customer customer) {
		String encodedPassword = passwordEncoder.encode(customer.getPassword());
		customer.setPassword(encodedPassword);
	}

	@Transactional
	public boolean verifyCustomer(String verificationCode) {
		Customer customer = customerRepo.findByVerificationCode(verificationCode);

		if(customer == null || customer.isEnabled()) return false;

		customerRepo.enable(customer.getId());

		return true;
	}
}
