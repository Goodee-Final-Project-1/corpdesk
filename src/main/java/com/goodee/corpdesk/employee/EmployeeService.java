package com.goodee.corpdesk.employee;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goodee.corpdesk.department.Department;
import com.goodee.corpdesk.department.DepartmentRepository;
import com.goodee.corpdesk.position.Position;
import com.goodee.corpdesk.position.PositionRepository;

@Service
@Transactional
public class EmployeeService implements UserDetailsService {

	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	private DepartmentRepository departmentRepository;
	@Autowired
	private PositionRepository positionRepository;

	// 등록
	public Employee addEmployee(Employee employee) {
		employee.setUseYn(true); // 기본값
		return employeeRepository.save(employee);
	}

	// 목록 조회
	public List<Employee> getActiveEmployees() {
		List<Employee> employees = employeeRepository.findByUseYnTrue();

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

	// 수정
	public Employee updateEmployee(Employee employee) {
		Employee persisted = employeeRepository.findById(employee.getUsername())
				.orElseThrow(() -> new IllegalArgumentException("Invalid employee id: " + employee.getUsername()));

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
		persisted.setStatus(employee.getStatus());
		persisted.setLastWorkingDay(employee.getLastWorkingDay());
		persisted.setDepartmentId(employee.getDepartmentId());
		persisted.setPositionId(employee.getPositionId());

		return employeeRepository.save(persisted);
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

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Employee> employeeOp = employeeRepository.findById(username);
		Employee employee = employeeOp.get();
		Optional<Role> roleOp = roleRepository.findById(employee.getRoleId());
		Role role = roleOp.get();
		employee.setRole(role);
		System.out.println("========================= " + employee.getRole().getRoleName());

		return employee;
	}

	public void join(Employee employee) {
		String encoded = passwordEncoder.encode(employee.getPassword());
		employee.setPassword(encoded);

		employeeRepository.save(employee);
	}

	public Employee detail(String username) {
		Optional<Employee> optional = employeeRepository.findById(username);
		return optional.get();
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

		origin.setExternalEmail(employee.getExternalEmail());
		origin.setExternalEmailPassword(employee.getExternalEmailPassword());

		return employeeRepository.save(origin);
	}
}
