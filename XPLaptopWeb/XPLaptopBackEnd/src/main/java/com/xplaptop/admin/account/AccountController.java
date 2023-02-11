package com.xplaptop.admin.account;

import java.io.IOException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.xplaptop.admin.FileUploadUtils;
import com.xplaptop.admin.security.XPLaptopUserDetails;
import com.xplaptop.common.exception.UserNotFoundException;
import com.xplaptop.admin.user.UserService;
import com.xplaptop.common.entity.Role;
import com.xplaptop.common.entity.User;

@Controller
public class AccountController {
	
	@Autowired
	UserService service;
	
	@GetMapping("/account")
	public String editUser(Authentication auth,
							Model model,
							RedirectAttributes redirectAttributes) {
		String userName = auth.getName();
		try {
			User user = service.getUserByEmail(userName);
			model.addAttribute("user", user);
			Set<Role> listRoles = user.getRoles();
			model.addAttribute("listRoles", listRoles);
			
			return "account_form";
		} catch (UserNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/";
		}	
	}
	
	@PostMapping("/account/update") 
	public String saveUser(User user, RedirectAttributes redirectAttributes,
							@AuthenticationPrincipal XPLaptopUserDetails userDetails,
							@RequestParam(name = "image", required = false) MultipartFile multipartFile) throws IOException {
//		update username(create method set firstname and lastname in class XPLaptopUserDetails)
		userDetails.setFirstName(user.getFirstName());
		userDetails.setLastName(user.getLastName());
		
		System.out.println(user);
		if(multipartFile.isEmpty()) {			
			service.updateAccount(user);
		} else {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			String uploadDirs = "users-photos";			
			if(user.getId() != null) {
				uploadDirs = uploadDirs + "/"+user.getId();
				user.setPhotos(fileName);
				service.updateAccount(user);
				
			} else {
				service.updateAccount(user);
				uploadDirs = uploadDirs + "/"+user.getId();
				user.setPhotos(fileName);
				service.save(user);				
			}			
			FileUploadUtils.cleanDir(uploadDirs);
			FileUploadUtils.uploadFile(uploadDirs, fileName, multipartFile);
		}
		redirectAttributes.addFlashAttribute("message", "All the change has been saved successfully!!!");
		System.out.println(user);
		return "redirect:/account";
	}
	
}
