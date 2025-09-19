package com.goodee.corpdesk.approval.dto;

import com.goodee.corpdesk.approval.entity.Approval;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalDTO {
	
	private Long approvalId;
	private String username;
	private Integer departmentId;
	private Long approvalFormId;
	private Character status;
		
	public Approval toEntity() {
		return Approval.builder()
					.approvalId(approvalId)
					.username(username)
					.departmentId(departmentId)
                    .approvalFormId(approvalFormId)
					.status(status)
					.build();
	}
	
}
