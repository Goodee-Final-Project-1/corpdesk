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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
public class HomeController {

    @Autowired
    private HomeService homeService;
    @Autowired
    private VacationService vacationService;
    @Autowired
    private AttendanceService attendanceService;

    @Value("${api.kakao.javascript.key}")
    private String appkey;

    @ModelAttribute("appkey")
    public String getAppkey() {
        return appkey;
    }

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
        
        String username = userDetails.getUsername();

        // 1. 직원 정보
        ResEmployeeDTO employee = homeService.getEmployee(username);
        model.addAttribute("employee", employee);

        // 2. 일정 정보
        ResPersonalScheduleDTO personalSchedule = homeService.getTodaySchedule(username);
        model.addAttribute("personalSchedule", personalSchedule);

        // 3. 결재 정보
        ResApprovalDTO approval = homeService.getApproval(username);
        model.addAttribute("approval", approval);

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
