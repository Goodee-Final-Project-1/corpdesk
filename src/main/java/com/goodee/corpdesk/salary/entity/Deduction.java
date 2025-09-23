package com.goodee.corpdesk.salary.entity;

import com.goodee.corpdesk.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity @Table
@DynamicInsert
public class Deduction extends BaseEntity {

	@Id
	private Long deductionId;
	private Long paymentId;
	private String deductionName;
	private Long deductionAmount;
}
