package com.goodee.corpdesk.department.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goodee.corpdesk.department.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {

	Optional<Department> findByDepartmentName(String departmentName);
	 Optional<Department> findByDepartmentId(Integer departmentId);
	List<Department> findByParentDepartmentId(Integer id);
	
	List<Department> findByUseYnTrue(); 
	List<Department> findByParentDepartmentIdAndUseYnTrue(Integer parentDepartmentId);

	
}
