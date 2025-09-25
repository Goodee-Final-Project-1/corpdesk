package com.goodee.corpdesk.attendance;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    @ResponseBody
    public Map<String, Object> deleteAttendance(@PathVariable("username") String username,
                                                @RequestBody Map<String, List<Long>> payload) {
        List<Long> attendanceIds = payload.get("attendanceIds");
        Map<String, Object> result = new HashMap<>();
        try {
            attendanceService.deleteAttendances(username, attendanceIds);
            result.put("success", true);
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        return result;
    }

 // 출퇴근 추가
    @PostMapping("/add")
    public Map<String, Object> addAttendance(
            @PathVariable(name = "username") String username,   // 명시적으로 name 지정
            @RequestBody AttendanceEditDTO dto) {

    	
    	
    	
        Map<String, Object> result = new HashMap<>();
        try {
            Attendance att = new Attendance();
            att.setUsername(username);
            att.setWorkStatus(dto.getWorkStatus());
            att.setUseYn(true);

            if (dto.getDateTime() != null && !dto.getDateTime().isEmpty()) {
                LocalDateTime dt = LocalDateTime.parse(dto.getDateTime(), 
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
                if ("출근".equals(dto.getWorkStatus())) {
                    att.setCheckInDateTime(dt);
                } else if ("퇴근".equals(dto.getWorkStatus())) {
                    att.setCheckOutDateTime(dt);
                }
            }

            Attendance saved = attendanceService.saveOrUpdateAttendance(att);
            result.put("success", true);
            result.put("attendanceId", saved.getAttendanceId());
            result.put("workStatus", saved.getWorkStatus());
            result.put("dateTime", saved.getCheckInDateTime() != null ? 
                    saved.getCheckInDateTime().format(formatterOutput) :
                    (saved.getCheckOutDateTime() != null ? 
                         saved.getCheckOutDateTime().format(formatterOutput) : null));

        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("error", e.getMessage());
        }

        return result;
        
    }
    

    @PostMapping("/edit")
    @ResponseBody
    public Map<String, Object> editAttendance(@PathVariable("username") String username,
            @RequestBody AttendanceEditDTO dto) {
			Map<String, Object> result = new java.util.HashMap<>();
			try {
			Attendance attendance = attendanceService.getAttendanceById(dto.getAttendanceId());
			
			if (!org.springframework.util.StringUtils.hasText(dto.getWorkStatus())) {
			attendance.setWorkStatus("-");
			} else {
			attendance.setWorkStatus(dto.getWorkStatus());
			}
			
			if (org.springframework.util.StringUtils.hasText(dto.getDateTime())) {
			LocalDateTime dateTime = LocalDateTime.parse(dto.getDateTime(), formatterInput);
			if ("출근".equals(attendance.getWorkStatus())) {
			attendance.setCheckInDateTime(dateTime);
			} else if ("퇴근".equals(attendance.getWorkStatus())) {
			attendance.setCheckOutDateTime(dateTime);
			}
			}
			
			attendanceService.saveOrUpdateAttendance(attendance);
			
			result.put("success", true);
			} catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("error", e.getMessage());
			}
			return result;
		}
	}
