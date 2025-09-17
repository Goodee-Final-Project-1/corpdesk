package com.goodee.corpdesk.approval.repository;

import com.goodee.corpdesk.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import com.goodee.corpdesk.approval.entity.Approval;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;

public interface ApprovalRepository extends JpaRepository<Approval, Long> {
    ArrayList<Approval> findAllByUseYnAndUsername(Boolean useYn, String username);

    @Query(value =
            "SELECT a2.* " +
            "FROM (SELECT a.* FROM Approver a WHERE a.username = :username) a " +
            "JOIN Approval a2 " +
            "WHERE " +
            "a.approval_id = a2.approval_id AND a2.use_yn = :useYn"
            ,nativeQuery = true)
    ArrayList<Approval> findAllByUseYnAndApproverUsername(@Param("useYn") Boolean useYn, @Param("username") String username);
}
