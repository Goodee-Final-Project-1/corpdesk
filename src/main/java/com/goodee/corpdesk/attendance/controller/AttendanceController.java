package com.goodee.corpdesk.attendance.controller;

import com.goodee.corpdesk.attendance.DTO.ResAttendanceDTO;
import com.goodee.corpdesk.attendance.entity.Attendance;
import com.goodee.corpdesk.attendance.service.AttendanceService;
import com.goodee.corpdesk.employee.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/attendance/**")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Value("${cat.attendance}")
    private String cat;
    @Autowired
    private EmployeeService employeeService;

    @ModelAttribute("cat")
    public String getCat() {
        return cat;
    }

    /**
     * 특정 직원에 대한 출퇴근 목록 화면을 반환합니다.
     *
     * @param username 직원의 username
     * @return 출퇴근 목록 화면 jsp가 있는 경로
     */
    @GetMapping("list")
    public String list(@RequestParam("username") String username, Model model)  throws Exception {

        // 1. 출퇴근 기록 중 출근 날짜가 가장 오래된 년도
        LocalDateTime oldestCheckIn = attendanceService.getOldestCheckInDateTime(username);

        // 2. 현재 상태&출퇴근id&출퇴근일시 조회 - 출퇴근 상태, 출근시간, 퇴근시간
        ResAttendanceDTO currAttd = attendanceService.getCurrentAttendance(username);

        // 3. 통계 데이터 조회 (전체 지각, 조퇴, 결근 횟수, 근무 시간, 근무일수) (전체 기준: year, month에 null 전달)
        ResAttendanceDTO attCnts = attendanceService.getAttendanceCounts(username, null, null);
        ResAttendanceDTO workSummary = attendanceService.getWorkSummary(username, null, null);

        // 4. 상세 기록 목록 (전체 기준)
        List<ResAttendanceDTO> attDatilList = attendanceService.getAttendanceDetailList(username, null, null);

        model.addAttribute("oldestCheckIn", oldestCheckIn);
        model.addAttribute("currAttd", currAttd);
        model.addAttribute("attCnts", attCnts);
        model.addAttribute("workSummary", workSummary);
        model.addAttribute("attDatilList", attDatilList);

        return "attendance/list";
    }

}