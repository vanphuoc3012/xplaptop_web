package com.xplaptop.admin.category.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.xplaptop.admin.paging.PagingAndSortingHelper;
import com.xplaptop.admin.paging.PagingAndSortingParam;
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
import com.xplaptop.admin.category.CategoryNotFoundException;
import com.xplaptop.admin.category.CategoryService;
import com.xplaptop.admin.exporter.CategoryCSVExporter;
import com.xplaptop.common.entity.Category;

@Controller
public class CategoryController {
	
	@Autowired
	CategoryService categoryService;
	
	@GetMapping("/categories")
	public String listAllCategories() {
		
		return "redirect:/categories/page/1?sortField=name&sortDir=asc";
	}
	
	@GetMapping("/categories/page/{pageNumber}")
	public String listAllByPage(@PagingAndSortingParam PagingAndSortingHelper helper,
								@RequestParam(name = "sortDir", required = false) String sortDir,
								Model model,
								@PathVariable Integer pageNumber,
								@RequestParam(name = "keyword", required = false) String keyword,
								@RequestParam(name = "sortField", required = false) String sortField) {
		if(sortDir == null || sortField == null) {
			return "redirect:/categories/page/"+pageNumber+"?sortField=name&sortDir=asc";
		}

		Page<Category> page = categoryService.getPage(pageNumber, sortDir, keyword);
		List<Category> listCategories = page.getContent();

		helper.updateModelPaginationAttributes(pageNumber, page);
		model.addAttribute("listCategories", listCategories);
		
		return "category/categories";
	}
	
	@GetMapping("/categories/enable/{id}")
	public String updateEnabledStatus(@PathVariable(name = "id") Integer id,
										RedirectAttributes ra,
										@RequestParam(name = "path") String path) {
		
		try {
			Category cate = categoryService.findById(id);
			categoryService.updateEnableStatus(id);
			if(cate.isEnabled()) {
				ra.addFlashAttribute("message", "Category "+cate.getName()+" and all sub-categories have been disabled.");
			} else {
				ra.addFlashAttribute("message", "Category "+cate.getName()+" have been enabled.");
			}
			
		} catch (CategoryNotFoundException e) {		
			ra.addFlashAttribute("message", e.getMessage());
		}
		
		return "redirect:"+path.replace(">", "&");
	}
	
	@GetMapping("/categories/edit/{id}")
	public String updateCategory(@PathVariable Integer id
								, RedirectAttributes ra
								, Model model,
								@RequestParam(name = "path") String path) {
		try {
			Category category = categoryService.findById(id);
			List<Category> categoriesInForm = categoryService.categoriesUsedInForm("asc");
			
			model.addAttribute("categoriesInForm", categoriesInForm);						
			model.addAttribute("category", category);
			model.addAttribute("title", "Edit Category: "+category.getName());
			model.addAttribute("path", path);
			
		} catch (CategoryNotFoundException e) {
			ra.addFlashAttribute("", e.getMessage());
		}
		return "category/categories_form"; 
	}
	
	@GetMapping("/categories/new")
	public String createNewCategory(Model model,
									@RequestParam(name = "path", required = false) String path) {
		Category category = new Category();
		category.setEnabled(true);
		
		List<Category> categoriesInForm = categoryService.categoriesUsedInForm("asc");

		model.addAttribute("categoriesInForm", categoriesInForm);
		model.addAttribute("title", "Create New Category");
		model.addAttribute("category", category);
		model.addAttribute("path", path);
		
		return "category/categories_form";
	}
	
	@GetMapping("/categories/delete/{id}")
	public String deleteCategory(@PathVariable(name = "id") Integer id,
								@RequestParam(name = "path") String path,
								RedirectAttributes redirectAttributes) throws IOException {
		try {
			categoryService.deleteCategory(id);
			String uploadedImgDir = "../category-images/" + id;
			FileUploadUtils.cleanDir(uploadedImgDir);
			redirectAttributes.addFlashAttribute("message", "Delete category successfully!");
		} catch (CategoryNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:"+path.replace(">", "&");
	}
	
	@PostMapping("/categories/save")
	public String saveCategory(Category category,
								@RequestParam(name = "cateImage") MultipartFile multipartFile,
								RedirectAttributes redirectAttributes,
								@RequestParam(name = "path") String path) throws IOException {
		System.out.println(category.getParent());
		String uploadPath = "../category-images";
		if(category.getId() == null) {			
			categoryService.save(category);
			redirectAttributes.addFlashAttribute("message", "Create new Category: " + category.getName() + " successfully");
		} else {
			redirectAttributes.addFlashAttribute("message", "All change on Category: "+ category.getName() +" have been save successfully");
		}
		
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		uploadPath = uploadPath + "/" + category.getId();
		
		if(!multipartFile.isEmpty()) {
			category.setImage(fileName);
			FileUploadUtils.cleanDir(uploadPath);
			FileUploadUtils.uploadFile(uploadPath, fileName, multipartFile);
		}
		categoryService.save(category);
	
		return "redirect:"+path.replace(">", "&");
	}
	
	@GetMapping("/categories/export/csv")
	public void exportCSV(HttpServletResponse response) throws IOException {
		CategoryCSVExporter exporter = new CategoryCSVExporter();
		List<Category> categoriesList = categoryService.categoriesUsedInForm("asc"); 
		
		exporter.export(categoriesList, response);
	}
}
