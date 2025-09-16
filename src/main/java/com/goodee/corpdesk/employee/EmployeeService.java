package com.goodee.corpdesk.employee;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goodee.corpdesk.department.Department;
import com.goodee.corpdesk.department.DepartmentRepository;
import com.goodee.corpdesk.position.Position;
import com.goodee.corpdesk.position.PositionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;

    // 등록
    public Employee addEmployee(Employee employee) {
        employee.setUseYn(true); // 기본값
        return employeeRepository.save(employee);
    }

    // 목록 조회
    public List<Employee> getActiveEmployees() {
        List<Employee> employees = employeeRepository.findByUseYnTrue();

        // 부서명, 직위명 매핑
        for(Employee emp : employees){
            if(emp.getDepartmentId() != null) {
                Department dept = departmentRepository.findById(emp.getDepartmentId()).orElse(null);
                if(dept != null) emp.setDepartmentName(dept.getDepartmentName());
            }
            if(emp.getPositionId() != null) {
                Position pos = positionRepository.findById(emp.getPositionId()).orElse(null);
                if(pos != null) emp.setPositionName(pos.getPositionName());
            }
        }
        return employees;
    }

    // 단일 조회
    public Optional<Employee> getEmployee(Integer id) {
        return employeeRepository.findById(id);
    }

    // 수정
    public Employee updateEmployee(Employee employee) {
        Employee persisted = employeeRepository.findById(employee.getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee id: " + employee.getEmployeeId()));

        persisted.setName(employee.getName());
        persisted.setPassword(employee.getPassword());
        persisted.setExternalEmail(employee.getExternalEmail());
        persisted.setHireDate(employee.getHireDate());
        persisted.setGender(employee.getGender());
        persisted.setMobilePhone(employee.getMobilePhone());
        persisted.setEnabled(employee.getEnabled());
        persisted.setStatus(employee.getStatus());
        persisted.setLastWorkingDay(employee.getLastWorkingDay());
        persisted.setDepartmentId(employee.getDepartmentId());
        persisted.setPositionId(employee.getPositionId());

        return employeeRepository.save(persisted);
    }

    // 삭제(비활성화)
    public void deactivateEmployee(Integer id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee id: " + id));
        if (employee.getLastWorkingDay() == null) {
            throw new IllegalStateException("퇴사일자가 없는 사원은 삭제할 수 없습니다.");
        }
        employee.setUseYn(false);
        employeeRepository.save(employee);
    }
}
