package com.goodee.corpdesk.vacation.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vacation/**")
public class VacationController {

    @GetMapping("list")
    public String list() {
        return "vacation/list";
    }

}
