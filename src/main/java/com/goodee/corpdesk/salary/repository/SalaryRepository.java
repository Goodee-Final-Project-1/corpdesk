package com.goodee.corpdesk.salary.repository;

import com.goodee.corpdesk.salary.dto.EmployeeSalaryDTO;
import com.goodee.corpdesk.salary.entity.SalaryPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SalaryRepository extends JpaRepository<SalaryPayment, Long> {

	@Query(value = """
			SELECT s.*, e.name
			FROM salary_payment s
			    JOIN employee e ON s.username = e.username
			ORDER BY s.payment_date DESC
			""",
			countQuery = "SELECT count(*) FROM salary_payment",
			nativeQuery = true)
	Page<EmployeeSalaryDTO> findAllEmployeeSalary(Pageable pageable);
}
