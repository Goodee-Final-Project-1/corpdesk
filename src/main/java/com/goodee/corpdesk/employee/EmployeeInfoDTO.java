package com.goodee.corpdesk.employee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmployeeInfoDTO {

	private String username;
	private String name;
	private LocalDate hireDate;

	private String departmentName;

	private String positionName;
}
