package com.goodee.corpdesk.chat.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.goodee.corpdesk.chat.entity.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>{


	List<ChatMessage> findByChatRoomIdOrderByMessageIdDesc(Long chatRoomId, Pageable pageable);

	List<ChatMessage> findByChatRoomIdAndMessageIdLessThanOrderByChatRoomId(Long chatRoomId, Long lastMessageNo,
			Pageable pageable);

	ChatMessage findTopByChatRoomIdOrderByMessageIdDesc(Long chatRoomId);
	
}
