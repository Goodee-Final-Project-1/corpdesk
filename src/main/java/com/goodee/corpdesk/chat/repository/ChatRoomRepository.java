package com.goodee.corpdesk.chat.repository;

import java.util.List;
import java.util.Optional;

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

	@Query("SELECT r FROM ChatRoom r "+
		   "JOIN ChatParticipant p ON r.chatRoomId = p.chatRoomId " +
			"WHERE r.chatRoomType = 'direct' AND p.employeeUsername IN (:userA ,:userB) "+
		   "GROUP BY r.chatRoomId "+
			"HAVING COUNT(p.employeeUsername) = 2 ")	
	Optional<ChatRoom> findDuplicatedRoom(@Param("userA")String userA, @Param("userB")String userB);

	
	
}
