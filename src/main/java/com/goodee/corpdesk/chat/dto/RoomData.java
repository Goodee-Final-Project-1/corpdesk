package com.goodee.corpdesk.chat.dto;


import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component

public class RoomData {
	private String roomTitle;
	private String username;
}
