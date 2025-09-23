package com.goodee.corpdesk.employee;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.goodee.corpdesk.approval.dto.ResApprovalDTO;
import com.goodee.corpdesk.department.entity.Department;
import com.goodee.corpdesk.department.repository.DepartmentRepository;
import com.goodee.corpdesk.file.entity.EmployeeFile;
import com.goodee.corpdesk.file.repository.EmployeeFileRepository;
import com.goodee.corpdesk.position.entity.Position;
import com.goodee.corpdesk.position.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value; // ⭐ 추가
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile; // ⭐ 추가

import com.goodee.corpdesk.attendance.Attendance;
import com.goodee.corpdesk.attendance.AttendanceService;
import com.goodee.corpdesk.file.FileManager; // ⭐ 추가
import com.goodee.corpdesk.file.dto.FileDTO; // ⭐ 추가


@Service
@Transactional
public class EmployeeService implements UserDetailsService {

	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AesBytesEncryptor aesBytesEncryptor;

	@Autowired
    private AttendanceService attendanceService;
	
	@Autowired
	private DepartmentRepository departmentRepository;
	@Autowired
	private PositionRepository positionRepository;

    // ⭐⭐ 파일 처리를 위해 추가된 의존성 주입
    @Autowired
    private EmployeeFileRepository employeeFileRepository;
    @Autowired
    private FileManager fileManager;
    @Value("${app.upload}")
    private String uploadPath;

    // username으로 Employee 조회 (없으면 예외)
    public Employee getEmployeeOrThrow(String username) {
        return employeeRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("직원을 찾을 수 없습니다: " + username));
    }

    // 직원 출퇴근 내역 가져오기
    public List<Attendance> getAttendanceByUsername(String username) {
        return attendanceService.getAttendanceByUsername(username);
    }
    
    
    
	// 등록
	public Employee addEmployee(Employee employee) {
		employee.setUseYn(true); // 기본값
		return employeeRepository.save(employee);
	}

	// 목록 조회
	public List<Employee> getActiveEmployees() {
		List<Employee> employees = employeeRepository.findAllByUseYnTrue();

		// 부서명, 직위명 매핑
		for (Employee emp : employees) {
			if (emp.getDepartmentId() != null) {
				Department dept = departmentRepository.findById(emp.getDepartmentId()).orElse(null);
				if (dept != null)
					emp.setDepartmentName(dept.getDepartmentName());
			}
			if (emp.getPositionId() != null) {
				Position pos = positionRepository.findById(emp.getPositionId()).orElse(null);
				if (pos != null)
					emp.setPositionName(pos.getPositionName());
			}
		}
		return employees;
	}

	// 단일 조회
	public Optional<Employee> getEmployee(String id) {
		return employeeRepository.findById(id);
	}

    // ⭐⭐ 프로필 파일 정보 조회 메서드 추가
    public Optional<EmployeeFile> getEmployeeFileByUsername(String username) {
        return employeeFileRepository.findByUsername(username);
    }
    
	// ⭐️ 기존 수정 메서드는 그대로 유지
	public Employee updateEmployee(Employee employee) {
		Employee persisted = employeeRepository.findById(employee.getUsername())
				.orElseThrow(() -> new IllegalArgumentException("Invalid employee id: " + employee.getUsername()));
		
		persisted.setProfileImageSaveName(employee.getProfileImageSaveName());
	    persisted.setProfileImageExtension(employee.getProfileImageExtension());
	    persisted.setProfileImageOriName(employee.getProfileImageOriName());
		
	    persisted.setDirectPhone(employee.getDirectPhone());
		persisted.setStatus(employee.getStatus());
		persisted.setAddress(employee.getAddress());
		persisted.setBirthDate(employee.getBirthDate());
		persisted.setEnglishName(employee.getEnglishName());
		persisted.setVisaStatus(employee.getVisaStatus());
		persisted.setResidentNumber(employee.getResidentNumber());
		persisted.setName(employee.getName());
		persisted.setNationality(employee.getNationality());
		persisted.setPassword(employee.getPassword());
		persisted.setExternalEmail(employee.getExternalEmail());
		persisted.setHireDate(employee.getHireDate());
		persisted.setGender(employee.getGender());
		persisted.setEmployeeType(employee.getEmployeeType());
		persisted.setMobilePhone(employee.getMobilePhone());
		persisted.setEnabled(employee.getEnabled());
		persisted.setLastWorkingDay(employee.getLastWorkingDay());
		persisted.setDepartmentId(employee.getDepartmentId());
		persisted.setPositionId(employee.getPositionId());

		return employeeRepository.save(persisted);
	}

    // ⭐⭐ 새로 추가된 updateEmployee 메서드 (파일 처리 로직 포함)
    @Transactional
    public void updateEmployee(Employee employeeFromForm, MultipartFile profileImageFile) {
        // 기존 직원 정보 가져오기
        Employee persisted = employeeRepository.findById(employeeFromForm.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee id: " + employeeFromForm.getUsername()));

        // Employee 엔티티 필드 업데이트 (기존 updateEmployee 로직과 동일)
        persisted.setName(employeeFromForm.getName());
        persisted.setEmployeeType(employeeFromForm.getEmployeeType());
        persisted.setExternalEmail(employeeFromForm.getExternalEmail());
        persisted.setMobilePhone(employeeFromForm.getMobilePhone());
        persisted.setDepartmentId(employeeFromForm.getDepartmentId());
        persisted.setPositionId(employeeFromForm.getPositionId());
        persisted.setHireDate(employeeFromForm.getHireDate());
        persisted.setAddress(employeeFromForm.getAddress());
        persisted.setStatus(employeeFromForm.getStatus());
        persisted.setBirthDate(employeeFromForm.getBirthDate());
        persisted.setEnglishName(employeeFromForm.getEnglishName());
        persisted.setVisaStatus(employeeFromForm.getVisaStatus());
        persisted.setResidentNumber(employeeFromForm.getResidentNumber());
        persisted.setNationality(employeeFromForm.getNationality());
        persisted.setPassword(employeeFromForm.getPassword());
        persisted.setGender(employeeFromForm.getGender());
        persisted.setEnabled(employeeFromForm.getEnabled());
        persisted.setLastWorkingDay(employeeFromForm.getLastWorkingDay());
        persisted.setDirectPhone(employeeFromForm.getDirectPhone());
        persisted.setExternalEmail(employeeFromForm.getExternalEmail());
        
        employeeRepository.save(persisted);
        
        // ⭐⭐ 파일 처리 로직 ⭐⭐
        if (profileImageFile != null && !profileImageFile.isEmpty()) {
            // 1. 기존 파일 삭제
            Optional<EmployeeFile> oldFileOptional = employeeFileRepository.findByUsername(persisted.getUsername());
            
            if (oldFileOptional.isPresent()) {
                EmployeeFile oldFile = oldFileOptional.get();
                FileDTO oldFileDTO = new FileDTO(oldFile.getSaveName(), oldFile.getExtension());
                fileManager.deleteFile(uploadPath + "profile" + File.separator, oldFileDTO);
                employeeFileRepository.delete(oldFile);
            }
            
            // 2. 새 파일 저장
            String profileImagePath = uploadPath + "profile" + File.separator;
            FileDTO fileDTO = fileManager.saveFile(profileImagePath, profileImageFile);
            
            EmployeeFile newFile = new EmployeeFile();
            newFile.setUsername(persisted.getUsername());
            newFile.setOriName(fileDTO.getOriName());
            newFile.setSaveName(fileDTO.getSaveName());
            newFile.setExtension(fileDTO.getExtension());
            employeeFileRepository.save(newFile);
        }
    }

	// 삭제(비활성화)
	public void deactivateEmployee(String id) {
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid employee id: " + id));
		if (employee.getLastWorkingDay() == null) {
			throw new IllegalStateException("퇴사일자가 없는 사원은 삭제할 수 없습니다.");
		}
		employee.setUseYn(false);
		employeeRepository.save(employee);
	}
    
    // ⭐⭐ 프로필 이미지 삭제 메서드 추가
    @Transactional
    public void deleteProfileImage(String username) {
        Optional<EmployeeFile> fileOptional = employeeFileRepository.findByUsername(username);
        
        if (fileOptional.isPresent()) {
            EmployeeFile employeeFile = fileOptional.get();
            FileDTO fileDTO = new FileDTO(employeeFile.getSaveName(), employeeFile.getExtension());
            String profileImagePath = uploadPath + "profile" + File.separator;
            fileManager.deleteFile(profileImagePath, fileDTO);
            employeeFileRepository.delete(employeeFile);
        }
    }


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Employee> employeeOp = employeeRepository.findById(username);
		Employee employee = employeeOp.get();
		Optional<Role> roleOp = roleRepository.findById(employee.getRoleId());
		Role role = roleOp.get();
		employee.setRole(role);

		return employee;
	}

	public void join(Employee employee) {
		String encoded = passwordEncoder.encode(employee.getPassword());
		employee.setPassword(encoded);

		employeeRepository.save(employee);
	}

	public Employee detail(String username) {
		Optional<Employee> optional = employeeRepository.findById(username);
		Employee employee = optional.get();

		if (employee.getEncodedEmailPassword() != null) {
			byte[] decoded = aesBytesEncryptor.decrypt(employee.getEncodedEmailPassword());
			employee.setExternalEmailPassword(new String(decoded));
		}

		return employee;
	}

	public Employee updatePassword(Employee employee) {
		Optional<Employee> optional = employeeRepository.findById(employee.getUsername());
		Employee origin = optional.get();

		if (passwordEncoder.matches(origin.getPassword(), employee.getPasswordNew())) {
			throw new BadCredentialsException("비밀번호가 다릅니다.");
		}

		if (!Objects.equals(employee.getPasswordNew(), employee.getPasswordCheck())) {
			throw new RuntimeException("비밀번호 확인이 다릅니다.");
		}

		String encoded = passwordEncoder.encode(employee.getPasswordNew());

		origin.setPassword(encoded);
		return employeeRepository.save(origin);
	}

	public Employee updateEmail(Employee employee) {
		Optional<Employee> optional = employeeRepository.findById(employee.getUsername());
		Employee origin = optional.get();

		byte[] encoded = aesBytesEncryptor.encrypt(employee.getExternalEmailPassword().getBytes());

		origin.setExternalEmail(employee.getExternalEmail());
		origin.setEncodedEmailPassword(encoded);

		return employeeRepository.save(origin);
	}

}
