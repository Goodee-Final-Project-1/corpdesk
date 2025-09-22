package com.goodee.corpdesk.salary.entity;

import com.goodee.corpdesk.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
@DynamicInsert
public class Allowance extends BaseEntity {

	@Id
	private Long allowanceId;
	private Long paymentId;
	private String allowanceName;
	private Long allowanceAmount;
}
