package com.goodee.corpdesk.common;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true)
@Entity @Table(name = "approval_file")
public class ApprovalFile extends File {
	@Column(nullable = false)
	private Long approvalId;
}
