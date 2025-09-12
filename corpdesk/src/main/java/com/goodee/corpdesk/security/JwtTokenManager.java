package com.goodee.corpdesk.security;

import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtTokenManager {

	@Value("${jwt.secret.key}")
	private String secretKey;
	@Value("${jwt.valid.time.access}")
	private Long accessValidTime;
	@Value("${jwt.valid.time.refresh}")
	private Long refreshValidTime;
	@Value("${jwt.issuer}")
	private String issuer;
	
	private SecretKey key;
	
	@PostConstruct
	public void init() {
		byte[] base64 = Base64.getEncoder().encode(this.secretKey.getBytes());
		key = Keys.hmacShaKeyFor(base64);
	}
	
	// 엑세스 토큰
	public String makeAccessToken(Authentication authentication) {
		return this.makeToken(authentication, accessValidTime);
	}
	
	// 리프레시 토큰
	public String makeRefreshToken(Authentication authentication) {
		return this.makeToken(authentication, refreshValidTime);
	}
	
	// 토큰 발급
	public String makeToken(Authentication authentication, Long validTime) {
		return Jwts
				.builder()
				.subject(authentication.getName())
				.claim("roles", authentication.getAuthorities().toString())
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + validTime))
				.issuer(issuer)
				.signWith(key)
				.compact()
				;
	}
	
	public Authentication getAuthentication(String token) throws Exception {
		Claims claims = Jwts
				.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				;
		// FIXME: 유저 DTO를 가져와야 됨
		UserDetails userDetails = null;
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		
		return authentication;
	}

	public Long getAccessValidTime() {
		return accessValidTime;
	}
}
