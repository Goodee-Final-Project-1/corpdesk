package com.goodee.corpdesk.approval.controller;

import com.goodee.corpdesk.approval.dto.ReqApprovalDTO;
import com.goodee.corpdesk.approval.dto.ResApprovalDTO;
import com.goodee.corpdesk.approval.service.ApprovalFormService;
import com.goodee.corpdesk.approval.service.ApprovalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
//@RestController // TODO postman을 사용하여 controller를 테스트하기 위해 임시로 붙임. 추후 @Controller로 수정
@RequestMapping(value = "/approval-form/**")
public class ApprovalFormController {
	
	@Value("${cat.approval}")
	private String cat;
	
	@ModelAttribute("cat")
	public String getCat() {
		return cat;
	}
	
	@Autowired
	private ApprovalFormService approvalFormService;
	
    // 결재 폼 조회
    @GetMapping("{formId}")
    public String getApproval(@PathVariable("formId") Long formId, @RequestParam("departmentId") Integer departmentId, Model model) throws Exception {

        System.err.println("getApproval()");

        // 1. 폼 정보 얻어오기
        ResApprovalDTO resApprovalDTO = approvalFormService.getApprovalForm(formId);
        
        // 2. model에 폼이랑 departmentId 바인딩
        resApprovalDTO.setDepartmentId(departmentId);
        model.addAttribute("res", resApprovalDTO);

        return "approval/detail";

    }
    
}
