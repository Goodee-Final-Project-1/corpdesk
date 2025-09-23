package com.goodee.corpdesk.chat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	
	// 채팅방 닫을 때 제일 최신 메세지ID를 저장 
	  @Transactional
	public void updateLastMessage(String username, Long roomId, Long lastMessage) {
		chatParticipantRepository.updateLastMessage(username,roomId,lastMessage);
		
	}
}
