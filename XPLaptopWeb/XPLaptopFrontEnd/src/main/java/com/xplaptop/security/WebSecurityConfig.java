package com.xplaptop.security;

import com.xplaptop.security.oauth2.CustomerOAuth2UserService;
import com.xplaptop.security.oauth2.OAuth2LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	@Autowired
	private CustomerOAuth2UserService oAuth2UserService;
	@Autowired
	private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
	@Autowired
	private DatabaseLoginSuccessHandler databaseLoginSuccessHandler;
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.formLogin()
				.loginPage("/login").usernameParameter("email").permitAll()
				.successHandler(databaseLoginSuccessHandler)
				.and()
				.rememberMe().key("xplaptopstore").tokenValiditySeconds(14*24*60*60)
				.and()
				.logout().permitAll();

		http.authorizeRequests()
				.mvcMatchers("/account_details").authenticated()
				.mvcMatchers("/cart").authenticated()
				.anyRequest().permitAll();

		http.oauth2Login()
				.loginPage("/login")
				.userInfoEndpoint()
				.userService(oAuth2UserService)
				.and()
				.successHandler(oAuth2LoginSuccessHandler);

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
		return http.build();
	}
	
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().antMatchers("/webjars/**", "/images/**", "/fontawesome/**","/css/**", "/js/**");
	}
}
