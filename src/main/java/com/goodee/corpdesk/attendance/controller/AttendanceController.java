package com.goodee.corpdesk.attendance.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goodee.corpdesk.attendance.entity.Attendance;
import com.goodee.corpdesk.attendance.DTO.AttendanceEditDTO;
import com.goodee.corpdesk.attendance.service.AttendanceService;
import lombok.Getter;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RequestMapping("/attendance/**")
@Controller
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping("list")
    public String list(@RequestParam("username") String username) {
        return "attendance/list";
    }

}