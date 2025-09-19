package com.goodee.corpdesk.department.repository;

import com.goodee.corpdesk.department.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
