package com.goodee.corpdesk.chat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.goodee.corpdesk.chat.entity.ChatMessage;
import com.goodee.corpdesk.chat.entity.ChatParticipant;
import com.goodee.corpdesk.chat.repository.ChatMessageRepository;
import com.goodee.corpdesk.chat.repository.ChatParticipantRepository;

@Service
public class ChatMessageService {

	@Autowired
	private ChatMessageRepository chatMessageRepository;
	
	@Autowired
	private ChatParticipantRepository chatParticipantRepository;
	
	public void messageSave(ChatMessage msg) {
		chatMessageRepository.save(msg);
		
	}
	
	public List<ChatParticipant> participantListByRoom(Long RoomId){
		return chatParticipantRepository.findAllByChatRoomId(RoomId);
	}

	//방번호로 해당 방의 메세지를 조회해옴
	public List<ChatMessage> chatMessageList(Long chatRoomId, Long lastMessageNo, int size) {
		Pageable pageable = PageRequest.of(0, size);
		if(lastMessageNo==0|| lastMessageNo==null) {
			return chatMessageRepository.findByChatRoomIdOrderByMessageIdDesc(chatRoomId , pageable);
		}else {
			return chatMessageRepository.findByChatRoomIdAndMessageIdLessThanOrderByChatRoomId(chatRoomId,lastMessageNo, pageable);
		}
	
	}

}
