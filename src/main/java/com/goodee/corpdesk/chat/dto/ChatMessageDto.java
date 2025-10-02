package com.goodee.corpdesk.chat.dto;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.goodee.corpdesk.approval.entity.Approver;

import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDto{
	
	private Long messageId;
	
	private String employeeUsername;
	private Long chatRoomId;
	private String messageContent;
	private String messageType;
	
	
	@CreationTimestamp
	private LocalDateTime sentAt;
	
	@Transient
	private String notificationType;
	@Transient
	private boolean focused;
	@Transient
	private String imgPath;
	@Transient
	private String viewName;
}
