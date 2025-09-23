package com.goodee.corpdesk.approval.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.goodee.corpdesk.approval.dto.ResApprovalDTO;
import com.goodee.corpdesk.approval.entity.ApprovalForm;
import com.goodee.corpdesk.approval.repository.ApprovalFormRepository;
import com.goodee.corpdesk.department.entity.Department;
import com.goodee.corpdesk.department.repository.DepartmentRepository;
import com.goodee.corpdesk.employee.Employee;
import com.goodee.corpdesk.employee.EmployeeRepository;
import com.goodee.corpdesk.employee.ResEmployeeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goodee.corpdesk.approval.dto.ApprovalDTO;
import com.goodee.corpdesk.approval.dto.ApproverDTO;
import com.goodee.corpdesk.approval.dto.ReqApprovalDTO;
import com.goodee.corpdesk.approval.entity.Approval;
import com.goodee.corpdesk.approval.entity.Approver;
import com.goodee.corpdesk.approval.repository.ApprovalRepository;
import com.goodee.corpdesk.approval.repository.ApproverRepository;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class ApprovalService {
	
	@Autowired
	private ApprovalRepository approvalRepository;
	@Autowired
	private ApproverRepository approverRepository;
    @Autowired
    private ApprovalFormRepository approvalFormRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    // 반환값 종류
    // ResApprovalDTO: approval 혹은 approval과 approver insert 성공, approval의 정보만 반환
	// Exception: approval 혹은 approval의 조회 결과가 없거나 insert 실패
	public ResApprovalDTO createApproval(ReqApprovalDTO reqApprovalDTO, String modifiedBy) throws Exception {
		
		// 1. 결재 내용에 insert
		Approval approval = reqApprovalDTO.toApprovalEntity();
		approval.setModifiedBy(modifiedBy);
		approval = approvalRepository.save(approval); // 조회 결과가 없다면 예외가 터지고 롤백됨

        ResApprovalDTO resApprovalDTO = approval.toResApprovalDTO();

		// 2. 결재자에 insert
		log.warn("{}", reqApprovalDTO.getApproverDTOList());

        // 결재자 정보가 없다면 바로 return
        if (reqApprovalDTO.getApproverDTOList() == null || reqApprovalDTO.getApproverDTOList().isEmpty()) return resApprovalDTO;

		for (ApproverDTO approverDTO : reqApprovalDTO.getApproverDTOList()) {
			Approver approver = approverDTO.toEntity();
			approver.setApprovalId(approval.getApprovalId());
			approver.setModifiedBy(modifiedBy);
			
			// 만약 승인 순서가 1이 아니면 use_yn값을 false로 하여 insert
			if(approver.getApprovalOrder() != 1) approver.setUseYn(false);

			approver = approverRepository.save(approver); // 조회 결과가 없다면 예외가 터지고 롤백됨
		}
		
		// 3. DTO 반환 (approverDTOList()는 null인 채로 반환됨)
        return resApprovalDTO;
		
	}
	 
	// 반환값 종류
    // DENIED: 결재가 반려 처리됨
    // PROCESSED: 결재가 승인됨
    // NOT_FOUND: approvalId에 해당하는 Approval이 없음
    // Exception: update 실패
	public String deleteApproval(Long approvalId) throws Exception {
		
		// 1. 결재 use_yn false로 update
		Optional<Approval> result = approvalRepository.findById(approvalId); // id로 결재 정보를 조회해 옴

        // 삭제할 결재 정보가 없으면 이후의 삭제 로직을 수행할 필요 없으므로 바로 return
		if (result.isEmpty()) return "NOT_FOUND";
		
		// 삭제할 결재 정보가 있으면 삭제 로직 진행...
		// 결재가 완료되었는가? (결재 상태가 y/n인가?)
		// -> true -> 결재 취소 거부
		// -> false -> 결재 취소 진행
		Approval approval = result.get();
		if (approval.getStatus() == 'Y' || approval.getStatus() == 'N') return "DENIED";
		else approval.setUseYn(false);
		
		// 2. 결재자들 use_yn false로 update
		List<Approver> approverList = approverRepository.findAllByApprovalId(approvalId);
        approverList.forEach(approver -> approver.setUseYn(false));
		
		return "PROCESSED";
	} 
	
    // 해당 결재의 결재자 정보의 approve_yn 정보를 approveYn값으로 수정한 다음,
    // 그 다음 승인 순서인 결재자 정보(만약 있다면)의 use_yn값을 true로 수정
    // 반환값 종류
    // NOT_FOUND: 수정할 결재자 정보 혹은 수정할 결재 정보가 없음
    // PROCESSED: 결재 반려/승인이 처리됨
    // Exception: 그 외 예외상황
	public String processApproval(Long approvalId, ReqApprovalDTO reqApprovalDTO) throws Exception {
        
        // 0. DTO에서 approverId, approveYn를 받아옴
        log.warn("{}", reqApprovalDTO);
        Long approverId = reqApprovalDTO.getApproverId();
        System.err.println("approverId = " + approverId);
        String approveYn = reqApprovalDTO.getApproveYn().toString();
        System.err.println("approveYn = " + approveYn);

        // 1. 해당 결재의 결재자 정보의 approve_yn 정보를 approveYn값으로 수정
		// 결재자 정보 중 approval_id = approvalId 이고 approver_id = approverId 인 정보 select
		Approver result = approverRepository.findByApprovalIdAndApproverId(approvalId, approverId);
		
		// 수정할 결재자 정보가 없으면 이후의 로직을 수행할 필요 없으므로 바로 return
		log.warn("1: {}", result);
		if (result == null) return "NOT_FOUND";
		
		// 수정할 결재자 정보가 있으면 수정
		result.setApproveYn(approveYn.charAt(0));
		
		// 결재자가 결재를 반려했다면(approveYn = n) 결재 상태의 값을 반려로 수정
		if(approveYn.equalsIgnoreCase("N")) {
			// 결재 정보 조회
			Optional<Approval> result2 = approvalRepository.findById(approvalId);
			log.warn("2: {}", result2);
			
			// approval이 null이 아닐 때만 다음 로직 진행
			if(result2.isEmpty()) return "NOT_FOUND";
			Approval approval = result2.get();
			
			// 결재 상태 수정
			approval.setStatus('N');
		}
		// 결재자가 승인을 했다면 (approveYn = y) 아래 2번 로직 진행
		if(approveYn.equalsIgnoreCase("Y")) {
			// 2. 다음 승인자가 있는가?
			// false -> 결재 상태의 값을 승인으로 수정
			// true -> 결재 상태 수정없음(대기), 그 다음 승인 순서인 결재자 정보(만약 있다면)의 use_yn값을 true로 수정
			
			// 그 다음 승인 순서인 결재자 정보 조회
			Approver nextApprover = approverRepository.findByApprovalIdAndApprovalOrder(approvalId, result.getApprovalOrder() + 1);
			log.warn("3: {}", nextApprover);
			
			// 수정할 결재자 정보가 없으면 결재 상태의 값을 승인으로&use_yn=true로 수정하고 return
			if (nextApprover == null) {
				// 결재 정보 조회
				Optional<Approval> result2 = approvalRepository.findById(approvalId);
				
				// approval이 null이 아닐 때만 다음 로직 진행
				if(result2.isEmpty()) return "NOT_FOUND";
				Approval approval = result2.get();
				
				// 결재 상태 수정
				approval.setStatus('Y');
			} else {
				// 수정할 결재자 정보가 있으면 결재 상태의 값을 수정하지 않고 그 다음 승인 순서인 결재자 정보(만약 있다면)의 use_yn값을 true로 수정
				nextApprover.setUseYn(true);
				System.err.println(nextApprover);
			}
			
		}
		
		return "PROCESSED";
	}
    
    public List<ResApprovalDTO> getAllApprovalList(String listType, String username) throws Exception {

        List<ResApprovalDTO> result = new ArrayList<>();
        switch (listType) {
            case "temp" -> result = approvalRepository.findApprovalSummaryByStatus(true, username, List.of("T")); // 내 결재 임시 보관함
            case "request" -> result = approvalRepository.findApprovalSummaryByStatus(true, username, List.of("W", "N", "Y")); // 내 결재 요청 목록
            case "wait" -> result = approvalRepository.findSummaryByApproverWithApproveYnAndStatus(true, username, List.of("W"), List.of("W")); // 결재 대기 목록
            case "storage" -> result = approvalRepository.findSummaryByApproverWithApproveYnAndStatus(true, username, List.of("Y", "N"), List.of("W", "Y", "N")); // 내가 결재한 목록
        }

        return result;

    }

    public List<ResApprovalDTO> getApprovalList(String listType, String username) throws Exception {

        List<ResApprovalDTO> result = new ArrayList<>();
        switch (listType) {
            case "temp" -> result = approvalRepository.findApprovalSummaryByStatus(
                                                    true, username, List.of("T"), 10L
                                                        ); // 내 결재 임시 보관함
            case "request" -> result = approvalRepository.findApprovalSummaryByStatus(
                                                    true, username, List.of("W", "N", "Y"), 10L
                                                        ); // 내 결재 요청 목록
            case "wait" -> result = approvalRepository.findSummaryByApproverWithApproveYnAndStatus(
                                                    true, username, List.of("W"), List.of("W"), 10L
                                                        ); // 결재 대기 목록
            case "storage" -> result = approvalRepository.findSummaryByApproverWithApproveYnAndStatus(
                                                    true, username, List.of("Y", "N"), List.of("W", "Y", "N"), 10L
                                                        ); // 내가 결재한 목록
        }

        return result;

    }

    public ResApprovalDTO getApproval(Long approvalId) throws Exception {
        // 1. 결재 + 결재 양식 + 부서 + 기안자 조회
        // 결재 조회
        Optional<Approval> result = approvalRepository.findById(approvalId);

        if(result.isEmpty())  return null;

        Approval approval = result.get();
        ResApprovalDTO resApprovalDTO = approval.toResApprovalDTO();

        // 결재 양식 조회
        ApprovalForm result3 = approvalFormRepository.findById(approval.getApprovalFormId()).get();
        resApprovalDTO.setApprovalFormId(result3.getApprovalFormId());
        resApprovalDTO.setFormTitle(result3.getFormTitle());

        // 부서 조회
        Department result4 = departmentRepository.findById(approval.getDepartmentId()).get();
        resApprovalDTO.setDepartmentName(result4.getDepartmentName());

        // 기안자 조회
        Employee result5 = employeeRepository.findById(approval.getUsername()).get();
        resApprovalDTO.setName(result5.getName());

        // 2. 결재자 조회
        List<ApproverDTO> approverDTOList = approverRepository.findByApprovalIdWithEmployeeAndDepartment(approvalId);

        if(approverDTOList.isEmpty())  return resApprovalDTO; // 결재자가 없으면? approval만 DTO에 담아서 return

        // 3. 결재와 결재자를 DTO에 담아 반환
        resApprovalDTO.setApproverDTOList(approverDTOList);

        return resApprovalDTO;
    }

    public ResEmployeeDTO getDetailWithDeptAndPosition(String username) {
        return employeeRepository.findEmployeeWithDeptAndPosition(username);
    }

    public List<ResApprovalDTO> getEmployeeWithDeptAndPositionAndFile(Integer departmentId, Boolean useYn) {
        return employeeRepository.findEmployeeWithDeptAndPositionAndFile(departmentId, useYn);
    }

    public ResApprovalDTO getDetail(String username) {
        return employeeRepository.findByUsername(username).get().toResApprovalDTO();
    }

    public ResApprovalDTO getAppover(Long approvalId, String username) {
        Approver approver = approverRepository.findAllByApprovalIdAndUsername(approvalId, username);

        if(approver == null)  return null;

        return approver.toResApprovalDTO();
    }
}
