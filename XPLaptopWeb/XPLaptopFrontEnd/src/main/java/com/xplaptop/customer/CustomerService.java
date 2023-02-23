package com.xplaptop.customer;

import com.xplaptop.common.entity.user.AuthenticationType;
import com.xplaptop.common.entity.country.Country;
import com.xplaptop.common.entity.country.State;
import com.xplaptop.common.entity.customer.Customer;
import com.xplaptop.common.exception.CustomerNotFoundException;
import com.xplaptop.setting.CountryRepository;
import com.xplaptop.setting.StateRepository;
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
	@Autowired
	private StateRepository stateRepository;

	public List<Country> listAllCountries() {
		return countryRepo.findAllByOrderByNameAsc();
	}

	public Customer findCustomerByEmail(String email) {
		return customerRepo.findByEmail(email);
	}

	public Customer findCustomerByResetPasswordToken(String token) throws CustomerNotFoundException {
		return customerRepo.findByResetPasswordToken(token).orElseThrow(
				() -> new CustomerNotFoundException("Invalid token")
		);
	}

	public List<State> findAllStatesByCountryId(Integer countryId) {
		return stateRepository.findAllByCountry_Id(countryId);
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

	@Transactional
	public void updateAuthentication(Customer customer, AuthenticationType type) {
		if(!customer.getAuthenticationType().equals(type)) {
			customerRepo.updateAuthenticationType(customer.getId(), type);
		}
	}

	public void addNewCustomerUponOAuthLogin(String name, String email, String countryCode, AuthenticationType authenticationType) {
		Customer customer = new Customer();
		setName(name, customer);
		customer.setEmail(email);
		customer.setEnabled(true);
		customer.setCreatedTime(new Date());
		customer.setAuthenticationType(authenticationType);
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

	public void updateCustomerInfo(Customer customerInForm) throws CustomerNotFoundException {
		Customer customerInDB = customerRepo.findById(customerInForm.getId())
				.orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
		if(customerInForm.getAuthenticationType().equals(customerInDB.getAuthenticationType())) {
			if(customerInForm.getPassword() == null || customerInForm.getPassword().isEmpty()) {
				customerInForm.setPassword(customerInDB.getPassword());
			} else {
				encodePassword(customerInForm);
			}
		} else {
			customerInForm.setPassword(customerInDB.getPassword());
		}
		customerInForm.setCreatedTime(customerInDB.getCreatedTime());
		customerInForm.setVerificationCode(customerInDB.getVerificationCode());
		customerInForm.setAuthenticationType(customerInDB.getAuthenticationType());

		customerRepo.save(customerInForm);
	}

	public String updateResetPasswordToken(String email) throws CustomerNotFoundException {
		Customer customer = findCustomerByEmail(email);
		if(customer == null) throw new CustomerNotFoundException("Can not found any user with email: "+email);
		String token = RandomString.make(32);
		customer.setResetPasswordToken(token);
		customerRepo.save(customer);

		return token;
	}

	public void updateNewPassword(String token, String password) throws CustomerNotFoundException {
		Customer customer = customerRepo.findByResetPasswordToken(token).orElseThrow(
				() -> new CustomerNotFoundException("Invalid token")
		);
		customer.setPassword(passwordEncoder.encode(password));
		customer.setResetPasswordToken(null);
		customerRepo.save(customer);
	}
}
