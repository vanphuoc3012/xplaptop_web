package com.xplaptop.admin.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {
	
	@Autowired
	private UserService service;
	
	@PostMapping("/user/check_email")
	public String checkDuplicateEmail(@Param("id") Integer id, 
									@Param("email") String email) {
		if(service.isEmailExist(id, email)) {
			return "Duplicated";
		} else {
			return "OK";
		}
	}

}
