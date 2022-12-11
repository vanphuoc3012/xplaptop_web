package com.xplaptop.admin.brand;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.xplaptop.common.entity.Brand;

public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer>{
	
	Brand findByName(String name);
	
	Page<Brand> findAll(Pageable pageable);
	
	@Query("SELECT NEW Brand(b.id, b.name) FROM Brand b ORDER BY b.name ASC")
	List<Brand> listAll();
}
