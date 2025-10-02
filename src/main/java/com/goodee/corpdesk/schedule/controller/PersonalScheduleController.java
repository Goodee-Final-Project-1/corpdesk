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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

//    @ModelAttribute("todaySchedules")
//    public List<ResPersonalScheduleDTO> getTodaySchedules(
//        @RequestParam(value = "username", required = false) String username) {
//
//        String finalUsername;
//
//        // 💡 테스트를 위해, username 파라미터가 없으면 'jung_frontend'로 대체
//        if (username == null || username.isEmpty()) {
//            finalUsername = "jung_frontend";
//        } else {
//            finalUsername = username;
//        }
//
//        // 오늘의 일정을 구하는 service 로직 호출
//        // List<ResPersonalScheduleDTO> todaySchedules = personalScheduleService.getTodaySchedules(finalUsername);
//
//        // TODO: 실제 서비스 로직으로 변경
//        // return todaySchedules;
//        return Collections.emptyList(); // 임시 반환
//    }

    @Autowired
    private PersonalScheduleService personalScheduleService;

    @GetMapping("list")
    public String list(ReqPersonalScheduleDTO reqPersonalScheduleDTO, Model model) {

        // username, useYn, (year, month)로 일정 데이터들 조회
        List<ResPersonalScheduleDTO> schedules = personalScheduleService.getSchedules(reqPersonalScheduleDTO);

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

        ResPersonalScheduleDTO newSchedule = personalScheduleService.createSchedule(reqPersonalScheduleDTO);

        // redirect 상세정보 페이지
        // TODO 반환타입을 void->String으로 변경 후, 상세정보 페이지를 return하는 코드 추가

        log.warn("ResPersonalScheduleDTO:{}", newSchedule);

    }

//    @GetMapping("{personalScheduleId}")
//    public String detail(@PathVariable("personalScheduleId") Long personalScheduleId, Model model) {
//
//        // id로 상세정보 조회해옴
//        ResPersonalScheduleDTO schedule = personalScheduleService.getScheduleById(personalScheduleId);
//
//        model.addAttribute("schedule", schedule);
//
//        return "schedule/detail";
//    }
//
//    @GetMapping("{personalScheduleId}/edit")
//    public String edit(@PathVariable("personalScheduleId") Long personalScheduleId, Model model) {
//
//        // id로 조회
//        ResPersonalScheduleDTO schedule = personalScheduleService.getScheduleById(personalScheduleId);
//
//        // 조회해온 데이터를 model에 바인딩한 후 수정 폼으로 이동
//        model.addAttribute("schedule", schedule);
//
//        return "schedule/edit";
//
//    }
//
//    @PutMapping("{personalScheduleId}")
//    public String update(@PathVariable("personalScheduleId") Long personalScheduleId
//                        , ReqPersonalScheduleDTO reqPersonalScheduleDTO) {
//
//        // service의 수정 로직 (id로 조회 -> save)
//        personalScheduleService.updateSchedule(personalScheduleId, reqPersonalScheduleDTO);
//
//        // 상세정보 페이지로 redirect
//        return "redirect:/personal-schedule/" + personalScheduleId;
//
//    }
//
//    @DeleteMapping("{personalScheduleId}")
//    public String delete(@PathVariable("personalScheduleId") Long personalScheduleId) {
//
//        // service의 삭제 로직
//        personalScheduleService.deleteSchedule(personalScheduleId);
//
//        // list 페이지로 redirect
//        return "redirect:/personal-schedule/list";
//
//    }
}
