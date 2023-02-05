package com.xplaptop.customer;

import com.xplaptop.common.entity.AuthenticationType;
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
	private PasswordEncoder passwordEncoder;

	public List<Country> listAllCountries() {
		return countryRepo.findAllByOrderByNameAsc();
	}

	public Customer findCustomerByEmail(String email) {
		return customerRepo.findByEmail(email);
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
		customer.setAuthenticationType(AuthenticationType.DATABASE);
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

	public void updateAuthentication(Customer customer, AuthenticationType type) {
		if(!customer.getAuthenticationType().equals(type)) {
			customerRepo.updateAuthenticationType(customer.getId(), type);
		}
	}

	public void addNewCustomerUponOAuthLogin(String name, String email, String countryCode) {
		Customer customer = new Customer();

		setName(name, customer);

		customer.setEmail(email);
		customer.setEnabled(true);
		customer.setCreatedTime(new Date());
		customer.setAuthenticationType(AuthenticationType.GOOGLE);
		customer.setPassword("");
		customer.setAddressLine1("");
		customer.setPhoneNumber("");
		customer.setPostalCode("");
		customer.setCity("");
		customer.setState("");
		customer.setCountry(countryRepo.findByCode(countryCode));

		customerRepo.save(customer);
	}

	private void setName(String name, Customer customer) {
		String[] split = name.split(" ");
		if(split.length < 2) {
			customer.setFirstName(name);
			customer.setLastName("");
		} else {
			customer.setFirstName(split[0]);
			customer.setLastName(name.replace(split[0] + " ",""));
		}
	}
}
