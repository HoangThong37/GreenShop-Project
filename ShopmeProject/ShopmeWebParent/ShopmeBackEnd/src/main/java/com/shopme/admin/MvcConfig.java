package com.shopme.admin;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// user
        exposeDirectory("user-photos", registry);
        // category
        exposeDirectory("../category-images", registry);
        // brand
        exposeDirectory("../brand-logos", registry);
	}
	
	private void exposeDirectory(String pathPattern,ResourceHandlerRegistry registry) {
		Path path = Paths.get(pathPattern);
		String absolutePath = path.toFile().getAbsolutePath();
		
		String resourcePattern = pathPattern.replace("../", "") + "/**";
		
		registry.addResourceHandler(resourcePattern)
		.addResourceLocations("file:/" + absolutePath + "/");
	}
}
