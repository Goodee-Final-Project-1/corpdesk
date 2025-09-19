package com.goodee.corpdesk.employee;

import com.goodee.corpdesk.employee.validation.UpdateEmail;
import com.goodee.corpdesk.employee.validation.UpdatePassword;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.goodee.corpdesk.employee.validation.UpdateEmail;
import com.goodee.corpdesk.employee.validation.UpdatePassword;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table
@DynamicInsert
@DynamicUpdate
public class Employee implements UserDetails {

//	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Integer employeeId;

	//	private Integer positionId;
//	private Integer departmentId;
	public interface CreateGroup {
	} // 등록 시 검증

	public interface UpdateGroup {
	} // 수정 시 검증 (비밀번호 제외)

//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Integer employeeId;

	//파일
	@Column(name = "profile_image_save_name")
	private String profileImageSaveName;
	@Column(name = "profile_image_extension")
    private String profileImageOriName;
	@Column(name = "profile_image_ori_name")
    private String profileImageExtension;
	
	
	
	
	@Column(name = "position_id")
	private Integer positionId;

	@Column(name = "department_id")
	private Integer departmentId;

	private String departmentName;

	private String positionName;

	@ColumnDefault("2")
	private Integer roleId;

	@ColumnDefault("1")
	private Boolean accountNonExpired;
	@ColumnDefault("1")
	private Boolean accountNonLocked;
	@ColumnDefault("1")
	private Boolean credentialsNonExpired;
	@ColumnDefault("1")
	private Boolean enabled;

	@NotBlank(message = "이름은 필수입니다.", groups = { CreateGroup.class, UpdateGroup.class })
	private String name;

	@Column(name = "username", unique = true, nullable = false)
	@NotBlank(message = "아이디는 필수입니다.")
	@Size(min = 5, max = 20, message = "아이디는 5~20자여야 합니다.")
	@Id
	private String username;
	@Column(nullable = false)
	@NotBlank(groups = UpdatePassword.class)
	private String password;
	@Transient
	@NotBlank(groups = UpdatePassword.class)
	private String passwordNew;
	@Transient
	@NotBlank(groups = UpdatePassword.class)
	private String passwordCheck;

	@NotBlank(groups = UpdateEmail.class)
	private String externalEmail;
	@Transient
	@NotBlank(groups = UpdateEmail.class)
	private String externalEmailPassword;
	private byte[] encodedEmailPassword;

//	private String employeeType;
//	private LocalDate hireDate;
//	private String responsibility;
//	private String resident_number;
//	private String directPhone;
//	private String mobilePhone;
//	private String nationality;
//	private String visaStatus;
//	private String englishName;
//	private Character gender;
//	private LocalDate birthDate;
//	private String address;

	private String employeeType;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate hireDate;
	private String responsibility;
	private String residentNumber;
	private String directPhone;

	@NotBlank(message = "휴대전화는 필수 입력입니다", groups = { CreateGroup.class, UpdateGroup.class })
	@Pattern(regexp = "^(01[0-9])[\\-]?(\\d{3,4})[\\-]?(\\d{4})$", message = "휴대전화 형식이 올바르지 않습니다 (예: 01012345678 또는 010-1234-5678)")
	@Column(unique = true)
	private String mobilePhone;
	private String nationality;
	private String visaStatus;
	private String englishName;
	private Character gender;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthDate;
	private String address;
	private String status; // 출근,퇴근,휴가,출근전
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate lastWorkingDay;
	@ColumnDefault("true")
	private boolean useYn;

	@Transient
	private Role role;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		if (role != null) {
			authorities.add(new SimpleGrantedAuthority(this.role.getRoleName()));
		}

		return authorities;
	}
}
