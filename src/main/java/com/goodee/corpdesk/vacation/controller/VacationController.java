package com.goodee.corpdesk.vacation.controller;

import com.goodee.corpdesk.approval.service.ApprovalService;
import com.goodee.corpdesk.vacation.dto.ReqVacationDTO;
import com.goodee.corpdesk.vacation.dto.ResVacationDTO;
import com.goodee.corpdesk.vacation.service.VacationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/vacation/**")
public class VacationController {

    @Autowired
    private VacationService vacationService;

    @GetMapping("list")
    public String list(ReqVacationDTO reqVacationDTO, Model model) throws Exception {

        ResVacationDTO vacation = new ResVacationDTO();
        List<ResVacationDTO> details = new ArrayList<>();

        if(reqVacationDTO.getVacationType() == null){
            if(reqVacationDTO.getUsername() != null) {
                vacation = vacationService.getVacation(reqVacationDTO.getUsername());

                model.addAttribute("vacation", vacation);
            }

            details = vacationService.getVacationDetails(vacation.getVacationId());
        } else {
            if(reqVacationDTO.getUsername() != null) {
                vacation = vacationService.getVacation(reqVacationDTO.getUsername());

                model.addAttribute("vacation", vacation);
            }

            details = vacationService.getVacationDetails(vacation.getVacationId(), reqVacationDTO.getVacationType());

            model.addAttribute("vacationType", reqVacationDTO.getVacationType());
        }

        model.addAttribute("details", details);

        if(reqVacationDTO.getUsername() != null) model.addAttribute("username", reqVacationDTO.getUsername());

        return "vacation/list";

    }

}
