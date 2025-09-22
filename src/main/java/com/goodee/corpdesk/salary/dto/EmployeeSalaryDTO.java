package com.goodee.corpdesk.salary.dto;

import com.goodee.corpdesk.salary.entity.SalaryPayment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class EmployeeSalaryDTO extends SalaryPayment {

	private String username;
	private String name;
}
