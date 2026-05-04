package com.mobileApplication.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Order(2)
public class WebSecurityConfig {
	
    @Bean
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher(
                "/web/**",
                "/adminLogin",
                "/doctorLogin",
                "/receptLogin",
                "/role",
                "/web/login",
                "/web/logout",
                "/web/forgot/**",
                "/css/**",
                "/js/**",
                "/images/**",
                "/fonts/**",
                "/scripts/**",
                "/adminRegister",
                "/registerProcess",
                "/web/register/**",
                "/web/otp/**",
                "/doctorDashboard",
                "/doctorAppointment",
                "/doctorPatient",
                "/doctorMessage",
                "/doctorProfile"
            )

            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/adminLogin",
                    "/doctorLogin",
                    "/receptLogin",
                    "/role",
                    "/web/login",
                    "/web/forgot/**",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/fonts/**",
                    "/scripts/**",
                    "/adminRegister",
                    "/registerProcess",
                    "/web/register/**",
                    "/web/otp/**"
                ).permitAll()

                .requestMatchers("/web/admin/**").hasRole("ADMIN")
                .requestMatchers("/web/doctor/**").hasRole("DENTIST")
                .requestMatchers("/web/recep/**").hasRole("RECEPTIONIST")

                .anyRequest().authenticated()
            )

            .formLogin(form -> form.disable())

            .logout(logout -> logout
                .logoutUrl("/web/logout")
                .logoutSuccessUrl("/role")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            )

            .sessionManagement(session -> session
                .invalidSessionUrl("/role?expired=true")
                .maximumSessions(1)
                .expiredUrl("/role?expired=true")
            );

        return http.build();
    }

}
