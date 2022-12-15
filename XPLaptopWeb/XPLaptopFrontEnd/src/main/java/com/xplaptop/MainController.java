package com.xplaptop;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.xplaptop.category.CategoryService;
import com.xplaptop.common.entity.Category;

@Controller
public class MainController {
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("")
	public String viewHomepage(Model model) {
		List<Category> listCategories = categoryService.listCategoriesNoChild();
		model.addAttribute("listCategories", listCategories);
		
		return "index";
	}

}
