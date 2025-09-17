package com.goodee.corpdesk.approval.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goodee.corpdesk.approval.dto.ApprovalDTO;
import com.goodee.corpdesk.approval.dto.ApproverDTO;
import com.goodee.corpdesk.approval.dto.RequestApprovalDTO;
import com.goodee.corpdesk.approval.service.ApprovalService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
//@Controller
@RestController // TODO postman을 사용하여 controller를 테스트하기 위해 임시로 붙임. 추후 @Controller로 수정
@RequestMapping(value = "/approvals/**")
public class ApprovalController {
	
	@Autowired
	private ApprovalService approvalService;
	
	// 결재 요청 
	@PostMapping("")
	public String submit(RequestApprovalDTO requestApprovalDTO) throws Exception {
		
		System.err.println("submit()");
		
		// TODO 로그인 기능과 합칠 때, modifiedBy를 username으로 변경, username을 submit 메서드의 매개변수로 받아오도록 수정
		String modifiedBy = "test";
		ApprovalDTO approvalDTO = approvalService.createApproval(requestApprovalDTO, modifiedBy);
		log.info("{}", approvalDTO);
		
		return approvalDTO.toString();
		
	}
	
	// 결재 요청 취소
	@DeleteMapping("{approvalId}")
	public String cancel(@PathVariable("approvalId") Long approvalId) throws Exception {
		
		System.err.println("cancel()");
		
		String result = approvalService.deleteApproval(approvalId); // NOT_FOUND, DENIED, PROCESSED 중 하나의 값이 반환됨
		// TODO result 반환값에 따라 화면 표출 처리
		log.info("{}", result);
		
		return result.toString();
		
	}
	
	// 결재 승인/반려
	// 1. approvalId, approverId를 사용해 결재와 결재자 데이터를 받아와 수정하는 방식의 메서드
	@PutMapping("{approvalId}/{approverId}/{approveYn}")
	public String process(@PathVariable("approvalId") Long approvalId, @PathVariable("approverId") Long approverId, @PathVariable("approveYn") String approveYn) throws Exception {
		
		System.err.println("process()");
		
		String result = approvalService.processApproval(approvalId, approverId, approveYn); // NOT_FOUND, PROCESSED 중 하나의 값이 반환됨
		log.info("{}", result);
		
		return result.toString();
	}
	
	// 2. approvalId, employeeId를 사용해 결재와 결재자 데이터를 받아와 수정하는 방식의 메서드
	/*
	@PutMapping("{approvalId}/{approveYn}")
	public String process(@PathVariable("approvalId") Long approvalId, @PathVariable("approveYn") String approveYn) throws Exception {
		
		System.err.println("process()");
		
		// TODO
		// TODO 로그인 기능과 합칠 때, employeeId를 username으로 변경, username을 process 메서드의 매개변수로 받아오도록 수정
		String employeeId = "3";
		Boolean result = approvalService.processApproval(approvalId, employeeId, approveYn);
		log.info("{}", result);
		
		return result.toString();
	}
	*/
	
}
