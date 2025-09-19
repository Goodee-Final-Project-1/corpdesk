package com.goodee.corpdesk.attendance;

import java.time.LocalDateTime;
import java.util.List;

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

	
	
	
	// useYn = true 인 데이터만 조회
	public List<Attendance> getAttendanceByUsername(String username) {
	    return attendanceRepository.findByUsernameAndUseYn(username, true);
	}

	 // 소프트 삭제
    public void deleteAttendances(String username, List<Long> attendanceIds) {
    	List<Attendance> records = attendanceRepository.findAllById(attendanceIds);

        for (Attendance att : records) {
            if (!att.getUsername().equals(username)) {
                throw new IllegalArgumentException("다른 직원의 출퇴근 기록은 삭제할 수 없습니다. ID=" + att.getAttendanceId());
            }
            att.setUseYn(false); // soft delete
        }

        attendanceRepository.saveAll(records);
    }

	    public Attendance getAttendance(Long id) {
	        return attendanceRepository.findById(id).orElse(null);
	    }

	    public Attendance saveAttendance(Attendance attendance) {
	        return attendanceRepository.save(attendance);
	    }
	
	    public void updateAttendanceInline(Long attendanceId, String workStatus, String dateTime) {
	        Attendance attendance = attendanceRepository.findById(attendanceId)
	                .orElseThrow(() -> new RuntimeException("출퇴근 기록이 없습니다."));
	        attendance.setWorkStatus(workStatus);
	        if(workStatus.equals("출근")) {
	            attendance.setCheckInDateTime(LocalDateTime.parse(dateTime));
	        } else {
	            attendance.setCheckOutDateTime(LocalDateTime.parse(dateTime));
	        }
	        attendanceRepository.save(attendance);
	    }
}
