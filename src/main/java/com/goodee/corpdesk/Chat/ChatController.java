package com.goodee.corpdesk.Chat;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("chat/")
public class ChatController {

	
	public void sendMessage(ChatMessage msg , Principal principal) {
		
		 
	}

@GetMapping("form")
public String chatForm() {
	return "Chat/chat_page";
	
}
	
}
