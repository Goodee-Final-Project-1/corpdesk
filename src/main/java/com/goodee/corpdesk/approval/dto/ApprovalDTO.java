package com.goodee.corpdesk.approval.dto;

import com.goodee.corpdesk.approval.entity.Approval;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ApprovalDTO {
	
	private Long approvalId;
	private Integer employeeId;
	private Integer departmentId;
	private String formType;
	private String formContent;
	private Character status;
		
	public Approval toEntity() {
		return Approval.builder()
					.approvalId(approvalId)
					.employeeId(employeeId)
					.departmentId(departmentId)
					.formType(formType)
					.formContent(formContent)
					.status(status)
					.build();
	}
	
}
