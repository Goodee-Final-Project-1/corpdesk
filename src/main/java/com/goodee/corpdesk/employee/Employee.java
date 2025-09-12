package com.goodee.corpdesk.employee;

import java.time.LocalDate;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	private Integer positionId;
	private Integer departmentId;
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
	
	
}
