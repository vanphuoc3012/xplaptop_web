package com.xplaptop.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.xplaptop.common.entity.product.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer>{
	
	@Query("SELECT p FROM Product p WHERE p.enabled = true AND (p.category.id = ?1 "
			+"OR p.category.allParentIds LIKE %?2%)")
	Page<Product> productsByCategory(Integer categoryId, String categoryMatch, Pageable pageable);
	
	Product findByAlias(String alias);
}
