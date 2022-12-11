package com.xplaptop.admin.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.xplaptop.common.entity.product.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer>{

	Product findByName(String name);
	
	Product findByAlias(String name);
	
	Page<Product> findAll(Pageable pageable);
	
	@Query("SELECT p FROM Product p WHERE CONCAT(p.name,' ', p.fullDescription,' ', p.shortDescription,' ', p.alias) LIKE %?1%")
	Page<Product> findAll(String keyword, Pageable pageable);
}
