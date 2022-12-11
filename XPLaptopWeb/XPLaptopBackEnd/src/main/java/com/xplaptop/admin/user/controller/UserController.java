package com.xplaptop.admin.user.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.xplaptop.admin.FileUploadUtils;
import com.xplaptop.admin.exporter.UserCSVExporter;
import com.xplaptop.admin.exporter.UserExcelExporter;
import com.xplaptop.admin.exporter.UserPDFExporter;
import com.xplaptop.admin.user.UserNotFoundException;
import com.xplaptop.admin.user.UserService;
import com.xplaptop.common.entity.Role;
import com.xplaptop.common.entity.User;

@Controller
public class UserController {
	
	@Autowired
	private UserService service;
	
	
	@GetMapping("/users")
	public String listAllUsers(Model model,
								HttpServletRequest request) {
		return listUserByPage(1, model, "firstName", "asc", null, request);
	}
	
	@GetMapping("users/page/{pageNumber}")
	public String listUserByPage(@PathVariable Integer pageNumber,
									Model model,
									@RequestParam String sortField,
									@RequestParam String sortDir,
									@RequestParam(required = false) String keyword,
									HttpServletRequest request) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		Page<User> page = service.listByPage(pageNumber, sort, keyword);
		List<User> listUsers = page.getContent();
		
		int totalPage = page.getTotalPages();
		int userPerPage = page.getNumberOfElements();
		long totalElement = page.getTotalElements();
		int startElement = (pageNumber - 1) * userPerPage + 1;
		int endElement = pageNumber * userPerPage;
		
		if(totalPage > 0) {
			List<Integer> listPages = IntStream.rangeClosed(1, totalPage)
					.boxed()
					.collect(Collectors.toList());
			model.addAttribute("listPages", listPages);
		}
		model.addAttribute("pageNumber", pageNumber);
		model.addAttribute("startElement", startElement);
		model.addAttribute("endElement", endElement);
		model.addAttribute("totalElement", totalElement);
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		
		if(keyword != null) {
			if(!keyword.isEmpty()) {
				keyword = null;
			}
			model.addAttribute("keyword", keyword);
		}	
		model.addAttribute("listUsers", listUsers);
		 
		String queryString  = request.getQueryString();
		String path = request.getServletPath();
		if(request.getQueryString() != null) {
			path += "?" + queryString.replace("&", ">");
		}
		model.addAttribute("path", path);
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
	
	@GetMapping("/login")
	public String loginPage() {
		return "login";
	}

}
