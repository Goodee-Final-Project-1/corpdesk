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

    @GetMapping("list")
    public String list(@RequestParam("username") String username, Model model) {
        ResAttendanceDTO resAttendanceDTO = attendanceService.getAttendanceStatus(username);



        model.addAttribute("resAttendanceDTO", resAttendanceDTO);

        return "attendance/list";
    }

}