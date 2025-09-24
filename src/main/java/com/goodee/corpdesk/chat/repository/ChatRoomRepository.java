package com.goodee.corpdesk.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.goodee.corpdesk.chat.dto.RoomData;
import com.goodee.corpdesk.chat.entity.ChatRoom;

public interface ChatRoomRepository extends  JpaRepository<ChatRoom, Long> {

	@Query("SELECT r FROM ChatRoom r JOIN ChatParticipant p "+
			"ON r.chatRoomId = p.chatRoomId "+
			"WHERE p.employeeUsername = :username")
	List<ChatRoom> findAllByUsername(@Param("username") String username);

	
	
}
