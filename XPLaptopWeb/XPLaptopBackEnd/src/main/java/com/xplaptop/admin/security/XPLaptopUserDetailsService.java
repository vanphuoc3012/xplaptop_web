package com.xplaptop.admin.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xplaptop.admin.user.UserRepository;
import com.xplaptop.common.entity.user.User;

@Service
public class XPLaptopUserDetailsService implements UserDetailsService {
	
	@Autowired
	UserRepository userRepo;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepo.findByEmail(username);
		if(user.isPresent()) {
			return new XPLaptopUserDetails(user.get());
		}
		throw new UsernameNotFoundException("Wrong username or password");
	}

}
