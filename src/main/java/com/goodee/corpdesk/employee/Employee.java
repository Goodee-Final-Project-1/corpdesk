package com.goodee.corpdesk.employee;

import java.time.LocalDate;
import java.util.Collection;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity @Table
@DynamicInsert
public class Employee implements UserDetails {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer employeeId;
	private Integer positionId;
	private Integer departmentId;
	private Integer roleId;
	
	@ColumnDefault("1")
	private Boolean accountNonExpired;
	@ColumnDefault("1")
	private Boolean accountNonLocked;
	@ColumnDefault("1")
	private Boolean credentialsNonExpired;
	@ColumnDefault("1")
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
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		return null;
	}
	
	
}
