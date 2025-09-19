package com.goodee.corpdesk.approval.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.goodee.corpdesk.approval.entity.Approval;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResApprovalDTO {

	private Long approvalId;
	private String username;
	private Integer departmentId;
	private String formType;
	private String formContent;
	private Character status;
	
	private LocalDateTime createdAt;
	
	List<ApproverDTO> approverDTOList;
	
	public Approval toApprovalEntity() {
		return Approval.builder()
					.approvalId(approvalId)
					.username(username)
					.departmentId(departmentId)
					.status(status)
					.build();
	}
	
}
