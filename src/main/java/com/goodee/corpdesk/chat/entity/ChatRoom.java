package com.goodee.corpdesk.chat.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table
@Getter
@Setter
@ToString
public class ChatRoom {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long chatRoomId;
	private String chatRoomTitle;
	private String chatRoomType;
	
	@Transient
	private Long unreadCount;
	
	private LocalDateTime updated_at;
	private LocalDateTime creates_at;
	private String modifed_by;
	boolean use_yn;
	
}
