package com.goodee.corpdesk.attendance.controller;

import com.goodee.corpdesk.attendance.DTO.ResAttendanceDTO;
import com.goodee.corpdesk.attendance.service.AttendanceService;
import com.goodee.corpdesk.employee.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    // TODO 더 뿌려야 할 데이터
    // TODO (해당 월의/전체)지각, 조퇴, 결근 횟수
    // TODO (해당 월의/전체)근무 시간
    // TODO 출근일, 출근시간, 퇴근일, 퇴근시간, 근무상태(출근/퇴근/출근전/휴가)

    // TODO 더 뿌려야 할 데이터
    // TODO (해당 월의/전체)지각, 조퇴, 결근 횟수
    // TODO (해당 월의/전체)근무 시간
    // TODO 출근일, 출근시간, 퇴근일, 퇴근시간, 근무상태(출근/퇴근/출근전/휴가)

    /**
     * 특정 직원에 대한 출퇴근 목록 화면을 반환
     *
     * @param username 직원의 username
     * @return 출퇴근 목록 화면 jsp가 있는 경로
     */
    @GetMapping("list")
    public String list(@RequestParam("username") String username, Model model)  throws Exception {

        // 1. 현재 상태&출퇴근id&출퇴근일시 조회 - 출퇴근 상태, 출근시간, 퇴근시간
        ResAttendanceDTO currAttd = attendanceService.getCurrentAttendance(username);

        // 2. 출퇴근 기록 중 출근 날짜가 가장 오래된 년도
        currAttd.setOldestCheckInDateTime(attendanceService.getOldestCheckInDateTime(username));

        // 3. 전체 지각, 조퇴, 결근 횟수


        // 4. 전체 근무 시간, 근무일수
        // 5. 전체 출퇴근 기록 목록

        model.addAttribute("currAttd", currAttd);

        return "attendance/list";
    }

}