package com.xplaptop.product;

import com.xplaptop.category.CategoryService;
import com.xplaptop.common.entity.Category;
import com.xplaptop.common.entity.product.Product;
import com.xplaptop.common.exception.CategoryNotFoundException;
import com.xplaptop.common.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductsController {
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService productService;
	
	@GetMapping("/c/{alias}")
	public String productsByCategoryAlias(
			@PathVariable(name = "alias") String alias,
			Model model) {
		
		return getProductCategoryAliasPage(alias, 1, "asc", "name", null, model);
	}
	
	@GetMapping("/c/{alias}/page/{pageNumber}")
	public String getProductCategoryAliasPage(
			@PathVariable(name = "alias") String alias,
			@PathVariable(name = "pageNumber") Integer pageNumber,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "keyword", required = false) String keyword,
			Model model) {
		
		Category category;
		try {
			category = categoryService.getCategoryByAlias(alias);
		} catch (CategoryNotFoundException e) {
			return "error/404";
		}
		
		if(sortDir == null) sortDir = "asc";
		if(sortField == null) sortField = "name";
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("sortField", sortField);
		model.addAttribute("keyword", keyword);
		
		List<Category> listCategoryParent = categoryService.listCategoryParent(category);
		model.addAttribute("listCategoryParent", listCategoryParent);
		model.addAttribute("category", category);
		
		Page<Product> page = productService.pageProductByCategory(category.getId(), pageNumber, sortDir, sortField);
		int totalPage = page.getTotalPages();
		int categoryPerPage = page.getNumberOfElements();
		long totalElement = page.getTotalElements();
		int startElement = (pageNumber - 1) * categoryPerPage + 1;
		int endElement = pageNumber * categoryPerPage;
		model.addAttribute("pageNumber", pageNumber);
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("totalElement", totalElement);
		model.addAttribute("startElement", startElement);
		model.addAttribute("endElement", endElement);
		
		List<Product> productsByCategory = page.getContent();
		model.addAttribute("productsByCategory", productsByCategory);
		
		return "product/products_by_category_alias";
	}
	
	@GetMapping("/p/{alias}")
	public String productDetail(
		@PathVariable(name = "alias") String alias,
		Model model) {
		
		Product product;
		try {
			product = productService.getProductByAlias(alias);
		} catch (ProductNotFoundException e) {
			return "error/404";
		}
		model.addAttribute("product", product);
		System.out.println(product.getName());
		List<Category> listCategoryParent = categoryService.listCategoryParent(product.getCategory());
		model.addAttribute("listCategoryParent", listCategoryParent);
		
		return "product/product_detail";
	}
	
	@GetMapping("/search")
	public String searchProductsFirstPage(
			Model model,
			@RequestParam(name = "keyword") String keyword) {
		
		return searchProducts(model, keyword, 1);
	}
	
	@GetMapping("/search/page/{pageNumber}")
	public String searchProducts(
			Model model,
			@RequestParam(name = "keyword") String keyword,
			@PathVariable(name = "pageNumber") Integer pageNumber) {
		
		Page<Product> page = productService.findProductsByKeyword(keyword, pageNumber);
		List<Product> searchResults = page.getContent();
		
		if(searchResults.isEmpty()) {
			model.addAttribute("message", "No products found for keyword: "+keyword);
		} else {
			model.addAttribute("searchResults", searchResults);
			
			int totalPage = page.getTotalPages();
			int categoryPerPage = page.getNumberOfElements();
			long totalElement = page.getTotalElements();
			int startElement = (pageNumber - 1) * categoryPerPage + 1;
			int endElement = pageNumber * categoryPerPage;
			model.addAttribute("pageNumber", pageNumber);
			model.addAttribute("totalPage", totalPage);
			model.addAttribute("totalElement", totalElement);
			model.addAttribute("startElement", startElement);
			model.addAttribute("endElement", endElement);
		}
		model.addAttribute("keyword", keyword);
		return "product/products_search_result";
	}
	
}
