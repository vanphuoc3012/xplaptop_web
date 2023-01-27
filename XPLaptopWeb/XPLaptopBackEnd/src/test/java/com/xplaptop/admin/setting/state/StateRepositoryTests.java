package com.xplaptop.admin.setting.state;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.xplaptop.admin.settting.country.StateRepository;
import com.xplaptop.common.entity.country.Country;
import com.xplaptop.common.entity.country.State;

@DataJpaTest(showSql = true)
@Rollback(false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class StateRepositoryTests {
	
	@Autowired
	private StateRepository repository;
	
	@Test
	public void testGetStateByCountry() {
		List<State> listStates = repository.findByCountryOrderByNameAsc(new Country(99));
		listStates.forEach(e -> System.out.println(e.getName()));
	}
}
