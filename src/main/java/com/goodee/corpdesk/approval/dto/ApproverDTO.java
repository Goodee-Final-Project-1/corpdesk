package com.goodee.corpdesk.approval.dto;

import com.goodee.corpdesk.approval.entity.Approver;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ApproverDTO {
	
	private Long approverId;
	private Long approvalId;
	private Integer employeeId;
	private Integer approvalOrder;
	private Character approveYn;
	
	
	public Approver toEntity() {
		return Approver.builder()
					.approverId(approverId)
					.approvalId(approvalId)
					.employeeId(employeeId)
					.approvalOrder(approvalOrder)
					.approveYn(approveYn)
					.build();
	}
}
