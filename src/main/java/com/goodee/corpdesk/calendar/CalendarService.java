package com.goodee.corpdesk.calendar;

import com.goodee.corpdesk.attendance.entity.Attendance;
import com.goodee.corpdesk.attendance.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarService {

	private final AttendanceRepository attendanceRepository;

	public List<Attendance> getAttendance(CalendarDTO calendarDTO, String username) {
		return attendanceRepository.findAllByUsernameAndCheckInDateTimeBetween(username, calendarDTO.getStartDateTime(), calendarDTO.getEndDateTime());
	}
}
