package com.goodee.corpdesk.employee;

import java.time.LocalDate;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.format.annotation.DateTimeFormat;

import com.goodee.corpdesk.department.Department;
import com.goodee.corpdesk.position.Position;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity @Table(name = "employee")
public class Employee {

	public interface CreateGroup {} // 등록 시 검증
	public interface UpdateGroup {} // 수정 시 검증 (비밀번호 제외)
	
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Integer employeeId;
	
	@Column(name = "position_id")
	private Integer positionId;
	
	@Column(name = "department_id")
	private Integer departmentId;


	
	private String departmentName;
	
	private String positionName;

	
	
	private Integer roleId;
	@ColumnDefault("true")
	private Boolean accountNonExpired;
	@ColumnDefault("true")
	private Boolean accountNonLocked;
	@ColumnDefault("true")
	private Boolean credentialsNonExpired;
	@ColumnDefault("true")
	private Boolean enabled;
	
	@NotBlank(message = "이름은 필수입니다.", groups = {CreateGroup.class, UpdateGroup.class})
	private String name;
	
	@Column(name = "username", unique = true, nullable = false)
    @NotBlank(message = "아이디는 필수입니다.")
    @Size(min = 5, max = 20, message = "아이디는 5~20자여야 합니다.")
	@Id
	private String username;
	
	@Column(nullable = false)
	@NotBlank(message = "비밀번호는 필수입니다.", groups = CreateGroup.class)
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
	private String password;
	
	@Email(message = "유효한 이메일을 입력하세요.")
	private String externalEmail;
	private String externalEmailPassword;
	
	private String employeeType;
	private LocalDate hireDate;
	private String responsibility;
	private String resident_number;
	private String directPhone;
	
	@NotBlank(message = "휴대전화는 필수 입력입니다")
	@Pattern(
	    regexp = "^(01[0-9])[\\-]?(\\d{3,4})[\\-]?(\\d{4})$",
	    message = "휴대전화 형식이 올바르지 않습니다 (예: 01012345678 또는 010-1234-5678)"
	)
	@Column(unique = true, nullable = false)
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
