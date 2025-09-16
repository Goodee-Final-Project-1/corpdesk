package com.goodee.corpdesk.approval.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goodee.corpdesk.approval.dto.ApprovalDTO;
import com.goodee.corpdesk.approval.dto.ApproverDTO;
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
	public ApprovalDTO createApproval(ApprovalDTO approvalDTO, ArrayList<ApproverDTO> approverDTOList, String modifiedBy) throws Exception {
		System.err.println("approverDTOList1");
		
		// 1. 결재 내용에 insert
		Approval approval = approvalDTO.toEntity();
		approval.setModifiedBy(modifiedBy);
		approval = approvalRepository.save(approval);
		
		System.err.println("approverDTOList2");
		log.warn("{}", approval);
		
		// 2. 결재자에 insert
		// approval이 null이 아닐 때만 진행, null이면 롤백 (결재 요청 결과의 무결성을 지킴 & NPE 방지)
		if(approval == null) throw new NoResultException("Approval insert 실패"); // 예외가 터지면서 롤백됨
		
		log.warn("{}", approverDTOList);
		for (ApproverDTO approverDTO : approverDTOList) {
			System.err.println("approverDTOList3");
			
			Approver approver = approverDTO.toEntity();
			approver.setApprovalId(approval.getApprovalId());
			
			// 만약 승인 순서가 1이 아니면 use_yn값을 false로 하여 insert
			if(approver.getApprovalOrder() != 1) approver.setUseYn(false);
			
			approver = approverRepository.save(approver);
			System.err.println(approver);
			
			// approver이 null이 아닐 때만 다음 반복 진행, null이면 롤백 (결재 요청 결과의 무결성을 지킴)
			if(approver == null) throw new NoResultException("Approver insert 실패"); // 예외가 터지면서 롤백됨
		}
		
		// 3. insert 결과로 얻어진 approvalId를 DTO에 담아 반환
		approvalDTO.setApprovalId(approval.getApprovalId());
		
		return approvalDTO;
		
	}
	 
	// approvalId에 해당하는 Approval이 없다면 false 반환,
	// 있다면 approval과 approver 수정한 후 true 반환
	public boolean deleteApproval(Long approvalId) throws Exception {
		
		// 1. 결재 use_yn false로 update
		// id로 결재 정보를 조회해 옴
		Optional<Approval> result = approvalRepository.findById(approvalId);
		
		// 삭제할 결재 정보가 없으면 이후의 삭제 로직을 수행할 필요 없으므로 바로 return
		if (result == null) return false; 
		
		// 삭제할 결재 정보가 있으면 삭제처리
		Approval approval = result.get();
		approval.setUseYn(false);
		
		// 2. 결재자들 use_yn false로 update
		List<Approver> approverList = approverRepository.findAllByApprovalId(approvalId);
		for (Approver approver : approverList) {
			approver.setUseYn(false);
		}
		
		return true;
	}
	
	public boolean processApproval(Long approvalId, Long approverId, String approveYn) throws Exception {
		// 해당 결재의 결재자 정보의 approve_yn 정보를 approveYn값으로 수정한 다음, 
		// 그 다음 승인 순서인 결재자 정보(만약 있다면)의 use_yn값을 true로 수정
		
		// 1. 해당 결재의 결재자 정보의 approve_yn 정보를 approveYn값으로 수정
		// 결재자 정보 중 approval_id = approvalId 이고 approver_id = approverId 인 정보 select
		Approver result = approverRepository.findByApprovalIdAndApproverId(approvalId, approverId);
		
		// 수정할 결재자 정보가 없으면 이후의 로직을 수행할 필요 없으므로 바로 return
		if (result == null) return false;
		
		// 수정할 결재자 정보가 있으면 수정
		result.setApproveYn(approveYn.charAt(0));
		
		// 2. 그 다음 승인 순서인 결재자 정보(만약 있다면)의 use_yn값을 true로 수정
		Approver nextApprover = approverRepository.findByApprovalIdAndApprovalOrder(approvalId, result.getApprovalOrder() + 1);
		
		// 수정할 결재자 정보가 없으면 이후의 로직을 수행할 필요 없으므로 바로 return
		if (nextApprover == null) return false;
		
		// 수정할 결재자 정보가 있으면 수정
		result.setUseYn(true);
		
		return false;
	}
	
}
