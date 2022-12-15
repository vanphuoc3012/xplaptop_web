package com.xplaptop.category;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.xplaptop.common.entity.Category;

public interface CategoryRepository extends CrudRepository<Category, Integer>{
	
	@Query("SELECT c FROM Category c WHERE c.enabled = TRUE ORDER BY name ASC")
	List<Category> findByEnabled();
	
	Category findByAlias(String alias);
}
