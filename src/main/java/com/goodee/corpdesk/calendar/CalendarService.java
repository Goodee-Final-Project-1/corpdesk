package com.goodee.corpdesk.calendar;

import com.goodee.corpdesk.attendance.entity.Attendance;
import com.goodee.corpdesk.attendance.repository.AttendanceRepository;
import com.goodee.corpdesk.vacation.entity.Vacation;
import com.goodee.corpdesk.vacation.entity.VacationDetail;
import com.goodee.corpdesk.vacation.repository.VacationDetailRepository;
import com.goodee.corpdesk.vacation.repository.VacationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarService {

	private final AttendanceRepository attendanceRepository;
	private final VacationRepository vacationRepository;
	private final VacationDetailRepository vacationDetailRepository;
//	private final PersonalScheduleRepository personalScheduleRepository;

	public List<Attendance> getAttendance(CalendarDTO calendarDTO, String username) {
		return attendanceRepository.findAllByUsernameAndDateTime(username, calendarDTO.getStartDateTime(), calendarDTO.getEndDateTime());
	}

	public List<VacationDetail> getVacation(CalendarDTO calendarDTO, String username) {
		Vacation vacation = vacationRepository.findByUsername(username).get();

		return vacationDetailRepository.findAllByVacationIdAndDateTime(vacation.getVacationId(), calendarDTO.getStartDate(), calendarDTO.getEndDate());
	}

//	public List<PersonalSchedule> getSchedule(CalendarDTO calendarDTO, String username) {
//		return personalScheduleRepository.findAllByUsernameAndUseYnTrueAndScheduleDateTimeGreaterThanEqualAndEndDateLessThanEqual(username, calendarDTO.getStartDate(), calendarDTO.getEndDate());
//	}
}
