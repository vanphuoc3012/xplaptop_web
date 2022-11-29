package com.xplaptop.admin;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String rootDir = "users-photos";
		Path path = Paths.get(rootDir);
		
		String userFolder = path.toFile().getAbsolutePath();
		
		registry.addResourceHandler("/users-photos/**")
				.addResourceLocations("file:/"+userFolder+"/");
	}
	
}
