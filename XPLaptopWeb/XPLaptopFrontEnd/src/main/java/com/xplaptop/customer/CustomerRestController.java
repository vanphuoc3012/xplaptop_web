package com.xplaptop.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerRestController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/check_customer_email")
    public String checkEmailUnique(@RequestParam(name = "email") String email) {
        System.out.println(email);
        boolean emailAlreadyUsed = customerService.isEmailAlreadyUsed(email);
        if(emailAlreadyUsed) return "Duplicated Email";
        return "OK";
    }
}
