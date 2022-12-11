package com.xplaptop.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import com.xplaptop.common.entity.Category;

@DataJpaTest(showSql = false)
@Rollback(false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class CategoryRepositoryTests {
	
	@Autowired
	CategoryRepository repo;

	
	@Test
	public void getCategoryTest() {
		Integer id = 1;
		Category cateTest = repo.findById(id).get();
		
		assertThat(cateTest.getId()).isGreaterThan(0);
	}
	
	@Test
	public void createSubCategoryTest() {
		String name = "Camera";
		Category parent = new Category(2);	
		
		Category subCategory = new Category(name, parent);
		Category laptop = new Category("Smartphone", parent);
		repo.save(subCategory);
		repo.save(laptop);
	}
	
	@Test
	public void printChildHierarchicalTest() {		
		Iterable<Category> iterator = repo.findAll();
		for(Category c : iterator) {
			if(c.getParent() == null) {
				System.out.println(c.getName());
				printChild(c, 0);
			}
		}	
	}
	
	public void printChild(Category category, int a) {
		a++;
		for(Category c : category.getChildren()) {	
			for(int i = 0; i < a; i++) {
				System.out.print("--");
			}
			System.out.println(c.getName()+c.getId());
			printChild(c, a);
		}
	}
	
	@Test
	public void getRootCategoryTest() {
		List<Category> list = repo.findRootCategories(Sort.by("name").ascending());
		
		for(Category c : list) {
			printChild(c, 1);
		}	
	}
	
}
