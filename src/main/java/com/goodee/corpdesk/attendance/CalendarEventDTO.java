package com.goodee.corpdesk.attendance;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CalendarEventDTO {
	
	private String title;
	private String start;
	private String end;
	private boolean allDay;
	private String color;

}
