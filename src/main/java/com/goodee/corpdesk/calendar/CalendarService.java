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
//	private final VacationDetailRepository vacationDetailRepository;
//	private final PersonalScheduleRepository personalScheduleRepository;

	public List<Attendance> getAttendance(CalendarDTO calendarDTO, String username) {
		return attendanceRepository.findAllByUsernameAndUseYnTrueAndCheckInDateTimeGreaterThanEqualAndCheckOutDateTimeLessThanEqual(username, calendarDTO.getStartDateTime(), calendarDTO.getEndDateTime());
	}

//	public List<VacationDetail> getVacation(CalendarDTO calendarDTO, String username) {
//		return vacationDetailRepository.findAllByUsernameAndUseYnTrueAndStartDateGreaterThanEqualAndEndDateLessThanEqual(username, calendarDTO.getStartDate(), calendarDTO.getEndDate());
//	}

//	public List<PersonalSchedule> getSchedule(CalendarDTO calendarDTO, String username) {
//		return personalScheduleRepository.findAllByUsernameAndUseYnTrueAndScheduleDateTimeBetween(username, calendarDTO.getStartDate(), calendarDTO.getEndDate());
//	}
}
