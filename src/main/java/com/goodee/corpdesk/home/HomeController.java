package com.goodee.corpdesk.home;

import com.goodee.corpdesk.approval.dto.ResApprovalDTO;
import com.goodee.corpdesk.approval.service.ApprovalService;
import com.goodee.corpdesk.attendance.DTO.ResAttendanceDTO;
import com.goodee.corpdesk.attendance.service.AttendanceService;
import com.goodee.corpdesk.employee.Employee;
import com.goodee.corpdesk.employee.EmployeeService;
import com.goodee.corpdesk.employee.ResEmployeeDTO;
import com.goodee.corpdesk.file.entity.EmployeeFile;
import com.goodee.corpdesk.schedule.dto.ResPersonalScheduleDTO;
import com.goodee.corpdesk.schedule.entity.PersonalSchedule;
import com.goodee.corpdesk.schedule.service.PersonalScheduleService;
import com.goodee.corpdesk.vacation.dto.ResVacationDTO;
import com.goodee.corpdesk.vacation.service.VacationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private HomeService homeService;
    @Autowired
    private PersonalScheduleService personalScheduleService;
    @Autowired
    private ApprovalService approvalService;
    @Autowired
    private VacationService vacationService;
    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/")
	public String home() {
		return "login";
	}

	@GetMapping("login/{msg}")
	public String login(@PathVariable String msg, Model model) {
		msg = msg.replaceAll("\\+", " ");
		model.addAttribute("msg", msg);
		return "login";
	}

    @GetMapping("/dashboard")
    public String home(@AuthenticationPrincipal UserDetails userDetails
                       , Model model) throws Exception {
        
        // 1. 직원 정보
        String username = userDetails.getUsername();
        ResEmployeeDTO employee = homeService.getEmployeeInfo(username);
        
        model.addAttribute("employee", employee);

        // 2. 일정 정보
        // 오늘의 일정 갯수
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        List<ResPersonalScheduleDTO> todaySchedules = personalScheduleService.getSchedulesByDate(username, startOfDay, endOfDay);
        model.addAttribute("todayScheduleCnt", todaySchedules.size());

        // TODO 외부 일정

        // 3. 결재 정보
        List<ResApprovalDTO> reqApprovals =  approvalService.getApprovalList("request", username);
        model.addAttribute("reqApprovalCnt", reqApprovals.size()); // 결재 대기 문서 갯수
        model.addAttribute("waitList", reqApprovals);// 결재 대기 문서

        // 4. 연차
        // 잔여 연차
        ResVacationDTO vacation = vacationService.getVacation(username);
        model.addAttribute("remainingVacation", vacation.getRemainingVacation());

        // TODO 휴가
        model.addAttribute("vacation", vacation);

        // 5. 근태
        ResAttendanceDTO currAttd = attendanceService.getCurrentAttendance(username);
        model.addAttribute("currAttd", currAttd);

        // TODO 게시판 기능 완성되면 아래 항목도 추가 구현
        // 6. 공지사항

        return "dashboard";

    }
}
