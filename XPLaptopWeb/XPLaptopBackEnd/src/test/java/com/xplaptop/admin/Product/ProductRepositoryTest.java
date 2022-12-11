package com.xplaptop.admin.Product;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.xplaptop.admin.product.ProductRepository;
import com.xplaptop.common.entity.Brand;
import com.xplaptop.common.entity.Category;
import com.xplaptop.common.entity.product.Product;

@DataJpaTest(showSql = false)
@Rollback(false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ProductRepositoryTest {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private TestEntityManager testEntityManager;
	
	@Test
	public void createNewProduct() {
		Brand brand = testEntityManager.find(Brand.class, 2);
		Category category = testEntityManager.find(Category.class, 8);
		
		Product product = new Product();
		String name = "Samsung Galaxy Note 10 Korea";
		product.setName(name);
		product.setAlias(name.toLowerCase().replaceAll(" ", "-"));
		product.setShortDescription("Samsung Galaxy Note 10 Korea");
		product.setFullDescription("Samsung Galaxy Note 10 Korea");
		product.setLength(1d);
		product.setWidth(1d);
		product.setHeight(1d);
		product.setWeight(1d);
		product.setPrice(1200d);
		product.setDiscountPercent(5d);
		
		product.setBrand(brand);
		product.setCategory(category);
		
		product.setCreatedTime(LocalDateTime.now());
		
		product.addExtrasImage("image1.png");
		product.addExtrasImage("image2.png");
		
		product.addDetail("OS", "OneUI");
		product.addDetail("screen size", "5 inch");
	
		Product saved = productRepository.save(product);
		System.out.println(saved.getCreatedTime());
		assertThat(saved.getId()).isGreaterThan(0);
	}
	
	@Test
	public void getProductImageTest() {
		Product p = productRepository.findById(28).get();
		assertThat(p.getProductImages()).hasSize(2);
	}
	
	@Test
	public void deleteById() {
		productRepository.deleteById(64);
		
	}
	
	@Test
	public void getProductDetailTest() {
		Product p = productRepository.findById(65).get();
		assertThat(p.getProductDetails()).hasSize(2);
	}
}
