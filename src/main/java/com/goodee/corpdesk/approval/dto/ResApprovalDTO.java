package com.goodee.corpdesk.approval.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.goodee.corpdesk.approval.entity.Approval;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResApprovalDTO {

    // approval
	private Long approvalId;
	private String username;
	private Integer departmentId;
    private Long approvalFormId;
	private Character status;
	
//	private LocalDateTime createdAt;
	private LocalDateTime createdAt;

    // approver
	private List<ApproverDTO> approverDTOList;

    // approval_form
    private String formTitle;
    private String formContent;

    // department
    private String departmentName;

    // file
    private Integer fileCount;

    // SQL 결과용 생성자
    public ResApprovalDTO(Long approvalId, Timestamp createdAt, Character status, String username,
                          String formTitle, Long fileCount, String departmentName) {
        this.approvalId = approvalId;
        this.createdAt = LocalDateTime.ofInstant(createdAt.toInstant(), ZoneId.of("Asia/Seoul"));
        this.status = status;
        this.username = username;
        this.formTitle = formTitle;
        this.fileCount = fileCount != null ? fileCount.intValue() : 0;
        this.departmentName = departmentName;
    }

    public ResApprovalDTO(Long approvalId, Timestamp createdAt, Character status,
                          String formTitle, Long fileCount, String departmentName) {
        this.approvalId = approvalId;
        this.createdAt = LocalDateTime.ofInstant(createdAt.toInstant(), ZoneId.of("Asia/Seoul"));
        this.status = status;
        this.formTitle = formTitle;
        this.fileCount = fileCount != null ? fileCount.intValue() : 0;
        this.departmentName = departmentName;
    }
}
