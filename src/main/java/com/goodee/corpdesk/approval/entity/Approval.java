package com.goodee.corpdesk.approval.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity @Table(name = "approval")
public class Approval {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long approvalId;
	
	@Column(nullable = false)
	private Integer employeeId;
	
	@Column(nullable = false)
	private Integer departmentId;
	
	@Column(nullable = false)
	private LocalDate createdDate;
	
	@Column(nullable = false)
	private String formType;
	
	@Column(nullable = false) @Lob
	private String formContent;
	
	@Column(nullable = false)
	private Character status;
	
}
