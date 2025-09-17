package com.goodee.corpdesk.approval.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goodee.corpdesk.approval.dto.ApprovalDTO;
import com.goodee.corpdesk.approval.dto.ApproverDTO;
import com.goodee.corpdesk.approval.dto.RequestApprovalDTO;
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
	
	// approval과 approver insert에 성공하면 insert한 Approval의 정보가 담기 ApprovalDTO 반환, 
	// 아니면 exception을 터뜨려서 rollback
	public ApprovalDTO createApproval(RequestApprovalDTO requestApprovalDTO, String modifiedBy) throws Exception {
		
		// 1. 결재 내용에 insert
		Approval approval = requestApprovalDTO.toApprovalEntity();
		approval.setModifiedBy(modifiedBy);
		approval = approvalRepository.save(approval);
		
		// 2. 결재자에 insert
		// approval이 null이 아닐 때만 진행, null이면 롤백 (결재 요청 결과의 무결성을 지킴 & NPE 방지)
		if(approval == null) throw new NoResultException("Approval insert 실패"); // 예외가 터지면서 롤백됨
		
		log.warn("{}", requestApprovalDTO.getApproverDTOList());
		for (ApproverDTO approverDTO : requestApprovalDTO.getApproverDTOList()) {
			
			Approver approver = approverDTO.toEntity();
			approver.setApprovalId(approval.getApprovalId());
			approver.setModifiedBy(modifiedBy);
			
			// 만약 승인 순서가 1이 아니면 use_yn값을 false로 하여 insert
			if(approver.getApprovalOrder() != 1) approver.setUseYn(false);
			
			approver = approverRepository.save(approver);
			
			// approver이 null이 아닐 때만 다음 반복 진행, null이면 롤백 (결재 요청 결과의 무결성을 지킴)
			if(approver == null) throw new NoResultException("Approver insert 실패"); // 예외가 터지면서 롤백됨
		}
		
		// 3. insert 결과로 얻어진 approvalId를 DTO에 담아 반환
		ApprovalDTO approvalDTO = new ApprovalDTO();
		approvalDTO.setApprovalId(approval.getApprovalId());
		
		return approvalDTO;
		
	}
	 
	// approvalId에 해당하는 Approval이 없다면 false 반환,
	// 있다면 approval과 approver 수정한 후 true 반환
	public String deleteApproval(Long approvalId) throws Exception {
		
		// 1. 결재 use_yn false로 update
		// id로 결재 정보를 조회해 옴
		Optional<Approval> result = approvalRepository.findById(approvalId);
		
		// 삭제할 결재 정보가 없으면 이후의 삭제 로직을 수행할 필요 없으므로 바로 return
		if (result == null) return "NOT_FOUND"; 
		
		// 삭제할 결재 정보가 있으면 삭제 로직 진행
		Approval approval = result.get();
		
		// 결재가 완료되었는가? (결재 상태가 y/n인가?)
		// -> true -> 결재 취소 거부
		// -> false -> 결재 취소 진행
		if (approval.getStatus() == 'y' || approval.getStatus() == 'n') return "DENIED"; 
		else approval.setUseYn(false);
		
		// 2. 결재자들 use_yn false로 update
		List<Approver> approverList = approverRepository.findAllByApprovalId(approvalId);
		for (Approver approver : approverList) {
			approver.setUseYn(false);
		}
		
		return "PROCESSED";
	} 
	
	public String processApproval(Long approvalId, Long approverId, String approveYn) throws Exception {
		// 해당 결재의 결재자 정보의 approve_yn 정보를 approveYn값으로 수정한 다음, 
		// 그 다음 승인 순서인 결재자 정보(만약 있다면)의 use_yn값을 true로 수정
		
		// 1. 해당 결재의 결재자 정보의 approve_yn 정보를 approveYn값으로 수정
		// 결재자 정보 중 approval_id = approvalId 이고 approver_id = approverId 인 정보 select
		Approver result = approverRepository.findByApprovalIdAndApproverId(approvalId, approverId);
		
		// 수정할 결재자 정보가 없으면 이후의 로직을 수행할 필요 없으므로 바로 return
		log.warn("1: {}", result);
		if (result == null) return "NOT_FOUND";
		
		// 수정할 결재자 정보가 있으면 수정
		result.setApproveYn(approveYn.charAt(0));
		
		// 결재자가 결재를 반려했다면 (approveYn = n) 결재 상태의 값을 반려로 수정
		if(approveYn.equalsIgnoreCase("n")) {
			// 결재 정보 조회
			Optional<Approval> result2 = approvalRepository.findById(approvalId);
			log.warn("2: {}", result2);
			
			// approval이 null이 아닐 때만 다음 로직 진행
			if(result2 == null) return "NOT_FOUND";
			Approval approval = result2.get();
			
			// 결재 상태 수정
			approval.setStatus('n');
		}
		// 결재자가 승인을 했다면 (approveYn = y) 아래 2번 로직 진행
		if(approveYn.equalsIgnoreCase("y")) {
			// 2. 다음 승인자가 있는가?
			// false -> 결재 상태의 값을 승인으로 수정
			// true -> 결재 상태 수정없음(대기), 그 다음 승인 순서인 결재자 정보(만약 있다면)의 use_yn값을 true로 수정
			
			// 그 다음 승인 순서인 결재자 정보 조회
			Approver nextApprover = approverRepository.findByApprovalIdAndApprovalOrder(approvalId, result.getApprovalOrder() + 1);
			log.warn("7: {}", nextApprover);
			
			// 수정할 결재자 정보가 없으면 결재 상태의 값을 승인으로&use_yn=true로 수정하고 return
			if (nextApprover == null) {
				// 결재 정보 조회
				Optional<Approval> result2 = approvalRepository.findById(approvalId);
				
				// approval이 null이 아닐 때만 다음 로직 진행
				if(result2 == null) return "NOT_FOUND";
				Approval approval = result2.get();
				
				// 결재 상태 수정
				approval.setStatus('y');
			} else {
				// 수정할 결재자 정보가 있으면 결재 상태의 값을 수정하지 않고 그 다음 승인 순서인 결재자 정보(만약 있다면)의 use_yn값을 true로 수정
				nextApprover.setUseYn(true);
				System.err.println(nextApprover);
			}
			
		}
		
		return "PROCESSED";
	}
	
	/*
	public boolean processApproval(Long approvalId, String employeeId, String approveYn) throws Exception {
		// 해당 결재의 결재자 정보의 approve_yn 정보를 approveYn값으로 수정한 다음, 
		// 그 다음 승인 순서인 결재자 정보(만약 있다면)의 use_yn값을 true로 수정
		
		// 1. 해당 결재의 결재자 정보의 approve_yn 정보를 approveYn값으로 수정
		// 결재자 정보 중 approval_id = approvalId 이고 approver_id = approverId 인 정보 select
		Approver result = approverRepository.findByApprovalIdAndApproverId(approvalId, approverId);
		
		// 수정할 결재자 정보가 없으면 이후의 로직을 수행할 필요 없으므로 바로 return
		log.warn("1: {}", result);
		if (result == null) return false;
		log.warn("2");
		
		// 수정할 결재자 정보가 있으면 수정
		log.warn("3");
		result.setApproveYn(approveYn.charAt(0));
		log.warn("4");
		
		// 2. 그 다음 승인 순서인 결재자 정보(만약 있다면)의 use_yn값을 true로 수정
		log.warn("5");
		Approver nextApprover = approverRepository.findByApprovalIdAndApprovalOrder(approvalId, result.getApprovalOrder() + 1);
		log.warn("6");
		
		// 수정할 결재자 정보가 없으면 이후의 로직을 수행할 필요 없으므로 바로 return
		log.warn("7: {}", nextApprover);
		if (nextApprover == null) return true;
		log.warn("8");
		
		// 수정할 결재자 정보가 있으면 수정
		log.warn("9");
		result.setUseYn(true);
		log.warn("10");
		
		return true;
	}
	*/
    
    // TODO 엔티티 리스트를 반환하는 것에서 DTO 리스트를 반환하는 것으로 변경
    public List<Approval> getApprovalList(String listType, String username) throws Exception {

        List<Approval> result = null;
        switch (listType) {
            case "request" -> result = approvalRepository.findAllByUseYnAndUsername(true, username);
            case "wait" -> result = approvalRepository.findAllByUseYnAndApproverUsername(true, username);
            default -> {
                result = new ArrayList<>();
                result.addAll(approvalRepository.findAllByUseYnAndUsername(true, username));
                result.addAll(approvalRepository.findAllByUseYnAndApproverUsername(true, username));
                log.warn("{}", result);
            }
        }

        return result;

    }

}
