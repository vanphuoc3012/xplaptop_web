package com.xplaptop.admin.customer;

import com.xplaptop.common.entity.customer.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomerRepositoryTests {

    @Autowired
    private CustomerRepository repository;

    @Test
    public void listCustomer() {
        List<Customer> customerList = repository.findAll();
        customerList.forEach(System.out::println);

        assertThat(customerList).hasSizeGreaterThan(1);
    }

    @Test
    public void listCustomerPage() {
        Pageable pageable = PageRequest.of(0, 5);

        Page<Customer> page = repository.findAll(pageable);
        assertThat(page.getContent()).hasSize(5);


    }
}
