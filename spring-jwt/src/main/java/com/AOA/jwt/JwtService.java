package com.AOA.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {
	
	private static final String SECRET_KEY = "Ben+jI33dqjsWQXDP1e1iEcwc4uzDRCJERU+pELgAKM=";
	
	public String generateToken(UserDetails userDetails) {
		
		Map <String, String> claimsMap = new HashMap<>();
		claimsMap.put("role", "Admin");
		
		return Jwts.builder()
		.setSubject(userDetails.getUsername())
		.setIssuedAt(new Date())
		.setClaims(claimsMap)
		.setExpiration(new Date(System.currentTimeMillis() + 1000*60*60*2)) // 1000 milisecond (= 1 second) * 60 ( = a minute) * 60 ( = an hour) * 2 ( two hours)
		.signWith(getKey(), SignatureAlgorithm.HS256)
		.compact();
	}
	
	public <T> T exportToken(String token, Function<Claims, T> claimsFunction){
		Claims claims = Jwts
		.parserBuilder()
		.setSigningKey(getKey())
		.build()
		.parseClaimsJws(token)
		.getBody();
		
		return claimsFunction.apply(claims);
	}
	
	public Key getKey(){
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	public String getUsernameByToken(String token) {
		return exportToken(token, Claims::getSubject);
	}
	
	public boolean isTokenExpired(String token) {
		Date expireDate = exportToken(token, Claims::getExpiration);
		return new Date().before(expireDate);
		
	}
	
}
