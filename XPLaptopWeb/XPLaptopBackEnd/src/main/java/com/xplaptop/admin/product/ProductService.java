package com.xplaptop.admin.product;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.xplaptop.common.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.xplaptop.common.exception.BrandNotFoundException;
import com.xplaptop.admin.brand.BrandService;
import com.xplaptop.common.entity.Category;
import com.xplaptop.common.entity.product.Product;

@Service
public class ProductService {
	
	public final static Integer PRODUCT_PER_PAGE = 4;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private BrandService brandService;
	
	public List<Product> listAll() {
		return (List<Product>) productRepository.findAll();
	}
	
	public List<Product> listProductPage(Integer pageNumber, String sortDir, String sortField, String keyword, Integer categoryId) {
		Page<Product> page = pageProductPage(pageNumber, sortDir, sortField, keyword,categoryId);
		return page.getContent();
	}
	
	public Page<Product> pageProductPage(Integer pageNumber, String sortDir, String sortField, String keyword, Integer categoryId) {
		Sort sort = Sort.by(sortField);
		if(sortDir.equals("asc")) {
			sort = sort.ascending();
		} else {
			sort = sort.descending();
		}
		Pageable pageable = PageRequest.of(pageNumber-1, PRODUCT_PER_PAGE, sort);
		if(keyword != null && !keyword.isEmpty()) {
			if(categoryId != null && categoryId > 0) {
				String categoryIdMatch = "-"+categoryId+"-";
				return productRepository.searchAllInCategory(categoryId, categoryIdMatch, keyword, pageable);
			}
			return productRepository.findAll(keyword, pageable);
		}
		if(categoryId != null && categoryId > 0) {
			String categoryIdMatch = "-"+categoryId+"-";
			return productRepository.findAllInCategory(categoryId, categoryIdMatch, pageable);
		}
		return productRepository.findAll(pageable);
		
	}
	
	public Product findProductById(Integer id) throws ProductNotFoundException {
		Product product = productRepository.findById(id).get();
		if(product !=  null) {
			return product; 
		} else {
			throw new ProductNotFoundException("Can't found any product with ID: "+id);
		}
	}
	
	public String checkUnique(Integer id, String name, String alias) {
		boolean isCreateNew = (id == null) || (id == 0);
		Product productByName = productRepository.findByName(name);
		Product productByAlias = productRepository.findByAlias(alias);
		if(isCreateNew) {
			if(productByName != null) {
				return "DuplicatedName";
			}
			if(productByAlias != null) {
				return "DuplicatedAlias";
			}			
		} else {
			if(productByName != null && productByName.getId() != id) {
				return "DuplicatedName";
			}
			if(productByAlias != null && productByAlias.getId() != id) {
				return "DuplicatedAlias";
			}	
		}	
		return "OK";
	}
	
	public Set<Category> listCategoryForProductForm(Integer brandId) throws BrandNotFoundException {
		return brandService.listCategoriesByBrandId(brandId);
	}
	
	public Product save(Product product) {
		if(product.getId() == null || product.getId() == 0) {
			product.setCreatedTime(LocalDateTime.now());
		} else {
			product.setUpdatedTime(LocalDateTime.now());
		}
		product.setAlias(product.getAlias().replaceAll(" ", "-"));
		return productRepository.save(product);
	}
	
	public String enableStatusUpdate(Integer id) throws ProductNotFoundException {
		Product product = findProductById(id);
		String m = "";
		if(product.isEnabled()) {
			product.setEnabled(false);
			m = "disabled";
		} else {
			product.setEnabled(true);
			m = "enabled";
		}
		save(product);
		return m;
	}
	
	public void deleteProductById(Integer id) throws ProductNotFoundException {
		@SuppressWarnings("unused")
		Product product = findProductById(id);
		productRepository.deleteById(id);
	}
}
