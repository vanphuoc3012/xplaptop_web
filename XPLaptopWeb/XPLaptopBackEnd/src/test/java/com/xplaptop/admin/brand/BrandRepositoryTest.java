package com.xplaptop.admin.brand;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.xplaptop.common.entity.Brand;
import com.xplaptop.common.entity.Category;

@DataJpaTest(showSql = false)
@Rollback(false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class BrandRepositoryTest {
	
	@Autowired
	private BrandRepository brandRepository;
	
	@Test
	public void createNewBrandTest() {
		String name = "Acer";
		
		Brand brand = new Brand();
		brand.setName(name);
		
		Set<Category> categories = new HashSet<>();
		categories.add(new Category(77));
		categories.add(new Category(1));
		brand.setCategories(categories);
		
		Brand b = brandRepository.save(brand);
		
		assertThat(b.getId()).isGreaterThan(0);
		
	}
	
	@Test
	public void testListAllBrand() {
		List<Brand> findAllBrands = brandRepository.listAll();
		findAllBrands.forEach(System.out::println);
		assertThat(findAllBrands).isNotEmpty();
	}
}
