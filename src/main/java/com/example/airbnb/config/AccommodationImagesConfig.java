package com.example.airbnb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

@Configuration
public class AccommodationImagesConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/accommodation/images/**").addResourceLocations(
                Path.of(System.getProperty("user.home"), "accommodation", "images").toUri().toString()
        );
    }
}
