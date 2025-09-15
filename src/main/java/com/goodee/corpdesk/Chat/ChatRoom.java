package com.goodee.corpdesk.Chat;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table

public class ChatRoom {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long chatRoomId;
	String chatRoomTiTle;

	
	
	LocalDateTime updated_at;
	LocalDateTime creates_at;
	String modifed_by;
	boolean use_yn;
	
}
