package com.goodee.corpdesk.Chat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
	@Autowired
	ChatRoomRepository chatRoomRepository;
	
	public  void getChatRoom() {
		
	}

	//해당 유저의 모든 채팅방 목록을 불러옴
	public List<ChatRoom> getChatRoomList(String username) {
		List<ChatRoom> list= chatRoomRepository.findAllByUsername(username);
		return list;
	}
}
