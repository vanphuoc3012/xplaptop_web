package com.xplaptop.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(16);
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()
			.mvcMatchers("/users/**", "/setting/**").hasAuthority("Admin")
			.mvcMatchers("/categories/**", "/brands/**","/articles/**", "/menus/**").hasAnyAuthority("Admin", "Editor")
			
			.mvcMatchers("/products", "/products/page/**", "/products/detail/**").hasAnyAuthority("Admin", "Salesperson", "Editor", "Shipper")
			.mvcMatchers("/products/new", "/product/delete/**").hasAnyAuthority("Admin", "Editor")
			.mvcMatchers("products/edit/**", "/products/save", "/products/check_unique").hasAnyAuthority("Admin", "Salesperson", "Editor")
			.mvcMatchers("/products/**").hasAnyAuthority("Admin", "Editor")
			
			.mvcMatchers("/questions/**", "/reviews/**").hasAnyAuthority("Admin", "Assistant")
			.mvcMatchers("/customers/**", "/shipping/**", "/orders/**", "/reports/**").hasAnyAuthority("Admin")
			.anyRequest().authenticated()
			.and()
			.formLogin()
				.loginPage("/login")
				.usernameParameter("email")
				.permitAll()
			.and()
				.logout()
				.permitAll()
			.and()
				.rememberMe()
				.key("xplatop3012");
		
		return http.build();
	}
	
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().antMatchers("/webjars/**", "/images/**", "/fontawesome/**","/css/**", "/js/**");
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
		dao.setUserDetailsService(userDetailsService);
		dao.setPasswordEncoder(passwordEncoder());
		
		return dao;
	}
}
