package com.xplaptop.test.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.xplaptop.category.CategoryRepository;
import com.xplaptop.common.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class CategoryRepositoryTest {
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Test
	public void testListAllCategory() {
		List<Category> listCategories = categoryRepository.findByEnabled();
		
		for(Category c : listCategories) {
			System.out.println(c.getName());
		}
		assertThat(listCategories).hasSizeGreaterThan(4);
	}
	
	@Test
	public void testGetCategoryByAlias() {
		Category category = categoryRepository.findByAlias("books");
		
		assertThat(category).isNotNull();
	}
}
