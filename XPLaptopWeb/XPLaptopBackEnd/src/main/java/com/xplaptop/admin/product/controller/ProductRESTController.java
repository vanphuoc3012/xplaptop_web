package com.xplaptop.admin.product.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xplaptop.admin.brand.BrandNotFoundException;
import com.xplaptop.admin.brand.BrandNotFoundRestException;
import com.xplaptop.admin.category.CategoryDTO;
import com.xplaptop.admin.product.ProductService;
import com.xplaptop.common.entity.Category;

@RestController
public class ProductRESTController {
	
	@Autowired
	private ProductService productService;
	
	@PostMapping("/products/check_unique")
	public String checkUnique(@RequestBody Map<String, String> product) {
		String stringId = product.get("id");
		Integer id = stringId.isEmpty() ? null : Integer.valueOf(stringId);
		String name = product.get("name");
		String alias = product.get("alias");
		return productService.checkUnique(id, name, alias);
	}
	
	@PostMapping("/products/getCategory")
	public List<CategoryDTO> getCategory(@RequestParam("brandId") Integer brandId) throws BrandNotFoundRestException {
		try {
			List<CategoryDTO> listCategoryDTOs = new ArrayList<>();
			Set<Category> listCategoriesByBrand = productService.listCategoryForProductForm(brandId);
			for(Category c: listCategoriesByBrand) {
				CategoryDTO categoryDTO = new CategoryDTO(c.getId(), c.getName());
				listCategoryDTOs.add(categoryDTO);
			}
			return listCategoryDTOs;
		} catch (BrandNotFoundException e) {
			throw new BrandNotFoundRestException();
		}
		
	}
}
