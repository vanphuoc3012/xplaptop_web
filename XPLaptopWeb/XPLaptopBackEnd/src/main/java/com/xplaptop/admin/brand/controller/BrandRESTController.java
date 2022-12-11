package com.xplaptop.admin.brand.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.xplaptop.admin.brand.BrandService;
import com.xplaptop.common.entity.Brand;

@RestController
public class BrandRESTController {
	
	@Autowired
	private BrandService brandService;
	
	@PostMapping("/brands/check_unique")
	private String checkUnique(@RequestBody Brand brand) {
		
		return brandService.checkUnique(brand);
	}
}
