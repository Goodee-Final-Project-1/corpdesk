package com.goodee.corpdesk.approval.repository;

import com.goodee.corpdesk.approval.dto.ResApprovalDTO;
import com.goodee.corpdesk.approval.entity.ApprovalForm;
import com.goodee.corpdesk.employee.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.goodee.corpdesk.approval.entity.Approval;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;

public interface ApprovalRepository extends JpaRepository<Approval, Long> {

    @Query("""
        WITH a AS (
        	SELECT a
        	FROM Approval a
        	WHERE
                a.useYn = :useYn
            	AND a.username = :username
            	AND a.status IN :statusList
        )
        SELECT new com.goodee.corpdesk.approval.dto.ResApprovalDTO(
            a.approvalId AS approvalId, a.createdAt AS createdAt, a.status AS status
        	, af.formTitle AS formTitle, count(DISTINCT af2.fileId) AS fileCount
        	, d.departmentName AS departmentName
        )
        FROM a
        LEFT JOIN ApprovalFile af2 ON a.approvalId = af2.approvalId
        JOIN ApprovalForm af ON a.approvalFormId = af.approvalFormId
        JOIN Department d ON a.departmentId = d.departmentId
        GROUP BY a.approvalId
    """)
    Page<ResApprovalDTO> findApprovalSummaryByStatus(
        @Param("useYn") Boolean useYn,
        @Param("username") String username,
        @Param("statusList") List<String> statusList,
        Pageable pageable
    );

    @NativeQuery("""
        WITH a AS (
        	SELECT *
        	FROM approval
        	WHERE
                use_yn = :useYn
            	AND username = :username
            	AND status IN :statusList
        )
        SELECT a.approval_id AS approvalId, a.created_at AS createdAt, a.status AS status
        	, af.form_title AS formTitle, count(DISTINCT af2.file_id) AS fileCount
        	, d.department_name AS departmentName
        FROM a
        LEFT JOIN approval_file af2 ON a.approval_id = af2.approval_id
        JOIN approval_form af ON a.approval_form_id = af.approval_form_id
        JOIN department d ON a.department_id = d.department_id
        GROUP BY a.approval_id
        ORDER BY a.created_at DESC
        LIMIT :limit
    """)
    ArrayList<ResApprovalDTO> findApprovalSummaryByStatus(
                                    @Param("useYn") Boolean useYn, @Param("username") String username, @Param("statusList") List<String> statusList
                                    , @Param("limit") Long limit
                                );

    @Query("""
        WITH
            a2 AS (
            SELECT ar
            FROM Approver ar
            WHERE
                ar.useYn = :useYn
                AND ar.username = :username
                AND ar.approveYn IN :approveYnList
            ),
            a AS (
            SELECT al
            FROM Approval al
            WHERE al.status IN :statusList
            )
        SELECT new com.goodee.corpdesk.approval.dto.ResApprovalDTO(
             a.approvalId AS approvalId, a.createdAt AS createdAt, a.status AS status
        	, a.username AS username
        	, af.formTitle AS formTitle, count(DISTINCT af2.fileId) AS fileCount
        	, d.departmentName AS departmentName
        )
        FROM a
        JOIN a2 ON a.approvalId = a2.approvalId
        LEFT JOIN ApprovalFile af2 ON a.approvalId = af2.approvalId
        JOIN ApprovalForm af ON a.approvalFormId = af.approvalFormId
        JOIN Department d ON a.departmentId = d.departmentId
        GROUP BY a.approvalId
    """)
    Page<ResApprovalDTO> findSummaryByApproverWithApproveYnAndStatus(
        @Param("useYn") Boolean useYn,
        @Param("username") String username,
        @Param("approveYnList") List<String> approveYnList,
        @Param("statusList") List<String> statusList,
        Pageable pageable
    );

    @NativeQuery("""
        WITH
            a2 AS (
            SELECT *
            FROM approver
            WHERE
                use_yn = :useYn
                AND username = :username
                AND approve_yn IN :approveYnList
            ),
            a AS (
            SELECT *
            FROM approval
            WHERE status IN :statusList
            )
        SELECT a.approval_id AS approvalId, a.created_at AS createdAt, a.status AS status
        	, a.username AS username
        	, af.form_title AS formTitle, count(DISTINCT af2.file_id) AS fileCount
        	, d.department_name AS departmentName
        FROM a
        JOIN a2 ON a.approval_id = a2.approval_id
        LEFT JOIN approval_file af2 ON a.approval_id = af2.approval_id
        JOIN approval_form af ON a.approval_form_id = af.approval_form_id
        JOIN department d ON a.department_id = d.department_id
        GROUP BY a.approval_id
        ORDER BY a.created_at DESC
        LIMIT :limit
    """)
    ArrayList<ResApprovalDTO> findSummaryByApproverWithApproveYnAndStatus(
                                    @Param("useYn") Boolean useYn, @Param("username") String username, @Param("approveYnList") List<String> approveYnList
                                    , @Param("statusList") List<String> statusList,  @Param("limit") Long limit
                                );

    /*
    @NativeQuery("""
        WITH a AS (
        	SELECT *
        	FROM approval
        	WHERE
                use_yn = :useYn
            	AND username = :username
            	AND status = 't'
        )
        SELECT a.approval_id AS approvalId, a.created_at AS createdAt, a.status AS status
        	, af.form_title AS formTitle, count(DISTINCT af2.file_id) AS fileCount
        	, d.department_name AS departmentName
        FROM a
        LEFT JOIN approval_file af2 ON a.approval_id = af2.approval_id
        JOIN approval_form af ON a.approval_form_id = af.approval_form_id
        JOIN department d ON a.department_id = d.department_id
        GROUP BY a.approval_id
    """)
    ArrayList<ResApprovalDTO> findDraftApprovalSummary(@Param("useYn") Boolean useYn, @Param("username") String username);

    @NativeQuery("""
        WITH a2 AS (
                SELECT *
                FROM approver
                WHERE
                    use_yn = :useYn
                    AND username = :username
        )
        SELECT a.approval_id AS approvalId, a.created_at AS createdAt, a.status AS status
        	, a.username AS username
        	, af.form_title AS formTitle, count(DISTINCT af2.file_id) AS fileCount
        	, d.department_name AS departmentName
        FROM approval a
        JOIN a2 ON a.approval_id = a2.approval_id
        LEFT JOIN approval_file af2 ON a.approval_id = af2.approval_id
        JOIN approval_form af ON a.approval_form_id = af.approval_form_id
        JOIN department d ON a.department_id = d.department_id
        GROUP BY a.approval_id
    """)
    ArrayList<ResApprovalDTO> findApprovalSummaryByApprover(@Param("useYn") Boolean useYn, @Param("username") String username);

    @NativeQuery("""
        WITH
            a2 AS (
            SELECT *
            FROM approver
            WHERE
                use_yn = :useYn
                AND username = :username
                AND approve_yn IN :approveYnList
            ),
            a AS (
            SELECT *
            FROM approval
            WHERE status = IN :statusList
            )
        SELECT a.approval_id AS approvalId, a.created_at AS createdAt, a.status AS status
        	, a.username AS username
        	, af.form_title AS formTitle, count(DISTINCT af2.file_id) AS fileCount
        	, d.department_name AS departmentName
        FROM approval a
        JOIN a2 ON a.approval_id = a2.approval_id
        LEFT JOIN approval_file af2 ON a.approval_id = af2.approval_id
        JOIN approval_form af ON a.approval_form_id = af.approval_form_id
        JOIN department d ON a.department_id = d.department_id
        GROUP BY a.approval_id
    """)
    ArrayList<ResApprovalDTO> findSummaryByApproverWithApproveYn(
            @Param("useYn") Boolean useYn, @Param("username") String username, @Param("approveYnList") List<String> approveYnList);
     */
}
