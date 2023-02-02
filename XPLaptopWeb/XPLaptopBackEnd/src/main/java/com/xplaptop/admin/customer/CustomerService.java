package com.xplaptop.admin.customer;

import com.xplaptop.common.entity.country.Country;
import com.xplaptop.common.entity.customer.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final static int CUSTOMERS_PER_PAGE = 5;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private PasswordEncoder encoder;

    public Page<Customer> getPage(Integer pageNumber, String sortDir, String sortField, String keyword) {
        Sort sort = Sort.by(sortField);
        if(sortDir.equals("asc")) {
            sort = sort.ascending();
        } else {
            sort = sort.descending();
        }
        Pageable pageable = PageRequest.of(pageNumber - 1, CUSTOMERS_PER_PAGE, sort);

        if(keyword == null) return customerRepo.findAll(pageable);

        return customerRepo.findAll(keyword, pageable);
    }

    public String updateEnabledStatus(Integer id) throws CustomerNotFoundException {
        Customer customer = findCustomerById(id);
        String m = "";
        if(customer.isEnabled()) {
            customer.setEnabled(false);
            m = "disabled";
        } else {
            customer.setEnabled(true);
            m = "enabled";
        }
        customerRepo.save(customer);
        return m;
    }

    public void deleteCustomerById(Integer id) throws CustomerNotFoundException {
        Customer customer = findCustomerById(id);
        customerRepo.deleteById(id);
    }

    public Customer findCustomerById(Integer id) throws CustomerNotFoundException {
        Customer customer = customerRepo.findById(id).get();
        if(customer !=  null) {
            return customer;
        } else {
            throw new CustomerNotFoundException("Can't found any customer with ID: "+id);
        }
    }

    public String checkCustomerEmail(Integer id, String email) {
        Customer customer = customerRepo.findByEmail(email);
        if(customer == null) return "OK";

        if(customer.getId() == id) return "OK";

        return "Duplicated Email";
    }

    public Customer updateCustomer(Customer customerInform) {
        if(customerInform.getPassword() != null) {
            customerInform.setPassword(encoder.encode(customerInform.getPassword()));
        } else {
            Customer customer = customerRepo.findById(customerInform.getId()).get();
            customerInform.setPassword(customer.getPassword());
        }
        return customerRepo.save(customerInform);

    }

    @Transactional
    public List<Customer> updateAllCustomer() {
        List<Customer> customerList = customerRepo.findAll();
        String password = encoder.encode("password");
        Random random = new Random();
        List<Customer> customerList1 = customerList.stream()
                .map(customer -> {
                    customer.setCreatedTime(new Date());
                    customer.setPassword(password);
                    customer.setCountry(new Country(random.nextInt(250)));
                    System.out.println(customer.getEmail());
                    return customer;
                })
                .collect(Collectors.toList());

        return customerList1;
    }
}
