package com.goodee.corpdesk.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goodee.corpdesk.chat.entity.ChatParticipant;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long>{

	List<ChatParticipant> findAllByChatRoomId(Long chatRoomId);
	boolean existsByChatRoomIdAndEmployeeUsername(Long chatRoomId , String username);
}
