package com.goodee.corpdesk.chat.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.goodee.corpdesk.chat.entity.ChatMessage;
import com.goodee.corpdesk.chat.entity.ChatParticipant;
import com.goodee.corpdesk.chat.service.ChatMessageService;

@Controller
@RequestMapping("/chat/message/**")
public class ChatMessageController {
	
	@Autowired
	// Spring에서 서버가 클라이언트(STOMP 구독자)에게 메시지를 푸시하기 위해 제공하는 템플릿 객체
	private SimpMessagingTemplate messagingTemplate;
	
	@Autowired
	private ChatMessageService chatMessageService;

	// websocket 요청에 대한 매핑 위의 requestMapping과 관련없고 websocket config에서 지정해준 prefix 사용
	@MessageMapping("/chat/message")
	public void chatsendMessage(ChatMessage msg) {
		chatMessageService.messageSave(msg);
		messagingTemplate.convertAndSend("/sub/chat/room/" + msg.getChatRoomId(), msg);
		

	}
	
	@GetMapping("list/{chatRoomId}/{messageNo}/{size}")
	@ResponseBody
	public Map<String , Object> chatMessageList(@PathVariable(value = "chatRoomId")Long chatRoomId , 
			@PathVariable(value = "messageNo")Long lastMessageNo, @PathVariable(value = "size")int size){
		
		List<ChatMessage> list = chatMessageService.chatMessageList(chatRoomId,lastMessageNo,size);
		Map<String, Object> map = new HashMap<>();
		map.put("size", list.size());
		map.put("messages",list);
		
		return map;
		
	}
	
	
	
	
}
