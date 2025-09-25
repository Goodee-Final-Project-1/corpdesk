package com.goodee.corpdesk.calendar;

import com.goodee.corpdesk.attendance.Attendance;
import com.goodee.corpdesk.attendance.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarService {

	private final AttendanceRepository attendanceRepository;

	public List<Attendance> getAttendance(CalendarDTO calendarDTO) {
		return attendanceRepository.findAllByCheckInDateTimeBetween(calendarDTO.getStartDateTime(), calendarDTO.getEndDateTime());
	}
}
