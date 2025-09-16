package com.goodee.corpdesk.approval.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goodee.corpdesk.approval.dto.ApprovalDTO;
import com.goodee.corpdesk.approval.dto.ApproverDTO;
import com.goodee.corpdesk.approval.service.ApprovalService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Slf4j
@Controller
@RestController // TODO postman을 사용하여 controller를 테스트하기 위해 임시로 붙임. 추후 @Controller로 수정
@RequestMapping(value = "/approvals/**")
public class ApprovalController {
	
	@Autowired
	private ApprovalService approvalService;
	
	// 결재 요청 
	@PostMapping("")
	public String submit(ApprovalDTO approvalDTO, ArrayList<ApproverDTO> approverDTOList) throws Exception {
		
		System.err.println("submit()");
		
		// TODO 로그인 기능과 합칠 때, modifiedBy를 username으로 변경, username을 submit 메서드의 매개변수로 받아오도록 수정
		String modifiedBy = "test";
		approvalDTO = approvalService.createApproval(approvalDTO, approverDTOList, modifiedBy);
		log.info("{}", approvalDTO);
		
		return approvalDTO.toString();
		
	}
	
	// 결재 요청 취소
	@DeleteMapping("{approvalId}")
	public String cancel(@PathVariable("approvalId") Long approvalId) throws Exception {
		
		System.err.println("cancel()");
		
		Boolean result = approvalService.deleteApproval(approvalId);
		log.info("{}", result);
		
		return result.toString();
		
	}
	
	// 결재 승인/반려
	@PutMapping("")
	public void process() {
		
		System.err.println("process()");
		// TODO
		
	}
	
}
