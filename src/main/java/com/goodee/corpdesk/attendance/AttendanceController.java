package com.goodee.corpdesk.attendance;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employee/{username}/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    private final DateTimeFormatter formatterInput = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private final DateTimeFormatter formatterOutput = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostMapping("/delete")
    public String deleteAttendance(@PathVariable("username") String username,
                                   @RequestParam("attendanceIds") List<Long> attendanceIds) {
        attendanceService.deleteAttendances(username, attendanceIds);
        return "redirect:/employee/edit/" + username;
    }

    @PostMapping("/edit")
    @ResponseBody
    public Map<String, Object> editAttendance(@PathVariable("username") String username,
                                              @RequestBody AttendanceEditDTO dto) {
        Map<String, Object> result = new HashMap<>();
        try {
            Attendance attendance = attendanceService.getAttendanceById(dto.getAttendanceId());

            // 구분 처리 (빈값이면 "-"로)
            if (!StringUtils.hasText(dto.getWorkStatus())) {
                attendance.setWorkStatus("-");
            } else {
                attendance.setWorkStatus(dto.getWorkStatus());
            }

            // 일시 처리
            if (StringUtils.hasText(dto.getDateTime())) {
                LocalDateTime dateTime = LocalDateTime.parse(dto.getDateTime(), formatterInput);

                // "출근" / "퇴근"에 따라 일시 저장
                if ("출근".equals(attendance.getWorkStatus())) {
                    attendance.setCheckInDateTime(dateTime);
                } else if ("퇴근".equals(attendance.getWorkStatus())) {
                    attendance.setCheckOutDateTime(dateTime);
                }
            }

            attendanceService.updateAttendance(attendance);

            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        return result;
    }
}
