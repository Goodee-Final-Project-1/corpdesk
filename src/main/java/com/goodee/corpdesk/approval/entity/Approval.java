package com.goodee.corpdesk.approval.entity;

import java.time.LocalDate;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.goodee.corpdesk.approval.dto.ApprovalDTO;
import com.goodee.corpdesk.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity @Table(name = "approval")
@DynamicInsert
@DynamicUpdate
public class Approval extends BaseEntity {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long approvalId;
	
	@Column(nullable = false)
	private Integer employeeId;
	
	@Column(nullable = false)
	private Integer departmentId;
	
	@Column(nullable = false)
	private String formType;
	
//	@Column(nullable = false) @Lob 대신 아래의 어노테이션 적용
//	이유: String 타입에 @Lob을 붙여도 text 타입 대신 tinytext 타입으로 매핑됨
//	대용량 데이터를 담기 위해 tinytext 대신 longtext 사용
	@Column(nullable = false, columnDefinition = "longtext")
	private String formContent;
	
	@Column(nullable = false)
	@ColumnDefault("'w'") // 기본값은 w(결재대기)
	private Character status;
	
}
