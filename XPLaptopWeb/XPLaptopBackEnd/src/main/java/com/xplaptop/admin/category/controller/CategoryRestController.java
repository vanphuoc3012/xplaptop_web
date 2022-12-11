package com.xplaptop.admin.category.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.xplaptop.admin.category.CategoryService;
import com.xplaptop.common.entity.Category;

@RestController
public class CategoryRestController {
	
	@Autowired
	private CategoryService service;
	
	@PostMapping("/categories/check_unique")
	public String checkUnique(@RequestBody Category checkCategory) {	
		System.out.println("Checking");
		System.out.println(checkCategory.getName() + checkCategory.getAlias());
		return service.checkUniqe(checkCategory);
	}
}
