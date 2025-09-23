package com.goodee.corpdesk.chat.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.goodee.corpdesk.chat.dto.RoomData;
import com.goodee.corpdesk.chat.entity.ChatMessage;
import com.goodee.corpdesk.chat.entity.ChatRoom;
import com.goodee.corpdesk.chat.service.ChatRoomService;

@Controller
@RequestMapping("/chat/room/**")
public class ChatRoomController {

	@Autowired
	ChatRoomService chatRoomService;
	@Autowired
	SimpMessagingTemplate simpMessagingTemplate;
	@GetMapping("list")
	public String chatRoomList(Principal principal ,Model model) {
			String username= principal.getName();
			List<ChatRoom> roomList= chatRoomService.getChatRoomList(username);
			model.addAttribute("roomList", roomList);
		
		return "Chat/chat_list";
	}
	@GetMapping("detail/{roomId}")
	public String chatRoomDetail(@PathVariable(value = "roomId") Long roomId , Model model) {
		model.addAttribute("roomId",roomId);
		
		return "Chat/chat_page";
	}
	
	@ResponseBody
	@PostMapping("createRoom")
	public ChatRoom createRoom(@RequestBody RoomData roomdata , Principal principal) {
		//채팅방 생성 
		Long roomId =chatRoomService.createRoom(roomdata, principal);
		ChatRoom chatRoom = new ChatRoom();
		chatRoom.setChatRoomId(roomId);
		chatRoom.setChatRoomTitle(roomdata.getRoomTitle());
		
		//상대방 구독 알림 보냄
		simpMessagingTemplate.convertAndSendToUser(roomdata.getUsername(),"/queue/notifications" , chatRoom);
		return chatRoom;
	}
}
