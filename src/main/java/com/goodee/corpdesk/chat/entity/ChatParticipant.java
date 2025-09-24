package com.goodee.corpdesk.chat.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table
@Getter
@Setter
@ToString
public class ChatParticipant {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long participantId;
	private String employeeUsername;
	private Long chatRoomId;
	private Long lastCheckMessageId;
	private LocalDateTime updated_at;
	private LocalDateTime creates_at;
	private String modifed_by;
	private boolean use_yn;
	
}
