package com.goodee.corpdesk.chat.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table
public class ChatMessage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long messageId;
	
	private String employeeUsername;
	private Long chatRoomId;
	private String messageContent;
	private String messageType;
	
	@CreationTimestamp
	private LocalDateTime sent_at;
	
	
	private LocalDateTime updated_at;
	private LocalDateTime creates_at;
	private String modifed_by;
	private boolean use_yn;
}
