package com.xplaptop.category;

import java.util.ArrayList;
import java.util.List;

import com.xplaptop.common.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplaptop.common.entity.Category;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	public List<Category> listCategoriesNoChild() {
		List<Category> listCategories = categoryRepository.findByEnabled();
		List<Category> noChildCategories = new ArrayList<>();
		
		for(Category c : listCategories) {
			if(c.getChildren() == null || c.getChildren().size() == 0) {
				noChildCategories.add(c);
			}
		}
		return noChildCategories;
	}
	
	public Category getCategoryByAlias(String alias) throws CategoryNotFoundException {
		Category cat = categoryRepository.findByAlias(alias);
		if(cat == null) {
			throw new CategoryNotFoundException("No such category found with alias: "+alias);
		}
		return cat;
	}
	
	public List<Category> listCategoryParent(Category child) {
		List<Category> parentCategories = new ArrayList<>();
		
		Category parent = child.getParent();
		while(parent != null) {
			parentCategories.add(0, parent);
			parent = parent.getParent();
		}
		parentCategories.add(child);
		return parentCategories;
	}
	
}
