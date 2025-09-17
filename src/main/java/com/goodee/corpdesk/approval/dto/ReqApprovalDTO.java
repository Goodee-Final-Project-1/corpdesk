package com.goodee.corpdesk.approval.dto;

import java.util.ArrayList;

import com.goodee.corpdesk.approval.entity.Approval;

import lombok.*;

@Getter
@Setter
@ToString
@Builder @NoArgsConstructor @AllArgsConstructor
public class ReqApprovalDTO {

	private Long approvalId;
	private String username;
	private Integer departmentId;
	private String formType;
	private String formContent;
	private Character status;
	
	ArrayList<ApproverDTO> approverDTOList;
	
	public Approval toApprovalEntity() {
		return Approval.builder()
					.approvalId(approvalId)
					.username(username)
					.departmentId(departmentId)
					.formType(formType)
					.formContent(formContent)
					.status(status)
					.build();
	}
	
}
