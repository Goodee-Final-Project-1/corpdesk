package com.goodee.corpdesk.employee;

import com.goodee.corpdesk.attendance.entity.Attendance;
import com.goodee.corpdesk.attendance.service.AttendanceService;
import com.goodee.corpdesk.department.entity.Department;
import com.goodee.corpdesk.department.repository.DepartmentRepository;
import com.goodee.corpdesk.employee.dto.EmployeeListDTO;
import com.goodee.corpdesk.employee.dto.EmployeeSecurityDTO;
import com.goodee.corpdesk.file.FileManager;
import com.goodee.corpdesk.file.dto.FileDTO;
import com.goodee.corpdesk.file.entity.EmployeeFile;
import com.goodee.corpdesk.file.repository.EmployeeFileRepository;
import com.goodee.corpdesk.position.entity.Position;
import com.goodee.corpdesk.position.repository.PositionRepository;
import com.goodee.corpdesk.salary.dto.EmployeeSalaryDTO;
import com.goodee.corpdesk.salary.entity.Allowance;
import com.goodee.corpdesk.salary.entity.Deduction;
import com.goodee.corpdesk.salary.entity.SalaryPayment;
import com.goodee.corpdesk.salary.repository.AllowanceRepository;
import com.goodee.corpdesk.salary.repository.DeductionRepository;
import com.goodee.corpdesk.salary.repository.SalaryRepository;
import com.goodee.corpdesk.vacation.VacationManager;
import com.goodee.corpdesk.vacation.entity.Vacation;
import com.goodee.corpdesk.vacation.repository.VacationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.util.*;

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
	@Autowired
	private SalaryRepository salaryRepository;
	@Autowired
	private AllowanceRepository allowanceRepository;
	@Autowired
	private DeductionRepository deductionRepository;

    @Autowired
    private RoleService roleService;
    @Autowired
    private EmployeeFileRepository employeeFileRepository;
    @Autowired
    private FileManager fileManager;
    @Value("${app.upload}")
    private String uploadPath;

    
    
    @Autowired
    private VacationRepository vacationRepository;
    @Autowired
    private VacationManager vacationManager;

    // ---------------------- Controller용 Service 메서드 ----------------------

    // 모든 부서 조회
    public List<Department> getAllDepartments() {
    	return departmentRepository.findByUseYnTrue();
    }

    // 모든 직위 조회
    public List<Position> getAllPositions() {
        return positionRepository.findAll();
    }

    // username 존재 여부 체크
    public boolean isUsernameExists(String username) {
        return employeeRepository.existsByUsername(username);
    }

    // 휴대폰 존재 여부 체크
    public boolean isMobilePhoneExists(String mobilePhone) {
        return employeeRepository.existsByMobilePhone(mobilePhone);
    }

    // username으로 Employee 조회 (없으면 예외)
    public Employee getEmployeeOrThrow(String username) {
        return employeeRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("직원을 찾을 수 없습니다: " + username));
    }

    // 직원 출퇴근 내역 가져오기
    public List<Attendance> getAttendanceByUsername(String username) {
        return attendanceService.getAttendanceByUsername(username);
    }

    // 직원 프로필 파일 조회
    public Optional<EmployeeFile> getEmployeeFileByUsername(String username) {
        return employeeFileRepository.findByUsername(username);
    }

    // 직원 등록
    public Employee addEmployee(Employee employee) throws Exception {
    	// 직원 등록
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        employee.setUseYn(true);
        roleService.assignRole(employee);
        Employee newEmployee = employeeRepository.save(employee);

        // 추가) 직원 등록 성공시 휴가 테이블에 insert
        Vacation newVacation = new Vacation();
        newVacation.setUsername(employee.getUsername());

        // 직원 등록시 입사일 정보도 같이 입력됐다면 그 기준으로 총발생연차 계산
        // 입력되지 않았다면 총발생연차 0으로 설정
        int totalVacation = 0;
        LocalDate hireDate = employee.getHireDate();
        if(hireDate != null) {
            totalVacation = vacationManager.calTotalVacation(hireDate);
        }
        newVacation.setTotalVacation(totalVacation);

        newVacation.setRemainingVacation(totalVacation);
        newVacation.setUsedVacation(0);

        vacationRepository.save(newVacation);

        return newEmployee;
    }

    public List<EmployeeListDTO> getActiveEmployeesForList() {
        List<Employee> employees = employeeRepository.findAllByUseYnTrue();
        List<EmployeeListDTO> result = new ArrayList<>();

        for (Employee emp : employees) {
            // 부서명
        	String deptName = "-";
        	if (emp.getDepartmentId() != null) {
        	    deptName = departmentRepository.findById(emp.getDepartmentId())
        	                  .map(Department::getDepartmentName)
        	                  .orElse("-");
        	}

            // 직위명
        	String posName = "-";
        	if (emp.getPositionId() != null) {
        	    posName = positionRepository.findById(emp.getPositionId())
        	                  .map(Position::getPositionName)
        	                  .orElse("-");
        	}

            // ✅ 현재 출퇴근 상태 (출근, 퇴근, 휴가, 출근전)
            String workStatus = "-";
            try {
                var attendanceDto = attendanceService.getCurrentAttendance(emp.getUsername());
                if (attendanceDto != null) {
                    if ("출근".equals(attendanceDto.getWorkStatus()) || "퇴근".equals(attendanceDto.getWorkStatus())) {
                        workStatus = attendanceDto.getWorkStatus();
                    }
                }
            } catch (Exception e) {
                workStatus = "-"; // 오류시 기본값
            }

            // 🔥 여기서 deptName, posName을 사용해야 함
            result.add(new EmployeeListDTO(
                    emp.getUsername(),
                    emp.getName(),
                    deptName,          // ✅ 수정: emp.getDepartmentName() ❌ → deptName ✅
                    emp.getDepartmentId(),
                    emp.getPositionId(),
                    posName,           // ✅ 수정: emp.getPositionName() ❌ → posName ✅
                    emp.getMobilePhone(),
                    emp.getHireDate(),
                    emp.getLastWorkingDay(),
                    emp.getEnabled(),
                    workStatus,
                    emp.getPassword(),
                    emp.getUseYn()
            ));
        }

        return result;
    }





 // 직원 정보 수정 (파일 포함)
    public void updateEmployee(Employee employeeFromForm, MultipartFile profileImageFile) throws Exception {
        Employee persisted = employeeRepository.findById(employeeFromForm.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee id: " + employeeFromForm.getUsername()));

        // Employee 필드 업데이트
        persisted.setName(employeeFromForm.getName());
        persisted.setEmployeeType(employeeFromForm.getEmployeeType());
        persisted.setExternalEmail(employeeFromForm.getExternalEmail());
        persisted.setMobilePhone(employeeFromForm.getMobilePhone());
        persisted.setDepartmentId(employeeFromForm.getDepartmentId());
        persisted.setPositionId(employeeFromForm.getPositionId());
        persisted.setHireDate(employeeFromForm.getHireDate());
        persisted.setAddress(employeeFromForm.getAddress());
        persisted.setBirthDate(employeeFromForm.getBirthDate());
        persisted.setEnglishName(employeeFromForm.getEnglishName());
        persisted.setVisaStatus(employeeFromForm.getVisaStatus());
        persisted.setResidentNumber(employeeFromForm.getResidentNumber());
        persisted.setNationality(employeeFromForm.getNationality());
        
        // ---------------- 비밀번호 처리 ----------------
        if (employeeFromForm.getPassword() != null && !employeeFromForm.getPassword().isEmpty()) {
            persisted.setPassword(passwordEncoder.encode(employeeFromForm.getPassword()));
        } // null이면 기존 비밀번호 그대로 유지

        if (employeeFromForm.getEnabled() != null) {
            persisted.setEnabled(employeeFromForm.getEnabled());
        }
        persisted.setGender(employeeFromForm.getGender());
        persisted.setLastWorkingDay(employeeFromForm.getLastWorkingDay());
        persisted.setDirectPhone(employeeFromForm.getDirectPhone());

        Employee editedEmployee = employeeRepository.save(persisted);
        
        roleService.assignRole(editedEmployee);
        employeeRepository.save(editedEmployee);
        // ---------------- 프로필 이미지 처리 ----------------
        if (profileImageFile != null && !profileImageFile.isEmpty()) {
            Optional<EmployeeFile> fileOptional = employeeFileRepository.findByUsername(persisted.getUsername());
            String profileImagePath = uploadPath + "profile" + File.separator;
            FileDTO fileDTO = fileManager.saveFile(profileImagePath, profileImageFile);

            if (fileOptional.isPresent()) {
                EmployeeFile existingFile = fileOptional.get();
                existingFile.setOriName(fileDTO.getOriName());
                existingFile.setSaveName(fileDTO.getSaveName());
                existingFile.setExtension(fileDTO.getExtension());
                existingFile.setUseYn(true);
                employeeFileRepository.save(existingFile);
            } else {
                EmployeeFile newFile = new EmployeeFile();
                newFile.setUsername(persisted.getUsername());
                newFile.setOriName(fileDTO.getOriName());
                newFile.setSaveName(fileDTO.getSaveName());
                newFile.setExtension(fileDTO.getExtension());
                newFile.setUseYn(true);
                employeeFileRepository.save(newFile);
            }
        }

        // 추가) 직원 정보 수정 성공시 휴가 테이블 update
        if(editedEmployee.getHireDate() != null) {
            // 직원의 휴가 테이블 조회
//            Vacation vacation = vacationRepository.findByUseYnAndUsername(true, editedEmployee.getUsername());
//
//            // 총 연차 update
//            int totalVacation = vacationManager.calTotalVacation(editedEmployee.getHireDate());
//            vacation.setTotalVacation(totalVacation);
//            // 잔여 연차 update (update된 총 연차 - 사용 연차)
//            int usedVacation = vacation.getUsedVacation();
//            vacation.setRemainingVacation(totalVacation - usedVacation);
//
//            vacationRepository.save(vacation);
        }
    }


    // 프로필 이미지 삭제
    public void deleteProfileImage(String username) {
        Optional<EmployeeFile> fileOptional = employeeFileRepository.findByUsername(username);
        if (fileOptional.isPresent()) {
            EmployeeFile employeeFile = fileOptional.get();
            FileDTO fileDTO = new FileDTO(employeeFile.getSaveName(), employeeFile.getExtension());
            String profileImagePath = uploadPath + "profile" + File.separator;
            fileManager.deleteFile(profileImagePath, fileDTO);
            employeeFile.setUseYn(false);
            employeeFileRepository.save(employeeFile);
        }
    }

    // 직원 삭제(비활성화)
    public void deactivateEmployee(String username) {
        Employee employee = employeeRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("해당 직원이 존재하지 않습니다."));
 

        employee.setUseYn(false);  // 여기 수정
        employeeRepository.save(employee); // DB 반영
    }



    // ---------------------- 기타 기존 메서드 ----------------------
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Employee employee = employeeRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
//        employee.setRole(roleRepository.findById(employee.getRoleId()).orElse(null));
		EmployeeSecurityDTO employee = employeeRepository.findEmployeeSecurityByUsername(username).get();
        return employee;
    }

    public void join(Employee employee) {
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        employee.setDepartmentId(1);
        employee.setPositionId(1);
        employeeRepository.save(employee);
    }

    public Map<String, Object> detail(String username) {
        Employee employee = employeeRepository.findById(username).orElseThrow();
		Department department = null;
		if(employee.getDepartmentId() != null) department = departmentRepository.findById(employee.getDepartmentId()).orElse(null);
        Position position = null;
		if(employee.getPositionId() != null) position = positionRepository.findById(employee.getPositionId()).orElse(null);

        Map<String, Object> map = new HashMap<>();
        map.put("employee", employee);
        map.put("department", department);
        map.put("position", position);
        return map;
    }

	public Page<EmployeeSalaryDTO> getSalaryList(String username, Pageable pageable) {
		return salaryRepository.findAllEmployeeSalaryByUsername(username, pageable);
	}

	public Map<String, Object> getSalaryDetail(String username, Long paymentId) {
		Map<String, Object> map = new HashMap<>();
		SalaryPayment salaryPayment = salaryRepository.findByUsernameAndPaymentId(username, paymentId).get();

		if (salaryPayment == null) return null;

		List<Allowance> allowanceList = allowanceRepository.findAllByPaymentId(salaryPayment.getPaymentId());
		List<Deduction> deductionList = deductionRepository.findAllByPaymentId(salaryPayment.getPaymentId());

		EmployeeInfoDTO employee = employeeRepository.findByIdWithDept(salaryPayment.getUsername()).get();

		map.put("salaryPayment", salaryPayment);
		map.put("allowanceList", allowanceList);
		map.put("deductionList", deductionList);

		map.put("employee", employee);

		return map;
	}

	public Employee updatePassword(Employee employee, BindingResult bindingResult) {
		Optional<Employee> optional = employeeRepository.findById(employee.getUsername());
		Employee origin = optional.get();

		if (!passwordEncoder.matches(employee.getPassword(), origin.getPassword())) {
			bindingResult.addError(new FieldError("Employee", "password", "비밀번호가 다릅니다."));
			return null;
//			throw new BadCredentialsException("비밀번호가 다릅니다.");
		}

		if (!Objects.equals(employee.getPasswordNew(), employee.getPasswordCheck())) {
			bindingResult.addError(new FieldError("Employee", "passwordNew", "비밀번호 확인이 다릅니다."));
			return null;
//			throw new RuntimeException("비밀번호 확인이 다릅니다.");
		}

		String encoded = passwordEncoder.encode(employee.getPasswordNew());

		origin.setPassword(encoded);
		return origin;
//		return employeeRepository.save(origin);
	}

    public Employee detailSecret(String username) {
        Employee employee = employeeRepository.findById(username).orElseThrow();
        if (employee.getEncodedEmailPassword() != null) {
            byte[] decoded = aesBytesEncryptor.decrypt(employee.getEncodedEmailPassword());
            employee.setExternalEmailPassword(new String(decoded));
        }
        return employee;
    }

    public Employee updateEmail(Employee employee) {
        Employee origin = employeeRepository.findById(employee.getUsername()).orElseThrow();
        origin.setExternalEmail(employee.getExternalEmail());
        origin.setEncodedEmailPassword(aesBytesEncryptor.encrypt(employee.getExternalEmailPassword().getBytes()));
        return employeeRepository.save(origin);
    }

    public ResEmployeeDTO getFulldetail(String username) {
        return employeeRepository.findEmployeeWithDeptAndPosition(true, username);
    }
}
