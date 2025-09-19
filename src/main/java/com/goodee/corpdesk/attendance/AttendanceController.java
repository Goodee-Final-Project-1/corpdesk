package com.goodee.corpdesk.attendance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/employee/{username}/attendance")
public class AttendanceController {

	@Autowired
    private AttendanceService attendanceService;

    @PostMapping("/delete") // POST 방식
    public String deleteAttendance(@PathVariable("username") String username,
                                   @RequestParam("attendanceIds") List<Long> attendanceIds) {
        attendanceService.deleteAttendances(username, attendanceIds);
        return "redirect:/employee/edit/" + username;
    }
    
    @PostMapping("/edit")
    @ResponseBody
    public Map<String, Object> editAttendanceAjax(@PathVariable("username") String username,
            @RequestBody Map<String, String> payload) {

        Long attendanceId = Long.valueOf(payload.get("attendanceId"));
        String workStatus = payload.get("workStatus");
        String dateTime = payload.get("dateTime");

        attendanceService.updateAttendanceInline(attendanceId, workStatus, dateTime);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return response;
    }
    
}
