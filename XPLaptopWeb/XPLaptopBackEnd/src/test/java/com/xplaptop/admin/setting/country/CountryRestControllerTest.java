package com.xplaptop.admin.setting.country;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.persistence.EntityManager;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xplaptop.common.entity.country.Country;

@SpringBootTest
@AutoConfigureMockMvc
public class CountryRestControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private EntityManager entityManager;
	
	@Test
	@WithMockUser(username = "admin@xplaptop.com", password = "test12345678", roles = "ADMIN")
	public void testListAllCountries() throws Exception {
		String url = "/countries/list";
		
		MvcResult mockMvcResult = mockMvc.perform(get(url)).
			andExpect(status().isOk()).
			andDo(print())
			.andReturn();
		
		String jsonResponse = mockMvcResult.getResponse().getContentAsString();
		System.out.println(jsonResponse);
		
		Country[] countries = objectMapper.readValue(jsonResponse, Country[].class);
		Arrays.asList(countries).forEach(c -> System.out.println(c));
	}
	
	@Test
	@WithMockUser(username = "admin@xplaptop.com", password = "test12345678", roles = "ADMIN")
	public void testSaveCountry() throws JsonProcessingException, Exception {
		String url = "/countries/save";
		
		String countryName = "Country Test";
		String countryCode = "CT";
		Country country = new Country(countryName, countryCode);
		MvcResult mvcResult = mockMvc.perform(post(url)
				.contentType("application/json").content(objectMapper.writeValueAsString(country))
				.with(csrf()))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		String contentAsString = mvcResult.getResponse().getContentAsString();
		System.out.println(contentAsString);
		
		Integer id = Integer.parseInt(contentAsString);
		
		Country foundCountry = entityManager.find(Country.class, id);
		assertThat(foundCountry.getName()).isEqualTo(countryName);
	}
	
	@Test
	@WithMockUser(username = "admin@xplaptop.com", password = "test12345678", roles = "ADMIN")
	public void testUpdateCountry() throws JsonProcessingException, Exception {
		String url = "/countries/save";
		
		String countryName = "Country Test Update";
		String countryCode = "CT";
		Country country = new Country(countryName, countryCode);
		country.setId(253);
		MvcResult mvcResult = mockMvc.perform(post(url)
				.contentType("application/json").content(objectMapper.writeValueAsString(country))
				.with(csrf()))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		String contentAsString = mvcResult.getResponse().getContentAsString();
		System.out.println(contentAsString);
		
		Integer id = Integer.parseInt(contentAsString);
		
		Country foundCountry = entityManager.find(Country.class, id);
		assertThat(foundCountry.getName()).isEqualTo(countryName);
	}
	
	@Test
	@WithMockUser(username = "admin@xplaptop.com", password = "test12345678", roles = "ADMIN")
	public void testDeleteUser() throws Exception {
		String url = "/countries/delete/253";
		mockMvc.perform(get(url))
			.andExpect(status().isOk());
		
		Country foundCountry = entityManager.find(Country.class, 253);
		
		assertThat(foundCountry).isNull();
	}
}
