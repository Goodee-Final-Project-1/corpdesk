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
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
				} catch (ExpiredJwtException expired) {
					// FIXME: 리프레시 토큰으로 엑세스 토큰 생성
					log.info("========================== 리프레시 가져오기");
					// FIXME: 액세스 토큰으로 유저네임 뽑아오기
					String refreshToken = "";
					try {
						refreshToken = jwtTokenManager.getRefreshToken(expired).getBody();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					log.info("===================== 리프레시 {}", refreshToken);
					
					try {
						Authentication authentication = jwtTokenManager.getAuthentication(refreshToken);
						SecurityContextHolder.getContext().setAuthentication(authentication);
						accessToken = jwtTokenManager.makeAccessToken(authentication);
						
						Cookie cookie = new Cookie("accessToken", accessToken);
						cookie.setPath("/");
						cookie.setMaxAge(jwtTokenManager.getRefreshValidTime());
						cookie.setHttpOnly(true);
						
						response.addCookie(cookie);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
//		super.doFilterInternal(request, response, chain);
		chain.doFilter(request, response);
	}

	
}
