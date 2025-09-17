package com.goodee.corpdesk.approval.controller;

import java.util.List;

import com.goodee.corpdesk.approval.dto.ResApprovalDTO;
import com.goodee.corpdesk.approval.entity.Approval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.goodee.corpdesk.approval.dto.ApprovalDTO;
import com.goodee.corpdesk.approval.dto.ReqApprovalDTO;
import com.goodee.corpdesk.approval.service.ApprovalService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Controller
@RestController // TODO postman을 사용하여 controller를 테스트하기 위해 임시로 붙임. 추후 @Controller로 수정
@RequestMapping(value = "/approvals/**")
public class ApprovalController {
	
	@Autowired
	private ApprovalService approvalService;
	
	// 결재 요청 
	@PostMapping("")
	public String submit(ReqApprovalDTO reqApprovalDTO) throws Exception {
		
		System.err.println("submit()");
		
		// TODO 로그인 기능과 합칠 때, modifiedBy를 username으로 변경, username을 submit 메서드의 매개변수로 받아오도록 수정
		String modifiedBy = "test";
		ResApprovalDTO resApprovalDTO = approvalService.createApproval(reqApprovalDTO, modifiedBy);
		log.info("{}", resApprovalDTO);
		
		return resApprovalDTO.toString();
		
	}
	
	// 결재 요청 취소
	@DeleteMapping("{approvalId}")
	public String cancel(@PathVariable("approvalId") Long approvalId) throws Exception {
		
		System.err.println("cancel()");
		
		String result = approvalService.deleteApproval(approvalId); // NOT_FOUND, DENIED, PROCESSED 중 하나의 값이 반환됨
		// TODO result 반환값에 따라 화면 표출 처리
		log.info("{}", result);
		
		return result;
		
	}
	
	// 결재 승인/반려
	// 1. approvalId, approverId를 사용해 결재와 결재자 데이터를 받아와 수정하는 방식의 메서드
	@PutMapping("{approvalId}/{approverId}/{approveYn}")
	public String process(@PathVariable("approvalId") Long approvalId, @PathVariable("approverId") Long approverId, @PathVariable("approveYn") String approveYn) throws Exception {
		
		System.err.println("process()");
		
		String result = approvalService.processApproval(approvalId, approverId, approveYn); // NOT_FOUND, PROCESSED 중 하나의 값이 반환됨
		log.info("{}", result);
		
		return result;
	}

	// 특정 결재 목록 조회
    @GetMapping("list/{listType}/{username}")
    public List<ResApprovalDTO> getApprovalList(@PathVariable("listType") String listType, @PathVariable("username") String username) throws Exception {

        System.err.println("list()");

        List<ResApprovalDTO> result = approvalService.getApprovalList(listType, username); // list 혹은 null 반환
        log.info("{}", result);

        return result;

    }

    // 모든 결재 목록 조회
    @GetMapping("list/{username}")
    public List<ResApprovalDTO> getApprovalList(@PathVariable("username") String username) throws Exception {

        System.err.println("list()");

        List<ResApprovalDTO> result = approvalService.getApprovalList("", username); // list 혹은 null 반환
        log.info("{}", result);

        return result;

    }

    // 결재 상세 조회
    @GetMapping("{approvalId}")
    public ResApprovalDTO getApproval(@PathVariable("approvalId") Long approvalId) throws Exception {

        System.err.println("getApproval()");

        ResApprovalDTO result = approvalService.getApproval(approvalId);
        log.info("{}", result);

        return result;

    }
	
}
