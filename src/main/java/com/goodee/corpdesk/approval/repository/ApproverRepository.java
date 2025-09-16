package com.goodee.corpdesk.approval.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goodee.corpdesk.approval.entity.Approver;

public interface ApproverRepository extends JpaRepository<Approver, Long> {
	public List<Approver> findAllByApprovalId(Long approvalId);
}
