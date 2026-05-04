package com.mobileApplication;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class PasswordRunner {
	




	    @Bean
	    CommandLineRunner run() {
	        return args -> {
	            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	            String hash = encoder.encode("dentist123");
	            System.out.println("BCrypt hash: " + hash);
	        };
	    }
	}


