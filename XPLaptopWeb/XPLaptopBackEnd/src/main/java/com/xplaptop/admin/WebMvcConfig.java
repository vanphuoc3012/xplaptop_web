package com.xplaptop.admin;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.xplaptop.admin.paging.PagingAndSortingResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		exposeDirectory("users-photos", registry);
		exposeDirectory("../category-images", registry);
		exposeDirectory("../brand-logos", registry);
		exposeDirectory("../product-images", registry);
		exposeDirectory("../site-logo", registry);
		
	}
	
	private void exposeDirectory(String folderName, ResourceHandlerRegistry registry) {
		Path path = Paths.get(folderName);
		
		String absPath = path.toFile().getAbsolutePath();
		
		registry.addResourceHandler("/"+folderName.replace("../", "")+"/**")
				.addResourceLocations("file:/"+absPath+"/");
	}

	/**
	 * @param resolvers initially an empty list
	 */
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new PagingAndSortingResolver());
	}
}
