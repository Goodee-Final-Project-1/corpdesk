package com.goodee.corpdesk.attendance;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/attendance")
public class AttendanceViewController {
	
    // /attendance/calendar 요청 시 JSP 뷰 반환
    @GetMapping("/calendar")
    public String calendarView() {
        // 내부: ViewResolver가 /WEB-INF/views/attendance/calendar.jsp 를 찾아서 렌더링
        return "attendance/calendar";
    }
}
