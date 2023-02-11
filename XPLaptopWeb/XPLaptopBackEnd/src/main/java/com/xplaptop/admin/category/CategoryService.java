package com.xplaptop.admin.category;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.xplaptop.common.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.xplaptop.common.entity.Category;

@Service
public class CategoryService {
	
	private static final Integer ROOT_CATEGORY_PER_PAGE = 4;
	
	@Autowired
	CategoryRepository categoryRepo;
	
	public List<Category> findAllCategories() {
		return (List<Category>) categoryRepo.findAll();
	}
	
	public List<Category> categoriesUsedInForm(String sortDir) {
		Sort sort = Sort.by("name");
		if(sortDir.equals("asc")) {
			sort = sort.ascending();
		} else {
			sort = sort.descending();
		}		
		List<Category> rootCategories = categoryRepo.findRootCategories(sort);
		
		List<Category> categoriesToShow = new ArrayList<>();
		
		for(Category c : rootCategories) {
			categoriesToShow.add(c);
			listChildInHierachy(c, 0, categoriesToShow, sortDir);
		}	
		return categoriesToShow;
	}
	
	public List<Category> pageCategories(Integer pageNum, String sortDir, String keyword) {
		
		Page<Category> pageRootCategories = getPage(pageNum, sortDir, keyword);
		
		if(keyword == null || keyword.isEmpty()) {
			List<Category> categoriesInForm = new ArrayList<>();
			
			for(Category c : pageRootCategories.getContent()) {
				categoriesInForm.add(c);
				listChildInHierachy(c, 0, categoriesInForm, sortDir);
			}	
			return categoriesInForm;
		}
		
		return pageRootCategories.getContent();
		
		
	}
	
	public Page<Category> getPage(Integer pageNum, String sortDir, String keyword) {
		Sort sort = Sort.by("name");
		if(sortDir.equals("asc")) {
			sort = sort.ascending();
		} else {
			sort = sort.descending();
		}
		Pageable pageable = PageRequest.of(pageNum-1, ROOT_CATEGORY_PER_PAGE, sort);
		
		if(keyword == null || keyword.isEmpty()) {
			return categoryRepo.findRootCategoriesPage(pageable);
		} else {
			return categoryRepo.findCategoriesByKeyword(keyword, pageable);
		}
		
		
	}
	
	public Category findById(Integer id) throws CategoryNotFoundException {
		return categoryRepo.findById(id).orElseThrow(
				() -> new CategoryNotFoundException("No category found with ID: "+id)
		);

	}
	
	public Category save(Category category) {	
		Category parentCategory = category.getParent();
		if(parentCategory != null) {
			String allParentIds = parentCategory.getParent() == null ? "-" : parentCategory.getAllParentIds();
			allParentIds += parentCategory.getId() + "-";
			category.setAllParentIds(allParentIds);
		}
		return categoryRepo.save(category);
	}
	
	public void deleteCategory(Integer id) throws CategoryNotFoundException {
		Category category = categoryRepo.findById(id).get();
		if(category != null) {
			if(category.getChildren() == null || category.getChildren().isEmpty()) {
				categoryRepo.deleteById(id);
			} else {
				throw new CategoryNotFoundException("Can't delete parent category, please delete all its sub-category first");
			}		
		} else {
			throw new CategoryNotFoundException("No category found with ID: "+id);
		}
	}
	
	public void updateEnableStatus(Integer id) throws CategoryNotFoundException{
		if(categoryRepo.existsById(id)) {
			Category cateToUpdate = categoryRepo.findById(id).get();
			if(cateToUpdate.isEnabled()) {
				cateToUpdate.setEnabled(false);
				List<Category> childCate = new ArrayList<>();
				
				listChildAndUpdateAllEnableStatus(cateToUpdate, childCate, false);
				
				categoryRepo.save(cateToUpdate);
				categoryRepo.saveAll(childCate);
			} else {
				cateToUpdate.setEnabled(true);
				this.save(cateToUpdate);
			}
		} else {
			throw new CategoryNotFoundException("No category found with ID: "+id);
		}
	}
	
	public String checkUniqe(Category checkCategory) {
		boolean isCreateNew = checkCategory.getId() == null || checkCategory.getId() == 0;
		Category cateByName = categoryRepo.findByName(checkCategory.getName());
		Category cateByAlias = categoryRepo.findByAlias(checkCategory.getAlias());
		if(isCreateNew) {
			if(cateByName != null) {
				return "DuplicatedName";
			} 
			if(cateByAlias != null) {
				return "DuplicatedAlias";
			}
		} else {
			if(cateByName != null && cateByName.getId() != checkCategory.getId()) {
				return "DuplicatedName";
			} 
			if(cateByAlias != null && cateByAlias.getId() != checkCategory.getId()) {
				return "DuplicatedAlias";
			}
		}
		
		return "OK";
	}
	
	private List<Category> listChildInHierachy(Category category, int a, List<Category> list, String sortDir) {
		a++;		
		for(Category c : this.sortSubCategory(category.getChildren(), sortDir)) {	
			String name = "";
			for(int i = 0; i < a; i++) {
				name += "--";
			}
			name += c.getName();
			Category cat = Category.copy(c);
			cat.setName(name);
			list.add(cat);
			listChildInHierachy(c, a, list, sortDir);
		}
		return list;
	}
	
	private List<Category> listChildAndUpdateAllEnableStatus(Category category, List<Category> list, boolean status) {		
		for(Category c : category.getChildren()) {	
			c.setEnabled(status);
            list.add(c);
			listChildAndUpdateAllEnableStatus(c, list, status);
		}
		return list;
	}
	
	public SortedSet<Category> sortSubCategory(Set<Category> subCategory, String sortDir) {
		SortedSet<Category> setcaCategories = new TreeSet<>(new Comparator<Category>() {
			@Override
			public int compare(Category c1, Category c2) {
				if(sortDir.equals("asc")) {
					return c1.getName().compareTo(c2.getName());
				} else {
					return c2.getName().compareTo(c1.getName());
				}
				
			}
		});
		setcaCategories.addAll(subCategory);
		
		return setcaCategories;
	}
}
