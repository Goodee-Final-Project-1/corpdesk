package com.goodee.corpdesk.Chat;

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
	Long messageId;
	
	int employeeId;
	Long chatRoomId;
	String messageContent;
	String messageType;
	
	@CreationTimestamp
	LocalDateTime sent_at;
	
	
	LocalDateTime updated_at;
	LocalDateTime creates_at;
	String modifed_by;
	boolean use_yn;
}
