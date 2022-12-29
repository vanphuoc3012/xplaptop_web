package com.xplaptop.admin.setting.country;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.xplaptop.admin.settting.country.CountryRepository;
import com.xplaptop.admin.settting.country.StateRepository;
import com.xplaptop.common.entity.country.Country;
import com.xplaptop.common.entity.country.State;

@DataJpaTest(showSql = true)
@Rollback(false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class CountryRepositoryTest {
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private StateRepository stateRepository;
	
	@Test
	public void testCreateCountry() {
		List<Country> listCountries = new ArrayList<>(); 
		try {
			File file = new File("D:\\XPLaptop\\XPLaptopProject\\XPLaptopWeb\\XPLaptopBackEnd\\src\\main\\java\\countries.csv");
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = "";
			while((line = br.readLine()) != null) {
				String[] country_code = line.split(",");
				System.out.println(country_code[0]+"-"+country_code[1]);
				Country country = new Country(country_code[0].replace(".", ","), country_code[1]);
				listCountries.add(country);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(listCountries.size());
		countryRepository.saveAll(listCountries);
		assertThat(countryRepository.findAllByOrderByNameAsc()).hasSizeGreaterThan(1);
	}
	
	@Test
	public void testCreateStates() {
		List<State> listStates = new ArrayList<>();
		Map<String, Country> listCountries = countryRepository.findAllByOrderByNameAsc().stream()
																				.collect(Collectors.toMap(Country::getCode, Function.identity()));;
		try {
			File file = new File("D:\\XPLaptop\\XPLaptopProject\\XPLaptopWeb\\XPLaptopBackEnd\\src\\main\\java\\states.csv");
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = "";
			while((line = br.readLine()) != null) {
				String[] state = line.split(",");
				System.out.println(state[0]+"-"+state[1]);
				State s = new State(state[0].replace(".", ","), listCountries.get(state[1]));
				listStates.add(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(listStates.size());
		stateRepository.saveAll(listStates);
		
		assertThat(stateRepository.findAllByOrderByNameAsc()).hasSizeGreaterThan(1);
	}
}
