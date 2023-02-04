package com.xplaptop.admin.customer.controller;

import com.xplaptop.admin.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerRestController {

    @Autowired
    private CustomerService customerService;

    @PostMapping ("/check_customer_email")
    public String checkCustomerEmail(@RequestParam(name = "email") String email,
                                     @RequestParam(name = "id") Integer id) {
        return customerService.checkCustomerEmail(id, email);
    }

//    @GetMapping("/update_all_customer")
//    public ResponseEntity<?> updateAllCustomer() {
//
//        return new ResponseEntity<>(customerService.updateAllCustomer(), HttpStatus.OK);
//    }

}
