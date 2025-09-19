package com.goodee.corpdesk.employee;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, String> {
	boolean existsByUsername(String username);

	List<Employee> findAllByUseYnTrue();

	boolean existsByMobilePhone(String mobilePhone);

	Optional<Employee> findByUsername(String username);
}
