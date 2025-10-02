package com.goodee.corpdesk.admin;

import com.goodee.corpdesk.employee.Employee;
import com.goodee.corpdesk.employee.EmployeeRepository;
import com.goodee.corpdesk.employee.Role;
import com.goodee.corpdesk.employee.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

	private final EmployeeRepository employeeRepository;
	private final RoleRepository roleRepository;

	public List<Map<String, Object>> employeeList() {
		return employeeRepository.findAllWithDepartmentAndPosition();
	}

	public List<Role> roleList() {
		return roleRepository.findAll();
	}

	public void updateRole(String username, Integer roleId) {
		Employee employee = employeeRepository.findById(username).get();

		employee.setRoleId(roleId);
	}
}
