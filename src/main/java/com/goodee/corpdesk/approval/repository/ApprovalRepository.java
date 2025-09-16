package com.goodee.corpdesk.approval.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goodee.corpdesk.approval.entity.Approval;

public interface ApprovalRepository extends JpaRepository<Approval, Long> {

}
