package com.goodee.corpdesk.department.service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goodee.corpdesk.approval.dto.ResApprovalDTO;
import com.goodee.corpdesk.department.dto.DepartmentDetailDTO;
import com.goodee.corpdesk.department.dto.DepartmentTreeDTO;
import com.goodee.corpdesk.department.entity.Department;
import com.goodee.corpdesk.department.repository.DepartmentRepository;
import com.goodee.corpdesk.employee.Employee;
import com.goodee.corpdesk.employee.EmployeeRepository;
import com.goodee.corpdesk.position.repository.PositionRepository;

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
    
    @Autowired
    private PositionRepository positionRepository;

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



    public DepartmentDetailDTO getDepartmentDetail(Integer id) {
        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("부서 없음"));

        // 상위 부서명
        String parentName = null;
        if (dept.getParentDepartmentId() != null) {
            parentName = departmentRepository.findById(dept.getParentDepartmentId())
                    .map(Department::getDepartmentName)
                    .orElse(null);
        }

        // 하위 부서들
        List<String> childDepartments = departmentRepository.findByParentDepartmentId(dept.getDepartmentId())
                .stream()
                .map(Department::getDepartmentName)
                .toList();

        // 직원 목록 (PositionRepository 통해 직위명 가져오기)
        List<DepartmentDetailDTO.MemberDTO> members = employeeRepository.findByDepartmentIdAndUseYnTrue(dept.getDepartmentId())
        	    .stream()
        	    .map(emp -> new DepartmentDetailDTO.MemberDTO(
        	        emp.getUsername(), 
        	        emp.getName(),
        	        positionRepository.findById(emp.getPositionId())
        	                          .map(p -> p.getPositionName())
        	                          .orElse("정보 없음")
        	    ))
        	    .toList();

        return DepartmentDetailDTO.builder()
                .departmentId(dept.getDepartmentId())
                .departmentName(dept.getDepartmentName())
                .employeeCount(members.size())
                .createdDate(
                    dept.getCreatedAt() != null
                        ? dept.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                        : "정보 없음"
                )
                .parentDepartmentName(parentName != null ? parentName : "미지정")
                .childDepartments(
                    childDepartments.isEmpty() ? List.of("정보 없음") : childDepartments
                )
                .members(members)
                .build();
    }

    @Transactional
    public void moveEmployees(List<String> usernames, Integer newDeptId) {
        for (String username : usernames) {
            Employee emp = employeeRepository.findById(username)
                    .orElseThrow(() -> new RuntimeException("직원 없음: " + username));
            emp.setDepartmentId(newDeptId);
            employeeRepository.save(emp);
        }
    }

    @Transactional
    public void deactivateDepartment(Integer id) {
        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("부서를 찾을 수 없습니다."));

        // 현재 부서 useYn false
        dept.setUseYn(false);
        departmentRepository.save(dept);

        // 하위 부서들도 같이 useYn false
        List<Department> children = departmentRepository.findByParentDepartmentId(id);
        for (Department child : children) {
            deactivateDepartment(child.getDepartmentId());
        }
    }
    
    @Transactional
    public void excludeEmployees(List<String> usernames) {
        for (String username : usernames) {
            Employee emp = employeeRepository.findById(username)
                    .orElseThrow(() -> new RuntimeException("직원 없음: " + username));
            emp.setDepartmentId(null);      // 부서 ID null 처리
            emp.setDepartmentName(null);
            employeeRepository.save(emp);
        }
    }
    
}
