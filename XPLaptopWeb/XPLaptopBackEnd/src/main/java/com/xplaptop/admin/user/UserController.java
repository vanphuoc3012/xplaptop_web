package com.xplaptop.admin.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.xplaptop.admin.FileUploadUtils;
import com.xplaptop.common.entity.Role;
import com.xplaptop.common.entity.User;

@Controller
public class UserController {
	
	@Autowired
	private UserService service;
	
	
	@GetMapping("/users")
	public String listAllUsers(Model model) {
		return listUserByPage(1, model);
	}
	
	@GetMapping("users/page/{pageNumber}")
	public String listUserByPage(@PathVariable Integer pageNumber,
									Model model) {
		Page<User> page = service.listByPage(pageNumber);
		List<User> listUsers = page.getContent();
		
		int totalPage = page.getTotalPages();
		int userPerPage = page.getNumberOfElements();
		long totalUser = page.getTotalElements();
		int startUser = (pageNumber - 1) * userPerPage + 1;
		int endUser = pageNumber * userPerPage;
		
		if(totalPage > 0) {
			List<Integer> listPages = IntStream.rangeClosed(1, totalPage)
					.boxed()
					.collect(Collectors.toList());
			model.addAttribute("listPages", listPages);
		}
		model.addAttribute("pageNumber", pageNumber);
		model.addAttribute("startUser", startUser);
		model.addAttribute("endUser", endUser);
		model.addAttribute("totalUser", totalUser);
		model.addAttribute("totalPage", totalPage);
		
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
	public String saveUser(User user, RedirectAttributes redirectAttributes,
							@RequestParam(name = "image", required = false) MultipartFile multipartFile) throws IOException {
		System.out.println(user);
		if(multipartFile.isEmpty()) {
			if(user.getId() != null) {
				redirectAttributes.addFlashAttribute("message", "All the change on user ID: "+user.getId()+" has been saved successfully!!!");
			} else {
				redirectAttributes.addFlashAttribute("message", "The user has been saved successfully!!!");
			}
			service.save(user);
		} else {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			String uploadDirs = "users-photos";			
			if(user.getId() != null) {
				uploadDirs = uploadDirs + "/"+user.getId();
				user.setPhotos(fileName);
				service.save(user);
				redirectAttributes.addFlashAttribute("message", "All the change on user ID: "+user.getId()+" has been saved successfully!!!");
			} else {
				service.save(user);
				uploadDirs = uploadDirs + "/"+user.getId();
				user.setPhotos(fileName);
				service.save(user);
				redirectAttributes.addFlashAttribute("message", "The user has been saved successfully!!!");
			}
			FileUploadUtils.cleanDir(uploadDirs);
			FileUploadUtils.uploadFile(uploadDirs, fileName, multipartFile);
		}
		System.out.println(user);
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
							RedirectAttributes redirectAttributes) throws IOException {
		try {
			service.deleteUserById(id);
			String uploadDirs = "users-photos/"+id;
			FileUploadUtils.cleanDir(uploadDirs);
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
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/users";
	}
}
