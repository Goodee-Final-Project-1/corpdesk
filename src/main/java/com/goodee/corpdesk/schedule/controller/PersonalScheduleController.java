package com.goodee.corpdesk.schedule.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/personal-schedule/**")
public class PersonalScheduleController {

    @Value("${cat.schedule}")
    private String cat;

    @ModelAttribute("cat")
    public String getCat() {
        return cat;
    }

    @GetMapping("list")
    public String list() {
        return "schedule/list";
    }

}
