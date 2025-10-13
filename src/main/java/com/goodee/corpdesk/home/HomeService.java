package com.goodee.corpdesk.home;

import com.goodee.corpdesk.approval.dto.ResApprovalDTO;
import com.goodee.corpdesk.approval.service.ApprovalService;
import com.goodee.corpdesk.employee.EmployeeService;
import com.goodee.corpdesk.employee.ResEmployeeDTO;
import com.goodee.corpdesk.file.entity.EmployeeFile;
import com.goodee.corpdesk.schedule.dto.ResPersonalScheduleDTO;
import com.goodee.corpdesk.schedule.service.PersonalScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class HomeService {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private PersonalScheduleService personalScheduleService;
    @Autowired
    private ApprovalService approvalService;

    public ResEmployeeDTO getEmployee (String username) {

        ResEmployeeDTO employee = employeeService.getFulldetail(username);

        Optional<EmployeeFile> optionalFile = employeeService.getEmployeeFileByUsername(username);
        if (optionalFile.isPresent()) {
            EmployeeFile file = optionalFile.get();
            employee.setSaveName(file.getSaveName());
            employee.setExtension(file.getExtension());
        }

        return employee;

    }

    public ResPersonalScheduleDTO getTodaySchedule (String username) {

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        List<ResPersonalScheduleDTO> todaySchedules = personalScheduleService.getSchedulesByDate(username, startOfDay, endOfDay);

        ResPersonalScheduleDTO result = new ResPersonalScheduleDTO();

        // 오늘의 일정 갯수
        result.setTodayScheduleCnt(todaySchedules.size());

        return result;

    }

    public ResApprovalDTO getApproval (String username) throws Exception {

        ResApprovalDTO approval = new ResApprovalDTO();

        List<ResApprovalDTO> reqApprovals =  approvalService.getApprovalList("request", username);
        approval.setApprovals(reqApprovals);
        approval.setApprovalCnt(reqApprovals.size());

        return approval;

    }

}
