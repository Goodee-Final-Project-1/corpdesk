package com.goodee.corpdesk.schedule.controller;

import com.goodee.corpdesk.schedule.dto.ReqPersonalScheduleDTO;
import com.goodee.corpdesk.schedule.dto.ResPersonalScheduleDTO;
import com.goodee.corpdesk.schedule.repository.PersonalScheduleRepository;
import com.goodee.corpdesk.schedule.service.PersonalScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/personal-schedule/**")
@Slf4j
public class PersonalScheduleController {

    @Value("${cat.schedule}")
    private String cat;

    @ModelAttribute("cat")
    public String getCat() {
        return cat;
    }

    @Autowired
    private PersonalScheduleService personalScheduleService;

    @GetMapping("list")
    public String list(ReqPersonalScheduleDTO reqPersonalScheduleDTO, Model model) {

        // username, useYn, (year, month)로 일정 데이터들 조회
        List<ResPersonalScheduleDTO> schedules = personalScheduleService.getPersonalSchedules(reqPersonalScheduleDTO);

        // yearRange 생성
        List<Integer> yearRange = personalScheduleService.getYearRangeByUsername(reqPersonalScheduleDTO.getUsername());

        model.addAttribute("schedules", schedules);
        model.addAttribute("yearRange", yearRange);
        model.addAttribute("selectedYear", reqPersonalScheduleDTO.getYear());
        model.addAttribute("selectedMonth", reqPersonalScheduleDTO.getMonth());

        return "schedule/list";
    }

    @PostMapping("")
    public void add(ReqPersonalScheduleDTO reqPersonalScheduleDTO) {

        System.err.println("/personal-schedule/add");

        ResPersonalScheduleDTO newSchedule = personalScheduleService.createSchedule(reqPersonalScheduleDTO);

        // redirect 상세정보 페이지
        // TODO 반환타입을 void->String으로 변경 후, 상세정보 페이지를 return하는 코드 추가

        log.warn("ResPersonalScheduleDTO:{}", newSchedule);

    }
}
