package com.goodee.corpdesk.attendance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CalendarEventDTO {
	
    // 이벤트 제목 (예: 출근 (NORMAL))
    private String title;
    // 시작일시 ISO 문자열
    private String start;
    // 종료일시 ISO 문자열
    private String end;
    // 종일 여부(근태는 보통 false)
    private boolean allDay;
    // 상태별 색상 코드
    private String color;

}
