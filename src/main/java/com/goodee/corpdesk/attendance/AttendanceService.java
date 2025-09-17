package com.goodee.corpdesk.attendance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttendanceService {
	
	@Autowired
	private AttendanceRepository attendanceRepository;
	
	
	
//	// 문자열을 LocalDateTime으로 파싱하기 위한 포맷 (예: "2025-09-10T09:00")
//	private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
//	
//    // 기간 전체 조회(직원 미지정) → 풀캘린더 DTO 변환
//	public List<CalendarEventDTO> getEvents(String startIso, String endIso) {
//		// 문자열 → LocalDateTime
//		LocalDateTime start = LocalDateTime.parse(startIso, ISO);
//		LocalDateTime end = LocalDateTime.parse(endIso, ISO);
//		
//		List<Attendance> list = attendanceRepository
//								.findAllByCheckInDatetimeBetween(start, end);
//		
//        // 엔티티 → 풀캘린더 DTO로 변환
//		return list.stream()
//			    .map(this::toCalendarEvent)
//				.collect(Collectors.toList());
//	}

}
