package com.xplaptop.admin.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.xplaptop.common.entity.Role;
import com.xplaptop.common.entity.User;

@Controller
public class UserController {
	
	@Autowired
	private UserService service;
	
	
	@GetMapping("/users")
	public String listAllUsers(Model model) {
		List<User> listUsers = service.listAllUser();
		model.addAttribute("listUsers", listUsers);
		return "users";
	}
	
	@GetMapping("/users/new") 
	public String createNewUser(Model model) {
		User user = new User();
		user.setEnabled(true);
		List<Role> listRoles = service.listAllRoles();
		
		model.addAttribute("user", user);
		model.addAttribute("listRoles", listRoles);
		
		return "user_form";
	}
	
	@PostMapping("/users/save") 
	public String saveUser(User user, RedirectAttributes redirectAttributes) {
		System.out.println(user);
			
		User savedUser = service.save(user);
		
		redirectAttributes.addFlashAttribute("message", "The user has been saved successfully!!!");
		System.out.println(savedUser);
		return "redirect:/users";
	}
}
