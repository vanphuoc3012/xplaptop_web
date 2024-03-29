package com.xplaptop.admin.user;

import com.xplaptop.common.entity.user.Role;
import com.xplaptop.common.entity.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Rollback(false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserRepositoryTests {
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateUser() {
		Role admin = entityManager.find(Role.class, 1);
		User user1 = new User("test1@gmail.com", "test123456", "First", "Last");
		user1.addRole(admin);
		
		User savedUser = repo.save(user1);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateUserWithTwoRoles() {
		Role admin = entityManager.find(Role.class, 1);
		Role editor = entityManager.find(Role.class, 2);
		User user2 = new User("test2@gmail.com", "test2123456", "First2", "Last2");
		user2.addRole(admin);
		user2.addRole(editor);
		
		User savedUser = repo.save(user2);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = repo.findAll();
		listUsers.forEach(System.out::println);
		
	}
	
	@Test
	public void testGetUserById() {
		User user = repo.findById(1).get();
		assertThat(user).isNotNull();
		System.out.println(user);
	}
	
	@Test
	public void testUpdateUserDetails() {
		User user = repo.findById(1).get();
		assertThat(user).isNotNull();
		
		user.setEnabled(true);
		user.setEmail("emailupdate@gamil.com");
		
		User updatedUser = repo.save(user);
		
		assertThat(updatedUser.isEnabled()).isEqualTo(true);
		assertThat(updatedUser.getEmail()).isEqualTo("emailupdate@gamil.com");
		
		System.out.println(user);
	}
	
	@Test
	public void testRemoveUserRoles() {
		User user = repo.findById(2).get();
		
		Role roleAdmin = new Role(1);
		Role roleEditor = new Role(3);
		
		user.getRoles().remove(roleAdmin);
		user.getRoles().remove(roleEditor);
		
		repo.save(user);
	}
	
	@Test
	public void testDeleteUser() {
		repo.deleteById(2);
	}
	
	@Test
	public void testGetUserByEmail() {
		User u = repo.getUserByEmail("test9@gmail.com");
		
		System.out.println(u);
		assertThat(u).isNotNull();
	}
	
	@Test
	public void testUpdateEnableStatus() {
		repo.updateEnableStatus(2, true);
	}
	
	@Test
	public void testPage() {
		int pageNumber = 0;
		int userPerPage = 5;
		
		Pageable pageable =  PageRequest.of(pageNumber, userPerPage);
		
		Page<User> page = repo.findAll(pageable);
		
		System.out.println(page.getContent());
		assertThat(page.getContent().size()).isEqualTo(5);
	}
	
	@Test 
	public void testFindUserByKeyword() {
		String keyword = "Jack";
		
		int pageNumber = 0;
		int userPerPage = 5;
		
		Pageable pageable =  PageRequest.of(pageNumber, userPerPage);
		
		Page<User> page = repo.findAll(keyword, pageable);
		System.out.println(page.getContent());
		assertThat(page.getContent().size()).isGreaterThan(0);
	}
	
	@Test
	public void testFindUserByEmail() {
		String email = "aryastark@codejava.net";
		User user = repo.findByEmail(email).get();
		System.out.println(user);
		assertThat(user.getEmail()).isEqualTo("aryastark@codejava.net");
		
	}
}
