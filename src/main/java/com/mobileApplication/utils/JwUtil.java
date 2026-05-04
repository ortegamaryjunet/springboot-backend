package com.mobileApplication.utils;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mobileApplication.models.UserModel;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwUtil {
	
//	private final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//	private final long EXPIRATION = 1000 * 60 * 60;
	
	// Dev: Hardcoded OK, Prod: Use @Value("${jwt.secret}")
	
	@Value("${jwt.secret}")
	private String secret;
	private Key secretKey;
    
    @PostConstruct
    public void init() {
    	this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }
    
    private final long EXPIRATION = 1000L * 60 * 60 * 24 * 7;
	
	public String generateToken(UserModel user) {
        return Jwts.builder()
                .setSubject(user.getUsername())  
                .claim("id", user.getId())      
                .claim("fullName", user.getUsername())
                .claim("branchId", user.getBranchId())
                .setIssuedAt(new Date())       
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION)) 
                .signWith(secretKey) 
                .compact();
    }
	
	//Check if token is valid
	public Claims validateToken(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	public String extractUsername(String token) { 
		return validateToken(token).getSubject();
	}

}
