package com.xplaptop.admin.category;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.xplaptop.common.entity.Category;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {
	
	@Query("SELECT c FROM Category c WHERE c.parent = NULL")
	List<Category> findRootCategories(Sort sort);
	
	Category findByName(String name);
	
	Category findByAlias(String alias);
	
	@Query("SELECT c FROM Category c WHERE c.parent = NULL")
	Page<Category> findRootCategoriesPage(Pageable pageable);
	
	@Query("SELECT c FROM Category c WHERE c.name LIKE %?1%")
	Page<Category> findCategoriesByKeyword(String keyword, Pageable pageable);
}
