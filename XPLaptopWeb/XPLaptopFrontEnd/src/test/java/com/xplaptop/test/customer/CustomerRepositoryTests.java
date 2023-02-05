package com.xplaptop.test.customer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.xplaptop.common.entity.AuthenticationType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.xplaptop.common.entity.country.Country;
import com.xplaptop.common.entity.customer.Customer;
import com.xplaptop.customer.CustomerRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTests {

	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	TestEntityManager testEntityManager;
	
	@Test
	public void testCreateCustomer1() {
		Integer countryId = 236; //united state
		Country country = testEntityManager.find(Country.class, countryId);
		
		Customer customer = new Customer();
		customer.setCountry(country);
		customer.setFirstName("Davide");
		customer.setLastName("Beckham");
		customer.setPassword("password123");
		customer.setEmail("davidb@gmail.com.vn");
		customer.setPhoneNumber("312-462-7518");
		customer.setAddressLine1("1927 Dien Bien Phu");
		customer.setCity("hCM");
		customer.setState("HCM");
		customer.setPostalCode("70000");
		customer.setCreatedTime(new Date());
		
		Customer savedCustomer = customerRepository.save(customer);
		
		assertThat(savedCustomer).isNotNull();
		assertThat(savedCustomer.getId()).isGreaterThan(0);
		
	}
	
	@Test
	public void testCreateCustomer2() {
		Integer countryId = 144; 
		Country country = testEntityManager.find(Country.class, countryId);
		
		Customer customer = new Customer();
		customer.setCountry(country);
		customer.setFirstName("Davide");
		customer.setLastName("Beckham");
		customer.setPassword("password123");
		customer.setEmail("davidb2@gmail.com.vn");
		customer.setPhoneNumber("312-462-7518");
		customer.setAddressLine1("1927 Dien Bien Phu");
		customer.setCity("hCM");
		customer.setState("HCM");
		customer.setPostalCode("70000");
		customer.setCreatedTime(new Date());
		
		Customer savedCustomer = customerRepository.save(customer);
		
		assertThat(savedCustomer).isNotNull();
		assertThat(savedCustomer.getId()).isGreaterThan(0);
		
	}
	
	@Test
	public void getCustomerTest() {
		Optional<Customer> findById1 = customerRepository.findById(2);
		Optional<Customer> findById2 = customerRepository.findById(3);
		
		assertThat(findById1).isPresent();
		assertThat(findById1.get().getId()).isEqualTo(2);
		
		assertThat(findById2).isPresent();
		assertThat(findById2.get().getId()).isEqualTo(3);
		
		List<Customer> findAll = customerRepository.findAll();
		assertThat(findAll).hasSizeGreaterThan(1);
		
	}
	
	@Test
	public void testFindCustomerByEmail() {
		Customer findByEmail = customerRepository.findByEmail("davidb@gmail.com.vn");
		
		assertThat(findByEmail).isNotNull();
		assertThat(findByEmail.getId()).isGreaterThan(0);
	}

	@Test
	public void updateAuthenticationType() {
		int id = 3;
		customerRepository.updateAuthenticationType(id, AuthenticationType.DATABASE);

		Assertions.assertThat(customerRepository.findById(id).get().getAuthenticationType()).isEqualTo(AuthenticationType.DATABASE);
	}
}
