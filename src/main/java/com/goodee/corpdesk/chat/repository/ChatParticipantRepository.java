package com.goodee.corpdesk.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.goodee.corpdesk.chat.entity.ChatParticipant;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long>{

	List<ChatParticipant> findAllByChatRoomId(Long chatRoomId);
	boolean existsByChatRoomIdAndEmployeeUsername(Long chatRoomId , String username);
	@Modifying
	@Query("UPDATE ChatParticipant c SET lastCheckMessageId = :lastMessage "+
		   "WHERE c.employeeUsername = :username AND c.chatRoomId =:roomId")
	void updateLastMessage(@Param("username") String username, @Param("roomId") Long roomId,@Param("lastMessage") Long lastMessage);
	ChatParticipant findByChatRoomIdAndEmployeeUsername(Long roomId, String username);
	
	@Query("SELECT COUNT(*) FROM ChatMessage "+
			"WHERE messageId > :lastMessageId AND "+
			"chatRoomId =:chatRoomId")
	Long count(@Param("lastMessageId") Long getLastCheckMessageId ,@Param("chatRoomId")  Long chatRoomId);
	
	@Query("SELECT COUNT(*) FROM ChatMessage "+
			"WHERE chatRoomId =:chatRoomId")
	Long countAll(@Param("chatRoomId") Long roomId); 
	
	
}
