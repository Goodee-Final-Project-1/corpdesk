package com.goodee.corpdesk.vacation.controller;

import com.goodee.corpdesk.approval.service.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/vacation/**")
public class VacationController {

    @GetMapping("list")
    public String list(@RequestParam("listType") String listType
                        , @RequestParam(value = "username", required = false) String username
                        , Model model) {

        // 유저의 휴가 신청 현황(status all)
//        approvalService.getAllVacationList(listType, username);

        // 전사 휴가 현황(status 승인)

        return "vacation/list";
    }

}
