package com.mobileApplication.configuration;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobileApplication.utils.JwUtil;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwUtil jwUtil;
	
	@Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/web/") ||
               path.startsWith("/css/") ||
               path.startsWith("/js/") ||
               path.startsWith("/images/") ||
               path.startsWith("/fonts/") ||
               path.startsWith("/scripts/") ||
               path.startsWith("/WEB-INF/") ||
               path.equals("/") ||
               path.equals("/role") ||
               path.equals("/adminLogin") ||
               path.equals("/doctorLogin") ||
               path.equals("/receptLogin");
    }
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, 
			HttpServletResponse response, 
			FilterChain filterChain) 
					throws ServletException, IOException {
		System.out.println(">>> JWT FILTER HIT: " + request.getRequestURI());
	    
	    if (isPublicEndpoint(request.getRequestURI(), request.getServletPath())) {
	        filterChain.doFilter(request, response);
	        return;
	    }
	    
	    String authHeader = request.getHeader("Authorization");
	    
	    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	        sendAuthError(response, "No valid Bearer token found");
	        return;
	    }
        
        String token = authHeader.substring(7);
        try {
            Claims claims = jwUtil.validateToken(token);
            String username = claims.getSubject();
            
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    username, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            sendAuthError(response, "Token is invalid or expired");
            return;
        }
        
        filterChain.doFilter(request, response);
    }
    
    private boolean isPublicEndpoint(String fullUri, String servletPath) {
        List<String> publicPaths = Arrays.asList(
            "/api/auth/login", "/auth/login",
            "/api/auth/debug", "/auth/debug",
            "/api/auth/send-otp", "/auth/send-otp",
            "/api/auth/verify-otp", "/auth/verify-otp", 
            "/api/auth/reset-password", "/auth/reset-password",
            "/api/auth/register", "/auth/register"
        );
        // ✅ ADD THIS — prefix-based check for appointment endpoints, remove after testing
        List<String> publicPrefixes = Arrays.asList(
            "/api/appointments/",
            "/api/risks",
            "/api/clinic"
        );
        
        
        return publicPaths.contains(fullUri) || 
               publicPaths.contains(servletPath) ||
               publicPaths.stream().anyMatch(fullUri::endsWith) ||
               
        		publicPrefixes.stream().anyMatch(fullUri::startsWith); // ✅ ADD THIS LINE remove after testing
    }
    
    private void sendAuthError(HttpServletResponse response, String message) throws IOException {
        Map<String, String> error = new HashMap<>();
        error.put("status", "INVALID_TOKEN");
        error.put("message", message);
        
        response.setStatus(401);
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(error));
    }
    
}
