package com.xplaptop.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.xplaptop.common.entity.Category;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CategoryServiceTests {
	
	@MockBean
	private CategoryRepository categoryRepo;
	
	@InjectMocks
	private CategoryService categoryService;
	
	@Test
	public void testCreateNewNameDuplicated() {
		Integer id = null;
		String name = "Computer";
		String alias = "abc";
		
		Category category = new Category();
		category.setId(id);
		category.setName(name);
		category.setAlias(alias);
		
		Mockito.when(categoryRepo.findByName(name)).thenReturn(category);
		Mockito.when(categoryRepo.findByAlias(alias)).thenReturn(null);
		
		String result = categoryService.checkUniqe(category);
		
		assertThat(result).isEqualTo("DuplicatedName");
	}
	
	@Test
	public void testCreateNewAliasDuplicated() {
		Integer id = null;
		String name = "null";
		String alias = "computer";
		
		Category category = new Category();
		category.setId(id);
		category.setName(name);
		category.setAlias(alias);
		
		Mockito.when(categoryRepo.findByName(name)).thenReturn(null);
		Mockito.when(categoryRepo.findByAlias(alias)).thenReturn(category);
		
		String result = categoryService.checkUniqe(category);
		
		assertThat(result).isEqualTo("DuplicatedAlias");
	}
	
	@Test
	public void testUpdateNameDuplicated() {
		Integer id = 1;
		String name = "Computer";
		String alias = "aa";
		
		Category checkCate = new Category();
		checkCate.setId(id);
		checkCate.setName(name);
		checkCate.setAlias(alias);
		
		Category category = new Category();
		category.setId(2);
		category.setName(name);
		category.setAlias(alias);
		
		Mockito.when(categoryRepo.findByName(name)).thenReturn(category);
		Mockito.when(categoryRepo.findByAlias(alias)).thenReturn(null);
		
		String result = categoryService.checkUniqe(checkCate);
		
		assertThat(result).isEqualTo("DuplicatedName");
	}
	
	@Test
	public void testUpdateAliasDuplicated() {
		Integer id = 1;
		String name = "aa";
		String alias = "computer";
		
		Category checkCate = new Category();
		checkCate.setId(id);
		checkCate.setName(name);
		checkCate.setAlias(alias);
		
		Category category = new Category();
		category.setId(2);
		category.setName(name);
		category.setAlias(alias);
		
		Mockito.when(categoryRepo.findByName(name)).thenReturn(null);
		Mockito.when(categoryRepo.findByAlias(alias)).thenReturn(category);
		
		String result = categoryService.checkUniqe(checkCate);
		
		assertThat(result).isEqualTo("DuplicatedAlias");
	}
}

