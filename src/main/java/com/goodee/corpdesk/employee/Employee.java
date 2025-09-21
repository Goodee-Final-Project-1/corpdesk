package com.goodee.corpdesk.employee;

import com.goodee.corpdesk.employee.validation.UpdateEmail;
import com.goodee.corpdesk.employee.validation.UpdatePassword;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity @Table
@DynamicInsert
@DynamicUpdate
public class Employee implements UserDetails {

//	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Integer employeeId;

	private Integer positionId;
	private Integer departmentId;
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
	
	private String name;
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
    @NotBlank(groups = UpdateEmail.class)
	private String externalEmailPassword;
	
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
