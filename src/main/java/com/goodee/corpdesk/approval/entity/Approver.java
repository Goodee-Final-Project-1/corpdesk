package com.goodee.corpdesk.approval.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity @Table(name = "approver")
public class Approver {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long approverId;
	
	@Column(nullable = false)
	private Long approvalId;
	
	@Column(nullable = false)
	private Integer employeeId;
	
	@Column(nullable = false)
	private Integer approvalOrder;
	
	@Column(nullable = false)
	private Character approveYn;
	
}
