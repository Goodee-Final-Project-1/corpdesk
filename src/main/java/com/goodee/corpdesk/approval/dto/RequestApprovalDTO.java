package com.goodee.corpdesk.approval.dto;

import java.util.ArrayList;

import com.goodee.corpdesk.approval.entity.Approval;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RequestApprovalDTO {

	private Long approvalId;
	private Integer employeeId;
	private Integer departmentId;
	private String formType;
	private String formContent;
	private Character status;
	
	ArrayList<ApproverDTO> approverDTOList;
	
	public Approval toApprovalEntity() {
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
