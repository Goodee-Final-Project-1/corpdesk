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
		
		System.out.println("================== authentication filter ");
		
		Cookie[] cookies = request.getCookies();
		String accessToken = null;
		if (cookies != null) {
			for (Cookie c : cookies) {
				if (c.getName().equalsIgnoreCase("accessToken")) {
					accessToken = c.getValue();
					break;
				}
			}
			if (accessToken != null && accessToken.length() != 0) {
				try {
					Authentication authentication = jwtTokenManager.getAuthentication(accessToken);
					SecurityContextHolder.getContext().setAuthentication(authentication);
				} catch (Exception e) {
					e.printStackTrace();
					
					if (e instanceof ExpiredJwtException) {
						// FIXME: 리프레시 토큰으로 엑세스 토큰 생성
						
						String refreshToken = jwtTokenManager.getRefreshToken(accessToken).getBody();
						System.out.println("===============" + refreshToken);
						try {
							Authentication authentication = jwtTokenManager.getAuthentication(refreshToken);
							SecurityContextHolder.getContext().setAuthentication(authentication);
							accessToken = jwtTokenManager.makeAccessToken(authentication);
							
							Cookie cookie = new Cookie("accessToken", accessToken);
							cookie.setPath("/");
							cookie.setMaxAge(jwtTokenManager.getAccessValidTime());
							cookie.setHttpOnly(true);
							
							response.addCookie(cookie);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		}
		
//		super.doFilterInternal(request, response, chain);
		chain.doFilter(request, response);
	}

	
}
