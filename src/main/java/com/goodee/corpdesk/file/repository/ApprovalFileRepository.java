package com.goodee.corpdesk.file.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goodee.corpdesk.file.entity.ApprovalFile;

public interface ApprovalFileRepository extends JpaRepository<ApprovalFile, Long> {

}
