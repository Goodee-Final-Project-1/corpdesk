package com.goodee.corpdesk.vacation.controller;

import com.goodee.corpdesk.approval.service.ApprovalService;
import com.goodee.corpdesk.vacation.dto.ResVacationDTO;
import com.goodee.corpdesk.vacation.service.VacationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/vacation/**")
public class VacationController {

    @Autowired
    private VacationService vacationService;

    @GetMapping("list")
    public String list(@RequestParam(value = "username", required = false) String username, Model model) throws Exception {

        ResVacationDTO vacation = new ResVacationDTO();
        if(username != null) {
            vacation = vacationService.getVacation(username);

            model.addAttribute("vacation", vacation);
        }

        List<ResVacationDTO> details = vacationService.getVacationDetails(vacation.getVacationId());

        model.addAttribute("details", details);

        return "vacation/list";
    }

}
