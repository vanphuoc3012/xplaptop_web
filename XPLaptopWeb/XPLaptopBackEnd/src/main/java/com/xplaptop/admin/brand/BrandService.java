package com.xplaptop.admin.brand;

import java.util.List;
import java.util.Set;

import com.xplaptop.common.exception.BrandNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.xplaptop.admin.category.CategoryService;
import com.xplaptop.common.entity.Brand;
import com.xplaptop.common.entity.Category;

@Service
public class BrandService {
	
	private static final Integer BRAND_PER_PAGE = 4;
	
	@Autowired
	private BrandRepository brandRepository;
	
	@Autowired
	private CategoryService categoryService;

	public List<Brand> listAllBrandsForProductForm() {
		return brandRepository.listAll();
	}
	
	public List<Brand> listBrandPage(Integer pageNumber, String sortDir, String keyword) {
		List<Brand> list = pageBrands(pageNumber, sortDir, keyword).getContent();
		for(Brand b : list) {
			b.setCategories(categoryService.sortSubCategory(b.getCategories(), "asc"));
		}
		return list;
	}
	
	public Set<Category> listCategoriesByBrandId(Integer id) throws BrandNotFoundException {
		Set<Category> categoriesByBrand = findById(id).getCategories();
		for(Category c : categoriesByBrand) {
			c = new Category(c.getId(), c.getName());
		}
		return categoryService.sortSubCategory(categoriesByBrand, "asc");
	}
	
	public Page<Brand> pageBrands(Integer pageNumber, String sortDir, String keyword) {
		Sort sort = Sort.by("name");
		if(sortDir.equals("desc")) {
			sort = sort.descending();
		} else {
			sort = sort.ascending();
		}
		
		Pageable pageable = PageRequest.of(pageNumber-1, BRAND_PER_PAGE, sort);
		if(keyword == null) return brandRepository.findAll(pageable);
		return brandRepository.findAll(pageable, keyword);
	}
	
	public Brand save(Brand brand) {
		return brandRepository.save(brand);
	}
	
	public Brand findById(Integer id) throws BrandNotFoundException {
		Brand brand = brandRepository.findById(id).get();
		if(brand == null) {
			throw new BrandNotFoundException("No brand found with ID: "+id);
		}
		return brand;
	}
	
	public String checkUnique(Brand brandCheck) {
		boolean isCreateNew = brandCheck.getId() == null;
		Brand brandGetByName = brandRepository.findByName(brandCheck.getName());
		
		if(isCreateNew) {
			if(brandGetByName != null) {
				return "Duplicated";
			}
		} else {
			if(brandGetByName != null && brandGetByName.getId() != brandCheck.getId()) {
				return "Duplicated";
			}
		}
		return "OK";
	}
	
	public void delete(Integer id) throws BrandNotFoundException {
		if(brandRepository.existsById(id)) {
			brandRepository.deleteById(id);
		} else {
			throw new BrandNotFoundException("No brand found with ID: "+id);
		}
	}
}
