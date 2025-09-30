package com.goodee.corpdesk.stats;

import com.goodee.corpdesk.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface StatsRepository extends JpaRepository<Employee, String> {

	@Query("""
	SELECT count(e.username)
	FROM Employee e
	WHERE e.hireDate BETWEEN :start AND :end
	GROUP BY YEAR(e.hireDate), MONTH(e.hireDate)
""")
	List<Integer> countAllByHireDateYearAndHireDateMonth(LocalDate start, LocalDate end);
}
