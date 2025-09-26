package com.goodee.corpdesk.calendar;

import com.goodee.corpdesk.attendance.Attendance;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarApiController {

	private final CalendarService  calendarService;

	@PostMapping("attendance")
	public List<Attendance> getAttendance(@RequestBody CalendarDTO calendarDTO, Authentication authentication) {
//		System.out.println(calendarDTO);
		return calendarService.getAttendance(calendarDTO, authentication.getName());
	}
}
