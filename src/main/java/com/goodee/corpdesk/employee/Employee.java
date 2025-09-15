package com.goodee.corpdesk.employee;

import java.time.LocalDate;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.format.annotation.DateTimeFormat;

import com.goodee.corpdesk.department.Department;
import com.goodee.corpdesk.position.Position;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity @Table(name = "employee")
public class Employee {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer employeeId;
	
	@ManyToOne
	@JoinColumn(name = "position_id")
	private Position position;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "department_id")
	private Department department;
	private Integer roleId;
	@ColumnDefault("true")
	private Boolean accountNonExpired;
	@ColumnDefault("true")
	private Boolean accountNonLocked;
	@ColumnDefault("true")
	private Boolean credentialsNonExpired;
	@ColumnDefault("true")
	private Boolean enabled;
	
	private String name;
	private String username;
	private String password;
	
	private String externalEmail;
	private String externalEmailPassword;
	
	private String employeeType;
	private LocalDate hireDate;
	private String responsibility;
	private String resident_number;
	private String directPhone;
	private String mobilePhone;
	private String nationality;
	private String visaStatus;
	private String englishName;
	private Character gender;
	private LocalDate birthDate;
	private String address;
	private String status; // 출근,퇴근,휴가
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate lastWorkingDay;
	@ColumnDefault("true")
	private boolean useYn;
}
