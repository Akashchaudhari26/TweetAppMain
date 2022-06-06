package com.tweetapp.security.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.tweetapp.exception.InvalidOperationException;
import com.tweetapp.security.service.UserDetailsImp;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtils {

	@Value("${tweet.app.jwtSecret}")
	private String jwtSecret;

	@Value("${tweet.app.jwtExpirationMs}")
	private long jwtExpirationMs;

	public String generateJwtToken(Authentication authentication) {

		UserDetailsImp userPrincipal = (UserDetailsImp) authentication.getPrincipal();

		SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
		String jwt = Jwts.builder().setIssuer("Tweet App").setSubject("Login Token")
				.claim("username", userPrincipal.getUsername()).setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime() + jwtExpirationMs)).signWith(key).compact();

		return jwt;

	}

	public String getUserNameFromJwtToken(String token) {

		SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
		Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
		String username = String.valueOf(claims.get("username"));

		return username;

	}

	public boolean validateJwtToken(String authToken) throws InvalidOperationException {
		try {
			SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
			return true;
		} catch (Exception e) {
			log.info("Invalid token");
			throw new InvalidOperationException("Invalid Token: "+e.getMessage());
		}
	}
}