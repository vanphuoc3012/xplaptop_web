package com.xplaptop.admin.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.xplaptop.common.entity.Role;
import com.xplaptop.common.entity.User;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public List<User> listAllUser() {
		return (List<User>) userRepo.findAll();
	}
	
	public List<Role> listAllRoles() {
		return (List<Role>) roleRepo.findAll();
	}

	public User save(User user) {
		encodePassword(user);
		return userRepo.save(user);
	}
	
	public void encodePassword(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
	}
}
