package com.xplaptop.admin.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
		model.addAttribute("title", "Create new User");
		
		return "user_form";
	}
	
	@PostMapping("/users/save") 
	public String saveUser(User user, RedirectAttributes redirectAttributes) {
		System.out.println(user);
		if(user.getId() != null) {
			redirectAttributes.addFlashAttribute("message", "All the change on user ID: "+user.getId()+" has been saved successfully!!!");
		} else {
			redirectAttributes.addFlashAttribute("message", "The user has been saved successfully!!!");
		}
		User savedUser = service.save(user);
		
		System.out.println(savedUser);
		return "redirect:/users";
	}
	
	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable Integer id,
							Model model,
							RedirectAttributes redirectAttributes) {
		try {
			User user = service.getUserById(id);
			model.addAttribute("user", user);
			List<Role> listRoles = service.listAllRoles();
			model.addAttribute("listRoles", listRoles);
			model.addAttribute("title", "Edit user detail (ID: "+id+")");
			
			return "user_form";
		} catch (UserNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/users";
		}	
	}
	
	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id,
							RedirectAttributes redirectAttributes) {
		try {
			service.deleteUserById(id);
			redirectAttributes.addFlashAttribute("message", "User ID: "+id+" has been deleted successfully!");
		} catch (UserNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		
		return "redirect:/users";
	}
	
	@GetMapping("/users/enable/{id}")
	public String updateEnableStatus(@PathVariable(name = "id") Integer id,
			RedirectAttributes redirectAttributes) {
		try {
			Boolean isEnable = service.updateEnableStatus(id);
			if(isEnable) {
				redirectAttributes.addFlashAttribute("message", "User ID: "+id+" has been enabled!");
			} else {
				redirectAttributes.addFlashAttribute("message", "User ID: "+id+" has been disabled!");
			}
		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/users";
	}
}
