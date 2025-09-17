package com.goodee.corpdesk.approval.dto;

import com.goodee.corpdesk.approval.entity.Approval;
import lombok.*;

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
	
	List<ApproverDTO> approverDTOList;
	
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
