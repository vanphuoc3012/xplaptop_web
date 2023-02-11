package com.xplaptop.admin.user.controller;

import com.xplaptop.admin.FileUploadUtils;
import com.xplaptop.admin.exporter.UserCSVExporter;
import com.xplaptop.admin.exporter.UserExcelExporter;
import com.xplaptop.admin.exporter.UserPDFExporter;
import com.xplaptop.admin.paging.PagingAndSortingHelper;
import com.xplaptop.admin.paging.PagingAndSortingParam;
import com.xplaptop.common.exception.UserNotFoundException;
import com.xplaptop.admin.user.UserService;
import com.xplaptop.common.entity.Role;
import com.xplaptop.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class UserController {
	
	@Autowired
	private UserService service;
	
	
	@GetMapping("/users")
	public String listAllUsers() {
		return "redirect:/users/page/1?sortDir=asc&sortField=firstName";
	}
	
	@GetMapping("users/page/{pageNumber}")
	public String listUserByPage(@PagingAndSortingParam PagingAndSortingHelper helper,
								 @PathVariable Integer pageNumber,
								 Model model,
								 @RequestParam(required = false) String sortField,
								 @RequestParam(required = false) String sortDir,
								 @RequestParam(required = false) String keyword,
								 HttpServletRequest request) {
		if(sortDir == null || sortField == null) {
			return "redirect:/users/page/"+pageNumber+"?sortField=firstName&sortDir=asc";
		}

		model.addAttribute("keyword", keyword);

		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Page<User> page = service.listByPage(pageNumber, sort, keyword);
		List<User> listUsers = page.getContent();
		model.addAttribute("listUsers", listUsers);
		helper.updateModelPaginationAttributes(pageNumber, page);
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
	public String saveUser(User user, RedirectAttributes redirectAttributes
							,@RequestParam(name = "image", required = false) MultipartFile multipartFile
							) throws IOException {
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
		return "redirect:/users/page/1?sortField=email&sortDir=asc&keyword="+user.getEmail().split("@")[0];
		
			
		
		
	}
	
	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable Integer id,
							Model model,
							RedirectAttributes redirectAttributes
							) {
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
									RedirectAttributes redirectAttributes,
									@RequestParam(name = "path") String path) {
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
		
		System.out.println(path);
		return "redirect:"+path.replace(">", "&");
	}
	
	@GetMapping("/users/export/csv")
	public void exportCSV(HttpServletResponse response) throws IOException {
		
		List<User> listUsers = service.listAllUser();
		UserCSVExporter exporter = new UserCSVExporter();
		exporter.export(listUsers, response);
		
	}
	
	@GetMapping("/users/export/excel")
	public void exportExcel(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAllUser();
		
		UserExcelExporter exporter = new UserExcelExporter();
		exporter.export(listUsers, response);
		
	}
	
	@GetMapping("/users/export/pdf")
	public void exportPDF(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAllUser();
		UserPDFExporter exporter = new UserPDFExporter();
		exporter.export(listUsers, response);
		
	}
}
