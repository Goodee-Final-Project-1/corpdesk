package com.goodee.corpdesk.chat.dto;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component

public class RoomData {
	private String chatRoomTitle;
	private List<String> usernames;
	private String chatRoomType;
	private Long chatRoomId;
	private Long unreadCount;
	private LocalDateTime lastMessageTime;
	private String chatRoomLastMessage;
	private String notificationType;
}
