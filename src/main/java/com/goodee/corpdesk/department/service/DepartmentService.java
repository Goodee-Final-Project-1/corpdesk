package com.goodee.corpdesk.department.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goodee.corpdesk.approval.dto.ResApprovalDTO;
import com.goodee.corpdesk.department.dto.DepartmentTreeDTO;
import com.goodee.corpdesk.department.entity.Department;
import com.goodee.corpdesk.department.repository.DepartmentRepository;
import com.goodee.corpdesk.employee.Employee;
import com.goodee.corpdesk.employee.EmployeeRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class DepartmentService {

    @Autowired
	private DepartmentRepository departmentRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    public List<ResApprovalDTO> getApprovalFormList() throws Exception {
        List<Department> result = departmentRepository.findAll();

        return result.stream().map(Department::toResApprovalDTO).toList();
    }

    public ResApprovalDTO getDepartment(String username) throws Exception {
        // 1. 유저의 부서 정보 얻어오기
        Optional<Employee> result = employeeRepository.findById(username);
        Employee employee = result.get();

        // 2. 부서 정보의 부서명 얻어오기
        Optional<Department> result2 = departmentRepository.findById(employee.getDepartmentId());
        Department department = result2.get();

        // 3. DTO에 담기
        ResApprovalDTO resApprovalDTO = new ResApprovalDTO();
        resApprovalDTO.setDepartmentId(department.getDepartmentId());
        resApprovalDTO.setDepartmentName(department.getDepartmentName());
        resApprovalDTO.setParentDepartmentId(department.getParentDepartmentId());

        return resApprovalDTO;
    }

    public  ResApprovalDTO getDepartment(Integer departmentId) throws Exception {
        // 2. 부서 정보의 부서명 얻어오기
        Optional<Department> result = departmentRepository.findById(departmentId);
        Department department = result.get();

        // 3. DTO에 담기
        ResApprovalDTO resApprovalDTO = new ResApprovalDTO();
        resApprovalDTO.setDepartmentId(department.getDepartmentId());
        resApprovalDTO.setDepartmentName(department.getDepartmentName());
        resApprovalDTO.setParentDepartmentId(department.getParentDepartmentId());

        return resApprovalDTO;
    }

    public Integer getDepartmentIdByName(String departmentName) {
        Optional<Department> department = departmentRepository.findByDepartmentName(departmentName);
        return department.map(Department::getDepartmentId).orElse(null);
    }

    public List<DepartmentTreeDTO> getDepartmentTree() {
        List<Department> departments = departmentRepository.findAll();
        List<DepartmentTreeDTO> tree = new ArrayList<>();

        for (Department dept : departments) {
            // parentDepartmentId 사용
            String parent = (dept.getParentDepartmentId() == null) ? "#" : dept.getParentDepartmentId().toString();
            tree.add(new DepartmentTreeDTO(dept.getDepartmentId(), dept.getDepartmentName(), parent));
        }

        return tree;
    }



    public Department getDepartmentDetail(Integer departmentId) {
        return departmentRepository.findById(departmentId).orElse(null);
    }
    
}
