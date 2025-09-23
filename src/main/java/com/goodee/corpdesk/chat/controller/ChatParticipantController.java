package com.goodee.corpdesk.chat.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.goodee.corpdesk.chat.service.ChatParticipantService;


@Controller
@RequestMapping("/chat/participant/**")
public class ChatParticipantController {
	@Autowired
	ChatParticipantService chatParticipantService;
	@PostMapping("lastMessage/{roomId}")
	public void saveLastMessage(@PathVariable(value="roomId") Long roomId,Principal principal) {
		String username = principal.getName();
		chatParticipantService.updateLastMessage(username,roomId);
		
	}
}
