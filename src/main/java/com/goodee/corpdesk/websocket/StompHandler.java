package com.goodee.corpdesk.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.goodee.corpdesk.security.JwtTokenManager;

@Component
public class StompHandler implements ChannelInterceptor{

	@Autowired
	private JwtTokenManager jwtTokenManager;
	
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		
		//stomp로 요청이 connect인 경우 
		if(StompCommand.CONNECT.equals(accessor.getCommand())) {
			String token =(String)accessor.getSessionAttributes().get("token");
			try {
				Authentication auth = jwtTokenManager.getAuthentication(token);
				accessor.setUser(auth);
			} catch (Exception e) {
			
				// TODO 검증 실패시 예외 로직 작성
				e.printStackTrace();
			}
			
		
		}
		return message;
	}
	
}
