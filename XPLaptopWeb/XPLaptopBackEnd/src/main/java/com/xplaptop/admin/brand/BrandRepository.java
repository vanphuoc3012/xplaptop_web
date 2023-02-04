package com.xplaptop.admin.brand;

import com.xplaptop.common.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Integer> {
	
	Brand findByName(String name);
	
	Page<Brand> findAll(Pageable pageable);

	@Query("SELECT b FROM Brand b WHERE b.name LIKE %:keyword%")
	Page<Brand> findAll(Pageable pageable,@Param("keyword") String keyword);
	
	@Query("SELECT NEW Brand(b.id, b.name) FROM Brand b ORDER BY b.name ASC")
	List<Brand> listAll();
}
