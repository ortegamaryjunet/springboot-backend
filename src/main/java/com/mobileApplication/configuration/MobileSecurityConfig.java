package com.mobileApplication.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@Order(1)
public class MobileSecurityConfig {
	
	@Autowired
	private JwtAuthFilter jwtAuthFilter;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	//not included in APK building 
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		
		config.setAllowedOriginPatterns(List.of(
				"http://localhost:8081",
	            "http://localhost:19006",
	            "http://localhost:3000"
				));
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        config.setAllowCredentials(true);
        config.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
	}
	
	
	//Authenticate Token
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http
	    	.securityMatcher("/api/**")
	    	.cors(cors -> cors.configurationSource(corsConfigurationSource()))
	        .csrf(csrf -> csrf.disable())
	        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	        .authorizeHttpRequests(auth -> auth
	        	.requestMatchers("/api/auth/**").permitAll()
	            .requestMatchers("/api/auth/login", "/auth/login").permitAll()
	            
	            .requestMatchers("/api/appointments/**").permitAll() //make authenticated after testing
	            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() //remove for deployment 
	            
	            .requestMatchers("/api/risks/**").permitAll() //remove after testing
	            
	            .requestMatchers("/api/profile/**", "/profile/**").authenticated()
	            .anyRequest().permitAll()
	        )
	        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
	    
	    return http.build();
	}
	
//	 @Bean
//	    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//	        http
//	            .csrf(csrf -> csrf.disable())
//	            .authorizeHttpRequests(auth -> auth
//	                .anyRequest().permitAll()  // EVERYTHING PUBLIC
//	            );
//	        return http.build();
//	    }
	
}
