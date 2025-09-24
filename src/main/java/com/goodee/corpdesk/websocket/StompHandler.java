package com.goodee.corpdesk.websocket;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.goodee.corpdesk.chat.entity.ChatParticipant;
import com.goodee.corpdesk.chat.repository.ChatParticipantRepository;
import com.goodee.corpdesk.chat.service.ChatParticipantService;
import com.goodee.corpdesk.security.JwtTokenManager;

@Component
public class StompHandler implements ChannelInterceptor{

    private final ChatParticipantRepository chatParticipantRepository;

	@Autowired
	private JwtTokenManager jwtTokenManager;
	@Autowired
	private ChatParticipantService chatParticipantService;

    StompHandler(ChatParticipantRepository chatParticipantRepository) {
        this.chatParticipantRepository = chatParticipantRepository;
    }
	
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
		if(StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
			//stomp 헤더에서 principal을 꺼냄
			Authentication auth = (Authentication)accessor.getUser();
			String username = auth.getName();
			String destination = accessor.getDestination();
			//목적지 url을 가져와서 방번호를 추출함
			if(destination.startsWith("/sub/chat/room/")) {
				Long chatRoomId = Long.parseLong(destination.substring(destination.lastIndexOf("/")+1)); 
				ChatParticipant chatParticipant = new  ChatParticipant();
				chatParticipant.setChatRoomId(chatRoomId);
				chatParticipant.setEmployeeUsername(username);	
				if(!chatParticipantService.isRoomParticipant(chatParticipant)) {
		        	throw new AccessDeniedException("");
		        }
			}else if(destination.equals("/user/queue/notifications")) {
				
			}
			
			
			
			
		}
		return message;
	}
	
	
}
