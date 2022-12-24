package com.xplaptop.test.product;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.xplaptop.common.entity.product.Product;
import com.xplaptop.product.ProductRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ProductRepositoryTest {
	@Autowired
	private ProductRepository productRepository;
	
	@Test
	public void getProductsByCategoryTest() {
		Pageable pageable = PageRequest.of(0, 4);
		Page<Product> productsByCategory = productRepository.productsByCategory(4, "-4-", pageable);
		productsByCategory.getContent().forEach(System.out :: println);
		assertThat(productsByCategory.getContent()).hasSizeGreaterThan(0);
	}
	
	@Test
	public void getProductByAlias() {
		String alias = "alienware_x17_r2_(2022)_-_core_i7_12700h_/_32gb_/_512gb_ssd_/_rtx_3060_6gb_/_fhd_165hz";
		
		Product p = productRepository.findByAlias(alias);
		assertThat(p).isNotNull();
	}
	
	@Test
	public void testSearchFunction() {
		String keyword = "intel";
		
		Pageable pageable = PageRequest.of(0, 10);
		Page<Product> page = productRepository.search(keyword, pageable);
		
		assertThat(page.getContent()).hasSize(7);
	}
}
