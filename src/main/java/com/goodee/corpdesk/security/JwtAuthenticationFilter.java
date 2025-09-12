package com.goodee.corpdesk.security;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

	private JwtTokenManager jwtTokenManager;
	
	public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
			JwtTokenManager jwtTokenManager) {
		super(authenticationManager);
		
		this.jwtTokenManager =jwtTokenManager;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		Cookie[] cookies = request.getCookies();
		String token = null;
		if (cookies != null) {
			for (Cookie c : cookies) {
				if (c.getName().equalsIgnoreCase("accessToken")) {
					token = c.getValue();
					break;
				}
			}
			
			if (token != null && token.length() != 0) {
				try {
					Authentication authentication = jwtTokenManager.getAuthentication(token);
					SecurityContextHolder.getContext().setAuthentication(authentication);
				} catch (Exception e) {
					e.printStackTrace();
					
					if (e instanceof ExpiredJwtException) {
						// FIXME: 리프레시 토큰으로 엑세스 토큰 생성
					}
				}
			}
		}
		
//		super.doFilterInternal(request, response, chain);
		chain.doFilter(request, response);
	}

	
}
