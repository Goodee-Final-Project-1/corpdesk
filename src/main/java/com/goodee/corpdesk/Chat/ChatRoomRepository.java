package com.goodee.corpdesk.Chat;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatRoomRepository extends  JpaRepository<ChatRoom, Long> {

	@Query("SELECT r FROM ChatRoom r JOIN ChatParticipant p "+
			"ON r.chatRoomId = p.chatRoomId "+
			"WHERE p.employeeUsername = :username")
	List<ChatRoom> findAllByUsername(String username);
	
	
}
