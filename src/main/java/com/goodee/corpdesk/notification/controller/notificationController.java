package com.goodee.corpdesk.notification.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.goodee.corpdesk.chat.dto.ChatMessageDto;
import com.goodee.corpdesk.chat.service.ChatRoomService;


@ControllerAdvice
public class notificationController {
	@Autowired
	ChatRoomService chatRoomService;
	
	
	//알림은 모든 페이지 에서 공통으로 봐야함으로
	@ModelAttribute
	public void messageNotification(Model model , Principal principal) {
		List<ChatMessageDto> notificationList=  chatRoomService.getChatNotificationList(principal.getName());
		model.addAttribute("notificationList",notificationList);
		
	}
}
