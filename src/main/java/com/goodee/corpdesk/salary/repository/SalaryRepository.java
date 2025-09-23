package com.goodee.corpdesk.salary.repository;

import com.goodee.corpdesk.salary.dto.EmployeeSalaryDTO;
import com.goodee.corpdesk.salary.entity.SalaryPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SalaryRepository extends JpaRepository<SalaryPayment, Long> {

	@Query(value = """
			SELECT
					s.payment_id as paymentId,
					s.username as username,
					s.payment_date as paymentDate,
					s.base_salary as baseSalary,
								
					e.name as name,
					e.responsibility as responsibility,
								
					d.department_name as departmentName,
					p.position_name as positionName,
								
					a.allowance_amount as allowanceAmount,
					de.deduction_amount as deductionAmount
			FROM salary_payment s
			JOIN employee e ON s.username = e.username
			JOIN department d ON e.department_id = d.department_id
			JOIN position p ON e.position_id = p.position_id
			JOIN (
					SELECT payment_id, SUM(allowance_amount) as allowance_amount
					FROM allowance
					GROUP BY payment_id
			) a ON s.payment_id = a.payment_id
			JOIN (
					SELECT payment_id, SUM(deduction_amount) as deduction_amount
					FROM deduction
					GROUP BY payment_id
			) de ON s.payment_id = de.payment_id
			ORDER BY s.payment_id DESC
			""",
			countQuery = "SELECT count(*) FROM salary_payment",
			nativeQuery = true)
	Page<EmployeeSalaryDTO> findAllEmployeeSalary(Pageable pageable);
}
