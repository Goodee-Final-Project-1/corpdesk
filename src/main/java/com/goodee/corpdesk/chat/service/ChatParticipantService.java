package com.goodee.corpdesk.chat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goodee.corpdesk.chat.entity.ChatParticipant;
import com.goodee.corpdesk.chat.repository.ChatParticipantRepository;

@Service
public class ChatParticipantService {

	@Autowired
	private ChatParticipantRepository chatParticipantRepository;
	
	public List<ChatParticipant> participantList(Long chatRoomId) {
		List<ChatParticipant> list = chatParticipantRepository.findAllByChatRoomId(chatRoomId);
		return null;
	}

	public boolean isRoomParticipant(ChatParticipant chatParticipant) {
		boolean result = chatParticipantRepository.
				existsByChatRoomIdAndEmployeeUsername(chatParticipant.getChatRoomId(),chatParticipant.getEmployeeUsername())
				;
		return result;
	}
}
