package com.goodee.corpdesk.chat.service;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goodee.corpdesk.chat.dto.RoomData;
import com.goodee.corpdesk.chat.entity.ChatMessage;
import com.goodee.corpdesk.chat.entity.ChatParticipant;
import com.goodee.corpdesk.chat.entity.ChatRoom;
import com.goodee.corpdesk.chat.repository.ChatParticipantRepository;
import com.goodee.corpdesk.chat.repository.ChatRoomRepository;

@Service
public class ChatRoomService {

	@Autowired
	private ChatRoomRepository chatRoomRepository;
	
	@Autowired
	private ChatParticipantRepository chatParticipantRepository;

	//해당 유저의 모든 채팅방 목록을 불러옴
	public List<ChatRoom> getChatRoomList(String username) {
		List<ChatRoom> list= chatRoomRepository.findAllByUsername(username);
		return list;
	}


	public Long createRoom(RoomData roomdata , Principal principal ) {
		
		ChatRoom chatroom = new ChatRoom();
		ChatParticipant chatParticipant = new ChatParticipant();

		chatroom.setChatRoomTitle(roomdata.getUsername());
		chatroom = chatRoomRepository.save(chatroom);
		
		chatParticipant.setEmployeeUsername(roomdata.getUsername());
		chatParticipant.setChatRoomId(chatroom.getChatRoomId());
		chatParticipantRepository.save(chatParticipant);
		
		chatParticipant = new ChatParticipant();
		chatParticipant.setChatRoomId(chatroom.getChatRoomId());
		chatParticipant.setEmployeeUsername(principal.getName());
		chatParticipantRepository.save(chatParticipant);
		
		return chatroom.getChatRoomId();
		
	}

	
	//채팅방에 이전 메세지들을 가져옴 최신순으로 
	public List<ChatMessage> MessageList(Long roomId) {
		
		
		return null;
	}

}
