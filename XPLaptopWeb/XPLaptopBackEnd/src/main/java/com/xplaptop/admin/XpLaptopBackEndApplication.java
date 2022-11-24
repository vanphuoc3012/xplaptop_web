package com.xplaptop.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.xplaptop.common.entity", "com.xplaptop.admin.user"})
public class XpLaptopBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(XpLaptopBackEndApplication.class, args);
	}

}
