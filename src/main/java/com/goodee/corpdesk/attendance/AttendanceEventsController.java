package com.goodee.corpdesk.attendance;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attendance")
public class AttendanceEventsController {
	
	@Autowired
	private AttendanceService attendanceService;
	
	// start/end 또는 startStr/endStr 모두 허용 + 없으면 기본 기간 사용
    @GetMapping("/events")
    public ResponseEntity<List<CalendarEvent>> getEvents(
            @RequestParam(value = "start", required = false) String start,
            @RequestParam(value = "end",   required = false) String end,
            @RequestParam(value = "startStr", required = false) String startStr,
            @RequestParam(value = "endStr",   required = false) String endStr) {

        // 우선순위: start > startStr, end > endStr
        String s = (start != null && !start.isBlank()) ? start : startStr;
        String e = (end   != null && !end.isBlank())   ? end   : endStr;

        // 파라미터가 하나라도 없으면 기본 기간(오늘 기준 월초~말)으로 대체
        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        LocalDateTime monthStart = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime monthEnd   = monthStart.plusMonths(1).minusSeconds(1);

        LocalDateTime from = (s != null) ? parseToLocalDateTime(s) : monthStart;
        LocalDateTime to   = (e != null) ? parseToLocalDateTime(e) : monthEnd;

        // 기간 내 출근 레코드 조회
        List<Attendance> list = attendanceService.getListByCheckInBetween(from, to);

        // Attendance → FullCalendar 호환 이벤트로 변환
        List<CalendarEvent> events = list.stream().map(a -> new CalendarEvent(
                String.valueOf(a.getAttendanceId()),
                "출근 - " + (a.getEmployeeId() != null ? a.getEmployeeId() : "-"),
                a.getCheckInDatetime() != null ? a.getCheckInDatetime().toString() : null,
                a.getCheckOutDatetime() != null ? a.getCheckOutDatetime().toString() : null,
                false
        )).toList();

        return ResponseEntity.ok(events);
    }

    // 문자열을 Offset 또는 LocalDateTime으로 파싱해서 서버 로컬 시간 기준 LocalDateTime으로 변환
    private LocalDateTime parseToLocalDateTime(String s) {
        try {
            // 오프셋/UTC(Z) 포함 형태 예: 2025-09-01T00:00:00+09:00 or ...Z
            return OffsetDateTime.parse(s, DateTimeFormatter.ISO_DATE_TIME).toLocalDateTime();
        } catch (Exception ignore) {
            // 오프셋이 없으면 순수 LocalDateTime으로 해석 예: 2025-09-01T00:00:00
            return LocalDateTime.parse(s, DateTimeFormatter.ISO_DATE_TIME);
        }
    }

    // FullCalendar 응답 DTO
    public static record CalendarEvent(
            String id,
            String title,
            String start,
            String end,
            boolean allDay
    ) {}

}
