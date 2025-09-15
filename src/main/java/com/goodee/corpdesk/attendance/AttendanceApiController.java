package com.goodee.corpdesk.attendance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AttendanceApiController {
	
	@Autowired
	private AttendanceService attendanceService;
	
//	@GetMapping("attendances")
//	public List<CalendarEventDTO> list(
//	    @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
//	    @RequestParam("end")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
//	    @RequestParam(value="employeeId", required=false) Integer employeeId
//	) {
//	    LocalDateTime s = start.atStartOfDay();
//	    LocalDateTime e = end.atTime(23, 59, 59);
//	    return attendanceService.getEvents(s, e, employeeId);
//	}

	

}
