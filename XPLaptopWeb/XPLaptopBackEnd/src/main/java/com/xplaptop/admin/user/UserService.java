package com.xplaptop.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xplaptop.common.entity.Role;
import com.xplaptop.common.entity.User;

@Service
@Transactional
public class UserService {
	
	private static final Integer USER_PER_PAGE = 5;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public List<User> listAllUser() {
		return (List<User>) userRepo.findAll();
	}
	
	public Page<User> listByPage(Integer pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber - 1, USER_PER_PAGE);
		Page<User> page = userRepo.findAll(pageable);
		
		return page;
	}
	
	public List<Role> listAllRoles() {
		return (List<Role>) roleRepo.findAll();
	}

	public User save(User user) {
		boolean isCreatingNew = (user.getId() != null);
		if(isCreatingNew) {
			User existingUser = userRepo.findById(user.getId()).get();
			
			user.setPassword(existingUser.getPassword());
		} else {
			encodePassword(user);
		}	
		return userRepo.save(user);
	}
	
	public void encodePassword(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
	}
	
	public boolean isEmailExist(Integer id, String email) {
		User u = userRepo.getUserByEmail(email);
		if(u == null) {
			return false;
		} 
		boolean isCreateNewUser = (id == null);
		if(!isCreateNewUser) {
			if(u.getId() == id) {
				return false;
			}
		}
		return true;
	}

	public User getUserById(Integer id) throws UserNotFoundException {
		try {
			return userRepo.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new UserNotFoundException("Could not find user with ID: "+id);
		}
		
	}
	
	public void deleteUserById(Integer id) throws UserNotFoundException {
		Integer u = userRepo.countById(id);
		if(u ==  null || u == 0) {
			throw new UserNotFoundException("Could not find user with ID: "+id);
		} else {
			userRepo.deleteById(id);
		}
	}
	
	public Boolean updateEnableStatus(Integer id) throws UserNotFoundException {
		User u = getUserById(id);
		if(u.isEnabled()) {
			userRepo.updateEnableStatus(id, false);
			return false;
		} else {
			userRepo.updateEnableStatus(id, true);
			return true;
		}
	}
	
	
}
