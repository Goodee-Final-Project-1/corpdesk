package com.goodee.corpdesk.employee;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, String> {
	boolean existsByUsername(String username);
	List<Employee> findByUseYnTrue();
	boolean existsByMobilePhone(String mobilePhone);
}
