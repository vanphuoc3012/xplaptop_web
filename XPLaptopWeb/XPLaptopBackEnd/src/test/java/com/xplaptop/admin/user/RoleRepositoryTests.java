package com.xplaptop.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.xplaptop.common.entity.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTests {
	
	@Autowired
	private RoleRepository repo;
	
	@Test
	public void testCreateFirstRole() {
		Role roleAdmin = new Role("Admin", "Manage everything");
		Role returnedRole = repo.save(roleAdmin);
		
		assertThat(returnedRole.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testRestRoles() {
		Role roleSale = new Role("Salesperson", "Manage product price, customers, shipping, orders and sales report");
		Role roleEditor = new Role("Editor", "Manage categories, brands, products, articles and menus");
		Role roleShipper = new Role("Shipper", "View products, view orders and update order status");
		Role roleAssistant = new Role("Assistant", "Manage questions and reviews");
		
		repo.saveAll(Arrays.asList(roleSale, roleEditor, roleAssistant, roleShipper));
	}
	
}
