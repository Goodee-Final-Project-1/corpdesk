package com.goodee.corpdesk.attendance;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttendanceService {
	
	@Autowired
	private AttendanceRepository attendanceRepository;
	
	public List<CalendarEventDTO> getEvents(LocalDateTime start, LocalDateTime end, Integer employeeId) {

        // 조회 분기
        List<Attendance> rows = (employeeId == null)
            ? attendanceRepository.findByCheckInDateTimeBetween(start, end)
            : attendanceRepository.findByEmployeeIdAndCheckInDateTimeBetween(employeeId, start, end);

        // 엔티티 -> FullCalendar DTO 매핑
        return rows.stream().map(a -> {
            String title = "출근 (" + a.getWorkStatus() + ")";
            String color = "NORMAL".equalsIgnoreCase(a.getWorkStatus()) ? "#2E86DE" : "#E67E22";
            String s = a.getCheckInDateTime() == null ? null : a.getCheckInDateTime().toString();
            String e = a.getCheckOutDateTime() == null ? null : a.getCheckOutDateTime().toString();
            return new CalendarEventDTO(title, s, e, false, color);
        }).collect(Collectors.toList());
    }

}
