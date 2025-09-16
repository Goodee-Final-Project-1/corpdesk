package com.goodee.corpdesk.attendance;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class AttendanceApiController {
	
	private final AttendanceService attendanceService;
	
	@GetMapping("attendances")
	public List<CalendarEventDTO> list(
	    @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
	    @RequestParam("end")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
	    @RequestParam(value="employeeId", required=false) Integer employeeId
	) {
	    LocalDateTime s = start.atStartOfDay();
	    LocalDateTime e = end.atTime(23, 59, 59);
	    return attendanceService.getEvents(s, e, employeeId);
	}

	

}
