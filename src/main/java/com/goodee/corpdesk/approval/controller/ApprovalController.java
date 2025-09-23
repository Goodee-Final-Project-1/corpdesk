package com.goodee.corpdesk.approval.controller;

import java.util.List;

import com.goodee.corpdesk.approval.dto.ResApprovalDTO;
import com.goodee.corpdesk.approval.entity.Approval;
import com.goodee.corpdesk.approval.service.ApprovalFormService;
import com.goodee.corpdesk.department.service.DepartmentService;
import com.goodee.corpdesk.employee.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.goodee.corpdesk.approval.dto.ApprovalDTO;
import com.goodee.corpdesk.approval.dto.ReqApprovalDTO;
import com.goodee.corpdesk.approval.service.ApprovalService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
//@RestController // TODO postman을 사용하여 controller를 테스트하기 위해 임시로 붙임. 추후 @Controller로 수정
@RequestMapping(value = "/approval/**")
public class ApprovalController {

    @Autowired
    private ApprovalService approvalService;
    @Autowired
    private ApprovalFormService approvalFormService;
    @Autowired
    private DepartmentService departmentService;

	@Value("${cat.approval}")
	private String cat;
    @Autowired
    private EmployeeService employeeService;

    @ModelAttribute("cat")
	public String getCat() {
		return cat;
	}

    // 결재 양식 데이터 뿌리기
    @ModelAttribute("formList")
    public List<ResApprovalDTO> getFormList() throws  Exception {
        return approvalFormService.getApprovalFormList();
    }

    // 부서 목록 데이터 뿌리기
    @ModelAttribute("departmentList")
    public List<ResApprovalDTO> getDepartmentList() throws  Exception {
        return departmentService.getApprovalFormList();
    }
	
	// 결재 요청 
	@PostMapping("")
	public String submit(ReqApprovalDTO reqApprovalDTO) throws Exception {
		
		System.err.println("submit()");
		
		// TODO 로그인 기능과 합칠 때, modifiedBy를 username으로 변경, username을 submit 메서드의 매개변수로 받아오도록 수정
		String modifiedBy = "test";
		ResApprovalDTO resApprovalDTO = approvalService.createApproval(reqApprovalDTO, modifiedBy);
		log.info("{}", resApprovalDTO);
		
		return "approval/list";
		
	}
	
	// 결재 요청 취소
	@DeleteMapping("{approvalId}")
    @ResponseBody
	public String cancel(@PathVariable("approvalId") Long approvalId) throws Exception {
		
		System.err.println("cancel()");
		
		String result = approvalService.deleteApproval(approvalId); // NOT_FOUND, DENIED, PROCESSED 중 하나의 값이 반환됨
		// TODO result 반환값에 따라 화면 표출 처리
		log.info("{}", result);
		
		return result;
		
	}
	
	// 결재 승인/반려
	// 1. approvalId, approverId를 사용해 결재와 결재자 데이터를 받아와 수정하는 방식의 메서드
	@PatchMapping("{approvalId}")
    @ResponseBody
	public String process(@PathVariable("approvalId") Long approvalId, @RequestBody ReqApprovalDTO reqApprovalDTO) throws Exception {
		
		System.err.println("process()");

        // reqApprovalDTO에는 approverId, approveYn가 있음
        log.warn("{}", reqApprovalDTO);
		String result = approvalService.processApproval(approvalId, reqApprovalDTO); // NOT_FOUND, PROCESSED 중 하나의 값이 반환됨
		log.info("{}", result);
		
		return result;
	}

	// TODO username은 나중에 인증정보에서 꺼내오는 것으로 수정
    // TODO 첨부파일 유무&갯수 정보도 끌고와서 화면에 뿌려주면 유용할듯
    // TODO DTO에 부서 이름도 담아서 화면에 뿌리도록 수정
    // TODO 기안일 내림차순으로 정렬해서 상위 10개씩만 화면에 뿌리도록 수정
	// 특정 결재 목록 조회
    @GetMapping("list")
    public String getApprovalList(@RequestParam(value = "listType", required = false) String listType, @RequestParam("username") String username, Model model) throws Exception {

        System.err.println("list()");

        // 2. 결재 목록 데이터 뿌리기
        if (listType == null || listType.equals("")) {
            List<ResApprovalDTO> tempList = approvalService.getApprovalList("temp", username); // list 혹은 null 반환
            List<ResApprovalDTO> reqList = approvalService.getApprovalList("request", username); // list 혹은 null 반환
            List<ResApprovalDTO> waitList = approvalService.getApprovalList("wait", username); // list 혹은 null 반환
            List<ResApprovalDTO> storList = approvalService.getApprovalList("storage", username); // list 혹은 null 반환

            log.info("{}", tempList);
            log.info("{}", reqList);
            log.info("{}", waitList);
            log.info("{}", storList);

            model.addAttribute("tempList", tempList);
            model.addAttribute("reqList", reqList);
            model.addAttribute("waitList", waitList);
            model.addAttribute("storList", storList);
        } else {
            List<ResApprovalDTO> result = approvalService.getAllApprovalList(listType, username); // list 혹은 null 반환
            log.info("{}", result);
            model.addAttribute(listType, result);
        }

        return "approval/list";

    }

    // 결재 상세 조회
    @GetMapping("{approvalId}")
    public String getApproval(@PathVariable("approvalId") Long approvalId, @RequestParam("username") String username, Model model) throws Exception {
//    public ResApprovalDTO getApproval(@PathVariable("approvalId") Long approvalId) throws Exception {

        System.err.println("getApproval()");

        ResApprovalDTO result = approvalService.getApproval(approvalId);
        ResApprovalDTO userInfo = approvalService.getDetail(username);
        // TODO username과 approvalId로 approverId를 가져오는 로직 추가
        ResApprovalDTO approverInfo = approvalService.getAppover(approvalId,  username);

        model.addAttribute("res", result);
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("approverInfo", approverInfo);

        return "approval/detail";

    }
    
}
