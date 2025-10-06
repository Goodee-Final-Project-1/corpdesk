package com.goodee.corpdesk.file.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Setter
@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity @Table(name = "approval_file")
@DynamicInsert
public class ApprovalFile extends FileBase {
	@Column(nullable = false)
	private Long approvalId;
}
