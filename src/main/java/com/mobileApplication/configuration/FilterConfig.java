package com.mobileApplication.configuration;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
	
	@Bean
    public FilterRegistrationBean<JwtAuthFilter> jwtFilterRegistration(JwtAuthFilter jwtAuthFilter) {
        FilterRegistrationBean<JwtAuthFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(jwtAuthFilter);

        // IMPORTANT:
        // Prevents JwtAuthFilter from running globally on JSP/web pages.
        // It will still run inside MobileSecurityConfig for /api/**.
        registration.setEnabled(false);

        return registration;
    }

}
