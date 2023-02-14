package com.xplaptop.admin.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xplaptop.admin.user.UserService;

@RestController
public class UserRestController {
	
	@Autowired
	private UserService service;
	
	@PostMapping("/user/check_email")
	public String checkDuplicateEmail(@RequestParam("id") Integer id,
									@RequestParam("email") String email) {
		if(service.isEmailExist(id, email)) {
			return "Duplicated";
		} else {
			return "OK";
		}
	}

}
