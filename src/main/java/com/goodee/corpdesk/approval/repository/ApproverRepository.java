package com.goodee.corpdesk.approval.repository;

import java.util.List;

import com.goodee.corpdesk.approval.dto.ResApprovalDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import com.goodee.corpdesk.approval.entity.Approver;

public interface ApproverRepository extends JpaRepository<Approver, Long> {
	
	List<Approver> findAllByApprovalId(Long approvalId);
	Approver findByApprovalIdAndApproverId(Long approvalId, Long approverId);
	Approver findByApprovalIdAndApprovalOrder(Long approvalId, Integer approvalOrder);

    Approver findAllByApprovalIdAndUsername(Long approvalId, String username);
}
