package com.goodee.corpdesk.approval.dto;

import com.goodee.corpdesk.approval.entity.Approver;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApproverDTO {
	
	private Long approverId;
	private Long approvalId;
	private String username;
	private Integer approvalOrder;
	private Character approveYn;
	
	
	public Approver toEntity() {
		return Approver.builder()
					.approverId(approverId)
					.approvalId(approvalId)
					.username(username)
					.approvalOrder(approvalOrder)
					.approveYn(approveYn)
					.build();
	}
}
