package com.goodee.corpdesk.Chat;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

//http요청에 대한 url 매핑
@RequestMapping("chat/")
public class ChatController {
	
	@Autowired
	//Spring에서 서버가 클라이언트(STOMP 구독자)에게 메시지를 푸시하기 위해 제공하는 템플릿 객체
	private SimpMessagingTemplate messagingTemplate; 
	//websocket 요청에 대한 매핑 위의 requestMapping과 관련없고 websocket config에서 지정해준 prefix 사용
	@MessageMapping("/chat/message")
	public void chatMessage(ChatMessage msg , Principal principal) {
		
		
		ChatRoom chatRoom = new ChatRoom();
		 messagingTemplate.convertAndSend("/sub/chat/room/" + msg.getChatRoomId(), msg);
		 
	}

@GetMapping("form")
public String chatForm() {
	return "Chat/chat_page";
}
	
}
